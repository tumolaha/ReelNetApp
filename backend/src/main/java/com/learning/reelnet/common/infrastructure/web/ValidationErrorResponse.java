package com.learning.reelnet.common.infrastructure.web;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * Error response for validation errors.
 * This class extends ErrorResponse to include field-specific error details.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ValidationErrorResponse extends ErrorResponse {
    
    /**
     * Map of field names to error messages.
     */
    private Map<String, String> fieldErrors;
    
    /**
     * Default constructor for deserialization.
     */
    public ValidationErrorResponse() {
        super();
    }
}