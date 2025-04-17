package com.learning.reelnet.common.api.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Lớp tổng hợp quản lý các tham số truy vấn bao gồm phân trang và sắp xếp.
 * Kết hợp các chức năng của PaginationParams và SortParams.
 */
@Getter
@Setter
@NoArgsConstructor
public class QueryParams {

    /**
     * Tham số phân trang
     */
    private PaginationParams pagination = new PaginationParams();

    /**
     * Tham số sắp xếp
     */
    private SortParams sort = new SortParams();

    /**
     * Tạo Pageable object cho Spring Data với sắp xếp
     *
     * @return Pageable object
     */
    public Pageable toPageable() {
        if (!pagination.isPaginated()) {
            return Pageable.unpaged();
        }

        Sort springSort = sort.toSort();
        if (springSort.isUnsorted()) {
            return PageRequest.of(pagination.getPage(), pagination.getLimit());
        } else {
            return PageRequest.of(pagination.getPage(), pagination.getLimit(), springSort);
        }
    }

    /**
     * Tạo Pageable object cho Spring Data với sắp xếp giới hạn cho các trường cho phép
     *
     * @param allowedFields Danh sách các trường được phép sắp xếp
     * @return Pageable object
     */
    public Pageable toPageable(String... allowedFields) {
        if (!pagination.isPaginated()) {
            return Pageable.unpaged();
        }

        Sort springSort = sort.toSort(allowedFields);
        if (springSort.isUnsorted()) {
            return PageRequest.of(pagination.getPage(), pagination.getLimit());
        } else {
            return PageRequest.of(pagination.getPage(), pagination.getLimit(), springSort);
        }
    }

    /**
     * Cập nhật thông tin phân trang sau khi truy vấn
     *
     * @param total Tổng số bản ghi
     */
    public void updatePaginationInfo(long total) {
        int totalPages = pagination.getLimit() > 0 ? (int) Math.ceil((double) total / pagination.getLimit()) : 0;
        pagination.updatePaginationInfo(total, totalPages);
    }

    /**
     * Kiểm tra xem có áp dụng phân trang không
     *
     * @return true nếu áp dụng phân trang
     */
    public boolean isPaginationApplicable() {
        return pagination.isPaginated();
    }

    /**
     * Kiểm tra xem có áp dụng sắp xếp không
     *
     * @return true nếu áp dụng sắp xếp
     */
    public boolean hasSorting() {
        return sort.hasSorting();
    }
} 