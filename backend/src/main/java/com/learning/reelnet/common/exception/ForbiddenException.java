package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for forbidden access.
 * Used when a user doesn't have permission to access the requested resource.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    /**
     * Error code
     */
    private String errorCode;

    /**
     * Default constructor
     */
    public ForbiddenException() {
        super("Access forbidden");
    }

    /**
     * Constructor with message
     *
     * @param message Error message
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Constructor with message and error code
     *
     * @param message Error message
     * @param errorCode Error code
     */
    public ForbiddenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with message and cause
     *
     * @param message Error message
     * @param cause The cause
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message, error code and cause
     *
     * @param message Error message
     * @param errorCode Error code
     * @param cause The cause
     */
    public ForbiddenException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Get error code
     *
     * @return Error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}