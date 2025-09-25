package edu.ccrm.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Abstract base class representing a person
 * Demonstrates abstraction, inheritance, encapsulation
 * Uses protected access level for subclass access
 */
public abstract class Person {
    // Encapsulation: private fields with controlled access
    private final String id;
    private Name name;
    private String email;
    private LocalDate dateOfBirth;
    private LocalDate registrationDate;
    private boolean active;
    
    /**
     * Protected constructor for inheritance
     * @param id unique identifier
     * @param name person's name
     * @param email email address
     * @param dateOfBirth date of birth
     */
    protected Person(String id, Name name, String email, LocalDate dateOfBirth) {
        // Input validation
        assert id != null && !id.trim().isEmpty() : "ID cannot be null or empty";
        assert name != null : "Name cannot be null";
        assert email != null && email.contains("@") : "Valid email required";
        assert dateOfBirth != null && dateOfBirth.isBefore(LocalDate.now()) : "Valid birth date required";
        
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.registrationDate = LocalDate.now();
        this.active = true;
    }
    
    // Getters and setters demonstrating encapsulation
    public String getId() { return id; }
    
    public Name getName() { return name; }
    public void setName(Name name) { 
        assert name != null : "Name cannot be null";
        this.name = name; 
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        assert email != null && email.contains("@") : "Valid email required";
        this.email = email; 
    }
    
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { 
        assert dateOfBirth != null && dateOfBirth.isBefore(LocalDate.now()) : "Valid birth date required";
        this.dateOfBirth = dateOfBirth; 
    }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    /**
     * Calculate age in years
     * @return age in years
     */
    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }
    
    /**
     * Deactivate the person
     */
    public void deactivate() {
        this.active = false;
    }
    
    /**
     * Activate the person
     */
    public void activate() {
        this.active = true;
    }
    
    // Abstract methods to be implemented by subclasses
    public abstract String getPersonType();
    public abstract String getDisplayInfo();
    
    /**
     * Template method demonstrating polymorphism
     * @return formatted person summary
     */
    public final String getPersonSummary() {
        return String.format("[%s] %s (%s) - %s", 
            getPersonType(), 
            name.getFullName(), 
            email, 
            active ? "Active" : "Inactive");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Person person = (Person) obj;
        return Objects.equals(id, person.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("Person{id='%s', name=%s, email='%s', dob=%s, active=%s}", 
            id, name, email, dateOfBirth.format(formatter), active);
    }
    
    /**
     * Static nested class for person comparison utilities
     * Demonstrates static nested class usage
     */
    public static class PersonComparator {
        /**
         * Compare persons by name
         */
        public static int compareByName(Person p1, Person p2) {
            return p1.getName().getFullName().compareToIgnoreCase(p2.getName().getFullName());
        }
        
        /**
         * Compare persons by registration date
         */
        public static int compareByRegistrationDate(Person p1, Person p2) {
            return p1.getRegistrationDate().compareTo(p2.getRegistrationDate());
        }
        
        /**
         * Compare persons by age
         */
        public static int compareByAge(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }
    }
}