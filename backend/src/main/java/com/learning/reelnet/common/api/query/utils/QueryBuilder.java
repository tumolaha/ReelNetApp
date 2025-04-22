package com.learning.reelnet.common.api.query.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.QueryParams;
import com.learning.reelnet.common.api.query.SearchParams;
import com.learning.reelnet.common.api.query.annotation.SupportedParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Builder for creating query parameters from request parameters
 */
@Slf4j
public class QueryBuilder {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Build QueryParams, FilterParams, and SearchParams objects from request parameters
     * 
     * @param params Map of request parameters
     * @param entityClass Class of entity being queried (for validation)
     * @return Object array with [QueryParams, FilterParams, SearchParams]
     */
    public static Object[] buildQueryParams(Map<String, String> params, Class<?> entityClass) {
        QueryParams queryParams = buildQueryParams(params);
        FilterParams filterParams = buildFilterParams(params);
        SearchParams searchParams = buildSearchParams(params);
        
        // Validate if needed
        validateParams(queryParams, filterParams, searchParams, entityClass);
        
        return new Object[] { queryParams, filterParams, searchParams };
    }
    
    /**
     * Build QueryParams from request parameters
     */
    public static QueryParams buildQueryParams(Map<String, String> params) {
        Integer page = parseIntParam(params.get("page"), 0);
        Integer size = parseIntParam(params.get("size"), 10);
        String sortBy = params.getOrDefault("sortBy", "createdAt");
        String sortDirection = params.getOrDefault("sortDirection", "DESC");
        
        return QueryParams.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();
    }
    
    /**
     * Build FilterParams from request parameters
     */
    public static FilterParams buildFilterParams(Map<String, String> params) {
        // Tạo hai FilterParams riêng biệt
        FilterParams jsonFilterParams = new FilterParams();
        FilterParams dotNotationFilterParams = new FilterParams();
        
        // Process standard filter param
        String filterJson = params.get("filter");
        if (filterJson != null && !filterJson.isEmpty()) {
            try {
                jsonFilterParams = objectMapper.readValue(filterJson, FilterParams.class);
            } catch (Exception e) {
                log.warn("Failed to parse filter JSON: {}", e.getMessage());
            }
        }
        
        // Process field-specific filter params (field.operator=value)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            if (key.contains(".") && !key.startsWith("sort.") && !key.startsWith("page.")) {
                String[] parts = key.split("\\.", 2);
                if (parts.length == 2) {
                    String field = parts[0];
                    String operatorCode = parts[1].toUpperCase();
                    
                    // Convert operator name to enum value if needed
                    String finalOperator;
                    switch (operatorCode) {
                        case "EQ": finalOperator = "EQUALS"; break;
                        case "NEQ": finalOperator = "NOT_EQUALS"; break;
                        case "GT": finalOperator = "GREATER_THAN"; break;
                        case "LT": finalOperator = "LESS_THAN"; break;
                        case "GTE": finalOperator = "GREATER_THAN_EQUAL"; break;
                        case "LTE": finalOperator = "LESS_THAN_EQUAL"; break;
                        default: finalOperator = operatorCode;
                    }
                    
                    dotNotationFilterParams.addFilter(field, finalOperator, value);
                }
            }
        }
        
        // Kết hợp hai FilterParams
        FilterParams result = new FilterParams();
        
        // Thêm các filter từ JSON
        if (jsonFilterParams.hasFilters()) {
            jsonFilterParams.getFilters().forEach((field, filterMap) -> {
                result.getFilters().put(field, filterMap);
            });
        }
        
        // Thêm các filter từ dot notation
        if (dotNotationFilterParams.hasFilters()) {
            dotNotationFilterParams.getFilters().forEach((field, filterMap) -> {
                result.getFilters().put(field, filterMap);
            });
        }
        
        return result;
    }
    
    /**
     * Build SearchParams from request parameters
     */
    public static SearchParams buildSearchParams(Map<String, String> params) {
        SearchParams jsonSearchParams = new SearchParams();
        SearchParams simpleSearchParams = new SearchParams();
        
        // Process full search param
        String searchJson = params.get("search");
        if (searchJson != null && !searchJson.isEmpty()) {
            try {
                jsonSearchParams = objectMapper.readValue(searchJson, SearchParams.class);
            } catch (Exception e) {
                log.warn("Failed to parse search JSON: {}", e.getMessage());
            }
        }
        
        // Process simple q parameter
        String query = params.get("q");
        if (query != null && !query.isEmpty()) {
            simpleSearchParams.setQuery(query);
            
            // Add search fields if specified
            String fields = params.get("searchFields");
            if (fields != null && !fields.isEmpty()) {
                simpleSearchParams.setFields(new ArrayList<>(Arrays.asList(fields.split(","))));
            }
        }
        
        // Ưu tiên JSON search nếu cả hai đều có
        if (jsonSearchParams.hasSearch()) {
            return jsonSearchParams;
        } else {
            return simpleSearchParams;
        }
    }
    
    /**
     * Create Specification from filter and search params
     */
    public static <T> Specification<T> buildSpecification(FilterParams filterParams, SearchParams searchParams) {
        return SpecificationFactory.buildSpecification(filterParams, searchParams);
    }
    
    /**
     * Validate parameters against entity class
     */
    private static void validateParams(QueryParams queryParams, FilterParams filterParams, 
                                       SearchParams searchParams, Class<?> entityClass) {
        if (entityClass == null) return;
        
        SupportedParams annotation = entityClass.getAnnotation(SupportedParams.class);
        if (annotation == null) return;
        
        // Validate sort field
        if (queryParams != null && queryParams.getSortBy() != null) {
            List<String> allowedFields = Arrays.asList(annotation.allowedSortFields());
            if (!allowedFields.isEmpty() && !allowedFields.contains(queryParams.getSortBy())) {
                queryParams.setSortBy(allowedFields.get(0));
            }
        }
        
        // Validate page size
        if (queryParams != null && queryParams.getSize() > annotation.maxPageSize()) {
            queryParams.setSize(annotation.maxPageSize());
        }
        
        // Validate filter fields
        if (filterParams != null && filterParams.hasFilters()) {
            List<String> allowedFields = Arrays.asList(annotation.allowedFilterFields());
            if (!allowedFields.isEmpty()) {
                filterParams.getFilters().keySet().removeIf(field -> !allowedFields.contains(field));
            }
        }
        
        // Validate search fields
        if (searchParams != null && searchParams.hasSearch()) {
            List<String> allowedFields = Arrays.asList(annotation.allowedSearchFields());
            if (!allowedFields.isEmpty()) {
                searchParams.getFields().removeIf(field -> !allowedFields.contains(field));
                if (searchParams.getFields().isEmpty()) {
                    searchParams.setQuery(null);
                }
            }
        }
    }
    
    private static Integer parseIntParam(String value, Integer defaultValue) {
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
} 