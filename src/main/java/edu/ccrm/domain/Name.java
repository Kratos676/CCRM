package edu.ccrm.domain;

import java.util.Objects;

/**
 * Immutable value class representing a person's name
 * Demonstrates immutability and defensive copying
 */
public final class Name {
    private final String firstName;
    private final String middleName;
    private final String lastName;
    
    /**
     * Constructor with full name components
     */
    public Name(String firstName, String middleName, String lastName) {
        // Defensive copying and validation
        this.firstName = (firstName != null) ? firstName.trim() : "";
        this.middleName = (middleName != null) ? middleName.trim() : "";
        this.lastName = (lastName != null) ? lastName.trim() : "";
        
        // Validation assertion
        assert !this.firstName.isEmpty() && !this.lastName.isEmpty() : 
            "First name and last name are required";
    }
    
    /**
     * Constructor without middle name
     */
    public Name(String firstName, String lastName) {
        this(firstName, "", lastName);
    }
    
    // Only getters - immutable class
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    
    /**
     * Get full name as string
     * @return formatted full name
     */
    public String getFullName() {
        if (middleName.isEmpty()) {
            return firstName + " " + lastName;
        }
        return firstName + " " + middleName + " " + lastName;
    }
    
    /**
     * Get initials
     * @return name initials (e.g., "J.D." or "J.A.D.")
     */
    public String getInitials() {
        StringBuilder initials = new StringBuilder();
        if (!firstName.isEmpty()) {
            initials.append(firstName.charAt(0)).append(".");
        }
        if (!middleName.isEmpty()) {
            initials.append(middleName.charAt(0)).append(".");
        }
        if (!lastName.isEmpty()) {
            initials.append(lastName.charAt(0)).append(".");
        }
        return initials.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Name name = (Name) obj;
        return Objects.equals(firstName, name.firstName) &&
               Objects.equals(middleName, name.middleName) &&
               Objects.equals(lastName, name.lastName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleName, lastName);
    }
    
    @Override
    public String toString() {
        return getFullName();
    }
}