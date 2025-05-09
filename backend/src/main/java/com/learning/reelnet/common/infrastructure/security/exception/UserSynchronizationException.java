package com.learning.reelnet.common.infrastructure.security.exception;

/**
 * Exception được ném ra khi có lỗi trong quá trình đồng bộ thông tin người dùng từ Auth0
 * hoặc khi quá trình xác thực/đồng bộ người dùng gặp vấn đề.
 */
public class UserSynchronizationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Tạo ngoại lệ với thông báo lỗi
     * 
     * @param message Thông báo lỗi
     */
    public UserSynchronizationException(String message) {
        super(message);
    }

    /**
     * Tạo ngoại lệ với thông báo lỗi và nguyên nhân gốc
     * 
     * @param message Thông báo lỗi
     * @param cause Nguyên nhân gốc gây ra lỗi
     */
    public UserSynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Tạo ngoại lệ với nguyên nhân gốc
     * 
     * @param cause Nguyên nhân gốc gây ra lỗi
     */
    public UserSynchronizationException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Tạo ngoại lệ cho các lỗi liên quan đến API Auth0
     * 
     * @param userId ID người dùng
     * @param apiEndpoint Endpoint API gặp lỗi
     * @param statusCode Mã lỗi HTTP
     * @param errorMessage Thông báo lỗi
     */
    public UserSynchronizationException(String userId, String apiEndpoint, int statusCode, String errorMessage) {
        super(String.format("Lỗi khi đồng bộ người dùng %s từ Auth0 API %s: %d - %s", 
                            userId, apiEndpoint, statusCode, errorMessage));
    }
    
    /**
     * Tạo ngoại lệ khi token không hợp lệ
     * 
     * @param message Thông báo lỗi
     */
    public static UserSynchronizationException invalidToken(String message) {
        return new UserSynchronizationException("Invalid Auth0 token: " + message);
    }
    
    /**
     * Tạo ngoại lệ khi gặp lỗi rate limit từ Auth0
     */
    public static UserSynchronizationException rateLimitExceeded() {
        return new UserSynchronizationException("Auth0 API rate limit exceeded. Please try again later.");
    }
    
    /**
     * Tạo ngoại lệ khi người dùng không tồn tại trong Auth0
     * 
     * @param userId ID người dùng
     */
    public static UserSynchronizationException userNotFound(String userId) {
        return new UserSynchronizationException("User with ID " + userId + " not found in Auth0");
    }
    
    /**
     * Tạo ngoại lệ cho các lỗi đồng bộ metadata
     * 
     * @param userId ID người dùng
     * @param metadataType Loại metadata (user_metadata hoặc app_metadata)
     */
    public static UserSynchronizationException metadataSyncError(String userId, String metadataType) {
        return new UserSynchronizationException(
            "Failed to synchronize " + metadataType + " for user " + userId);
    }
}