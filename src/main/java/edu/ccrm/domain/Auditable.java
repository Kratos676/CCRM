package edu.ccrm.domain;

import java.util.List;

/**
 * Interface for auditable operations
 * Demonstrates interface with default methods and diamond problem resolution
 */
public interface Auditable {
    
    /**
     * Get audit trail for the object
     * @return list of audit entries
     */
    List<String> getAuditTrail();
    
    /**
     * Add audit entry
     * @param entry audit entry to add
     */
    void addAuditEntry(String entry);
    
    /**
     * Get creation timestamp
     * @return creation time in milliseconds
     */
    long getCreationTime();
    
    /**
     * Default method that might conflict with Persistable
     * Demonstrates diamond problem scenario
     * @return last modified timestamp
     */
    default long getLastModified() {
        List<String> trail = getAuditTrail();
        return trail.isEmpty() ? getCreationTime() : System.currentTimeMillis();
    }
    
    /**
     * Check if object has been modified
     * @return true if modified since creation
     */
    default boolean isModified() {
        return getLastModified() > getCreationTime();
    }
    
    /**
     * Get audit summary
     * @return formatted audit summary
     */
    default String getAuditSummary() {
        List<String> trail = getAuditTrail();
        return String.format("Audit Trail: %d entries, Last Modified: %d", 
            trail.size(), getLastModified());
    }
}