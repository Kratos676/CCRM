package edu.ccrm.util;

/**
 * Custom checked exception for duplicate enrollment scenarios
 * Demonstrates custom exception creation and inheritance
 */
public class DuplicateEnrollmentException extends Exception {
    
    private final String studentId;
    private final String courseCode;
    
    /**
     * Constructor with message
     * @param message exception message
     */
    public DuplicateEnrollmentException(String message) {
        super(message);
        this.studentId = null;
        this.courseCode = null;
    }
    
    /**
     * Constructor with message and cause
     * @param message exception message
     * @param cause underlying cause
     */
    public DuplicateEnrollmentException(String message, Throwable cause) {
        super(message, cause);
        this.studentId = null;
        this.courseCode = null;
    }
    
    /**
     * Constructor with detailed information
     * @param studentId student ID involved
     * @param courseCode course code involved
     * @param message exception message
     */
    public DuplicateEnrollmentException(String studentId, String courseCode, String message) {
        super(String.format("Duplicate enrollment for student %s in course %s: %s", 
            studentId, courseCode, message));
        this.studentId = studentId;
        this.courseCode = courseCode;
    }
    
    /**
     * Get student ID involved in the duplicate enrollment
     * @return student ID or null if not specified
     */
    public String getStudentId() {
        return studentId;
    }
    
    /**
     * Get course code involved in the duplicate enrollment
     * @return course code or null if not specified
     */
    public String getCourseCode() {
        return courseCode;
    }
    
    /**
     * Check if detailed information is available
     * @return true if student ID and course code are available
     */
    public boolean hasDetailedInfo() {
        return studentId != null && courseCode != null;
    }
    
    /**
     * Get formatted error details
     * @return formatted error information
     */
    public String getFormattedDetails() {
        if (hasDetailedInfo()) {
            return String.format("Student: %s, Course: %s", studentId, courseCode);
        }
        return "No detailed information available";
    }
}