package com.learning.reelnet.common.api.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryParams {
    @Builder.Default
    private int page = 0;
    
    @Builder.Default
    private int size = 10;
    
    @Builder.Default
    private String sortBy = "createdAt";
    
    @Builder.Default
    private String sortDirection = "DESC";
    
    private Long totalElements;
    private Integer totalPages;
    
    public Pageable toPageable() {
        // Xử lý sortDirection không hợp lệ
        String direction = this.sortDirection;
        if (direction == null || (!direction.equalsIgnoreCase("ASC") && !direction.equalsIgnoreCase("DESC"))) {
            direction = "DESC"; // Giá trị mặc định nếu không hợp lệ
        }
        
        Sort sort = Sort.by(
            Sort.Direction.fromString(direction), 
            sortBy
        );
        
        return PageRequest.of(page, size, sort);
    }
    
    public void updatePaginationInfo(Long totalElements) {
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }
}


