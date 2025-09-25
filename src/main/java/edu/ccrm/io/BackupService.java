package edu.ccrm.io;

import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Service for backup and restore operations using NIO.2
 * Demonstrates advanced NIO.2 features like file visitors and directory operations
 */
public class BackupService {
    
    private final AppConfig config;
    private final DateTimeFormatter timestampFormatter;
    
    /**
     * Constructor
     */
    public BackupService() {
        this.config = AppConfig.getInstance();
        this.timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
    }
    
    /**
     * Create backup of application data
     * @return path to backup file
     * @throws IOException if backup fails
     */
    public Path createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        String backupFileName = "ccrm_backup_" + timestamp + ".zip";
        Path backupPath = config.getBackupDirectory().resolve(backupFileName);
        
        // Ensure backup directory exists
        Files.createDirectories(config.getBackupDirectory());
        
        System.out.println("Creating backup: " + backupPath);
        
        // Create backup using file visitor
        try (ZipOutputStream zipOut = new ZipOutputStream(
                Files.newOutputStream(backupPath, StandardOpenOption.CREATE))) {
            
            BackupFileVisitor visitor = new BackupFileVisitor(zipOut, config.getDataDirectory());
            Files.walkFileTree(config.getDataDirectory(), visitor);
            
            // Skip export directory to avoid duplicates and large backup files
            // In a real system, exports would be handled separately
            /*
            if (Files.exists(config.getExportDirectory())) {
                BackupFileVisitor exportVisitor = new BackupFileVisitor(zipOut, config.getExportDirectory(), "exports/");
                Files.walkFileTree(config.getExportDirectory(), exportVisitor);
            }
            */
            
            System.out.println("Backup created successfully");
            System.out.println("Files backed up: " + visitor.getFileCount());
            System.out.println("Total size: " + formatFileSize(visitor.getTotalSize()));
        }
        
        return backupPath;
    }
    
    /**
     * File visitor for backup operations
     */
    private static class BackupFileVisitor extends SimpleFileVisitor<Path> {
        private final ZipOutputStream zipOut;
        private final Path rootPath;
        private final String prefix;
        private final AtomicLong fileCount = new AtomicLong(0);
        private final AtomicLong totalSize = new AtomicLong(0);
        
        public BackupFileVisitor(ZipOutputStream zipOut, Path rootPath) {
            this(zipOut, rootPath, "");
        }
        
        public BackupFileVisitor(ZipOutputStream zipOut, Path rootPath, String prefix) {
            this.zipOut = zipOut;
            this.rootPath = rootPath;
            this.prefix = prefix;
        }
        
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            // Skip hidden files and temporary files
            if (file.getFileName().toString().startsWith(".") || 
                file.getFileName().toString().endsWith(".tmp")) {
                return FileVisitResult.CONTINUE;
            }
            
            // Create relative path for zip entry
            Path relativePath = rootPath.relativize(file);
            String entryName = prefix + relativePath.toString().replace('\\', '/');
            
            // Add file to zip
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipEntry.setTime(attrs.lastModifiedTime().toMillis());
            zipOut.putNextEntry(zipEntry);
            
            Files.copy(file, zipOut);
            zipOut.closeEntry();
            
            fileCount.incrementAndGet();
            totalSize.addAndGet(attrs.size());
            
            return FileVisitResult.CONTINUE;
        }
        
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.err.println("Failed to backup file: " + file + " - " + exc.getMessage());
            return FileVisitResult.CONTINUE;
        }
        
        public long getFileCount() {
            return fileCount.get();
        }
        
        public long getTotalSize() {
            return totalSize.get();
        }
    }
    
    /**
     * Clean old backup files
     * @param daysToKeep number of days to keep backups
     * @return number of files deleted
     * @throws IOException if cleanup fails
     */
    public int cleanOldBackups(int daysToKeep) throws IOException {
        if (!Files.exists(config.getBackupDirectory())) {
            return 0;
        }
        
        System.out.println("Cleaning backups older than " + daysToKeep + " days...");
        
        long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60L * 60L * 1000L);
        AtomicLong deletedCount = new AtomicLong(0);
        AtomicLong freedSpace = new AtomicLong(0);
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                config.getBackupDirectory(), "ccrm_backup_*.zip")) {
            
            for (Path backupFile : stream) {
                BasicFileAttributes attrs = Files.readAttributes(backupFile, BasicFileAttributes.class);
                if (attrs.lastModifiedTime().toMillis() < cutoffTime) {
                    long fileSize = attrs.size();
                    Files.delete(backupFile);
                    deletedCount.incrementAndGet();
                    freedSpace.addAndGet(fileSize);
                    System.out.println("Deleted old backup: " + backupFile.getFileName());
                }
            }
        }
        
        if (deletedCount.get() > 0) {
            System.out.println("Cleanup completed: " + deletedCount.get() + 
                             " files deleted, " + formatFileSize(freedSpace.get()) + " freed");
        } else {
            System.out.println("No old backups found to delete");
        }
        
        return (int) deletedCount.get();
    }
    
    /**
     * List available backup files
     * @return array of backup file paths
     * @throws IOException if listing fails
     */
    public Path[] listBackups() throws IOException {
        if (!Files.exists(config.getBackupDirectory())) {
            return new Path[0];
        }
        
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                config.getBackupDirectory(), "ccrm_backup_*.zip")) {
            
            // Convert DirectoryStream to array
            java.util.List<Path> backupList = new java.util.ArrayList<>();
            for (Path backup : stream) {
                backupList.add(backup);
            }
            return backupList.toArray(new Path[0]);
        }
    }
    
    /**
     * Get backup file information
     * @param backupPath path to backup file
     * @return backup information string
     * @throws IOException if reading backup info fails
     */
    public String getBackupInfo(Path backupPath) throws IOException {
        if (!Files.exists(backupPath)) {
            return "Backup file does not exist";
        }
        
        BasicFileAttributes attrs = Files.readAttributes(backupPath, BasicFileAttributes.class);
        
        StringBuilder info = new StringBuilder();
        info.append("Backup File: ").append(backupPath.getFileName()).append("\n");
        info.append("Created: ").append(attrs.creationTime()).append("\n");
        info.append("Modified: ").append(attrs.lastModifiedTime()).append("\n");
        info.append("Size: ").append(formatFileSize(attrs.size())).append("\n");
        
        return info.toString();
    }
    
    /**
     * Create system health check report
     * @return path to health check report
     * @throws IOException if report creation fails
     */
    public Path createHealthCheckReport() throws IOException {
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        Path reportPath = config.getDataDirectory().resolve("health_check_" + timestamp + ".txt");
        
        StringBuilder report = new StringBuilder();
        report.append("CCRM System Health Check Report\n");
        report.append("===============================\n");
        report.append("Generated: ").append(LocalDateTime.now()).append("\n\n");
        
        // Check directories
        report.append("Directory Status:\n");
        report.append("-----------------\n");
        checkDirectory(report, "Data Directory", config.getDataDirectory());
        checkDirectory(report, "Export Directory", config.getExportDirectory());
        checkDirectory(report, "Backup Directory", config.getBackupDirectory());
        report.append("\n");
        
        // Check disk space
        report.append("Disk Space:\n");
        report.append("-----------\n");
        addDiskSpaceInfo(report, config.getDataDirectory());
        report.append("\n");
        
        // Check backup status
        report.append("Backup Status:\n");
        report.append("--------------\n");
        addBackupStatus(report);
        report.append("\n");
        
        // System properties
        report.append("System Properties:\n");
        report.append("------------------\n");
        report.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        report.append("Operating System: ").append(System.getProperty("os.name")).append(" ");
        report.append(System.getProperty("os.version")).append("\n");
        report.append("Available Processors: ").append(Runtime.getRuntime().availableProcessors()).append("\n");
        report.append("Total Memory: ").append(formatFileSize(Runtime.getRuntime().totalMemory())).append("\n");
        report.append("Free Memory: ").append(formatFileSize(Runtime.getRuntime().freeMemory())).append("\n");
        
        Files.write(reportPath, report.toString().getBytes());
        
        System.out.println("Health check report created: " + reportPath);
        return reportPath;
    }
    
    /**
     * Check directory status
     */
    private void checkDirectory(StringBuilder report, String name, Path directory) {
        try {
            if (Files.exists(directory)) {
                if (Files.isDirectory(directory)) {
                    long fileCount = Files.list(directory).count();
                    report.append(String.format("✓ %s: EXISTS (%d files)\n", name, fileCount));
                } else {
                    report.append(String.format("✗ %s: EXISTS BUT NOT A DIRECTORY\n", name));
                }
            } else {
                report.append(String.format("✗ %s: DOES NOT EXIST\n", name));
            }
        } catch (IOException e) {
            report.append(String.format("✗ %s: ERROR - %s\n", name, e.getMessage()));
        }
    }
    
    /**
     * Add disk space information
     */
    private void addDiskSpaceInfo(StringBuilder report, Path path) {
        try {
            FileStore store = Files.getFileStore(path);
            long totalSpace = store.getTotalSpace();
            long usableSpace = store.getUsableSpace();
            long usedSpace = totalSpace - usableSpace;
            
            report.append("Total Space: ").append(formatFileSize(totalSpace)).append("\n");
            report.append("Used Space: ").append(formatFileSize(usedSpace)).append("\n");
            report.append("Available Space: ").append(formatFileSize(usableSpace)).append("\n");
            
            double usagePercent = (double) usedSpace / totalSpace * 100;
            report.append("Usage: ").append(String.format("%.1f%%", usagePercent)).append("\n");
            
            if (usagePercent > 90) {
                report.append("⚠ WARNING: Disk usage is high (>90%)\n");
            }
            
        } catch (IOException e) {
            report.append("Error getting disk space info: ").append(e.getMessage()).append("\n");
        }
    }
    
    /**
     * Add backup status information
     */
    private void addBackupStatus(StringBuilder report) {
        try {
            if (Files.exists(config.getBackupDirectory())) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(
                        config.getBackupDirectory(), "ccrm_backup_*.zip")) {
                    
                    long backupCount = 0;
                    Path latestBackup = null;
                    long latestTime = 0;
                    
                    for (Path backup : stream) {
                        backupCount++;
                        BasicFileAttributes attrs = Files.readAttributes(backup, BasicFileAttributes.class);
                        if (attrs.lastModifiedTime().toMillis() > latestTime) {
                            latestTime = attrs.lastModifiedTime().toMillis();
                            latestBackup = backup;
                        }
                    }
                    
                    report.append("Total Backups: ").append(backupCount).append("\n");
                    if (latestBackup != null) {
                        report.append("Latest Backup: ").append(latestBackup.getFileName()).append("\n");
                        long daysSinceBackup = (System.currentTimeMillis() - latestTime) / (24 * 60 * 60 * 1000);
                        report.append("Days Since Last Backup: ").append(daysSinceBackup).append("\n");
                        
                        if (daysSinceBackup > 7) {
                            report.append("⚠ WARNING: No recent backup (>7 days)\n");
                        }
                    }
                }
            } else {
                report.append("No backup directory found\n");
            }
        } catch (IOException e) {
            report.append("Error checking backup status: ").append(e.getMessage()).append("\n");
        }
    }
    
    /**
     * Format file size in human readable format
     * @param bytes file size in bytes
     * @return formatted string
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}