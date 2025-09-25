package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for importing and exporting data using NIO.2 and Streams
 * Demonstrates NIO.2 Path/Files API, Stream processing, and CSV operations
 */
public class ImportExportService {
    
    private final AppConfig config;
    private final DateTimeFormatter dateFormatter;
    
    /**
     * Constructor
     */
    public ImportExportService() {
        this.config = AppConfig.getInstance();
        this.dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    
    /**
     * Import students from CSV file using NIO.2 and Streams
     * @param csvFilePath path to CSV file
     * @return list of imported students
     * @throws IOException if file operations fail
     */
    public List<Student> importStudents(Path csvFilePath) throws IOException {
        // Validate file exists
        if (!Files.exists(csvFilePath)) {
            throw new IOException("File does not exist: " + csvFilePath);
        }
        
        System.out.println("Importing students from: " + csvFilePath);
        
        try (Stream<String> lines = Files.lines(csvFilePath, StandardCharsets.UTF_8)) {
            List<Student> students = lines
                .skip(1) // Skip header line
                .filter(line -> !line.trim().isEmpty()) // Filter empty lines
                .map(this::parseStudentFromCsv)
                .filter(Objects::nonNull) // Filter failed parsing attempts
                .collect(Collectors.toList());
            
            System.out.println("Successfully imported " + students.size() + " students");
            return students;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Parse student from CSV line
     * @param csvLine CSV line
     * @return Student object or null if parsing fails
     */
    private Student parseStudentFromCsv(String csvLine) {
        try {
            String[] fields = csvLine.split(",");
            if (fields.length < 6) {
                System.err.println("Invalid CSV line (insufficient fields): " + csvLine);
                return null;
            }
            
            String id = fields[0].trim();
            String regNo = fields[1].trim();
            String firstName = fields[2].trim();
            String lastName = fields[3].trim();
            String email = fields[4].trim();
            String department = fields[5].trim();
            int semester = fields.length > 6 ? Integer.parseInt(fields[6].trim()) : 1;
            
            Name name = new Name(firstName, lastName);
            LocalDate dateOfBirth = LocalDate.now().minusYears(20); // Default age
            
            Student student = new Student(id, regNo, name, email, dateOfBirth, department);
            student.setCurrentSemester(semester);
            
            return student;
            
        } catch (Exception e) {
            System.err.println("Error parsing student CSV line: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Export students to CSV file
     * @param students list of students to export
     * @param csvFilePath output file path
     * @throws IOException if file operations fail
     */
    public void exportStudents(List<Student> students, Path csvFilePath) throws IOException {
        // Ensure parent directory exists
        Path parentDir = csvFilePath.getParent();
        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
        
        System.out.println("Exporting " + students.size() + " students to: " + csvFilePath);
        
        List<String> lines = new ArrayList<>();
        
        // Add header
        lines.add("student_id,reg_no,first_name,last_name,email,department,semester,gpa,status,enrollment_date");
        
        // Add student data using Stream API
        List<String> studentLines = students.stream()
            .map(this::formatStudentAsCsv)
            .collect(Collectors.toList());
        
        lines.addAll(studentLines);
        
        // Write to file using NIO.2
        Files.write(csvFilePath, lines, StandardCharsets.UTF_8, 
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        System.out.println("Students exported successfully");
    }
    
    /**
     * Format student as CSV line
     * @param student student object
     * @return CSV formatted string
     */
    private String formatStudentAsCsv(Student student) {
        return String.format("%s,%s,%s,%s,%s,%s,%d,%.2f,%s,%s",
            student.getId(),
            student.getRegistrationNumber(),
            student.getName().getFirstName(),
            student.getName().getLastName(),
            student.getEmail(),
            student.getDepartment(),
            student.getCurrentSemester(),
            student.calculateGPA(),
            student.isActive() ? "ACTIVE" : "INACTIVE",
            student.getEnrollmentDate().format(dateFormatter));
    }
    
    /**
     * Import courses from CSV file
     * @param csvFilePath path to CSV file
     * @return list of imported courses
     * @throws IOException if file operations fail
     */
    public List<Course> importCourses(Path csvFilePath) throws IOException {
        if (!Files.exists(csvFilePath)) {
            throw new IOException("File does not exist: " + csvFilePath);
        }
        
        System.out.println("Importing courses from: " + csvFilePath);
        
        try (Stream<String> lines = Files.lines(csvFilePath, StandardCharsets.UTF_8)) {
            List<Course> courses = lines
                .skip(1) // Skip header
                .filter(line -> !line.trim().isEmpty())
                .map(this::parseCourseFromCsv)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            
            System.out.println("Successfully imported " + courses.size() + " courses");
            return courses;
        }
    }
    
    /**
     * Parse course from CSV line using Builder pattern
     * @param csvLine CSV line
     * @return Course object or null if parsing fails
     */
    private Course parseCourseFromCsv(String csvLine) {
        try {
            String[] fields = csvLine.split(",");
            if (fields.length < 6) {
                System.err.println("Invalid CSV line: " + csvLine);
                return null;
            }
            
            String codeStr = fields[0].trim();
            String title = fields[1].trim();
            int credits = Integer.parseInt(fields[2].trim());
            String department = fields[3].trim();
            String semesterStr = fields[4].trim();
            String instructorId = fields.length > 5 ? fields[5].trim() : null;
            int maxCapacity = fields.length > 6 ? Integer.parseInt(fields[6].trim()) : 30;
            
            // Parse course code (assuming format like "CS101-A")
            String[] codeParts = codeStr.split("-");
            if (codeParts.length != 2) {
                System.err.println("Invalid course code format: " + codeStr);
                return null;
            }
            
            String deptCode = codeParts[0].replaceAll("\\d", "");
            int courseNumber = Integer.parseInt(codeParts[0].replaceAll("\\D", ""));
            String section = codeParts[1];
            
            CourseCode courseCode = new CourseCode(deptCode, courseNumber, section);
            Semester semester = Semester.valueOf(semesterStr.toUpperCase());
            
            // Use Builder pattern
            Course.Builder builder = new Course.Builder(courseCode, title)
                .credits(credits)
                .department(department)
                .semester(semester)
                .maxCapacity(maxCapacity);
            
            if (instructorId != null && !instructorId.isEmpty()) {
                builder.instructor(instructorId);
            }
            
            return builder.build();
            
        } catch (Exception e) {
            System.err.println("Error parsing course CSV line: " + csvLine + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Export courses to CSV file
     * @param courses list of courses to export
     * @param csvFilePath output file path
     * @throws IOException if file operations fail
     */
    public void exportCourses(List<Course> courses, Path csvFilePath) throws IOException {
        Files.createDirectories(csvFilePath.getParent());
        
        System.out.println("Exporting " + courses.size() + " courses to: " + csvFilePath);
        
        List<String> lines = new ArrayList<>();
        lines.add("course_code,title,credits,department,semester,instructor_id,max_capacity,current_enrollment,status");
        
        courses.stream()
            .map(this::formatCourseAsCsv)
            .forEach(lines::add);
        
        Files.write(csvFilePath, lines, StandardCharsets.UTF_8, 
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        
        System.out.println("Courses exported successfully");
    }
    
    /**
     * Format course as CSV line
     * @param course course object
     * @return CSV formatted string
     */
    private String formatCourseAsCsv(Course course) {
        return String.format("%s,%s,%d,%s,%s,%s,%d,%d,%s",
            course.getCourseCode().getFullCode(),
            course.getTitle().replace(",", ";"), // Handle commas in title
            course.getCredits(),
            course.getDepartment(),
            course.getSemester().name(),
            course.getInstructorId() != null ? course.getInstructorId() : "",
            course.getMaxCapacity(),
            course.getCurrentEnrollment(),
            course.isActive() ? "ACTIVE" : "INACTIVE");
    }
    
    /**
     * Export system data to timestamped directory
     * @param students list of students
     * @param courses list of courses
     * @return path to export directory
     * @throws IOException if export fails
     */
    public Path exportSystemData(List<Student> students, List<Course> courses) throws IOException {
        // Create timestamped export directory
        String timestamp = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Path exportDir = config.getExportDirectory().resolve("export_" + timestamp);
        Files.createDirectories(exportDir);
        
        System.out.println("Exporting system data to: " + exportDir);
        
        // Export students
        if (!students.isEmpty()) {
            exportStudents(students, exportDir.resolve("students.csv"));
        }
        
        // Export courses
        if (!courses.isEmpty()) {
            exportCourses(courses, exportDir.resolve("courses.csv"));
        }
        
        // Create export summary
        createExportSummary(exportDir, students.size(), courses.size());
        
        System.out.println("System data export completed");
        return exportDir;
    }
    
    /**
     * Create export summary file
     * @param exportDir export directory
     * @param studentCount number of students exported
     * @param courseCount number of courses exported
     * @throws IOException if file creation fails
     */
    private void createExportSummary(Path exportDir, int studentCount, int courseCount) throws IOException {
        List<String> summary = Arrays.asList(
            "CCRM Export Summary",
            "===================",
            "Export Date: " + LocalDate.now().format(dateFormatter),
            "Export Time: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")),
            "",
            "Exported Data:",
            "- Students: " + studentCount,
            "- Courses: " + courseCount,
            "",
            "Files Created:",
            "- students.csv",
            "- courses.csv",
            "- export_summary.txt (this file)",
            "",
            "Generated by Campus Course & Records Manager v1.0"
        );
        
        Files.write(exportDir.resolve("export_summary.txt"), summary, StandardCharsets.UTF_8);
    }
    
    /**
     * Get default import path for students
     * @return path to default students CSV
     */
    public Path getDefaultStudentsImportPath() {
        return Paths.get("test-data", "students.csv");
    }
    
    /**
     * Get default import path for courses
     * @return path to default courses CSV
     */
    public Path getDefaultCoursesImportPath() {
        return Paths.get("test-data", "courses.csv");
    }
    
    /**
     * Get default import path for instructors
     * @return path to default instructors CSV
     */
    public Path getDefaultInstructorsImportPath() {
        return Paths.get("test-data", "instructors.csv");
    }
    
    /**
     * Validate CSV file format
     * @param csvFilePath path to CSV file
     * @param expectedHeaders expected header columns
     * @return true if format is valid
     * @throws IOException if file operations fail
     */
    public boolean validateCsvFormat(Path csvFilePath, String[] expectedHeaders) throws IOException {
        if (!Files.exists(csvFilePath)) {
            return false;
        }
        
        try (Stream<String> lines = Files.lines(csvFilePath, StandardCharsets.UTF_8)) {
            Optional<String> firstLine = lines.findFirst();
            if (firstLine.isPresent()) {
                String[] headers = firstLine.get().split(",");
                return Arrays.equals(expectedHeaders, 
                    Arrays.stream(headers).map(String::trim).toArray(String[]::new));
            }
        }
        
        return false;
    }
}