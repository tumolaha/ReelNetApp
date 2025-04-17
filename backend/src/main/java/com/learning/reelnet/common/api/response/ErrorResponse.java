package com.learning.reelnet.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Cấu trúc phản hồi lỗi chuẩn cho API.
 * Định dạng chuẩn cho tất cả các phản hồi lỗi API trong hệ thống.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    /**
     * Mã trạng thái HTTP
     */
    private int status;
    
    /**
     * Thông báo lỗi
     */
    private String message;
    
    /**
     * Mã lỗi
     */
    private String errorCode;
    
    /**
     * Đường dẫn gây ra lỗi
     */
    private String path;
    
    /**
     * Thời gian phản hồi
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Danh sách lỗi chi tiết
     */
    @Builder.Default
    private List<ValidationError> errors = new ArrayList<>();
    
    /**
     * ID của yêu cầu gây ra lỗi
     */
    private String requestId;
    
    /**
     * Khởi tạo ErrorResponse với mã lỗi, thông báo và đường dẫn
     * 
     * @param errorCode Mã lỗi
     * @param message Thông báo lỗi
     * @param path Đường dẫn gây ra lỗi
     */
    public ErrorResponse(String errorCode, String message, String path) {
        this.status = 400; // Mặc định là Bad Request
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }
    
    /**
     * Đặt danh sách lỗi validation từ Map
     * 
     * @param validationErrors Map chứa các lỗi validation (key: tên trường, value: thông báo lỗi)
     */
    public void setValidationErrors(Map<String, String> validationErrors) {
        if (validationErrors != null) {
            if (this.errors == null) {
                this.errors = new ArrayList<>();
            } else {
                this.errors.clear();
            }
            
            validationErrors.forEach((field, message) -> {
                this.errors.add(new ValidationError(field, message));
            });
        }
    }
    
    /**
     * Thêm lỗi validation vào danh sách
     *
     * @param field Trường bị lỗi
     * @param message Thông báo lỗi
     * @return ErrorResponse
     */
    public ErrorResponse addValidationError(String field, String message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
        return this;
    }
    
    /**
     * Lớp đại diện cho lỗi validation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        /**
         * Trường bị lỗi
         */
        private String field;
        
        /**
         * Thông báo lỗi
         */
        private String message;
    }
    
    /**
     * Tạo phản hồi lỗi cơ bản
     *
     * @param status Mã trạng thái HTTP
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse of(int status, String message, String path) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .path(path)
                .build();
    }
    
    /**
     * Tạo phản hồi lỗi với mã lỗi
     *
     * @param status Mã trạng thái HTTP
     * @param message Thông báo lỗi
     * @param errorCode Mã lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse of(int status, String message, String errorCode, String path) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .path(path)
                .build();
    }
    
    /**
     * Tạo phản hồi lỗi 400 Bad Request
     *
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse badRequest(String message, String path) {
        return of(400, message, path);
    }
    
    /**
     * Tạo phản hồi lỗi 404 Not Found
     *
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse notFound(String message, String path) {
        return of(404, message, path);
    }
    
    /**
     * Tạo phản hồi lỗi 401 Unauthorized
     *
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse unauthorized(String message, String path) {
        return of(401, message, path);
    }
    
    /**
     * Tạo phản hồi lỗi 403 Forbidden
     *
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse forbidden(String message, String path) {
        return of(403, message, path);
    }
    
    /**
     * Tạo phản hồi lỗi 500 Internal Server Error
     *
     * @param message Thông báo lỗi
     * @param path Đường dẫn
     * @return ErrorResponse
     */
    public static ErrorResponse serverError(String message, String path) {
        return of(500, message, path);
    }
} 