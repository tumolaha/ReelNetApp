package com.learning.reelnet.common.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.learning.reelnet.common.api.query.PaginationParams;
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
 * Cấu trúc phản hồi phân trang chuẩn cho API.
 * Mở rộng từ ApiResponse để hỗ trợ thông tin phân trang.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PagedResponse<T> {
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
    private List<T> data;
    
    /**
     * Thời gian phản hồi
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Metadata phân trang
     */
    private PageMetadata page;
    
    /**
     * Metadata bổ sung
     */
    private Map<String, Object> meta;
    
    /**
     * Tạo phản hồi phân trang từ Spring Page
     * 
     * @param page Đối tượng Page của Spring
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng PagedResponse
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        return PagedResponse.<T>builder()
                .status(200)
                .message("Thành công")
                .data(page.getContent())
                .page(PageMetadata.from(page))
                .build();
    }
    
    /**
     * Tạo phản hồi phân trang từ Spring Page với thông báo
     * 
     * @param page Đối tượng Page của Spring
     * @param message Thông báo
     * @param <T> Kiểu dữ liệu
     * @return Đối tượng PagedResponse
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
     * Lớp đại diện cho metadata phân trang
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageMetadata {
        /**
         * Số trang hiện tại (dựa trên 0)
         */
        private int number;
        
        /**
         * Kích thước trang
         */
        private int size;
        
        /**
         * Tổng số trang
         */
        private int totalPages;
        
        /**
         * Tổng số phần tử
         */
        private long totalElements;
        
        /**
         * Có trang trước không
         */
        private boolean hasPrevious;
        
        /**
         * Có trang sau không
         */
        private boolean hasNext;
        
        /**
         * Tạo metadata phân trang từ Spring Page
         * 
         * @param page Đối tượng Page của Spring
         * @param <T> Kiểu dữ liệu
         * @return Đối tượng PageMetadata
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
     * Tạo phản hồi phân trang từ List và PaginationParams
     *
     * @param data Danh sách dữ liệu
     * @param paginationParams Thông tin phân trang
     * @param <T> Loại dữ liệu
     * @return PagedResponse
     */
    public static <T> PagedResponse<T> of(List<T> data, PaginationParams paginationParams) {
        PageMetadata pageMetadata = PageMetadata.builder()
                .number(paginationParams.getPage())
                .size(paginationParams.getLimit())
                .totalPages(paginationParams.getTotalPages() != null ? paginationParams.getTotalPages() : 1)
                .totalElements(paginationParams.getTotal() != null ? paginationParams.getTotal() : data.size())
                .hasPrevious(paginationParams.getHasPrevious() != null ? paginationParams.getHasPrevious() : false)
                .hasNext(paginationParams.getHasNext() != null ? paginationParams.getHasNext() : false)
                .build();
        
        return PagedResponse.<T>builder()
                .status(200)
                .message("Thành công")
                .data(data)
                .page(pageMetadata)
                .build();
    }
    
    /**
     * Thêm metadata vào phản hồi
     *
     * @param key Khóa
     * @param value Giá trị
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