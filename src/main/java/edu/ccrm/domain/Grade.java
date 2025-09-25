package edu.ccrm.domain;

/**
 * Enumeration representing academic grades with grade points
 * Demonstrates enum with constructors, fields, and calculation methods
 */
public enum Grade {
    S("S", 10.0, "Outstanding"),
    A("A", 9.0, "Excellent"), 
    B("B", 8.0, "Very Good"),
    C("C", 7.0, "Good"),
    D("D", 6.0, "Satisfactory"),
    E("E", 5.0, "Pass"),
    F("F", 0.0, "Fail");
    
    private final String letter;
    private final double gradePoint;
    private final String description;
    
    /**
     * Enum constructor
     * @param letter Grade letter
     * @param gradePoint Numeric grade point value
     * @param description Grade description
     */
    Grade(String letter, double gradePoint, String description) {
        this.letter = letter;
        this.gradePoint = gradePoint;
        this.description = description;
    }
    
    // Getters
    public String getLetter() { return letter; }
    public double getGradePoint() { return gradePoint; }
    public String getDescription() { return description; }
    
    /**
     * Calculate grade from marks
     * @param marks marks obtained (0-100)
     * @return Grade enum value
     */
    public static Grade fromMarks(double marks) {
        // Demonstrate nested if-else and operator precedence
        if (marks >= 90.0) return S;
        else if (marks >= 80.0 && marks < 90.0) return A;
        else if (marks >= 70.0 && marks < 80.0) return B;
        else if (marks >= 60.0 && marks < 70.0) return C;
        else if (marks >= 50.0 && marks < 60.0) return D;
        else if (marks >= 40.0 && marks < 50.0) return E;
        else return F;
    }
    
    /**
     * Check if grade is passing
     * @return true if grade is passing (not F)
     */
    public boolean isPassing() {
        return this != F;
    }
    
    /**
     * Calculate grade points for given credits
     * @param credits number of credits
     * @return total grade points
     */
    public double calculateGradePoints(int credits) {
        // Operator precedence demonstration: multiplication before comparison
        return gradePoint * credits;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%.1f) - %s", letter, gradePoint, description);
    }
}