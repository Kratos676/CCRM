package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.io.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.domain.*;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

/**
 * Main menu CLI for the CCRM application
 * Demonstrates switch statements, loops, and decision structures
 */
public class MainMenu {
    
    private final Scanner scanner;
    private final AppConfig config;
    private final StudentService studentService;
    private final CourseService courseService;
    private boolean running;
    
    /**
     * Constructor
     */
    public MainMenu() {
        this.scanner = new Scanner(System.in);
        this.config = AppConfig.getInstance();
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.running = false;
    }
    
    /**
     * Start the main menu loop
     * Demonstrates while loop and switch statement
     */
    public void start() {
        running = true;
        
        System.out.println("Welcome to Campus Course & Records Manager!");
        System.out.println("Current Configuration:");
        System.out.println(config.getConfigurationSummary());
        
        // Main application loop - demonstrates while loop
        while (running) {
            try {
                displayMainMenu();
                int choice = getMenuChoice();
                processMenuChoice(choice);
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
        
        // Cleanup
        scanner.close();
        System.out.println("Thank you for using CCRM!");
    }
    
    /**
     * Display main menu options
     */
    private void displayMainMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CAMPUS COURSE & RECORDS MANAGER - MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Grade Management");
        System.out.println("5. Reports & Analytics");
        System.out.println("6. Import/Export Data");
        System.out.println("7. Backup & Utilities");
        System.out.println("8. System Configuration");
        System.out.println("9. Demo Operations");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
        System.out.print("Enter your choice (0-9): ");
    }
    
    /**
     * Get menu choice from user with validation
     * Demonstrates input validation and exception handling
     */
    private int getMenuChoice() {
        try {
            String input = scanner.nextLine().trim();
            
            // Input validation with custom logic
            if (input.isEmpty()) {
                throw new IllegalArgumentException("Please enter a choice");
            }
            
            int choice = Integer.parseInt(input);
            
            // Range validation
            if (choice < 0 || choice > 9) {
                throw new IllegalArgumentException("Choice must be between 0 and 9");
            }
            
            return choice;
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number");
        }
    }
    
    /**
     * Process menu choice using enhanced switch statement
     * Demonstrates enhanced switch (Java 14+ feature, but shown for completeness)
     * and classic switch with break statements
     */
    private void processMenuChoice(int choice) {
        // Classic switch statement with break/continue demonstration
        switch (choice) {
            case 1:
                handleStudentManagement();
                break;
            case 2:
                handleCourseManagement();
                break;
            case 3:
                handleEnrollmentManagement();
                break;
            case 4:
                handleGradeManagement();
                break;
            case 5:
                handleReportsAndAnalytics();
                break;
            case 6:
                handleImportExport();
                break;
            case 7:
                handleBackupUtilities();
                break;
            case 8:
                handleSystemConfiguration();
                break;
            case 9:
                handleDemoOperations();
                break;
            case 0:
                handleExit();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    /**
     * Handle student management submenu
     */
    private void handleStudentManagement() {
        boolean backToMain = false;
        
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("STUDENT MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. Search Student");
            System.out.println("4. Update Student");
            System.out.println("5. Remove Student");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));
            System.out.print("Enter your choice (0-5): ");
            
            try {
                int choice = getMenuChoice();
                
                switch (choice) {
                    case 1:
                        addStudent();
                        break;
                    case 2:
                        listAllStudents();
                        break;
                    case 3:
                        searchStudent();
                        break;
                    case 4:
                        updateStudent();
                        break;
                    case 5:
                        removeStudent();
                        break;
                    case 0:
                        backToMain = true;
                        break;
                    default:
                        if (choice >= 0 && choice <= 5) {
                            System.out.println("This feature will be implemented soon.");
                        } else {
                            System.out.println("Invalid choice. Please enter 0-5.");
                        }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        try {
            System.out.print("Enter Student ID: ");
            String studentId = scanner.nextLine().trim();
            
            System.out.print("Enter Registration Number: ");
            String regNo = scanner.nextLine().trim();
            
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();
            
            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();
            
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();
            
            // Create student - using the available constructor
            Name name = new Name(firstName, lastName);
            Student student = new Student(studentId, regNo, name, email, 
                                        LocalDate.now(), department);
            
            studentService.addStudent(student);
            System.out.println("✓ Student added successfully!");
            
        } catch (Exception e) {
            System.err.println("✗ Failed to add student: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void listAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentService.getAll();
        
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.printf("%-10s %-15s %-25s %-25s %-15s%n", 
                "ID", "Reg No", "Name", "Email", "Department");
            System.out.println("-".repeat(90));
            
            for (Student student : students) {
                System.out.printf("%-10s %-15s %-25s %-25s %-15s%n",
                    student.getId(),
                    student.getRegistrationNumber(),
                    student.getName().getFullName(),
                    student.getEmail(),
                    student.getDepartment());
            }
            System.out.println("\nTotal students: " + students.size());
        }
        pressEnterToContinue();
    }
    
    private void searchStudent() {
        System.out.println("\n--- Search Student ---");
        System.out.print("Enter search term (ID, name, or email): ");
        String searchTerm = scanner.nextLine().trim();
        
        try {
            // Use predicate to search by name or email containing the search term
            List<Student> results = studentService.search(student -> 
                student.getName().getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                student.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) ||
                student.getId().toLowerCase().contains(searchTerm.toLowerCase())
            );
            
            if (results.isEmpty()) {
                System.out.println("No students found matching: " + searchTerm);
            } else {
                System.out.println("Found " + results.size() + " student(s):");
                System.out.printf("%-10s %-15s %-25s %-25s %-15s%n", 
                    "ID", "Reg No", "Name", "Email", "Department");
                System.out.println("-".repeat(90));
                
                for (Student student : results) {
                    System.out.printf("%-10s %-15s %-25s %-25s %-15s%n",
                        student.getId(),
                        student.getRegistrationNumber(),
                        student.getName().getFullName(),
                        student.getEmail(),
                        student.getDepartment());
                }
            }
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void updateStudent() {
        System.out.println("\n--- Update Student ---");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine().trim();
        
        try {
            Student student = studentService.findById(studentId);
            if (student == null) {
                System.out.println("Student not found with ID: " + studentId);
                pressEnterToContinue();
                return;
            }
            
            System.out.println("Current details: " + student.getName().getFullName());
            System.out.print("Enter new email (or press Enter to keep current): ");
            String newEmail = scanner.nextLine().trim();
            
            if (!newEmail.isEmpty()) {
                student.setEmail(newEmail);
                studentService.updateStudent(student);
                System.out.println("✓ Student updated successfully!");
            } else {
                System.out.println("No changes made.");
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to update student: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void removeStudent() {
        System.out.println("\n--- Remove Student ---");
        System.out.print("Enter Student ID to remove: ");
        String studentId = scanner.nextLine().trim();
        
        try {
            Student student = studentService.findById(studentId);
            if (student == null) {
                System.out.println("Student not found with ID: " + studentId);
                pressEnterToContinue();
                return;
            }
            
            System.out.println("Student: " + student.getName().getFullName());
            System.out.print("Are you sure you want to deactivate this student? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = studentService.deactivateStudent(studentId);
                if (success) {
                    System.out.println("✓ Student deactivated successfully!");
                } else {
                    System.out.println("✗ Failed to deactivate student.");
                }
            } else {
                System.out.println("Deactivation cancelled.");
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to deactivate student: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    /**
     * Handle course management submenu
     */
    private void handleCourseManagement() {
        boolean backToMain = false;
        
        while (!backToMain) {
            System.out.println("\n" + "=".repeat(40));
            System.out.println("COURSE MANAGEMENT");
            System.out.println("=".repeat(40));
            System.out.println("1. Add Course");
            System.out.println("2. List All Courses");
            System.out.println("3. Search Courses");
            System.out.println("4. Update Course");
            System.out.println("5. Assign Instructor");
            System.out.println("6. Deactivate Course");
            System.out.println("7. Filter by Department");
            System.out.println("8. Filter by Semester");
            System.out.println("0. Back to Main Menu");
            System.out.println("=".repeat(40));
            System.out.print("Enter your choice (0-8): ");
            
            try {
                int choice = getMenuChoice();
                
                switch (choice) {
                    case 1:
                        addCourse();
                        break;
                    case 2:
                        listAllCourses();
                        break;
                    case 3:
                        searchCourses();
                        break;
                    case 4:
                        updateCourse();
                        break;
                    case 5:
                        assignInstructor();
                        break;
                    case 6:
                        deactivateCourse();
                        break;
                    case 7:
                        filterCoursesByDepartment();
                        break;
                    case 8:
                        filterCoursesBySemester();
                        break;
                    case 0:
                        backToMain = true;
                        break;
                    default:
                        if (choice >= 0 && choice <= 8) {
                            System.out.println("This feature will be implemented soon.");
                        } else {
                            System.out.println("Invalid choice. Please enter 0-8.");
                        }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void addCourse() {
        System.out.println("\n--- Add New Course ---");
        try {
            System.out.print("Enter Course Code (e.g., CS101, MATH201): ");
            String courseCodeInput = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Enter Course Title: ");
            String title = scanner.nextLine().trim();
            
            System.out.print("Enter Credits (1-6): ");
            int credits = Integer.parseInt(scanner.nextLine().trim());
            
            System.out.print("Enter Department: ");
            String department = scanner.nextLine().trim();
            
            System.out.print("Enter Description: ");
            String description = scanner.nextLine().trim();
            
            System.out.print("Enter Max Capacity (default 30): ");
            String capacityInput = scanner.nextLine().trim();
            int maxCapacity = capacityInput.isEmpty() ? 30 : Integer.parseInt(capacityInput);
            
            System.out.print("Enter Semester (SPRING, SUMMER, FALL): ");
            String semesterInput = scanner.nextLine().trim().toUpperCase();
            Semester semester = Semester.valueOf(semesterInput);
            
            System.out.print("Enter Instructor ID (optional): ");
            String instructorId = scanner.nextLine().trim();
            
            // Parse course code (e.g., "CS101" -> department="CS", number=101, section="A")
            String courseDept = "";
            int courseNumber = 0;
            String courseSection = "A"; // Default section
            
            // Extract department and number from input like "CS101"
            StringBuilder deptBuilder = new StringBuilder();
            StringBuilder numberBuilder = new StringBuilder();
            boolean inNumber = false;
            
            for (char c : courseCodeInput.toCharArray()) {
                if (Character.isDigit(c)) {
                    inNumber = true;
                    numberBuilder.append(c);
                } else if (!inNumber) {
                    deptBuilder.append(c);
                }
            }
            
            courseDept = deptBuilder.toString();
            courseNumber = Integer.parseInt(numberBuilder.toString());
            
            // Create course using Builder pattern
            CourseCode courseCode = new CourseCode(courseDept, courseNumber, courseSection);
            Course.Builder builder = new Course.Builder(courseCode, title)
                .credits(credits)
                .department(department)
                .description(description)
                .maxCapacity(maxCapacity)
                .semester(semester);
            
            if (!instructorId.isEmpty()) {
                builder.instructor(instructorId);
            }
            
            Course course = builder.build();
            courseService.addCourse(course);
            System.out.println("✓ Course added successfully!");
            
        } catch (IllegalArgumentException e) {
            System.err.println("✗ Invalid input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Failed to add course: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void listAllCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = courseService.getAll();
        
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.printf("%-10s %-30s %-8s %-15s %-10s %-12s %-8s%n", 
                "Code", "Title", "Credits", "Department", "Semester", "Instructor", "Status");
            System.out.println("-".repeat(100));
            
            for (Course course : courses) {
                System.out.printf("%-10s %-30s %-8d %-15s %-10s %-12s %-8s%n",
                    course.getCourseCode().getFullCode(),
                    truncateString(course.getTitle(), 30),
                    course.getCredits(),
                    course.getDepartment(),
                    course.getSemester(),
                    course.getInstructorId() != null ? course.getInstructorId() : "TBD",
                    course.isActive() ? "Active" : "Inactive");
            }
            System.out.println("\nTotal courses: " + courses.size());
            System.out.println("Active courses: " + courses.stream().mapToInt(c -> c.isActive() ? 1 : 0).sum());
        }
        pressEnterToContinue();
    }
    
    private void searchCourses() {
        System.out.println("\n--- Search Courses ---");
        System.out.print("Enter search term (code, title, or department): ");
        String searchTerm = scanner.nextLine().trim();
        
        try {
            // Use predicate to search by code, title, or department
            List<Course> results = courseService.search(course -> 
                course.getCourseCode().getFullCode().toLowerCase().contains(searchTerm.toLowerCase()) ||
                course.getTitle().toLowerCase().contains(searchTerm.toLowerCase()) ||
                course.getDepartment().toLowerCase().contains(searchTerm.toLowerCase())
            );
            
            if (results.isEmpty()) {
                System.out.println("No courses found matching: " + searchTerm);
            } else {
                System.out.println("Found " + results.size() + " course(s):");
                System.out.printf("%-10s %-30s %-8s %-15s %-10s %-12s %-8s%n", 
                    "Code", "Title", "Credits", "Department", "Semester", "Instructor", "Status");
                System.out.println("-".repeat(100));
                
                for (Course course : results) {
                    System.out.printf("%-10s %-30s %-8d %-15s %-10s %-12s %-8s%n",
                        course.getCourseCode().getFullCode(),
                        truncateString(course.getTitle(), 30),
                        course.getCredits(),
                        course.getDepartment(),
                        course.getSemester(),
                        course.getInstructorId() != null ? course.getInstructorId() : "TBD",
                        course.isActive() ? "Active" : "Inactive");
                }
            }
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void updateCourse() {
        System.out.println("\n--- Update Course ---");
        System.out.print("Enter Course Code to update: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        try {
            Course course = courseService.findById(courseCode);
            if (course == null) {
                System.out.println("Course not found with code: " + courseCode);
                pressEnterToContinue();
                return;
            }
            
            System.out.println("Current course: " + course.getTitle());
            System.out.println("1. Update Title");
            System.out.println("2. Update Credits");
            System.out.println("3. Update Department");
            System.out.println("4. Update Description");
            System.out.println("5. Update Max Capacity");
            System.out.print("What would you like to update (1-5): ");
            
            int updateChoice = Integer.parseInt(scanner.nextLine().trim());
            boolean updated = false;
            
            switch (updateChoice) {
                case 1:
                    System.out.print("Enter new title: ");
                    String newTitle = scanner.nextLine().trim();
                    if (!newTitle.isEmpty()) {
                        course.setTitle(newTitle);
                        updated = true;
                    }
                    break;
                case 2:
                    System.out.print("Enter new credits (1-6): ");
                    int newCredits = Integer.parseInt(scanner.nextLine().trim());
                    course.setCredits(newCredits);
                    updated = true;
                    break;
                case 3:
                    System.out.print("Enter new department: ");
                    String newDepartment = scanner.nextLine().trim();
                    if (!newDepartment.isEmpty()) {
                        course.setDepartment(newDepartment);
                        updated = true;
                    }
                    break;
                case 4:
                    System.out.print("Enter new description: ");
                    String newDescription = scanner.nextLine().trim();
                    course.setDescription(newDescription);
                    updated = true;
                    break;
                case 5:
                    System.out.print("Enter new max capacity: ");
                    int newCapacity = Integer.parseInt(scanner.nextLine().trim());
                    course.setMaxCapacity(newCapacity);
                    updated = true;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
            
            if (updated) {
                courseService.updateCourse(course);
                System.out.println("✓ Course updated successfully!");
            } else {
                System.out.println("No changes made.");
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to update course: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void assignInstructor() {
        System.out.println("\n--- Assign Instructor ---");
        System.out.print("Enter Course Code: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Enter Instructor ID: ");
        String instructorId = scanner.nextLine().trim();
        
        try {
            boolean success = courseService.assignInstructor(courseCode, instructorId);
            if (success) {
                System.out.println("✓ Instructor assigned successfully!");
            } else {
                System.out.println("✗ Course not found with code: " + courseCode);
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to assign instructor: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void deactivateCourse() {
        System.out.println("\n--- Deactivate Course ---");
        System.out.print("Enter Course Code to deactivate: ");
        String courseCode = scanner.nextLine().trim().toUpperCase();
        
        try {
            Course course = courseService.findById(courseCode);
            if (course == null) {
                System.out.println("Course not found with code: " + courseCode);
                pressEnterToContinue();
                return;
            }
            
            System.out.println("Course: " + course.getTitle() + " (" + course.getDepartment() + ")");
            System.out.println("Enrolled students: " + course.getEnrolledStudents().size());
            System.out.print("Are you sure you want to deactivate this course? (y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if ("y".equals(confirm) || "yes".equals(confirm)) {
                boolean success = courseService.deactivateCourse(courseCode);
                if (success) {
                    System.out.println("✓ Course deactivated successfully!");
                } else {
                    System.out.println("✗ Failed to deactivate course.");
                }
            } else {
                System.out.println("Deactivation cancelled.");
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to deactivate course: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void filterCoursesByDepartment() {
        System.out.println("\n--- Courses by Department ---");
        System.out.print("Enter Department name: ");
        String department = scanner.nextLine().trim();
        
        try {
            List<Course> courses = courseService.findByDepartment(department);
            
            if (courses.isEmpty()) {
                System.out.println("No courses found in department: " + department);
            } else {
                System.out.println("Courses in " + department + " department:");
                System.out.printf("%-10s %-30s %-8s %-10s %-12s %-8s%n", 
                    "Code", "Title", "Credits", "Semester", "Instructor", "Status");
                System.out.println("-".repeat(85));
                
                for (Course course : courses) {
                    System.out.printf("%-10s %-30s %-8d %-10s %-12s %-8s%n",
                        course.getCourseCode().getFullCode(),
                        truncateString(course.getTitle(), 30),
                        course.getCredits(),
                        course.getSemester(),
                        course.getInstructorId() != null ? course.getInstructorId() : "TBD",
                        course.isActive() ? "Active" : "Inactive");
                }
                System.out.println("\nTotal courses in " + department + ": " + courses.size());
            }
        } catch (Exception e) {
            System.err.println("Filter failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    private void filterCoursesBySemester() {
        System.out.println("\n--- Courses by Semester ---");
        System.out.print("Enter Semester (SPRING, SUMMER, FALL): ");
        String semesterInput = scanner.nextLine().trim().toUpperCase();
        
        try {
            Semester semester = Semester.valueOf(semesterInput);
            List<Course> courses = courseService.findBySemester(semester);
            
            if (courses.isEmpty()) {
                System.out.println("No courses found for semester: " + semester);
            } else {
                System.out.println("Courses for " + semester + " semester:");
                System.out.printf("%-10s %-30s %-8s %-15s %-12s %-8s%n", 
                    "Code", "Title", "Credits", "Department", "Instructor", "Status");
                System.out.println("-".repeat(90));
                
                for (Course course : courses) {
                    System.out.printf("%-10s %-30s %-8d %-15s %-12s %-8s%n",
                        course.getCourseCode().getFullCode(),
                        truncateString(course.getTitle(), 30),
                        course.getCredits(),
                        course.getDepartment(),
                        course.getInstructorId() != null ? course.getInstructorId() : "TBD",
                        course.isActive() ? "Active" : "Inactive");
                }
                System.out.println("\nTotal courses for " + semester + ": " + courses.size());
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid semester. Please enter SPRING, SUMMER, or FALL.");
        } catch (Exception e) {
            System.err.println("Filter failed: " + e.getMessage());
        }
        pressEnterToContinue();
    }
    
    /**
     * Utility method to truncate strings for display
     */
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Handle enrollment management
     */
    private void handleEnrollmentManagement() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("Enrollment Management functionality will be implemented next.");
        pressEnterToContinue();
    }
    
    /**
     * Handle grade management
     */
    private void handleGradeManagement() {
        System.out.println("\n--- Grade Management ---");
        System.out.println("Grade Management functionality will be implemented next.");
        pressEnterToContinue();
    }
    
    /**
     * Handle reports and analytics
     */
    private void handleReportsAndAnalytics() {
        System.out.println("\n--- Reports & Analytics ---");
        System.out.println("Reports functionality will be implemented next.");
        pressEnterToContinue();
    }
    
    /**
     * Handle import/export operations with NIO.2 demonstration
     */
    private void handleImportExport() {
        System.out.println("\n--- Import/Export Data (NIO.2 Demonstration) ---");
        
        try {
            ImportExportService ioService = new ImportExportService();
            
            System.out.println("1. Checking default import paths...");
            Path studentsPath = ioService.getDefaultStudentsImportPath();
            Path coursesPath = ioService.getDefaultCoursesImportPath();
            
            System.out.println("   Students CSV: " + studentsPath);
            System.out.println("   Courses CSV: " + coursesPath);
            
            // Demonstrate CSV validation
            System.out.println("\n2. Validating CSV formats...");
            String[] studentHeaders = {"student_id", "reg_no", "first_name", "last_name", "email", "department"};
            String[] courseHeaders = {"course_code", "title", "credits", "department", "semester", "instructor_id"};
            
            boolean studentsValid = ioService.validateCsvFormat(studentsPath, studentHeaders);
            boolean coursesValid = ioService.validateCsvFormat(coursesPath, courseHeaders);
            
            System.out.println("   Students CSV valid: " + (studentsValid ? "✓" : "✗"));
            System.out.println("   Courses CSV valid: " + (coursesValid ? "✓" : "✗"));
            
            // Demonstrate import if files exist
            if (studentsValid) {
                System.out.println("\n3. Importing students...");
                var students = ioService.importStudents(studentsPath);
                System.out.println("   Imported " + students.size() + " students successfully");
                
                // Show sample student data
                if (!students.isEmpty()) {
                    System.out.println("   Sample student: " + students.get(0).getName().getFullName() + 
                                     " (" + students.get(0).getDepartment() + ")");
                }
            }
            
            if (coursesValid) {
                System.out.println("\n4. Importing courses...");
                var courses = ioService.importCourses(coursesPath);
                System.out.println("   Imported " + courses.size() + " courses successfully");
                
                // Show sample course data
                if (!courses.isEmpty()) {
                    System.out.println("   Sample course: " + courses.get(0).getTitle() + 
                                     " (" + courses.get(0).getCourseCode().getFullCode() + ")");
                }
            }
            
            System.out.println("\n✓ Import/Export demonstration completed using NIO.2 APIs");
            
        } catch (Exception e) {
            System.err.println("Error during import/export: " + e.getMessage());
            System.out.println("This demonstrates exception handling in file operations");
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Handle backup and utilities with NIO.2 file operations
     */
    private void handleBackupUtilities() {
        System.out.println("\n--- Backup & Utilities (NIO.2 File Operations) ---");
        
        try {
            BackupService backupService = new BackupService();
            
            System.out.println("1. Creating system health check report...");
            Path healthReport = backupService.createHealthCheckReport();
            System.out.println("   Health report created: " + healthReport.getFileName());
            
            System.out.println("\n2. Listing existing backups...");
            Path[] backups = backupService.listBackups();
            if (backups.length > 0) {
                System.out.println("   Found " + backups.length + " backup files:");
                for (int i = 0; i < Math.min(backups.length, 3); i++) {
                    System.out.println("   - " + backups[i].getFileName());
                }
                if (backups.length > 3) {
                    System.out.println("   ... and " + (backups.length - 3) + " more");
                }
            } else {
                System.out.println("   No existing backups found");
            }
            
            System.out.println("\n3. Creating new backup...");
            Path newBackup = backupService.createBackup();
            System.out.println("   Backup created: " + newBackup.getFileName());
            
            System.out.println("\n4. Backup information:");
            String backupInfo = backupService.getBackupInfo(newBackup);
            System.out.println(backupInfo);
            
            System.out.println("✓ Backup operations completed using NIO.2 file visitor pattern");
            
        } catch (Exception e) {
            System.err.println("Error during backup operations: " + e.getMessage());
            System.out.println("This demonstrates robust exception handling in file operations");
        }
        
        pressEnterToContinue();
    }
    
    /**
     * Handle system configuration
     */
    private void handleSystemConfiguration() {
        System.out.println("\n--- System Configuration ---");
        System.out.println(config.getConfigurationSummary());
        System.out.println("Configuration options will be implemented next.");
        pressEnterToContinue();
    }
    
    /**
     * Handle demo operations - demonstrate various Java features
     */
    private void handleDemoOperations() {
        System.out.println("\n--- Demo Operations ---");
        System.out.println("1. Operator Demonstrations");
        System.out.println("2. Loop Demonstrations");
        System.out.println("3. Array Operations");
        System.out.println("4. String Methods Demo");
        System.out.println("5. Object Creation Demo");
        System.out.println("0. Back to Main Menu");
        
        System.out.print("Choose demo (0-5): ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            handleDemoChoice(choice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }
    
    /**
     * Handle demo choice with various Java demonstrations
     */
    private void handleDemoChoice(int choice) {
        switch (choice) {
            case 1:
                demonstrateOperators();
                break;
            case 2:
                demonstrateLoops();
                break;
            case 3:
                demonstrateArrays();
                break;
            case 4:
                demonstrateStrings();
                break;
            case 5:
                demonstrateObjectCreation();
                break;
            case 0:
                return; // Back to main menu
            default:
                System.out.println("Invalid demo choice.");
        }
        pressEnterToContinue();
    }
    
    /**
     * Demonstrate operators and operator precedence
     */
    private void demonstrateOperators() {
        System.out.println("\n--- Operator Demonstrations ---");
        
        // Arithmetic operators
        int a = 10, b = 3;
        System.out.println("Arithmetic Operators:");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a + b = " + (a + b));
        System.out.println("a - b = " + (a - b));
        System.out.println("a * b = " + (a * b));
        System.out.println("a / b = " + (a / b));
        System.out.println("a % b = " + (a % b));
        
        // Operator precedence demonstration
        System.out.println("\nOperator Precedence:");
        int result1 = a + b * 2; // Multiplication first, then addition
        int result2 = (a + b) * 2; // Parentheses change precedence
        System.out.println("a + b * 2 = " + result1 + " (multiplication first)");
        System.out.println("(a + b) * 2 = " + result2 + " (parentheses first)");
        
        // Relational operators
        System.out.println("\nRelational Operators:");
        System.out.println("a > b: " + (a > b));
        System.out.println("a < b: " + (a < b));
        System.out.println("a >= b: " + (a >= b));
        System.out.println("a <= b: " + (a <= b));
        System.out.println("a == b: " + (a == b));
        System.out.println("a != b: " + (a != b));
        
        // Logical operators
        System.out.println("\nLogical Operators:");
        boolean x = true, y = false;
        System.out.println("x = " + x + ", y = " + y);
        System.out.println("x && y: " + (x && y));
        System.out.println("x || y: " + (x || y));
        System.out.println("!x: " + (!x));
        
        // Bitwise operators
        System.out.println("\nBitwise Operators:");
        int m = 5, n = 3; // 5 = 101, 3 = 011 in binary
        System.out.println("m = " + m + " (101), n = " + n + " (011)");
        System.out.println("m & n = " + (m & n) + " (AND)");
        System.out.println("m | n = " + (m | n) + " (OR)");
        System.out.println("m ^ n = " + (m ^ n) + " (XOR)");
        System.out.println("~m = " + (~m) + " (NOT)");
    }
    
    /**
     * Demonstrate various loop constructs
     */
    private void demonstrateLoops() {
        System.out.println("\n--- Loop Demonstrations ---");
        
        // While loop
        System.out.println("While Loop (counting down from 5):");
        int count = 5;
        while (count > 0) {
            System.out.print(count + " ");
            count--;
        }
        System.out.println();
        
        // Do-while loop
        System.out.println("\nDo-While Loop (at least one execution):");
        int x = 0;
        do {
            System.out.print("Executed once: " + x + " ");
            x++;
        } while (x < 0); // Condition is false, but executes once
        System.out.println();
        
        // For loop
        System.out.println("\nFor Loop (printing even numbers 0-10):");
        for (int i = 0; i <= 10; i += 2) {
            System.out.print(i + " ");
        }
        System.out.println();
        
        // Enhanced for loop with array
        System.out.println("\nEnhanced For Loop (array iteration):");
        String[] courses = {"Math", "Physics", "Chemistry", "Biology"};
        for (String course : courses) {
            System.out.print(course + " ");
        }
        System.out.println();
        
        // Nested loops with labeled break
        System.out.println("\nNested Loops with Labeled Break:");
        outer: for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                if (i == 2 && j == 2) {
                    System.out.print("[BREAK] ");
                    break outer; // Labeled break - exits both loops
                }
                System.out.print("(" + i + "," + j + ") ");
            }
        }
        System.out.println();
        
        // Continue demonstration
        System.out.println("\nContinue demonstration (skip even numbers):");
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue; // Skip even numbers
            }
            System.out.print(i + " ");
        }
        System.out.println();
    }
    
    /**
     * Demonstrate array operations and Arrays class utilities
     */
    private void demonstrateArrays() {
        System.out.println("\n--- Array Demonstrations ---");
        
        // Array creation and initialization
        int[] numbers = {64, 34, 25, 12, 22, 11, 90};
        String[] courseCodes = {"CS101", "MATH201", "PHYS101", "CHEM101"};
        
        System.out.println("Original array: " + java.util.Arrays.toString(numbers));
        
        // Array sorting
        int[] sortedNumbers = numbers.clone();
        java.util.Arrays.sort(sortedNumbers);
        System.out.println("Sorted array: " + java.util.Arrays.toString(sortedNumbers));
        
        // Array searching
        int searchValue = 25;
        int index = java.util.Arrays.binarySearch(sortedNumbers, searchValue);
        System.out.println("Index of " + searchValue + " in sorted array: " + index);
        
        // Array filling
        int[] filledArray = new int[5];
        java.util.Arrays.fill(filledArray, 42);
        System.out.println("Filled array: " + java.util.Arrays.toString(filledArray));
        
        // Course code array operations
        System.out.println("Course codes: " + java.util.Arrays.toString(courseCodes));
        java.util.Arrays.sort(courseCodes);
        System.out.println("Sorted course codes: " + java.util.Arrays.toString(courseCodes));
    }
    
    /**
     * Demonstrate string methods
     */
    private void demonstrateStrings() {
        System.out.println("\n--- String Method Demonstrations ---");
        
        String original = "  Campus Course Records Manager  ";
        System.out.println("Original: '" + original + "'");
        
        // String methods
        System.out.println("Length: " + original.length());
        System.out.println("Trimmed: '" + original.trim() + "'");
        System.out.println("Uppercase: '" + original.toUpperCase() + "'");
        System.out.println("Lowercase: '" + original.toLowerCase() + "'");
        
        // Substring
        String trimmed = original.trim();
        System.out.println("Substring (0,6): '" + trimmed.substring(0, 6) + "'");
        
        // String splitting and joining
        String courseList = "Math,Physics,Chemistry,Biology";
        String[] courses = courseList.split(",");
        System.out.println("Split courses: " + java.util.Arrays.toString(courses));
        
        String joined = String.join(" | ", courses);
        System.out.println("Joined with ' | ': " + joined);
        
        // String comparison
        String str1 = "Java";
        String str2 = "Java";
        String str3 = new String("Java");
        
        System.out.println("str1 == str2: " + (str1 == str2)); // true (string pool)
        System.out.println("str1 == str3: " + (str1 == str3)); // false (different objects)
        System.out.println("str1.equals(str3): " + str1.equals(str3)); // true (content)
        System.out.println("str1.compareTo(str2): " + str1.compareTo(str2)); // 0 (equal)
    }
    
    /**
     * Demonstrate object creation and polymorphism
     */
    private void demonstrateObjectCreation() {
        System.out.println("\n--- Object Creation Demonstrations ---");
        System.out.println("This will demonstrate domain object creation.");
        System.out.println("Full implementation coming in service layer...");
        
        // Basic object creation examples
        System.out.println("Creating enum instances:");
        for (edu.ccrm.domain.Semester semester : edu.ccrm.domain.Semester.values()) {
            System.out.println("- " + semester);
        }
        
        System.out.println("\nCreating Grade instances:");
        for (edu.ccrm.domain.Grade grade : edu.ccrm.domain.Grade.values()) {
            System.out.println("- " + grade);
        }
    }
    
    /**
     * Handle application exit
     */
    private void handleExit() {
        System.out.println("\nExiting CCRM...");
        System.out.println("Application uptime: " + config.getUptimeSeconds() + " seconds");
        running = false;
    }
    
    /**
     * Utility method to pause for user input
     */
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}