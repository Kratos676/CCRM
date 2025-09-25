package edu.ccrm.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Student class extending Person and implementing interfaces
 * Demonstrates inheritance, polymorphism, composition, and interface implementation
 * Shows diamond problem resolution with explicit override
 */
public class Student extends Person implements Persistable, Auditable {
    private final String registrationNumber;
    private String department;
    private int currentSemester;
    private final Set<String> enrolledCourses; // Composition: Student has courses
    private final Map<String, Grade> courseGrades; // Course code -> Grade mapping
    private LocalDate enrollmentDate;
    private final List<String> auditTrail; // For Auditable interface
    private final long creationTime; // For Auditable interface
    
    /**
     * Constructor demonstrating super() usage
     */
    public Student(String id, String registrationNumber, Name name, String email, 
                   LocalDate dateOfBirth, String department) {
        // Call parent constructor using super()
        super(id, name, email, dateOfBirth);
        
        // Validation
        assert registrationNumber != null && !registrationNumber.trim().isEmpty() : 
            "Registration number is required";
        assert department != null && !department.trim().isEmpty() : 
            "Department is required";
        
        this.registrationNumber = registrationNumber;
        this.department = department;
        this.currentSemester = 1;
        this.enrolledCourses = new LinkedHashSet<>(); // Preserve insertion order
        this.courseGrades = new HashMap<>();
        this.enrollmentDate = LocalDate.now();
        this.auditTrail = new ArrayList<>();
        this.creationTime = System.currentTimeMillis();
        
        // Add initial audit entry
        addAuditEntry("Student created: " + name.getFullName());
    }
    
    // Getters and setters
    public String getRegistrationNumber() { return registrationNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        assert department != null && !department.trim().isEmpty() : "Department cannot be empty";
        this.department = department; 
    }
    
    public int getCurrentSemester() { return currentSemester; }
    public void setCurrentSemester(int currentSemester) { 
        assert currentSemester > 0 && currentSemester <= 8 : "Semester must be between 1 and 8";
        this.currentSemester = currentSemester; 
    }
    
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    
    /**
     * Get defensive copy of enrolled courses
     * @return unmodifiable set of course codes
     */
    public Set<String> getEnrolledCourses() {
        return Collections.unmodifiableSet(enrolledCourses);
    }
    
    /**
     * Get defensive copy of course grades
     * @return unmodifiable map of grades
     */
    public Map<String, Grade> getCourseGrades() {
        return Collections.unmodifiableMap(courseGrades);
    }
    
    /**
     * Enroll in a course
     * @param courseCode course code to enroll in
     * @return true if enrollment successful
     */
    public boolean enrollInCourse(String courseCode) {
        assert courseCode != null && !courseCode.trim().isEmpty() : "Course code required";
        boolean enrolled = enrolledCourses.add(courseCode.trim().toUpperCase());
        if (enrolled) {
            addAuditEntry("Enrolled in course: " + courseCode);
        }
        return enrolled;
    }
    
    /**
     * Unenroll from a course
     * @param courseCode course code to unenroll from
     * @return true if unenrollment successful
     */
    public boolean unenrollFromCourse(String courseCode) {
        assert courseCode != null : "Course code cannot be null";
        String normalizedCode = courseCode.trim().toUpperCase();
        
        // Remove grade if exists
        courseGrades.remove(normalizedCode);
        return enrolledCourses.remove(normalizedCode);
    }
    
    /**
     * Record grade for a course
     * @param courseCode course code
     * @param grade grade received
     */
    public void recordGrade(String courseCode, Grade grade) {
        assert courseCode != null && !courseCode.trim().isEmpty() : "Course code required";
        assert grade != null : "Grade cannot be null";
        
        String normalizedCode = courseCode.trim().toUpperCase();
        
        // Student must be enrolled in the course to receive a grade
        if (!enrolledCourses.contains(normalizedCode)) {
            throw new IllegalArgumentException("Student is not enrolled in course: " + courseCode);
        }
        
        courseGrades.put(normalizedCode, grade);
        addAuditEntry(String.format("Grade recorded for %s: %s", normalizedCode, grade.getLetter()));
    }
    
    /**
     * Calculate current GPA
     * Assumes each course is worth 3 credits (simplification)
     * @return GPA value
     */
    public double calculateGPA() {
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        
        // Using Stream API for calculation
        double totalGradePoints = courseGrades.values().stream()
            .mapToDouble(grade -> grade.getGradePoint() * 3) // 3 credits per course
            .sum();
        
        int totalCredits = courseGrades.size() * 3;
        return totalGradePoints / totalCredits;
    }
    
    /**
     * Get completed courses (courses with grades)
     * @return set of completed course codes
     */
    public Set<String> getCompletedCourses() {
        return courseGrades.keySet().stream()
            .collect(Collectors.toSet());
    }
    
    /**
     * Get pending courses (enrolled but no grade)
     * @return set of pending course codes
     */
    public Set<String> getPendingCourses() {
        return enrolledCourses.stream()
            .filter(course -> !courseGrades.containsKey(course))
            .collect(Collectors.toSet());
    }
    
    /**
     * Check if student has passed all completed courses
     * @return true if all grades are passing
     */
    public boolean isInGoodStanding() {
        return courseGrades.values().stream()
            .allMatch(Grade::isPassing);
    }
    
    // Polymorphic method implementations
    @Override
    public String getPersonType() {
        return "STUDENT";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Reg No: %s | Dept: %s | Sem: %d | GPA: %.2f | Courses: %d", 
            registrationNumber, department, currentSemester, calculateGPA(), enrolledCourses.size());
    }
    
    /**
     * Generate student transcript
     * @return formatted transcript string
     */
    public String generateTranscript() {
        StringBuilder transcript = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        transcript.append("=".repeat(60)).append("\n");
        transcript.append("STUDENT TRANSCRIPT\n");
        transcript.append("=".repeat(60)).append("\n");
        transcript.append(String.format("Student ID: %s\n", getId()));
        transcript.append(String.format("Registration No: %s\n", registrationNumber));
        transcript.append(String.format("Name: %s\n", getName().getFullName()));
        transcript.append(String.format("Department: %s\n", department));
        transcript.append(String.format("Current Semester: %d\n", currentSemester));
        transcript.append(String.format("Enrollment Date: %s\n", enrollmentDate.format(formatter)));
        transcript.append("-".repeat(60)).append("\n");
        transcript.append("COURSE GRADES:\n");
        transcript.append("-".repeat(60)).append("\n");
        
        if (courseGrades.isEmpty()) {
            transcript.append("No grades recorded yet.\n");
        } else {
            courseGrades.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> transcript.append(String.format("%-15s | %s\n", 
                    entry.getKey(), entry.getValue())));
        }
        
        transcript.append("-".repeat(60)).append("\n");
        transcript.append(String.format("Current GPA: %.2f\n", calculateGPA()));
        transcript.append(String.format("Academic Standing: %s\n", 
            isInGoodStanding() ? "Good Standing" : "Academic Warning"));
        transcript.append("=".repeat(60)).append("\n");
        
        return transcript.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Student{regNo='%s', name=%s, dept='%s', sem=%d, gpa=%.2f}", 
            registrationNumber, getName(), department, currentSemester, calculateGPA());
    }
    
    // Interface implementations
    
    // Persistable interface implementation
    @Override
    public boolean isValid() {
        return getId() != null && !getId().trim().isEmpty() &&
               registrationNumber != null && !registrationNumber.trim().isEmpty() &&
               getName() != null &&
               getEmail() != null && getEmail().contains("@") &&
               department != null && !department.trim().isEmpty();
    }
    
    @Override
    public String toCsv() {
        return String.format("%s,%s,%s,%s,%s,%s,%d,%.2f,%s",
            getId(),
            registrationNumber,
            getName().getFullName().replace(",", ";"), // Handle commas in names
            getEmail(),
            department,
            enrollmentDate,
            currentSemester,
            calculateGPA(),
            isActive() ? "ACTIVE" : "INACTIVE");
    }
    
    // Auditable interface implementation
    @Override
    public List<String> getAuditTrail() {
        return new ArrayList<>(auditTrail); // Defensive copy
    }
    
    @Override
    public void addAuditEntry(String entry) {
        if (entry != null && !entry.trim().isEmpty()) {
            String timestampedEntry = String.format("[%s] %s", 
                java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), 
                entry);
            auditTrail.add(timestampedEntry);
        }
    }
    
    @Override
    public long getCreationTime() {
        return creationTime;
    }
    
    /**
     * Diamond problem resolution: explicit override of conflicting default method
     * Both Persistable and Auditable have getLastModified() default methods
     * We explicitly choose Auditable's implementation and call it
     */
    @Override
    public long getLastModified() {
        // Explicitly call Auditable's default implementation
        return Auditable.super.getLastModified();
    }
    
    /**
     * Inner class for student-specific utilities
     * Demonstrates non-static inner class
     */
    public class StudentProgress {
        /**
         * Calculate completion percentage
         * @param totalCoursesRequired total courses required for graduation
         * @return completion percentage
         */
        public double getCompletionPercentage(int totalCoursesRequired) {
            if (totalCoursesRequired <= 0) return 0.0;
            return (double) getCompletedCourses().size() / totalCoursesRequired * 100.0;
        }
        
        /**
         * Get courses needed for graduation
         * @param totalCoursesRequired total courses required
         * @return number of courses still needed
         */
        public int getCoursesNeeded(int totalCoursesRequired) {
            return Math.max(0, totalCoursesRequired - getCompletedCourses().size());
        }
        
        /**
         * Check if eligible for graduation
         * @param totalCoursesRequired total courses required
         * @param minimumGPA minimum GPA required
         * @return true if eligible for graduation
         */
        public boolean isEligibleForGraduation(int totalCoursesRequired, double minimumGPA) {
            return getCompletedCourses().size() >= totalCoursesRequired &&
                   calculateGPA() >= minimumGPA &&
                   isInGoodStanding();
        }
    }
}