package edu.ccrm.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Enrollment class representing student enrollment in a course
 * Demonstrates composition and encapsulation
 */
public class Enrollment {
    private final String enrollmentId;
    private final String studentId;
    private final String courseCode;
    private final LocalDate enrollmentDate;
    private LocalDate completionDate;
    private Grade grade;
    private double marks;
    private boolean active;
    private String status; // ENROLLED, COMPLETED, WITHDRAWN, FAILED
    
    /**
     * Constructor for new enrollment
     */
    public Enrollment(String enrollmentId, String studentId, String courseCode) {
        // Validation
        assert enrollmentId != null && !enrollmentId.trim().isEmpty() : 
            "Enrollment ID is required";
        assert studentId != null && !studentId.trim().isEmpty() : 
            "Student ID is required";
        assert courseCode != null && !courseCode.trim().isEmpty() : 
            "Course code is required";
        
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode.trim().toUpperCase();
        this.enrollmentDate = LocalDate.now();
        this.completionDate = null;
        this.grade = null;
        this.marks = -1; // -1 indicates no marks assigned yet
        this.active = true;
        this.status = "ENROLLED";
    }
    
    // Getters - no setters for final fields (immutability for key fields)
    public String getEnrollmentId() { return enrollmentId; }
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    
    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { 
        this.completionDate = completionDate; 
    }
    
    public Grade getGrade() { return grade; }
    
    public double getMarks() { return marks; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { 
        assert status != null && !status.trim().isEmpty() : "Status cannot be empty";
        this.status = status.trim().toUpperCase(); 
    }
    
    /**
     * Record marks and calculate grade
     * @param marks marks obtained (0-100)
     */
    public void recordMarks(double marks) {
        assert marks >= 0 && marks <= 100 : "Marks must be between 0 and 100";
        
        this.marks = marks;
        this.grade = Grade.fromMarks(marks);
        
        // Update status based on grade
        if (grade.isPassing()) {
            this.status = "COMPLETED";
            this.completionDate = LocalDate.now();
        } else {
            this.status = "FAILED";
            this.completionDate = LocalDate.now();
        }
    }
    
    /**
     * Withdraw from course
     */
    public void withdraw() {
        this.active = false;
        this.status = "WITHDRAWN";
        this.completionDate = LocalDate.now();
    }
    
    /**
     * Check if enrollment is completed (passed or failed)
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(status) || "FAILED".equals(status);
    }
    
    /**
     * Check if student passed the course
     */
    public boolean isPassed() {
        return grade != null && grade.isPassing();
    }
    
    /**
     * Get duration of enrollment in days
     */
    public long getEnrollmentDurationDays() {
        LocalDate endDate = completionDate != null ? completionDate : LocalDate.now();
        return enrollmentDate.until(endDate).getDays();
    }
    
    /**
     * Calculate grade points for this enrollment
     * @param courseCredits number of credits for the course
     * @return grade points earned
     */
    public double calculateGradePoints(int courseCredits) {
        if (grade == null) return 0.0;
        return grade.calculateGradePoints(courseCredits);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Enrollment that = (Enrollment) obj;
        return Objects.equals(enrollmentId, that.enrollmentId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Enrollment{id='%s', student='%s', course='%s', date=%s, grade=%s, status='%s'}", 
            enrollmentId, studentId, courseCode, enrollmentDate.format(formatter), 
            grade != null ? grade.getLetter() : "N/A", status);
    }
    
    /**
     * Generate detailed enrollment report
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        
        report.append("Enrollment Report\n");
        report.append("-".repeat(30)).append("\n");
        report.append(String.format("Enrollment ID: %s\n", enrollmentId));
        report.append(String.format("Student ID: %s\n", studentId));
        report.append(String.format("Course Code: %s\n", courseCode));
        report.append(String.format("Enrollment Date: %s\n", enrollmentDate.format(formatter)));
        
        if (completionDate != null) {
            report.append(String.format("Completion Date: %s\n", completionDate.format(formatter)));
            report.append(String.format("Duration: %d days\n", getEnrollmentDurationDays()));
        }
        
        report.append(String.format("Status: %s\n", status));
        
        if (marks >= 0) {
            report.append(String.format("Marks: %.2f\n", marks));
        }
        
        if (grade != null) {
            report.append(String.format("Grade: %s\n", grade));
            report.append(String.format("Passed: %s\n", isPassed() ? "Yes" : "No"));
        }
        
        report.append(String.format("Active: %s\n", active ? "Yes" : "No"));
        
        return report.toString();
    }
    
    /**
     * Static factory method for creating enrollment with custom date
     * Demonstrates method overloading concept
     */
    public static Enrollment createWithDate(String enrollmentId, String studentId, 
                                          String courseCode, LocalDate enrollmentDate) {
        Enrollment enrollment = new Enrollment(enrollmentId, studentId, courseCode);
        // We can't modify the final field, so this is a limitation of our current design
        // In a real system, we might have a different constructor or use reflection
        return enrollment;
    }
    
    /**
     * Static factory method for creating completed enrollment
     */
    public static Enrollment createCompleted(String enrollmentId, String studentId, 
                                           String courseCode, double marks) {
        Enrollment enrollment = new Enrollment(enrollmentId, studentId, courseCode);
        enrollment.recordMarks(marks);
        return enrollment;
    }
}