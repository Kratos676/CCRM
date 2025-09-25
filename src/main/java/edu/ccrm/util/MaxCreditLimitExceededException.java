package edu.ccrm.util;

/**
 * Custom unchecked exception for credit limit violations
 * Demonstrates runtime exception and validation logic
 */
public class MaxCreditLimitExceededException extends RuntimeException {
    
    private final String studentId;
    private final int currentCredits;
    private final int maxAllowedCredits;
    private final int attemptedCredits;
    
    /**
     * Constructor with basic message
     * @param message exception message
     */
    public MaxCreditLimitExceededException(String message) {
        super(message);
        this.studentId = null;
        this.currentCredits = 0;
        this.maxAllowedCredits = 0;
        this.attemptedCredits = 0;
    }
    
    /**
     * Constructor with detailed credit information
     * @param studentId student ID
     * @param currentCredits current enrolled credits
     * @param maxAllowedCredits maximum allowed credits
     * @param attemptedCredits credits trying to add
     */
    public MaxCreditLimitExceededException(String studentId, int currentCredits, 
                                         int maxAllowedCredits, int attemptedCredits) {
        super(String.format(
            "Credit limit exceeded for student %s: Current=%d, Max=%d, Attempted=%d, Total would be=%d",
            studentId, currentCredits, maxAllowedCredits, attemptedCredits, 
            currentCredits + attemptedCredits));
        
        this.studentId = studentId;
        this.currentCredits = currentCredits;
        this.maxAllowedCredits = maxAllowedCredits;
        this.attemptedCredits = attemptedCredits;
    }
    
    /**
     * Constructor with message and cause
     * @param message exception message
     * @param cause underlying cause
     */
    public MaxCreditLimitExceededException(String message, Throwable cause) {
        super(message, cause);
        this.studentId = null;
        this.currentCredits = 0;
        this.maxAllowedCredits = 0;
        this.attemptedCredits = 0;
    }
    
    // Getters for exception details
    public String getStudentId() { return studentId; }
    public int getCurrentCredits() { return currentCredits; }
    public int getMaxAllowedCredits() { return maxAllowedCredits; }
    public int getAttemptedCredits() { return attemptedCredits; }
    
    /**
     * Calculate how many credits over the limit
     * @return excess credits
     */
    public int getExcessCredits() {
        if (hasDetailedInfo()) {
            return (currentCredits + attemptedCredits) - maxAllowedCredits;
        }
        return 0;
    }
    
    /**
     * Calculate available credit space
     * @return remaining credits that can be enrolled
     */
    public int getAvailableCredits() {
        if (hasDetailedInfo()) {
            return Math.max(0, maxAllowedCredits - currentCredits);
        }
        return 0;
    }
    
    /**
     * Check if detailed information is available
     * @return true if all credit information is available
     */
    public boolean hasDetailedInfo() {
        return studentId != null;
    }
    
    /**
     * Get suggested action message
     * @return helpful message for resolving the issue
     */
    public String getSuggestedAction() {
        if (!hasDetailedInfo()) {
            return "Please check your current enrollment and credit limits.";
        }
        
        int available = getAvailableCredits();
        if (available > 0) {
            return String.format("You can enroll in up to %d more credits this semester.", available);
        } else {
            return "You are already at your maximum credit limit. " +
                   "Consider dropping a course before enrolling in new ones.";
        }
    }
    
    /**
     * Get formatted error report
     * @return detailed error report
     */
    public String getErrorReport() {
        StringBuilder report = new StringBuilder();
        report.append("Credit Limit Violation Report\n");
        report.append("-".repeat(30)).append("\n");
        
        if (hasDetailedInfo()) {
            report.append(String.format("Student ID: %s\n", studentId));
            report.append(String.format("Current Credits: %d\n", currentCredits));
            report.append(String.format("Maximum Allowed: %d\n", maxAllowedCredits));
            report.append(String.format("Attempted to Add: %d\n", attemptedCredits));
            report.append(String.format("Would Total: %d\n", currentCredits + attemptedCredits));
            report.append(String.format("Excess Credits: %d\n", getExcessCredits()));
            report.append(String.format("Available Credits: %d\n", getAvailableCredits()));
        } else {
            report.append("Detailed information not available\n");
        }
        
        report.append("-".repeat(30)).append("\n");
        report.append("Suggested Action: ").append(getSuggestedAction()).append("\n");
        
        return report.toString();
    }
}