package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for invalid requests.
 * Used when input data does not meet business requirements.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Error code
     */
    private String errorCode;

    /**
     * Default constructor
     */
    public BadRequestException() {
        super("Invalid request");
    }

    /**
     * Constructor with message
     *
     * @param message Error message
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor with message and error code
     *
     * @param message Error message
     * @param errorCode Error code
     */
    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor with message and cause
     *
     * @param message Error message
     * @param cause The cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message, error code and cause
     *
     * @param message Error message
     * @param errorCode Error code
     * @param cause The cause
     */
    public BadRequestException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Get the error code
     *
     * @return Error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}