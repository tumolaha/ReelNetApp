package com.learning.reelnet.common.application.cqrs.query;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

/**
 * Base abstract class for queries that provides common query functionality.
 * Implements the Query interface with additional metadata support.
 *
 * @param <R> Type of result returned by this query
 */
@Getter
public abstract class BaseQuery<R> implements Query<R> {
    
    private final UUID queryId;
    private final String queryName;
    private final Map<String, Object> metadata;
    
    /**
     * Default constructor that initializes query ID and query name
     */
    protected BaseQuery() {
        this.queryId = UUID.randomUUID();
        this.queryName = this.getClass().getSimpleName();
        this.metadata = new HashMap<>();
    }
    
    /**
     * Add metadata to this query
     * 
     * @param key Metadata key
     * @param value Metadata value
     * @return this query instance for method chaining
     */
    public BaseQuery<R> addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }
    
    /**
     * Get a metadata value
     * 
     * @param key Metadata key
     * @return Metadata value or null if not found
     */
    public Object getMetadataValue(String key) {
        return this.metadata.get(key);
    }
    
    /**
     * Get a typed metadata value
     * 
     * @param <T> Type of value
     * @param key Metadata key
     * @param defaultValue Default value if not found or wrong type
     * @return Metadata value or default if not found or wrong type
     */
    @SuppressWarnings("unchecked")
    public <T> T getMetadataValue(String key, T defaultValue) {
        Object value = this.metadata.get(key);
        if (value == null) {
            return defaultValue;
        }
        
        try {
            return (T) value;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
} 