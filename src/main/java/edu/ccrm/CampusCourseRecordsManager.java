package edu.ccrm;

import edu.ccrm.cli.MainMenu;
import edu.ccrm.config.AppConfig;

/**
 * Campus Course & Records Manager (CCRM)
 * Main class demonstrating Java syntax & structure
 * 
 * This application demonstrates comprehensive Java SE features including:
 * - Object-Oriented Programming (Encapsulation, Inheritance, Abstraction, Polymorphism)
 * - Modern I/O with NIO.2, Streams, Date/Time API
 * - Design Patterns (Singleton, Builder)
 * - Exception Handling, Interfaces, Abstract Classes
 * - Nested Classes, Enums, Lambdas, Recursion
 * 
 * @author Campus Course Records Manager Team
 * @version 1.0
 */
public class CampusCourseRecordsManager {
    
    /**
     * Main method - entry point of the application
     * Demonstrates basic Java syntax and application startup
     * 
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Enable assertions for invariant checking
        // Run with: java -ea edu.ccrm.CampusCourseRecordsManager
        assert args != null : "Command line arguments should not be null";
        
        try {
            // Print application header
            printApplicationHeader();
            
            // Initialize application configuration (Singleton pattern)
            AppConfig config = AppConfig.getInstance();
            config.initializeApplication();
            
            // Start the main menu CLI
            MainMenu mainMenu = new MainMenu();
            mainMenu.start();
            
        } catch (Exception e) {
            System.err.println("Application startup failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Prints application header with platform information
     * Demonstrates String methods and basic output formatting
     */
    private static void printApplicationHeader() {
        String title = "Campus Course & Records Manager (CCRM)";
        String version = "Version 1.0";
        String platform = "Java SE Platform";
        
        // String manipulation demonstration
        String border = "=".repeat(title.length() + 4);
        String centeredTitle = String.format("| %s |", title);
        String centeredVersion = String.format("| %s%s |", version, 
            " ".repeat(title.length() - version.length()));
        String centeredPlatform = String.format("| %s%s |", platform,
            " ".repeat(title.length() - platform.length()));
        
        System.out.println(border);
        System.out.println(centeredTitle);
        System.out.println(centeredVersion);
        System.out.println(centeredPlatform);
        System.out.println(border);
        System.out.println();
        
        // Platform information as per requirements
        printPlatformInfo();
    }
    
    /**
     * Prints Java platform information
     * Demonstrates the difference between Java ME, SE, and EE
     */
    private static void printPlatformInfo() {
        System.out.println("Java Platform Information:");
        System.out.println("-------------------------");
        System.out.println("Current Platform: Java SE (Standard Edition)");
        System.out.println("• Java ME (Micro Edition): For embedded/mobile devices with limited resources");
        System.out.println("• Java SE (Standard Edition): For desktop and server applications");
        System.out.println("• Java EE (Enterprise Edition): For large-scale, distributed enterprise applications");
        System.out.println();
        
        // Display current Java runtime information
        System.out.println("Runtime Information:");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("JVM: " + System.getProperty("java.vm.name"));
        System.out.println();
    }
}