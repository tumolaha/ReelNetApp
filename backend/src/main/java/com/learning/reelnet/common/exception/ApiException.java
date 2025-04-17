package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Base exception class for all API-related errors.
 * ApiException represents errors that occur in the API layer, such as
 * invalid request parameters, unsupported operations, or other issues
 * related to processing API requests.
 */
@Getter
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "API_ERROR";

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final Object[] parameters;

    /**
     * Creates a new ApiException with a message, HTTP status, and default error code.
     *
     * @param message     The detailed error message
     * @param httpStatus  The HTTP status to return to the client
     * @param parameters  Additional contextual parameters for the error
     */
    public ApiException(String message, HttpStatus httpStatus, Object... parameters) {
        this(message, DEFAULT_ERROR_CODE, httpStatus, parameters);
    }

    /**
     * Creates a new ApiException with a message, error code, and HTTP status.
     *
     * @param message     The detailed error message
     * @param errorCode   The unique error code
     * @param httpStatus  The HTTP status to return to the client
     * @param parameters  Additional contextual parameters for the error
     */
    public ApiException(String message, String errorCode, HttpStatus httpStatus, Object... parameters) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.parameters = parameters;
    }

    /**
     * Creates a new ApiException with a message, error code, HTTP status, and cause.
     *
     * @param message     The detailed error message
     * @param errorCode   The unique error code
     * @param httpStatus  The HTTP status to return to the client
     * @param cause       The underlying cause of the exception
     * @param parameters  Additional contextual parameters for the error
     */
    public ApiException(String message, String errorCode, HttpStatus httpStatus, Throwable cause, Object... parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.parameters = parameters;
    }
} 