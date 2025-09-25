package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Application configuration using Singleton pattern
 * Demonstrates Singleton design pattern with thread safety
 */
public class AppConfig {
    
    // Singleton instance - volatile for thread safety
    private static volatile AppConfig instance;
    
    // Configuration properties
    private final Properties properties;
    private final Map<String, Object> runtimeConfig;
    
    // Application paths
    private Path dataDirectory;
    private Path backupDirectory;
    private Path exportDirectory;
    private Path importDirectory;
    
    // Application settings
    private int maxStudentsPerCourse;
    private int maxCoursesPerStudent;
    private double minimumGPA;
    private String defaultSemester;
    
    /**
     * Private constructor prevents external instantiation
     * Demonstrates Singleton pattern implementation
     */
    private AppConfig() {
        this.properties = new Properties();
        this.runtimeConfig = new ConcurrentHashMap<>();
        loadDefaultConfiguration();
    }
    
    /**
     * Get singleton instance with double-checked locking
     * Thread-safe lazy initialization
     * @return AppConfig instance
     */
    public static AppConfig getInstance() {
        // First check without synchronization for performance
        if (instance == null) {
            synchronized (AppConfig.class) {
                // Second check with synchronization
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize application with default settings
     */
    public void initializeApplication() {
        System.out.println("Initializing Campus Course & Records Manager...");
        
        // Set up directory structure
        setupDirectories();
        
        // Load configuration
        loadConfiguration();
        
        System.out.println("Application initialized successfully!");
        System.out.println("Data Directory: " + dataDirectory);
        System.out.println("Max Students per Course: " + maxStudentsPerCourse);
        System.out.println("Max Courses per Student: " + maxCoursesPerStudent);
        System.out.println("Minimum GPA: " + minimumGPA);
        System.out.println();
    }
    
    /**
     * Load default configuration values
     */
    private void loadDefaultConfiguration() {
        // Default application settings
        maxStudentsPerCourse = 30;
        maxCoursesPerStudent = 6;
        minimumGPA = 2.0;
        defaultSemester = "FALL";
        
        // Set runtime configuration
        runtimeConfig.put("app.name", "Campus Course & Records Manager");
        runtimeConfig.put("app.version", "1.0");
        runtimeConfig.put("app.author", "CCRM Team");
        runtimeConfig.put("startup.time", System.currentTimeMillis());
    }
    
    /**
     * Setup application directories
     */
    private void setupDirectories() {
        try {
            // Get current working directory
            Path currentDir = Paths.get(System.getProperty("user.dir"));
            
            // Setup data directories
            dataDirectory = currentDir.resolve("data");
            backupDirectory = currentDir.resolve("data/backups");
            exportDirectory = currentDir.resolve("data/exports");
            importDirectory = currentDir.resolve("data/imports");
            
            // Create directories if they don't exist
            java.nio.file.Files.createDirectories(dataDirectory);
            java.nio.file.Files.createDirectories(backupDirectory);
            java.nio.file.Files.createDirectories(exportDirectory);
            java.nio.file.Files.createDirectories(importDirectory);
            
        } catch (Exception e) {
            System.err.println("Error setting up directories: " + e.getMessage());
        }
    }
    
    /**
     * Load configuration from properties (future enhancement)
     */
    private void loadConfiguration() {
        // In a real application, this would load from config files
        properties.setProperty("max.students.per.course", String.valueOf(maxStudentsPerCourse));
        properties.setProperty("max.courses.per.student", String.valueOf(maxCoursesPerStudent));
        properties.setProperty("minimum.gpa", String.valueOf(minimumGPA));
        properties.setProperty("default.semester", defaultSemester);
    }
    
    // Getters for configuration values
    public Path getDataDirectory() { return dataDirectory; }
    public Path getBackupDirectory() { return backupDirectory; }
    public Path getExportDirectory() { return exportDirectory; }
    public Path getImportDirectory() { return importDirectory; }
    
    public int getMaxStudentsPerCourse() { return maxStudentsPerCourse; }
    public void setMaxStudentsPerCourse(int maxStudentsPerCourse) { 
        this.maxStudentsPerCourse = maxStudentsPerCourse; 
    }
    
    public int getMaxCoursesPerStudent() { return maxCoursesPerStudent; }
    public void setMaxCoursesPerStudent(int maxCoursesPerStudent) { 
        this.maxCoursesPerStudent = maxCoursesPerStudent; 
    }
    
    public double getMinimumGPA() { return minimumGPA; }
    public void setMinimumGPA(double minimumGPA) { 
        this.minimumGPA = minimumGPA; 
    }
    
    public String getDefaultSemester() { return defaultSemester; }
    public void setDefaultSemester(String defaultSemester) { 
        this.defaultSemester = defaultSemester; 
    }
    
    /**
     * Get runtime configuration value
     * @param key configuration key
     * @return configuration value
     */
    public Object getRuntimeConfig(String key) {
        return runtimeConfig.get(key);
    }
    
    /**
     * Set runtime configuration value
     * @param key configuration key
     * @param value configuration value
     */
    public void setRuntimeConfig(String key, Object value) {
        runtimeConfig.put(key, value);
    }
    
    /**
     * Get property value
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Get property value with default
     * @param key property key
     * @param defaultValue default value if key not found
     * @return property value or default
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Set property value
     * @param key property key
     * @param value property value
     */
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Get application startup time
     * @return startup time in milliseconds
     */
    public long getStartupTime() {
        return (Long) runtimeConfig.getOrDefault("startup.time", 0L);
    }
    
    /**
     * Get application uptime in seconds
     * @return uptime in seconds
     */
    public long getUptimeSeconds() {
        return (System.currentTimeMillis() - getStartupTime()) / 1000;
    }
    
    /**
     * Reset configuration to defaults
     */
    public synchronized void resetToDefaults() {
        properties.clear();
        runtimeConfig.clear();
        loadDefaultConfiguration();
        loadConfiguration();
    }
    
    /**
     * Get configuration summary
     * @return formatted configuration summary
     */
    public String getConfigurationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Application Configuration Summary\n");
        summary.append("=".repeat(40)).append("\n");
        summary.append(String.format("Application: %s v%s\n", 
            getRuntimeConfig("app.name"), getRuntimeConfig("app.version")));
        summary.append(String.format("Uptime: %d seconds\n", getUptimeSeconds()));
        summary.append(String.format("Data Directory: %s\n", dataDirectory));
        summary.append(String.format("Max Students/Course: %d\n", maxStudentsPerCourse));
        summary.append(String.format("Max Courses/Student: %d\n", maxCoursesPerStudent));
        summary.append(String.format("Minimum GPA: %.2f\n", minimumGPA));
        summary.append(String.format("Default Semester: %s\n", defaultSemester));
        summary.append("=".repeat(40)).append("\n");
        return summary.toString();
    }
    
    // Prevent cloning
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned");
    }
}