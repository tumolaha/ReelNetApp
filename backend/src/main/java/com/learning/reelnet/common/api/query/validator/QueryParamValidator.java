package com.learning.reelnet.common.api.query.validator;

import org.springframework.stereotype.Component;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.annotation.SupportedParams;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class QueryParamValidator {
    
    public void validateQueryParams(QueryParams queryParams, Class<?> entityClass) {
        SupportedParams annotation = entityClass.getAnnotation(SupportedParams.class);
        if (annotation == null) return;
        
        // Validate sort field
        if (queryParams != null && queryParams.getSortBy() != null) {
            List<String> allowedFields = Arrays.asList(annotation.allowedSortFields());
            if (!allowedFields.contains(queryParams.getSortBy())) {
                queryParams.setSortBy(allowedFields.isEmpty() ? "createdAt" : allowedFields.get(0));
            }
        }
        
        // Validate page size
        if (queryParams != null && queryParams.getSize() > annotation.maxPageSize()) {
            queryParams.setSize(annotation.maxPageSize());
        }
    }
    
    public void validateFilterParams(FilterParams filterParams, Class<?> entityClass) {
        SupportedParams annotation = entityClass.getAnnotation(SupportedParams.class);
        if (annotation == null) return;
        
        if (filterParams != null && filterParams.hasFilters()) {
            List<String> allowedFields = Arrays.asList(annotation.allowedFilterFields());
            Map<String, Map<String, Object>> filters = filterParams.getFilters();
            
            for (String field : filters.keySet()) {
                if (!allowedFields.contains(field)) {
                    throw new IllegalArgumentException("Invalid filter field: " + field);
                }
            }
        }
    }
    
    public void validateSearchParams(SearchParams searchParams, Class<?> entityClass) {
        SupportedParams annotation = entityClass.getAnnotation(SupportedParams.class);
        if (annotation == null) return;
        
        if (searchParams != null && searchParams.hasSearch()) {
            List<String> allowedFields = Arrays.asList(annotation.allowedSearchFields());
            List<String> searchFields = new ArrayList<>(searchParams.getFields());
            
            // Lọc trường không hợp lệ thay vì ném ngoại lệ
            List<String> validFields = new ArrayList<>();
            for (String field : searchFields) {
                if (allowedFields.contains(field)) {
                    validFields.add(field);
                }
            }
            
            // Thay thế danh sách fields bằng danh sách đã lọc
            searchParams.setFields(validFields);
            
            // Nếu không còn trường tìm kiếm nào hợp lệ, xóa query
            if (validFields.isEmpty()) {
                searchParams.setQuery(null);
            }
        }
    }
    
    public void validateAll(QueryParams queryParams, FilterParams filterParams, 
                           SearchParams searchParams, Class<?> entityClass) {
        validateQueryParams(queryParams, entityClass);
        validateFilterParams(filterParams, entityClass);
        validateSearchParams(searchParams, entityClass);
    }
}
