package com.learning.reelnet.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cấu trúc phản hồi chuẩn cho API.
 * Định dạng chuẩn cho tất cả các phản hồi API trong hệ thống.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     * Mã trạng thái HTTP
     */
    private int status;
    
    /**
     * Thông báo kết quả
     */
    private String message;
    
    /**
     * Dữ liệu phản hồi
     */
    private T data;
    
    /**
     * Thời gian phản hồi
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Mã lỗi (nếu có)
     */
    private String error;
    
    /**
     * Metadata bổ sung
     */
    private Map<String, Object> meta;
    
    /**
     * Danh sách các links HATEOAS
     */
    @Builder.Default
    private List<Link> links = new ArrayList<>();
    
    /**
     * Trạng thái thành công của phản hồi
     */
    @Builder.Default
    private boolean success = true;
    
    /**
     * Tạo phản hồi thành công với dữ liệu
     * 
     * @param data Dữ liệu phản hồi
     * @param message Thông báo thành công
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(200)
                .message(message)
                .data(data)
                .success(true)
                .build();
    }
    
    /**
     * Tạo phản hồi thành công chỉ với dữ liệu
     * 
     * @param data Dữ liệu phản hồi
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Thành công");
    }
    
    /**
     * Tạo phản hồi thành công với dữ liệu, thông báo và metadata
     *
     * @param data Dữ liệu phản hồi
     * @param message Thông báo
     * @param meta Metadata
     * @param <T> Loại dữ liệu
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> success(T data, String message, Map<String, Object> meta) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(200)
                .message(message)
                .data(data)
                .meta(meta)
                .success(true)
                .build();
    }
    
    /**
     * Tạo phản hồi lỗi
     * 
     * @param status Mã trạng thái HTTP
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> error(int status, String message) {
        return ApiResponse.<T>builder()
                .status(status)
                .message(message)
                .data(null)
                .success(false)
                .build();
    }
    
    /**
     * Tạo phản hồi lỗi từ đối tượng ErrorResponse
     *
     * @param errorResponse Đối tượng ErrorResponse
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> error(ErrorResponse errorResponse) {
        return ApiResponse.<T>builder()
                .status(errorResponse.getStatus())
                .message(errorResponse.getMessage())
                .error(errorResponse.getErrorCode())
                .timestamp(errorResponse.getTimestamp())
                .success(false)
                .build();
    }
    
    /**
     * Tạo phản hồi lỗi không tìm thấy tài nguyên
     * 
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> notFound(String message) {
        return error(404, message);
    }
    
    /**
     * Tạo phản hồi lỗi dữ liệu không hợp lệ
     * 
     * @param message Thông báo lỗi
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng ApiResponse
     */
    public static <T> ApiResponse<T> badRequest(String message) {
        return error(400, message);
    }
    
    /**
     * Thêm metadata vào phản hồi
     *
     * @param key Khóa
     * @param value Giá trị
     * @return ApiResponse
     */
    public ApiResponse<T> addMeta(String key, Object value) {
        if (meta == null) {
            meta = new HashMap<>();
        }
        meta.put(key, value);
        return this;
    }
    
    /**
     * Thêm link HATEOAS vào phản hồi
     *
     * @param href Đường dẫn của link
     * @param rel Mối quan hệ của link
     * @param method Phương thức HTTP
     * @return ApiResponse
     */
    public ApiResponse<T> addLink(String href, String rel, String method) {
        if (links == null) {
            links = new ArrayList<>();
        }
        links.add(new Link(href, rel, method));
        return this;
    }
    
    /**
     * Tạo phản hồi tạo mới thành công (201 Created)
     *
     * @param data Dữ liệu đã tạo
     * @param <T> Loại dữ liệu
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(201)
                .message("Tạo mới thành công")
                .data(data)
                .success(true)
                .build();
    }
    
    /**
     * Tạo phản hồi cập nhật thành công
     *
     * @param data Dữ liệu đã cập nhật
     * @param <T> Loại dữ liệu
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> updated(T data) {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(200)
                .message("Cập nhật thành công")
                .data(data)
                .success(true)
                .build();
    }
    
    /**
     * Tạo phản hồi xóa thành công
     *
     * @param <T> Loại dữ liệu
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> deleted() {
        return ApiResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .status(200)
                .message("Xóa thành công")
                .success(true)
                .build();
    }
    
    /**
     * Lớp đại diện cho một liên kết HATEOAS
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Link {
        /**
         * Đường dẫn của liên kết
         */
        private String href;
        
        /**
         * Mối quan hệ của liên kết
         */
        private String rel;
        
        /**
         * Phương thức HTTP sử dụng với liên kết
         */
        private String method;
    }
} 