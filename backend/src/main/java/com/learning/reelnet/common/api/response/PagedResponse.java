package com.learning.reelnet.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Standard paginated response structure for API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Result message
     */
    private String message;
    
    /**
     * Response data
     */
    private List<T> data;
    
    /**
     * Response timestamp
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Pagination metadata
     */
    private PageMetadata page;
    
    /**
     * Additional metadata
     */
    private Map<String, Object> meta;
    
    /**
     * Create paginated response from Spring Page
     * 
     * @param page Spring Page object
     * @param <T> Data type
     * @return PagedResponse object
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return PagedResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(page.getContent())
                .page(PageMetadata.from(page))
                .build();
    }
    
    /**
     * Create paginated response from Spring Page with custom message
     * 
     * @param page Spring Page object
     * @param message Custom message
     * @param <T> Data type
     * @return PagedResponse object
     */
    public static <T> PagedResponse<T> from(Page<T> page, String message) {
        return PagedResponse.<T>builder()
                .status(200)
                .message(message)
                .data(page.getContent())
                .page(PageMetadata.from(page))
                .build();
    }
    
    /**
     * Class representing pagination metadata
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageMetadata {
        /**
         * Current page number (zero-based)
         */
        private int number;
        
        /**
         * Page size
         */
        private int size;
        
        /**
         * Total number of pages
         */
        private int totalPages;
        
        /**
         * Total number of elements
         */
        private long totalElements;
        
        /**
         * Whether there is a previous page
         */
        private boolean hasPrevious;
        
        /**
         * Whether there is a next page
         */
        private boolean hasNext;
        
        /**
         * Create pagination metadata from Spring Page
         * 
         * @param page Spring Page object
         * @param <T> Data type
         * @return PageMetadata object
         */
        public static <T> PageMetadata from(Page<T> page) {
            return PageMetadata.builder()
                    .number(page.getNumber())
                    .size(page.getSize())
                    .totalPages(page.getTotalPages())
                    .totalElements(page.getTotalElements())
                    .hasPrevious(page.hasPrevious())
                    .hasNext(page.hasNext())
                    .build();
        }
    }
    
    /**
     * Create paginated response from List and custom pagination information
     *
     * @param data List of data
     * @param pageNumber Current page number
     * @param pageSize Page size
     * @param totalElements Total number of elements
     * @param <T> Data type
     * @return PagedResponse
     */
    public static <T> PagedResponse<T> of(List<T> data, int pageNumber, int pageSize, long totalElements) {
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        boolean hasPrevious = pageNumber > 0;
        boolean hasNext = pageNumber + 1 < totalPages;
        
        PageMetadata pageMetadata = PageMetadata.builder()
                .number(pageNumber)
                .size(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasPrevious(hasPrevious)
                .hasNext(hasNext)
                .build();
        
        return PagedResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .page(pageMetadata)
                .build();
    }
    
    /**
     * Create paginated response from List and Map containing pagination information
     *
     * @param data List of data
     * @param paginationInfo Map containing pagination information
     * @param <T> Data type
     * @return PagedResponse
     */
    public static <T> PagedResponse<T> of(List<T> data, Map<String, Object> paginationInfo) {
        Integer pageNumber = getIntValue(paginationInfo.get("pageNumber"), 0);
        Integer pageSize = getIntValue(paginationInfo.get("pageSize"), data.size());
        Long totalElements = getLongValue(paginationInfo.get("totalElements"), (long) data.size());
        Integer totalPages = getIntValue(paginationInfo.get("totalPages"), 
                pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0);
        Boolean hasPrevious = getBooleanValue(paginationInfo.get("hasPrevious"), pageNumber > 0);
        Boolean hasNext = getBooleanValue(paginationInfo.get("hasNext"), pageNumber + 1 < totalPages);
        
        PageMetadata pageMetadata = PageMetadata.builder()
                .number(pageNumber)
                .size(pageSize)
                .totalPages(totalPages)
                .totalElements(totalElements)
                .hasPrevious(hasPrevious)
                .hasNext(hasNext)
                .build();
        
        return PagedResponse.<T>builder()
                .status(200)
                .message("Success")
                .data(data)
                .page(pageMetadata)
                .build();
    }
    
    /**
     * Helper method to get Integer value from Object
     */
    private static Integer getIntValue(Object value, Integer defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * Helper method to get Long value from Object
     */
    private static Long getLongValue(Object value, Long defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Number) return ((Number) value).longValue();
        try {
            return Long.parseLong(value.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    
    /**
     * Helper method to get Boolean value from Object
     */
    private static Boolean getBooleanValue(Object value, Boolean defaultValue) {
        if (value == null) return defaultValue;
        if (value instanceof Boolean) return (Boolean) value;
        return Boolean.parseBoolean(value.toString());
    }
    
    /**
     * Add metadata to response
     *
     * @param key Key
     * @param value Value
     * @return PagedResponse
     */
    public PagedResponse<T> addMeta(String key, Object value) {
        if (meta == null) {
            meta = new HashMap<>();
        }
        meta.put(key, value);
        return this;
    }
}