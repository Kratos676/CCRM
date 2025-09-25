package edu.ccrm.domain;

import java.util.Objects;

/**
 * Immutable value class representing a course code
 * Demonstrates immutability with final fields and defensive copying
 */
public final class CourseCode {
    private final String department;
    private final int number;
    private final String section;
    
    /**
     * Constructor with validation
     * @param department Department code (e.g., "CS", "MATH")
     * @param number Course number
     * @param section Section identifier
     */
    public CourseCode(String department, int number, String section) {
        // Input validation with assertions
        assert department != null && !department.trim().isEmpty() : "Department cannot be null or empty";
        assert number > 0 : "Course number must be positive";
        assert section != null && !section.trim().isEmpty() : "Section cannot be null or empty";
        
        // Defensive copying for strings (immutable anyway, but demonstrates concept)
        this.department = department.trim().toUpperCase();
        this.number = number;
        this.section = section.trim().toUpperCase();
    }
    
    // Only getters - no setters (immutable)
    public String getDepartment() { 
        return department; // Safe to return directly as String is immutable
    }
    
    public int getNumber() { 
        return number; 
    }
    
    public String getSection() { 
        return section; // Safe to return directly as String is immutable
    }
    
    /**
     * Get full course code as string
     * @return formatted course code (e.g., "CS101-A")
     */
    public String getFullCode() {
        return String.format("%s%d-%s", department, number, section);
    }
    
    /**
     * Create a new CourseCode with different section
     * @param newSection new section identifier
     * @return new CourseCode instance
     */
    public CourseCode withSection(String newSection) {
        return new CourseCode(this.department, this.number, newSection);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        CourseCode that = (CourseCode) obj;
        return number == that.number &&
               Objects.equals(department, that.department) &&
               Objects.equals(section, that.section);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(department, number, section);
    }
    
    @Override
    public String toString() {
        return getFullCode();
    }
}