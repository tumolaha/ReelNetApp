package com.learning.reelnet.common.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for pagination and sorting requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {
    /**
     * The page number (0-based).
     */
    @Builder.Default
    private int page = 0;
    
    /**
     * The page size.
     */
    @Builder.Default
    private int size = 20;
    
    /**
     * Sort parameters, each in format "field,direction" where direction is "asc" or "desc".
     * Example: "name,asc", "createdAt,desc"
     */
    @Builder.Default
    private List<String> sort = new ArrayList<>();
    
    /**
     * Creates a new PageRequest with the specified page and size.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return a new PageRequest
     */
    public static PageRequest of(int page, int size) {
        return PageRequest.builder()
                .page(page)
                .size(size)
                .build();
    }
    
    /**
     * Creates a new PageRequest with the specified page, size, and sort parameters.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @param sort the sort parameters
     * @return a new PageRequest
     */
    public static PageRequest of(int page, int size, List<String> sort) {
        return PageRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build();
    }
    
    /**
     * Convert to Spring Data PageRequest.
     *
     * @return Spring Data PageRequest
     */
    public org.springframework.data.domain.PageRequest toSpringPageRequest() {
        if (sort.isEmpty()) {
            return org.springframework.data.domain.PageRequest.of(page, size);
        }
        
        List<org.springframework.data.domain.Sort.Order> orders = new ArrayList<>();
        
        for (String sortParam : sort) {
            String[] parts = sortParam.split(",");
            if (parts.length > 0) {
                String field = parts[0];
                String direction = parts.length > 1 ? parts[1] : "asc";
                
                org.springframework.data.domain.Sort.Direction dir = 
                        "desc".equalsIgnoreCase(direction) 
                                ? org.springframework.data.domain.Sort.Direction.DESC 
                                : org.springframework.data.domain.Sort.Direction.ASC;
                
                orders.add(new org.springframework.data.domain.Sort.Order(dir, field));
            }
        }
        
        return org.springframework.data.domain.PageRequest.of(
                page, 
                size, 
                org.springframework.data.domain.Sort.by(orders)
        );
    }
}