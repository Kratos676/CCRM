package edu.ccrm;

import edu.ccrm.config.AppConfig;
import edu.ccrm.io.*;
import edu.ccrm.domain.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Test class to demonstrate I/O functionality
 * Demonstrates NIO.2, Streams, and exception handling
 */
public class IODemonstration {
    
    public static void main(String[] args) {
        System.out.println("CCRM I/O Services Demonstration");
        System.out.println("================================\n");
        
        try {
            // Initialize configuration
            AppConfig config = AppConfig.getInstance();
            config.initializeApplication();
            
            System.out.println("1. Testing ImportExportService...");
            testImportExportService();
            
            System.out.println("\n2. Testing BackupService...");
            testBackupService();
            
            System.out.println("\n✅ All I/O tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Error during I/O testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test Import/Export functionality
     */
    private static void testImportExportService() throws Exception {
        ImportExportService ioService = new ImportExportService();
        
        // Test file validation
        System.out.println("   Validating CSV files...");
        Path studentsPath = Paths.get("test-data", "students.csv");
        Path coursesPath = Paths.get("test-data", "courses.csv");
        
        String[] studentHeaders = {"student_id", "reg_no", "first_name", "last_name", "email", "department", "semester"};
        String[] courseHeaders = {"course_code", "title", "credits", "department", "semester", "instructor_id", "max_capacity"};
        
        boolean studentsValid = ioService.validateCsvFormat(studentsPath, studentHeaders);
        boolean coursesValid = ioService.validateCsvFormat(coursesPath, courseHeaders);
        
        System.out.println("   ✓ Students CSV validation: " + (studentsValid ? "PASSED" : "FAILED"));
        System.out.println("   ✓ Courses CSV validation: " + (coursesValid ? "PASSED" : "FAILED"));
        
        // Test import operations
        if (studentsValid) {
            System.out.println("   Importing students...");
            List<Student> students = ioService.importStudents(studentsPath);
            System.out.println("   ✓ Imported " + students.size() + " students");
            
            // Display first few students
            students.stream()
                .limit(3)
                .forEach(student -> 
                    System.out.println("     - " + student.getName().getFullName() + 
                                     " (" + student.getDepartment() + ")"));
            
            // Test export
            System.out.println("   Testing export functionality...");
            Path exportPath = ioService.exportSystemData(students, List.of());
            System.out.println("   ✓ Export completed to: " + exportPath);
        }
        
        if (coursesValid) {
            System.out.println("   Importing courses...");
            List<Course> courses = ioService.importCourses(coursesPath);
            System.out.println("   ✓ Imported " + courses.size() + " courses");
            
            // Display first few courses
            courses.stream()
                .limit(3)
                .forEach(course -> 
                    System.out.println("     - " + course.getTitle() + 
                                     " (" + course.getCourseCode().getFullCode() + ")"));
        }
    }
    
    /**
     * Test Backup functionality
     */
    private static void testBackupService() throws Exception {
        BackupService backupService = new BackupService();
        
        // Create health check report
        System.out.println("   Creating health check report...");
        Path healthReport = backupService.createHealthCheckReport();
        System.out.println("   ✓ Health report: " + healthReport.getFileName());
        
        // List existing backups
        System.out.println("   Checking existing backups...");
        Path[] backups = backupService.listBackups();
        System.out.println("   ✓ Found " + backups.length + " existing backups");
        
        // Create new backup
        System.out.println("   Creating system backup...");
        Path newBackup = backupService.createBackup();
        System.out.println("   ✓ Backup created: " + newBackup.getFileName());
        
        // Show backup info
        String backupInfo = backupService.getBackupInfo(newBackup);
        System.out.println("   Backup details:");
        System.out.println("   " + backupInfo.replace("\n", "\n   "));
        
        // Test cleanup (keeping all backups for demo)
        System.out.println("   Testing backup cleanup (dry-run)...");
        int cleanedFiles = backupService.cleanOldBackups(365); // Keep 1 year
        System.out.println("   ✓ Would clean " + cleanedFiles + " old backups");
    }
}