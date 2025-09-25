package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.util.DuplicateEnrollmentException;
import edu.ccrm.util.MaxCreditLimitExceededException;
import edu.ccrm.config.AppConfig;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Service class for managing students
 * Demonstrates service layer, Stream API, lambdas, and functional interfaces
 */
public class StudentService implements Searchable<Student> {
    
    private final Map<String, Student> students; // ID -> Student mapping
    private final AppConfig config;
    
    /**
     * Constructor
     */
    public StudentService() {
        this.students = new LinkedHashMap<>(); // Preserve insertion order
        this.config = AppConfig.getInstance();
    }
    
    /**
     * Add a new student
     * @param student student to add
     * @throws IllegalArgumentException if student already exists
     */
    public void addStudent(Student student) {
        assert student != null : "Student cannot be null";
        assert student.isValid() : "Student must be valid";
        
        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " already exists");
        }
        
        students.put(student.getId(), student);
        System.out.println("Student added successfully: " + student.getName().getFullName());
    }
    
    /**
     * Update an existing student
     * @param student student to update
     * @throws IllegalArgumentException if student doesn't exist
     */
    public void updateStudent(Student student) {
        assert student != null : "Student cannot be null";
        assert student.isValid() : "Student must be valid";
        
        if (!students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student with ID " + student.getId() + " not found");
        }
        
        students.put(student.getId(), student);
        student.addAuditEntry("Student information updated");
        System.out.println("Student updated successfully: " + student.getName().getFullName());
    }
    
    /**
     * Deactivate a student
     * @param studentId student ID to deactivate
     * @return true if deactivated successfully
     */
    public boolean deactivateStudent(String studentId) {
        Student student = findById(studentId);
        if (student != null) {
            student.deactivate();
            student.addAuditEntry("Student deactivated");
            return true;
        }
        return false;
    }
    
    /**
     * Activate a student
     * @param studentId student ID to activate
     * @return true if activated successfully
     */
    public boolean activateStudent(String studentId) {
        Student student = findById(studentId);
        if (student != null) {
            student.activate();
            student.addAuditEntry("Student activated");
            return true;
        }
        return false;
    }
    
    /**
     * Enroll student in a course with validation
     * @param studentId student ID
     * @param courseCode course code
     * @param courseCredits course credits
     * @throws DuplicateEnrollmentException if already enrolled
     * @throws MaxCreditLimitExceededException if credit limit exceeded
     */
    public void enrollStudentInCourse(String studentId, String courseCode, int courseCredits) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException {
        
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        
        // Check if already enrolled
        if (student.getEnrolledCourses().contains(courseCode.toUpperCase())) {
            throw new DuplicateEnrollmentException(studentId, courseCode, 
                "Student is already enrolled in this course");
        }
        
        // Check credit limits
        int currentCredits = calculateCurrentCredits(student);
        int maxCredits = config.getMaxCoursesPerStudent() * 3; // Assuming 3 credits per course avg
        
        if (currentCredits + courseCredits > maxCredits) {
            throw new MaxCreditLimitExceededException(studentId, currentCredits, 
                maxCredits, courseCredits);
        }
        
        // Enroll the student
        student.enrollInCourse(courseCode);
        System.out.println("Student " + student.getName().getFullName() + 
            " enrolled in course " + courseCode);
    }
    
    /**
     * Calculate current credits for a student
     * @param student student object
     * @return current credits
     */
    private int calculateCurrentCredits(Student student) {
        // Simplified: assume each course is 3 credits
        return student.getEnrolledCourses().size() * 3;
    }
    
    /**
     * Record grade for a student
     * @param studentId student ID
     * @param courseCode course code
     * @param marks marks obtained
     */
    public void recordStudentGrade(String studentId, String courseCode, double marks) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        
        Grade grade = Grade.fromMarks(marks);
        student.recordGrade(courseCode, grade);
        
        System.out.println(String.format("Grade recorded: %s - %s: %.2f (%s)", 
            student.getName().getFullName(), courseCode, marks, grade.getLetter()));
    }
    
    // Searchable interface implementation
    @Override
    public List<Student> search(Predicate<Student> predicate) {
        return students.values().stream()
            .filter(predicate)
            .collect(Collectors.toList());
    }
    
    @Override
    public Student findById(String id) {
        return students.get(id);
    }
    
    @Override
    public List<Student> getAll() {
        return new ArrayList<>(students.values());
    }
    
    /**
     * Find students by department using Stream API
     * @param department department name
     * @return list of students in the department
     */
    public List<Student> findByDepartment(String department) {
        return search(student -> student.getDepartment().equalsIgnoreCase(department));
    }
    
    /**
     * Find students by registration number pattern
     * @param pattern registration number pattern
     * @return list of matching students
     */
    public List<Student> findByRegistrationPattern(String pattern) {
        return search(student -> student.getRegistrationNumber().contains(pattern));
    }
    
    /**
     * Find students by GPA range
     * @param minGPA minimum GPA
     * @param maxGPA maximum GPA
     * @return list of students in GPA range
     */
    public List<Student> findByGPARange(double minGPA, double maxGPA) {
        return search(student -> {
            double gpa = student.calculateGPA();
            return gpa >= minGPA && gpa <= maxGPA;
        });
    }
    
    /**
     * Find students in good standing
     * @return list of students in good standing
     */
    public List<Student> findStudentsInGoodStanding() {
        return search(Student::isInGoodStanding);
    }
    
    /**
     * Find active students
     * @return list of active students
     */
    public List<Student> findActiveStudents() {
        return search(Student::isActive);
    }
    
    /**
     * Get top students by GPA
     * @param limit number of top students to return
     * @return list of top students sorted by GPA descending
     */
    public List<Student> getTopStudentsByGPA(int limit) {
        return students.values().stream()
            .filter(Student::isActive)
            .sorted((s1, s2) -> Double.compare(s2.calculateGPA(), s1.calculateGPA()))
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Get students enrolled in a specific course
     * @param courseCode course code
     * @return list of students enrolled in the course
     */
    public List<Student> getStudentsInCourse(String courseCode) {
        return search(student -> student.getEnrolledCourses().contains(courseCode.toUpperCase()));
    }
    
    /**
     * Get department-wise student count using Stream API
     * @return map of department -> student count
     */
    public Map<String, Long> getDepartmentWiseCount() {
        return students.values().stream()
            .filter(Student::isActive)
            .collect(Collectors.groupingBy(
                Student::getDepartment,
                Collectors.counting()
            ));
    }
    
    /**
     * Get GPA distribution
     * @return map of GPA ranges -> student count
     */
    public Map<String, Long> getGPADistribution() {
        return students.values().stream()
            .filter(Student::isActive)
            .collect(Collectors.groupingBy(
                student -> {
                    double gpa = student.calculateGPA();
                    if (gpa >= 9.0) return "Outstanding (9.0+)";
                    else if (gpa >= 8.0) return "Excellent (8.0-8.9)";
                    else if (gpa >= 7.0) return "Very Good (7.0-7.9)";
                    else if (gpa >= 6.0) return "Good (6.0-6.9)";
                    else if (gpa >= 5.0) return "Satisfactory (5.0-5.9)";
                    else return "Below Average (<5.0)";
                },
                Collectors.counting()
            ));
    }
    
    /**
     * Calculate average GPA across all students
     * @return average GPA
     */
    public double calculateAverageGPA() {
        return students.values().stream()
            .filter(Student::isActive)
            .mapToDouble(Student::calculateGPA)
            .average()
            .orElse(0.0);
    }
    
    /**
     * Get statistics summary
     * @return formatted statistics summary
     */
    public String getStatisticsSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Student Statistics Summary\n");
        summary.append("=".repeat(40)).append("\n");
        
        long totalStudents = students.size();
        long activeStudents = count(Student::isActive);
        long studentsInGoodStanding = count(Student::isInGoodStanding);
        double avgGPA = calculateAverageGPA();
        
        summary.append(String.format("Total Students: %d\n", totalStudents));
        summary.append(String.format("Active Students: %d\n", activeStudents));
        summary.append(String.format("Students in Good Standing: %d\n", studentsInGoodStanding));
        summary.append(String.format("Average GPA: %.2f\n", avgGPA));
        
        summary.append("\nDepartment-wise Count:\n");
        getDepartmentWiseCount().entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .forEach(entry -> summary.append(String.format("  %s: %d\n", 
                entry.getKey(), entry.getValue())));
        
        summary.append("\nGPA Distribution:\n");
        getGPADistribution().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> summary.append(String.format("  %s: %d\n", 
                entry.getKey(), entry.getValue())));
        
        return summary.toString();
    }
    
    /**
     * Generate transcript for a student
     * @param studentId student ID
     * @return transcript string
     */
    public String generateTranscript(String studentId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentId);
        }
        
        return student.generateTranscript();
    }
    
    /**
     * Bulk operation: activate/deactivate students by department
     * @param department department name
     * @param active true to activate, false to deactivate
     * @return number of students affected
     */
    public int bulkUpdateStudentStatus(String department, boolean active) {
        List<Student> departmentStudents = findByDepartment(department);
        
        departmentStudents.forEach(student -> {
            student.setActive(active);
            student.addAuditEntry(active ? "Bulk activated" : "Bulk deactivated");
        });
        
        return departmentStudents.size();
    }
    
    /**
     * Get total number of students
     * @return total student count
     */
    public int getTotalCount() {
        return students.size();
    }
    
    /**
     * Check if a student exists
     * @param studentId student ID
     * @return true if student exists
     */
    public boolean studentExists(String studentId) {
        return students.containsKey(studentId);
    }
}