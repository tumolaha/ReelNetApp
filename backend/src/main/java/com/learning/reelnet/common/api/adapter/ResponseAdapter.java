package com.learning.reelnet.common.api.adapter;

import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.api.response.PagedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Adapter class to provide backward compatibility with legacy response formats.
 * This class helps translate between the old dto response patterns and the new
 * standardized API response objects.
 * <p>
 * This adapter is marked as deprecated and will be removed in a future release.
 * Please use the classes in the com.learning.reelnet.common.api.response package directly.
 */
@Deprecated(forRemoval = true, since = "1.0")
public final class ResponseAdapter {

    private ResponseAdapter() {
        // Prevent instantiation
    }

    /**
     * Creates a success response with data and message
     *
     * @param data Response data
     * @param message Success message
     * @param <T> Data type
     * @return ApiResponse containing the data
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.success(data, message);
    }

    /**
     * Creates a success response with data
     *
     * @param data Response data
     * @param <T> Data type
     * @return ApiResponse containing the data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    /**
     * Creates an error response with message and status
     *
     * @param message Error message
     * @param status HTTP status
     * @param <T> Data type
     * @return ApiResponse containing the error
     */
    public static <T> ApiResponse<T> error(String message, HttpStatus status) {
        return ApiResponse.<T>error(status.value(), message);
    }

    /**
     * Creates an error response with message, error code and status
     *
     * @param message Error message
     * @param errorCode Error code
     * @param status HTTP status
     * @param <T> Data type
     * @return ApiResponse containing the error
     */
    public static <T> ApiResponse<T> error(String message, String errorCode, HttpStatus status) {
        ApiResponse<T> response = ApiResponse.<T>error(status.value(), message);
        response.setError(errorCode);
        return response;
    }

    /**
     * Converts pagination data to a PagedResponse
     *
     * @param content Page content
     * @param totalElements Total number of elements
     * @param page Current page number
     * @param size Page size
     * @param <T> Data type
     * @return PagedResponse containing the data and pagination metadata
     */
    public static <T> PagedResponse<T> toPagedResponse(
            List<T> content, long totalElements, int page, int size) {
        
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        
        PagedResponse.PageMetadata pageMetadata = PagedResponse.PageMetadata.builder()
                .number(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasPrevious(!isFirst)
                .hasNext(!isLast)
                .build();
        
        return PagedResponse.<T>builder()
                .content(content)
                .page(pageMetadata)
                .build();
    }

    /**
     * Creates a ResponseEntity from an ApiResponse
     *
     * @param apiResponse ApiResponse object
     * @param <T> Data type
     * @return ResponseEntity containing the ApiResponse
     */
    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(ApiResponse<T> apiResponse) {
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    /**
     * Adds a HATEOAS link to an ApiResponse
     *
     * @param response ApiResponse to add the link to
     * @param href Link URL
     * @param rel Link relation
     * @param method HTTP method
     * @param <T> Data type
     * @return ApiResponse with the added link
     */
    public static <T> ApiResponse<T> addLink(
            ApiResponse<T> response, String href, String rel, String method) {
        return response.addLink(href, rel, method);
    }
} 