package com.learning.reelnet.common.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown when data validation fails.
 * ValidationException represents errors that occur during input validation,
 * such as missing required fields, invalid field formats, or constraint violations.
 * <p>
 * It maintains a list of specific validation errors with field references for
 * more detailed feedback to the user.
 */
public class ValidationException extends ApiException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "VALIDATION_ERROR";
    
    /**
     * List of validation errors containing field names and error messages
     */
    @Getter
    private final List<ValidationError> validationErrors;
    
    /**
     * Creates a new validation exception with the specified message and validation errors.
     *
     * @param message          The general error message
     * @param validationErrors The list of specific validation errors
     * @param parameters       Additional context parameters
     */
    public ValidationException(String message, List<ValidationError> validationErrors, Object... parameters) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST, parameters);
        this.validationErrors = validationErrors != null ? 
            new ArrayList<>(validationErrors) : new ArrayList<>();
    }
    
    /**
     * Creates a new validation exception with the specified message.
     *
     * @param message   The general error message
     * @param parameters Additional context parameters
     */
    public ValidationException(String message, Object... parameters) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.BAD_REQUEST, parameters);
        this.validationErrors = new ArrayList<>();
    }
    
    /**
     * Creates a new validation exception with a custom error code.
     *
     * @param message   The general error message
     * @param errorCode The unique error code
     * @param parameters Additional context parameters
     */
    public ValidationException(String message, String errorCode, Object... parameters) {
        super(message, errorCode, HttpStatus.BAD_REQUEST, parameters);
        this.validationErrors = new ArrayList<>();
    }
    
    /**
     * Adds a validation error to the list
     *
     * @param field   The field name that has the validation error
     * @param message The error message for the field
     * @return This exception instance for chaining
     */
    public ValidationException addError(String field, String message) {
        validationErrors.add(new ValidationError(field, message));
        return this;
    }
    
    /**
     * Returns an unmodifiable view of the validation errors list
     *
     * @return List of validation errors
     */
    public List<ValidationError> getErrors() {
        return Collections.unmodifiableList(validationErrors);
    }
    
    /**
     * Checks if the exception has any validation errors
     *
     * @return true if there are validation errors, false otherwise
     */
    public boolean hasErrors() {
        return !validationErrors.isEmpty();
    }
    
    /**
     * Helper class to represent a single validation error
     */
    @Getter
    public static class ValidationError {
        private final String field;
        private final String message;
        
        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
} 