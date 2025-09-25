package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;

/**
 * Course class representing an academic course
 * Demonstrates Builder pattern, composition, and encapsulation
 */
public class Course {
    private final CourseCode courseCode;
    private String title;
    private int credits;
    private String instructorId;
    private Semester semester;
    private String department;
    private String description;
    private final Set<String> prerequisites;
    private final Set<String> enrolledStudents;
    private int maxCapacity;
    private boolean active;
    private LocalDate creationDate;
    
    /**
     * Private constructor for Builder pattern
     */
    private Course(Builder builder) {
        this.courseCode = builder.courseCode;
        this.title = builder.title;
        this.credits = builder.credits;
        this.instructorId = builder.instructorId;
        this.semester = builder.semester;
        this.department = builder.department;
        this.description = builder.description;
        this.prerequisites = new LinkedHashSet<>(builder.prerequisites);
        this.enrolledStudents = new LinkedHashSet<>();
        this.maxCapacity = builder.maxCapacity;
        this.active = true;
        this.creationDate = LocalDate.now();
    }
    
    // Getters and setters
    public CourseCode getCourseCode() { return courseCode; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { 
        assert title != null && !title.trim().isEmpty() : "Title cannot be empty";
        this.title = title; 
    }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { 
        assert credits > 0 && credits <= 6 : "Credits must be between 1 and 6";
        this.credits = credits; 
    }
    
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { 
        assert semester != null : "Semester cannot be null";
        this.semester = semester; 
    }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        assert department != null && !department.trim().isEmpty() : "Department cannot be empty";
        this.department = department; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { 
        assert maxCapacity > 0 : "Max capacity must be positive";
        this.maxCapacity = maxCapacity; 
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public LocalDate getCreationDate() { return creationDate; }
    
    /**
     * Get defensive copy of prerequisites
     */
    public Set<String> getPrerequisites() {
        return Collections.unmodifiableSet(prerequisites);
    }
    
    /**
     * Get defensive copy of enrolled students
     */
    public Set<String> getEnrolledStudents() {
        return Collections.unmodifiableSet(enrolledStudents);
    }
    
    /**
     * Add a prerequisite
     */
    public boolean addPrerequisite(String prerequisiteCourseCode) {
        assert prerequisiteCourseCode != null && !prerequisiteCourseCode.trim().isEmpty() : 
            "Prerequisite course code required";
        return prerequisites.add(prerequisiteCourseCode.trim().toUpperCase());
    }
    
    /**
     * Remove a prerequisite
     */
    public boolean removePrerequisite(String prerequisiteCourseCode) {
        if (prerequisiteCourseCode == null) return false;
        return prerequisites.remove(prerequisiteCourseCode.trim().toUpperCase());
    }
    
    /**
     * Enroll a student
     */
    public boolean enrollStudent(String studentId) {
        assert studentId != null && !studentId.trim().isEmpty() : "Student ID required";
        
        if (enrolledStudents.size() >= maxCapacity) {
            throw new IllegalStateException("Course capacity exceeded");
        }
        
        return enrolledStudents.add(studentId);
    }
    
    /**
     * Unenroll a student
     */
    public boolean unenrollStudent(String studentId) {
        if (studentId == null) return false;
        return enrolledStudents.remove(studentId);
    }
    
    /**
     * Check if student is enrolled
     */
    public boolean isStudentEnrolled(String studentId) {
        return studentId != null && enrolledStudents.contains(studentId);
    }
    
    /**
     * Get current enrollment count
     */
    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
    
    /**
     * Check if course is full
     */
    public boolean isFull() {
        return getCurrentEnrollment() >= maxCapacity;
    }
    
    /**
     * Get available spots
     */
    public int getAvailableSpots() {
        return Math.max(0, maxCapacity - getCurrentEnrollment());
    }
    
    /**
     * Check if course has prerequisites
     */
    public boolean hasPrerequisites() {
        return !prerequisites.isEmpty();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Course course = (Course) obj;
        return Objects.equals(courseCode, course.courseCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }
    
    @Override
    public String toString() {
        return String.format("Course{code=%s, title='%s', credits=%d, instructor='%s', enrolled=%d/%d}", 
            courseCode, title, credits, instructorId, getCurrentEnrollment(), maxCapacity);
    }
    
    /**
     * Builder pattern implementation for Course creation
     * Demonstrates Builder design pattern
     */
    public static class Builder {
        private CourseCode courseCode;
        private String title;
        private int credits;
        private String instructorId;
        private Semester semester;
        private String department;
        private String description = "";
        private Set<String> prerequisites = new LinkedHashSet<>();
        private int maxCapacity = 30; // Default capacity
        
        public Builder(CourseCode courseCode, String title) {
            assert courseCode != null : "Course code is required";
            assert title != null && !title.trim().isEmpty() : "Title is required";
            
            this.courseCode = courseCode;
            this.title = title;
        }
        
        public Builder credits(int credits) {
            assert credits > 0 && credits <= 6 : "Credits must be between 1 and 6";
            this.credits = credits;
            return this;
        }
        
        public Builder instructor(String instructorId) {
            this.instructorId = instructorId;
            return this;
        }
        
        public Builder semester(Semester semester) {
            assert semester != null : "Semester cannot be null";
            this.semester = semester;
            return this;
        }
        
        public Builder department(String department) {
            assert department != null && !department.trim().isEmpty() : "Department is required";
            this.department = department;
            return this;
        }
        
        public Builder description(String description) {
            this.description = description != null ? description : "";
            return this;
        }
        
        public Builder maxCapacity(int maxCapacity) {
            assert maxCapacity > 0 : "Max capacity must be positive";
            this.maxCapacity = maxCapacity;
            return this;
        }
        
        public Builder prerequisite(String prerequisiteCourseCode) {
            if (prerequisiteCourseCode != null && !prerequisiteCourseCode.trim().isEmpty()) {
                this.prerequisites.add(prerequisiteCourseCode.trim().toUpperCase());
            }
            return this;
        }
        
        public Builder prerequisites(Collection<String> prerequisites) {
            if (prerequisites != null) {
                prerequisites.stream()
                    .filter(prereq -> prereq != null && !prereq.trim().isEmpty())
                    .forEach(prereq -> this.prerequisites.add(prereq.trim().toUpperCase()));
            }
            return this;
        }
        
        public Course build() {
            // Validation before building
            assert courseCode != null : "Course code is required";
            assert title != null && !title.trim().isEmpty() : "Title is required";
            assert credits > 0 : "Credits must be specified";
            assert semester != null : "Semester is required";
            assert department != null && !department.trim().isEmpty() : "Department is required";
            
            return new Course(this);
        }
    }
    
    /**
     * Inner class for course statistics
     * Demonstrates non-static inner class
     */
    public class CourseStatistics {
        /**
         * Get enrollment percentage
         */
        public double getEnrollmentPercentage() {
            if (maxCapacity == 0) return 0.0;
            return (double) getCurrentEnrollment() / maxCapacity * 100.0;
        }
        
        /**
         * Check if course is popular (more than 80% enrolled)
         */
        public boolean isPopular() {
            return getEnrollmentPercentage() > 80.0;
        }
        
        /**
         * Check if course is underenrolled (less than 30% enrolled)
         */
        public boolean isUnderenrolled() {
            return getEnrollmentPercentage() < 30.0;
        }
        
        /**
         * Get course status summary
         */
        public String getStatusSummary() {
            double percentage = getEnrollmentPercentage();
            if (percentage >= 90) return "FULL/WAITLIST";
            else if (percentage >= 80) return "HIGH_DEMAND";
            else if (percentage >= 50) return "MODERATE_ENROLLMENT";
            else if (percentage >= 30) return "LOW_ENROLLMENT";
            else return "UNDERENROLLED";
        }
    }
}