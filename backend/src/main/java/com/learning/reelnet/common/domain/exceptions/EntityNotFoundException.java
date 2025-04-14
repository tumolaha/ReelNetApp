package com.learning.reelnet.common.domain.exceptions;

/**
 * Exception thrown when an entity cannot be found in the system.
 * This typically occurs when attempting to retrieve an entity by its identifier
 * and no entity with that identifier exists.
 */
public class EntityNotFoundException extends DomainException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "ENTITY_NOT_FOUND";

    /**
     * Creates a new EntityNotFoundException for the specified entity type and identifier.
     *
     * @param entityName The name of the entity type
     * @param id The identifier that was not found
     */
    public EntityNotFoundException(String entityName, Object id) {
        super(
            String.format("%s with id '%s' not found", entityName, id),
            DEFAULT_ERROR_CODE,
            Severity.ERROR,
            entityName, id
        );
    }
    
    /**
     * Creates a new EntityNotFoundException with a custom message.
     *
     * @param message The detailed error message
     */
    public EntityNotFoundException(String message) {
        super(
            message,
            DEFAULT_ERROR_CODE,
            Severity.ERROR,
            message
        );
    }
    
    /**
     * Creates a new EntityNotFoundException with a custom error code.
     *
     * @param entityName The name of the entity type
     * @param id The identifier that was not found
     * @param errorCode A custom error code
     */
    public EntityNotFoundException(String entityName, Object id, String errorCode) {
        super(
            String.format("%s with id '%s' not found", entityName, id),
            errorCode,
            Severity.ERROR,
            entityName, id
        );
    }
    
    /**
     * Creates a new EntityNotFoundException for a specific entity with additional context.
     *
     * @param entityName The name of the entity type
     * @param id The identifier that was not found
     * @param context Additional context about the search (e.g., "in user's portfolio")
     */
    public EntityNotFoundException(String entityName, Object id, String context, Object... additionalParams) {
        super(
            String.format("%s with id '%s' not found %s", entityName, id, context),
            DEFAULT_ERROR_CODE,
            Severity.ERROR,
            entityName, id, context, additionalParams
        );
    }
}