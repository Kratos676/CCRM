package edu.ccrm.domain;

import java.util.List;
import java.util.function.Predicate;

/**
 * Generic interface for searchable objects
 * Demonstrates generic interfaces and functional programming
 * @param <T> type of object being searched
 */
public interface Searchable<T> {
    
    /**
     * Search by predicate
     * @param predicate search condition
     * @return list of matching objects
     */
    List<T> search(Predicate<T> predicate);
    
    /**
     * Find by ID
     * @param id object identifier
     * @return matching object or null
     */
    T findById(String id);
    
    /**
     * Get all objects
     * @return list of all objects
     */
    List<T> getAll();
    
    /**
     * Count objects matching predicate
     * @param predicate search condition
     * @return count of matching objects
     */
    default long count(Predicate<T> predicate) {
        return search(predicate).size();
    }
    
    /**
     * Check if any object matches predicate
     * @param predicate search condition
     * @return true if any match found
     */
    default boolean exists(Predicate<T> predicate) {
        return count(predicate) > 0;
    }
    
    /**
     * Get first matching object
     * @param predicate search condition
     * @return first matching object or null
     */
    default T findFirst(Predicate<T> predicate) {
        List<T> results = search(predicate);
        return results.isEmpty() ? null : results.get(0);
    }
}