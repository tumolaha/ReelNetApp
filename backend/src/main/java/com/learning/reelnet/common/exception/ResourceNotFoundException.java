package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource cannot be found.
 * This is typically used when an entity with a specific ID does not exist in the database.
 */
public class ResourceNotFoundException extends ApiException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "NOT_FOUND";

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.NOT_FOUND);
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.NOT_FOUND, cause);
    }

    /**
     * Creates a ResourceNotFoundException for a specific entity type and ID.
     *
     * @param entityName the name of the entity type
     * @param id the ID of the entity that was not found
     * @return a new ResourceNotFoundException with a formatted message
     */
    public static ResourceNotFoundException forEntity(String entityName, Object id) {
        return new ResourceNotFoundException(entityName + " not found with id: " + id);
    }
}