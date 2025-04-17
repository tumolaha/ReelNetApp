package com.learning.reelnet.common.exception;

/**
 * Exception thrown when a user attempts to access a resource 
 * or perform an operation for which they do not have sufficient permissions.
 * This is different from authentication failures and is used specifically for 
 * authorization issues where the user is authenticated but lacks the required permissions.
 */
public class UnauthorizedAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates an UnauthorizedAccessException for a specific resource and operation.
     *
     * @param resourceType the type of resource being accessed
     * @param resourceId the ID of the resource being accessed
     * @param operation the operation that was attempted
     * @return a new UnauthorizedAccessException with a formatted message
     */
    public static UnauthorizedAccessException forResource(String resourceType, Object resourceId, String operation) {
        return new UnauthorizedAccessException(
                "Not authorized to " + operation + " " + resourceType + " with id: " + resourceId);
    }

    /**
     * Creates an UnauthorizedAccessException for a specific operation.
     *
     * @param operation the operation that was attempted
     * @return a new UnauthorizedAccessException with a formatted message
     */
    public static UnauthorizedAccessException forOperation(String operation) {
        return new UnauthorizedAccessException("Not authorized to " + operation);
    }
}