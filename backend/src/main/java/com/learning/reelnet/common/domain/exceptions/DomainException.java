package com.learning.reelnet.common.domain.exceptions;

import lombok.Getter;

/**
 * Base exception for all domain-related exceptions in the system.
 * Domain exceptions represent business rule violations or invalid operations
 * within the domain model.
 * <p>
 * Using a hierarchy of domain-specific exceptions allows for:
 * 1. More precise error handling
 * 2. Better communication of business rule violations
 * 3. Separation between technical and domain errors
 * <p>
 * Domain exceptions should be self-explanatory and provide enough context
 * to understand what business rule was violated.
 */
@Getter
public abstract class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Error code uniquely identifying the type of domain exception.
     * This can be used for internationalization or client-side error handling.
     */
    private final String errorCode;
    
    /**
     * Severity level of the exception.
     */
    private final Severity severity;
    
    /**
     * Additional context information about the exception.
     */
    private final Object[] parameters;

    /**
     * Creates a new domain exception with the specified message and error code.
     *
     * @param message   The detailed error message
     * @param errorCode The unique error code
     * @param severity  The severity level of the exception
     * @param parameters Additional context parameters
     */
    protected DomainException(String message, String errorCode, Severity severity, Object... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.severity = severity;
        this.parameters = parameters;
    }

    /**
     * Creates a new domain exception with the specified message, error code, and cause.
     *
     * @param message   The detailed error message
     * @param errorCode The unique error code
     * @param severity  The severity level of the exception
     * @param cause     The underlying cause of the exception
     * @param parameters Additional context parameters
     */
    protected DomainException(String message, String errorCode, Severity severity, Throwable cause, Object... parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.severity = severity;
        this.parameters = parameters;
    }

    /**
     * Severity levels for domain exceptions.
     */
    public enum Severity {
        /**
         * Informational exception, doesn't indicate a problem but provides information.
         */
        INFO,
        
        /**
         * Warning level exception, indicates a potential issue that might need attention.
         */
        WARNING,
        
        /**
         * Error level exception, indicates a problem that prevented an operation from completing.
         */
        ERROR,
        
        /**
         * Critical level exception, indicates a severe problem that requires immediate attention.
         */
        CRITICAL
    }
    
    /**
     * Returns a string representation of this exception including the error code and parameters.
     * 
     * @return A detailed string representation of the exception
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
          .append("[")
          .append("errorCode=").append(errorCode)
          .append(", severity=").append(severity);
        
        if (parameters != null && parameters.length > 0) {
            sb.append(", parameters=[");
            for (int i = 0; i < parameters.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(parameters[i]);
            }
            sb.append("]");
        }
        
        sb.append("]: ").append(getMessage());
        
        return sb.toString();
    }
}