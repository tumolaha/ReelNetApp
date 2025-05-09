package com.learning.reelnet.common.infrastructure.security.exception;

/**
 * Exception xử lý lỗi khi gọi Auth0 Management API
 */
public class Auth0ApiException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private final int statusCode;
    private final String errorCode;
    
    public Auth0ApiException(String message, int statusCode, String errorCode) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    public Auth0ApiException(String message, int statusCode, String errorCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public boolean isRateLimitError() {
        return statusCode == 429;
    }
    
    public boolean isNotFoundError() {
        return statusCode == 404;
    }
    
    public boolean isAuthenticationError() {
        return statusCode == 401 || statusCode == 403;
    }
}