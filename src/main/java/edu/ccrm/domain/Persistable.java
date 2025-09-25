package edu.ccrm.domain;

/**
 * Interface for objects that can be persisted to storage
 * Demonstrates interface definition and default methods
 */
public interface Persistable {
    
    /**
     * Get unique identifier for persistence
     * @return unique ID
     */
    String getId();
    
    /**
     * Check if object is valid for persistence
     * @return true if valid
     */
    boolean isValid();
    
    /**
     * Convert object to CSV format
     * @return CSV representation
     */
    String toCsv();
    
    /**
     * Get last modified timestamp
     * @return timestamp in milliseconds
     */
    default long getLastModified() {
        return System.currentTimeMillis();
    }
    
    /**
     * Check if object needs to be saved
     * Default implementation - can be overridden
     * @return true if needs saving
     */
    default boolean needsSaving() {
        return isValid();
    }
    
    /**
     * Get object type for persistence
     * @return type identifier
     */
    default String getObjectType() {
        return this.getClass().getSimpleName().toUpperCase();
    }
}