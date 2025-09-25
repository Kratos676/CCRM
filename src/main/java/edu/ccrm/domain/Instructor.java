package edu.ccrm.domain;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Instructor class extending Person
 * Demonstrates inheritance, polymorphism, and composition
 */
public class Instructor extends Person {
    private final String employeeId;
    private String department;
    private String designation;
    private double salary;
    private final Set<String> assignedCourses; // Courses currently teaching
    private final Set<String> qualifications; // Academic qualifications
    private LocalDate joiningDate;
    private int experienceYears;
    
    /**
     * Constructor demonstrating inheritance and super() usage
     */
    public Instructor(String id, String employeeId, Name name, String email, 
                     LocalDate dateOfBirth, String department, String designation) {
        // Call parent constructor
        super(id, name, email, dateOfBirth);
        
        // Validation
        assert employeeId != null && !employeeId.trim().isEmpty() : 
            "Employee ID is required";
        assert department != null && !department.trim().isEmpty() : 
            "Department is required";
        assert designation != null && !designation.trim().isEmpty() : 
            "Designation is required";
        
        this.employeeId = employeeId;
        this.department = department;
        this.designation = designation;
        this.salary = 0.0;
        this.assignedCourses = new LinkedHashSet<>();
        this.qualifications = new LinkedHashSet<>();
        this.joiningDate = LocalDate.now();
        this.experienceYears = 0;
    }
    
    // Getters and setters demonstrating encapsulation
    public String getEmployeeId() { return employeeId; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { 
        assert department != null && !department.trim().isEmpty() : "Department cannot be empty";
        this.department = department; 
    }
    
    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { 
        assert designation != null && !designation.trim().isEmpty() : "Designation cannot be empty";
        this.designation = designation; 
    }
    
    public double getSalary() { return salary; }
    public void setSalary(double salary) { 
        assert salary >= 0 : "Salary cannot be negative";
        this.salary = salary; 
    }
    
    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) {
        assert joiningDate != null && !joiningDate.isAfter(LocalDate.now()) : 
            "Joining date cannot be in the future";
        this.joiningDate = joiningDate;
    }
    
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { 
        assert experienceYears >= 0 : "Experience cannot be negative";
        this.experienceYears = experienceYears; 
    }
    
    /**
     * Get defensive copy of assigned courses
     */
    public Set<String> getAssignedCourses() {
        return Collections.unmodifiableSet(assignedCourses);
    }
    
    /**
     * Get defensive copy of qualifications
     */
    public Set<String> getQualifications() {
        return Collections.unmodifiableSet(qualifications);
    }
    
    /**
     * Assign a course to instructor
     * @param courseCode course code to assign
     * @return true if assignment successful
     */
    public boolean assignCourse(String courseCode) {
        assert courseCode != null && !courseCode.trim().isEmpty() : "Course code required";
        return assignedCourses.add(courseCode.trim().toUpperCase());
    }
    
    /**
     * Unassign a course from instructor
     * @param courseCode course code to unassign
     * @return true if unassignment successful
     */
    public boolean unassignCourse(String courseCode) {
        assert courseCode != null : "Course code cannot be null";
        return assignedCourses.remove(courseCode.trim().toUpperCase());
    }
    
    /**
     * Add a qualification
     * @param qualification qualification to add
     * @return true if addition successful
     */
    public boolean addQualification(String qualification) {
        assert qualification != null && !qualification.trim().isEmpty() : 
            "Qualification cannot be empty";
        return qualifications.add(qualification.trim());
    }
    
    /**
     * Remove a qualification
     * @param qualification qualification to remove
     * @return true if removal successful
     */
    public boolean removeQualification(String qualification) {
        assert qualification != null : "Qualification cannot be null";
        return qualifications.remove(qualification.trim());
    }
    
    /**
     * Check if instructor is teaching a specific course
     * @param courseCode course code to check
     * @return true if teaching the course
     */
    public boolean isTeaching(String courseCode) {
        if (courseCode == null) return false;
        return assignedCourses.contains(courseCode.trim().toUpperCase());
    }
    
    /**
     * Get teaching load (number of courses)
     * @return number of assigned courses
     */
    public int getTeachingLoad() {
        return assignedCourses.size();
    }
    
    /**
     * Check if instructor is overloaded (teaching more than 4 courses)
     * @return true if overloaded
     */
    public boolean isOverloaded() {
        return getTeachingLoad() > 4;
    }
    
    /**
     * Calculate years of service
     * @return years since joining
     */
    public int getYearsOfService() {
        return LocalDate.now().getYear() - joiningDate.getYear();
    }
    
    /**
     * Check if instructor is senior (more than 5 years experience or service)
     * @return true if senior
     */
    public boolean isSenior() {
        return experienceYears > 5 || getYearsOfService() > 5;
    }
    
    // Polymorphic method implementations (method overriding)
    @Override
    public String getPersonType() {
        return "INSTRUCTOR";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("Emp ID: %s | Dept: %s | %s | Courses: %d | Exp: %d years", 
            employeeId, department, designation, getTeachingLoad(), experienceYears);
    }
    
    /**
     * Generate instructor profile
     * @return formatted profile string
     */
    public String generateProfile() {
        StringBuilder profile = new StringBuilder();
        
        profile.append("=".repeat(60)).append("\n");
        profile.append("INSTRUCTOR PROFILE\n");
        profile.append("=".repeat(60)).append("\n");
        profile.append(String.format("Employee ID: %s\n", employeeId));
        profile.append(String.format("Name: %s\n", getName().getFullName()));
        profile.append(String.format("Email: %s\n", getEmail()));
        profile.append(String.format("Department: %s\n", department));
        profile.append(String.format("Designation: %s\n", designation));
        profile.append(String.format("Experience: %d years\n", experienceYears));
        profile.append(String.format("Service: %d years\n", getYearsOfService()));
        profile.append(String.format("Salary: $%.2f\n", salary));
        profile.append("-".repeat(60)).append("\n");
        
        profile.append("QUALIFICATIONS:\n");
        if (qualifications.isEmpty()) {
            profile.append("No qualifications recorded.\n");
        } else {
            qualifications.forEach(qual -> profile.append("• ").append(qual).append("\n"));
        }
        
        profile.append("-".repeat(60)).append("\n");
        profile.append("ASSIGNED COURSES:\n");
        if (assignedCourses.isEmpty()) {
            profile.append("No courses assigned.\n");
        } else {
            assignedCourses.forEach(course -> profile.append("• ").append(course).append("\n"));
        }
        
        profile.append("-".repeat(60)).append("\n");
        profile.append(String.format("Teaching Load: %d courses", getTeachingLoad()));
        if (isOverloaded()) {
            profile.append(" (OVERLOADED)");
        }
        profile.append("\n");
        profile.append(String.format("Status: %s\n", isSenior() ? "Senior Instructor" : "Junior Instructor"));
        profile.append("=".repeat(60)).append("\n");
        
        return profile.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Instructor{empId='%s', name=%s, dept='%s', designation='%s', courses=%d}", 
            employeeId, getName(), department, designation, getTeachingLoad());
    }
    
    /**
     * Static nested class for instructor utilities
     * Demonstrates static nested class usage
     */
    public static class InstructorUtils {
        /**
         * Calculate average teaching load for a collection of instructors
         * @param instructors collection of instructors
         * @return average teaching load
         */
        public static double calculateAverageTeachingLoad(Collection<Instructor> instructors) {
            if (instructors.isEmpty()) return 0.0;
            
            return instructors.stream()
                .mapToInt(Instructor::getTeachingLoad)
                .average()
                .orElse(0.0);
        }
        
        /**
         * Find instructors by department
         * @param instructors collection of instructors
         * @param department department to filter by
         * @return list of instructors in the department
         */
        public static List<Instructor> findByDepartment(Collection<Instructor> instructors, String department) {
            return instructors.stream()
                .filter(instructor -> instructor.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());
        }
        
        /**
         * Find overloaded instructors
         * @param instructors collection of instructors
         * @return list of overloaded instructors
         */
        public static List<Instructor> findOverloadedInstructors(Collection<Instructor> instructors) {
            return instructors.stream()
                .filter(Instructor::isOverloaded)
                .collect(Collectors.toList());
        }
    }
}