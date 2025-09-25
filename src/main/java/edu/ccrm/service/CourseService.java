package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.config.AppConfig;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service class for managing courses
 * Demonstrates Stream API filtering, lambdas, and functional programming
 */
public class CourseService implements Searchable<Course> {
    
    private final Map<String, Course> courses; // Course code -> Course mapping
    private final AppConfig config;
    
    /**
     * Constructor
     */
    public CourseService() {
        this.courses = new LinkedHashMap<>();
        this.config = AppConfig.getInstance();
    }
    
    /**
     * Add a new course
     * @param course course to add
     * @throws IllegalArgumentException if course already exists
     */
    public void addCourse(Course course) {
        assert course != null : "Course cannot be null";
        
        String courseCode = course.getCourseCode().getFullCode();
        if (courses.containsKey(courseCode)) {
            throw new IllegalArgumentException("Course with code " + courseCode + " already exists");
        }
        
        courses.put(courseCode, course);
        System.out.println("Course added successfully: " + course.getTitle());
    }
    
    /**
     * Update an existing course
     * @param course course to update
     * @throws IllegalArgumentException if course doesn't exist
     */
    public void updateCourse(Course course) {
        assert course != null : "Course cannot be null";
        
        String courseCode = course.getCourseCode().getFullCode();
        if (!courses.containsKey(courseCode)) {
            throw new IllegalArgumentException("Course with code " + courseCode + " not found");
        }
        
        courses.put(courseCode, course);
        System.out.println("Course updated successfully: " + course.getTitle());
    }
    
    /**
     * Deactivate a course
     * @param courseCode course code to deactivate
     * @return true if deactivated successfully
     */
    public boolean deactivateCourse(String courseCode) {
        Course course = findById(courseCode);
        if (course != null) {
            course.setActive(false);
            return true;
        }
        return false;
    }
    
    /**
     * Activate a course
     * @param courseCode course code to activate
     * @return true if activated successfully
     */
    public boolean activateCourse(String courseCode) {
        Course course = findById(courseCode);
        if (course != null) {
            course.setActive(true);
            return true;
        }
        return false;
    }
    
    /**
     * Assign instructor to a course
     * @param courseCode course code
     * @param instructorId instructor ID
     * @return true if assigned successfully
     */
    public boolean assignInstructor(String courseCode, String instructorId) {
        Course course = findById(courseCode);
        if (course != null) {
            course.setInstructorId(instructorId);
            System.out.println("Instructor " + instructorId + " assigned to course " + courseCode);
            return true;
        }
        return false;
    }
    
    // Searchable interface implementation
    @Override
    public List<Course> search(Predicate<Course> predicate) {
        return courses.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    @Override
    public Course findById(String courseCode) {
        return courses.get(courseCode.toUpperCase());
    }
    
    @Override
    public List<Course> getAll() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Find courses by instructor using Stream API
     * @param instructorId instructor ID
     * @return list of courses taught by the instructor
     */
    public List<Course> findByInstructor(String instructorId) {
        return search(course -> Objects.equals(course.getInstructorId(), instructorId));
    }
    
    /**
     * Find courses by department
     * @param department department name
     * @return list of courses in the department
     */
    public List<Course> findByDepartment(String department) {
        return search(course -> course.getDepartment().equalsIgnoreCase(department));
    }
    
    /**
     * Find courses by semester
     * @param semester semester enum
     * @return list of courses in the semester
     */
    public List<Course> findBySemester(Semester semester) {
        return search(course -> course.getSemester() == semester);
    }
    
    /**
     * Find courses by credit range
     * @param minCredits minimum credits
     * @param maxCredits maximum credits
     * @return list of courses in credit range
     */
    public List<Course> findByCreditRange(int minCredits, int maxCredits) {
        return search(course -> course.getCredits() >= minCredits && course.getCredits() <= maxCredits);
    }
    
    /**
     * Find available courses (not full)
     * @return list of available courses
     */
    public List<Course> findAvailableCourses() {
        return search(course -> course.isActive() && !course.isFull());
    }
    
    /**
     * Find courses with prerequisites
     * @return list of courses that have prerequisites
     */
    public List<Course> findCoursesWithPrerequisites() {
        return search(Course::hasPrerequisites);
    }
    
    /**
     * Find popular courses (more than 80% enrolled)
     * @return list of popular courses
     */
    public List<Course> findPopularCourses() {
        return search(course -> {
            Course.CourseStatistics stats = course.new CourseStatistics();
            return stats.isPopular();
        });
    }
    
    /**
     * Find underenrolled courses (less than 30% enrolled)
     * @return list of underenrolled courses
     */
    public List<Course> findUnderenrolledCourses() {
        return search(course -> {
            Course.CourseStatistics stats = course.new CourseStatistics();
            return stats.isUnderenrolled();
        });
    }
    
    /**
     * Get courses by enrollment status using anonymous inner class
     * @return map of status -> course list
     */
    public Map<String, List<Course>> getCoursesByEnrollmentStatus() {
        // Anonymous inner class for custom categorization
        java.util.function.Function<Course, String> statusCategorizer = new java.util.function.Function<Course, String>() {
            @Override
            public String apply(Course course) {
                Course.CourseStatistics stats = course.new CourseStatistics();
                return stats.getStatusSummary();
            }
        };
        
        return courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.groupingBy(statusCategorizer));
    }
    
    /**
     * Get department-wise course count
     * @return map of department -> course count
     */
    public Map<String, Long> getDepartmentWiseCourseCount() {
        return courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.groupingBy(
                Course::getDepartment,
                Collectors.counting()
            ));
    }
    
    /**
     * Get instructor-wise course count
     * @return map of instructor -> course count
     */
    public Map<String, Long> getInstructorWiseCourseCount() {
        return courses.values().stream()
            .filter(Course::isActive)
            .filter(course -> course.getInstructorId() != null)
            .collect(Collectors.groupingBy(
                Course::getInstructorId,
                Collectors.counting()
            ));
    }
    
    /**
     * Get credit distribution
     * @return map of credits -> course count
     */
    public Map<Integer, Long> getCreditDistribution() {
        return courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.groupingBy(
                Course::getCredits,
                Collectors.counting()
            ));
    }
    
    /**
     * Calculate average enrollment percentage
     * @return average enrollment percentage
     */
    public double calculateAverageEnrollmentPercentage() {
        return courses.values().stream()
            .filter(Course::isActive)
            .mapToDouble(course -> {
                Course.CourseStatistics stats = course.new CourseStatistics();
                return stats.getEnrollmentPercentage();
            })
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get courses sorted by enrollment (descending)
     * @return list of courses sorted by enrollment
     */
    public List<Course> getCoursesSortedByEnrollment() {
        return courses.values().stream()
            .filter(Course::isActive)
            .sorted((c1, c2) -> Integer.compare(c2.getCurrentEnrollment(), c1.getCurrentEnrollment()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get courses sorted by available spots (ascending)
     * @return list of courses sorted by available spots
     */
    public List<Course> getCoursesSortedByAvailability() {
        return courses.values().stream()
            .filter(Course::isActive)
            .sorted(Comparator.comparingInt(Course::getAvailableSpots))
            .collect(Collectors.toList());
    }
    
    /**
     * Search courses by title (case-insensitive partial match)
     * @param titlePattern title pattern to search
     * @return list of matching courses
     */
    public List<Course> searchByTitle(String titlePattern) {
        return search(course -> course.getTitle().toLowerCase()
            .contains(titlePattern.toLowerCase()));
    }
    
    /**
     * Get courses that can accommodate a certain number of students
     * @param requiredSpots number of spots needed
     * @return list of courses with enough available spots
     */
    public List<Course> getCoursesWithAvailableSpots(int requiredSpots) {
        return search(course -> course.isActive() && course.getAvailableSpots() >= requiredSpots);
    }
    
    /**
     * Get statistics summary using functional programming
     * @return formatted statistics summary
     */
    public String getStatisticsSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Course Statistics Summary\n");
        summary.append("=".repeat(40)).append("\n");
        
        long totalCourses = courses.size();
        long activeCourses = count(Course::isActive);
        long coursesWithInstructors = count(course -> course.getInstructorId() != null);
        double avgEnrollmentPercentage = calculateAverageEnrollmentPercentage();
        
        summary.append(String.format("Total Courses: %d\n", totalCourses));
        summary.append(String.format("Active Courses: %d\n", activeCourses));
        summary.append(String.format("Courses with Instructors: %d\n", coursesWithInstructors));
        summary.append(String.format("Average Enrollment: %.1f%%\n", avgEnrollmentPercentage));
        
        summary.append("\nDepartment-wise Course Count:\n");
        getDepartmentWiseCourseCount().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> summary.append(String.format("  %s: %d\n", 
                entry.getKey(), entry.getValue())));
        
        summary.append("\nCredit Distribution:\n");
        getCreditDistribution().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> summary.append(String.format("  %d credits: %d courses\n", 
                entry.getKey(), entry.getValue())));
        
        summary.append("\nEnrollment Status Distribution:\n");
        getCoursesByEnrollmentStatus().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> summary.append(String.format("  %s: %d courses\n", 
                entry.getKey(), entry.getValue().size())));
        
        return summary.toString();
    }
    
    /**
     * Generate course catalog
     * @return formatted course catalog
     */
    public String generateCatalog() {
        StringBuilder catalog = new StringBuilder();
        catalog.append("COURSE CATALOG\n");
        catalog.append("=".repeat(80)).append("\n");
        
        // Group courses by department
        Map<String, List<Course>> coursesByDepartment = courses.values().stream()
            .filter(Course::isActive)
            .collect(Collectors.groupingBy(Course::getDepartment));
        
        coursesByDepartment.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                catalog.append("\n").append(entry.getKey()).append(" DEPARTMENT\n");
                catalog.append("-".repeat(50)).append("\n");
                
                entry.getValue().stream()
                    .sorted(Comparator.comparing(course -> course.getCourseCode().getFullCode()))
                    .forEach(course -> {
                        catalog.append(String.format("%-12s | %-30s | %d credits | %s\n",
                            course.getCourseCode().getFullCode(),
                            course.getTitle(),
                            course.getCredits(),
                            course.getSemester().getDisplayName()));
                        
                        if (course.hasPrerequisites()) {
                            catalog.append(String.format("             Prerequisites: %s\n",
                                String.join(", ", course.getPrerequisites())));
                        }
                        
                        catalog.append(String.format("             Enrollment: %d/%d (%d spots available)\n",
                            course.getCurrentEnrollment(),
                            course.getMaxCapacity(),
                            course.getAvailableSpots()));
                        catalog.append("\n");
                    });
            });
        
        return catalog.toString();
    }
    
    /**
     * Get total number of courses
     * @return total course count
     */
    public int getTotalCount() {
        return courses.size();
    }
    
    /**
     * Check if a course exists
     * @param courseCode course code
     * @return true if course exists
     */
    public boolean courseExists(String courseCode) {
        return courses.containsKey(courseCode.toUpperCase());
    }
}