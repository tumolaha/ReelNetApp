package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Lớp xử lý các tham số phân trang.
 * Có thể sử dụng độc lập hoặc kết hợp với QueryParams.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginationParams {
    
    /**
     * Số trang, bắt đầu từ 0
     */
    private Integer page = 0;
    
    /**
     * Số lượng bản ghi trên mỗi trang
     */
    private Integer limit = 20;
    
    /**
     * Tổng số bản ghi (được điền sau khi truy vấn)
     */
    private Long total;
    
    /**
     * Tổng số trang (được điền sau khi truy vấn)
     */
    private Integer totalPages;
    
    /**
     * Có trang trước không
     */
    private Boolean hasPrevious;
    
    /**
     * Có trang sau không
     */
    private Boolean hasNext;
    
    /**
     * Mã định danh cho cursor-based pagination
     */
    private String cursor;
    
    /**
     * Constructor với các tham số cơ bản
     * 
     * @param page Số trang
     * @param limit Số lượng bản ghi trên mỗi trang
     */
    public PaginationParams(Integer page, Integer limit) {
        this.page = page != null ? page : 0;
        this.limit = limit != null ? limit : 20;
    }
    
    /**
     * Tạo đối tượng Pageable cho Spring Data
     *
     * @return Đối tượng Pageable
     */
    public Pageable toPageable() {
        return PageRequest.of(page, limit);
    }
    
    /**
     * Tạo đối tượng Pageable với sắp xếp
     *
     * @param sort Đối tượng Sort
     * @return Đối tượng Pageable với sắp xếp
     */
    public Pageable toPageable(Sort sort) {
        return PageRequest.of(page, limit, sort);
    }
    
    /**
     * Cập nhật thông tin phân trang sau khi truy vấn
     *
     * @param total Tổng số bản ghi
     * @param totalPages Tổng số trang
     */
    public void updatePaginationInfo(Long total, Integer totalPages) {
        this.total = total;
        this.totalPages = totalPages;
        this.hasPrevious = page > 0;
        this.hasNext = page + 1 < totalPages;
    }
    
    /**
     * Kiểm tra xem có phân trang không
     *
     * @return true nếu có phân trang
     */
    public boolean isPaginated() {
        return page != null && limit != null;
    }
} 