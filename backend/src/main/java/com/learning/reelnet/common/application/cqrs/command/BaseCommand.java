package com.learning.reelnet.common.application.cqrs.command;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.Getter;

/**
 * Base abstract class for commands that provides common command functionality.
 * Implements the Command interface with additional metadata support.
 *
 * @param <R> Type of result returned by this command
 */
@Getter
public abstract class BaseCommand<R> implements Command<R> {
    
    private final UUID commandId;
    private final String commandName;
    private final Map<String, Object> metadata;
    
    /**
     * Default constructor that initializes command ID and command name
     */
    protected BaseCommand() {
        this.commandId = UUID.randomUUID();
        this.commandName = this.getClass().getSimpleName();
        this.metadata = new HashMap<>();
    }
    
    /**
     * Add metadata to this command
     * 
     * @param key Metadata key
     * @param value Metadata value
     * @return this command instance for method chaining
     */
    public BaseCommand<R> addMetadata(String key, Object value) {
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