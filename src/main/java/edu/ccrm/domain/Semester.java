package edu.ccrm.domain;

/**
 * Enumeration representing academic semesters
 * Demonstrates enum with constructors, fields, and methods
 */
public enum Semester {
    SPRING("Spring", 1, "January - May"),
    SUMMER("Summer", 2, "June - August"), 
    FALL("Fall", 3, "September - December"),
    WINTER("Winter", 4, "December - January");
    
    private final String displayName;
    private final int semesterCode;
    private final String duration;
    
    /**
     * Enum constructor
     * @param displayName Human-readable name
     * @param semesterCode Numeric code for semester
     * @param duration Duration description
     */
    Semester(String displayName, int semesterCode, String duration) {
        this.displayName = displayName;
        this.semesterCode = semesterCode;
        this.duration = duration;
    }
    
    // Getters for enum fields
    public String getDisplayName() { return displayName; }
    public int getSemesterCode() { return semesterCode; }
    public String getDuration() { return duration; }
    
    /**
     * Get semester by code
     * @param code semester code
     * @return Semester enum value
     */
    public static Semester getBySemesterCode(int code) {
        for (Semester semester : values()) {
            if (semester.getSemesterCode() == code) {
                return semester;
            }
        }
        throw new IllegalArgumentException("Invalid semester code: " + code);
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", displayName, duration);
    }
}