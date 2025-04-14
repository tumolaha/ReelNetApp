package com.learning.reelnet.common.infrastructure.web;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Standard error response for the application.
 * This class provides a consistent structure for error responses.
 */
@Data
@SuperBuilder
public class ErrorResponse {
    
    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;
    
    /**
     * The HTTP status code.
     */
    private int status;
    
    /**
     * The error type.
     */
    private String error;
    
    /**
     * The error message.
     */
    private String message;
    
    /**
     * The application-specific error code.
     */
    private String errorCode;
    
    /**
     * The path of the request that caused the error.
     */
    private String path;
    
    /**
     * Default constructor for deserialization.
     */
    public ErrorResponse() {}
}