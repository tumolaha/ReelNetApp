package com.learning.reelnet.common.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper.
 * Provides a consistent structure for all API responses.
 *
 * @param <T> the type of data contained in the response
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    
    /**
     * Creates a successful response with data.
     *
     * @param data the response data
     * @param <T> the type of data
     * @return a successful ApiResponse containing the data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates a successful response with data and a message.
     *
     * @param data the response data
     * @param message the success message
     * @param <T> the type of data
     * @return a successful ApiResponse containing the data and message
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error response with a message.
     *
     * @param message the error message
     * @param <T> the type of data
     * @return an error ApiResponse with the specified message
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Creates an error response with a message and data.
     *
     * @param message the error message
     * @param data additional error data
     * @param <T> the type of data
     * @return an error ApiResponse with the specified message and data
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }
}