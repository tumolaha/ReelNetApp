package com.learning.reelnet.common.application.cqrs.query;

import java.util.UUID;

/**
 * Marker interface for queries in CQRS pattern.
 * Queries represent a request for information that does not change system state.
 * 
 * @param <R> The type of result returned by the query
 */
public interface Query<R> {
    /**
     * Get the unique identifier for this query.
     * Default implementation generates a new UUID for each query.
     * 
     * @return The query ID
     */
    default UUID getQueryId() {
        return UUID.randomUUID();
    }
    
    /**
     * Get the name of this query.
     * Default implementation uses the simple class name.
     * 
     * @return The query name
     */
    default String getQueryName() {
        return this.getClass().getSimpleName();
    }
} 