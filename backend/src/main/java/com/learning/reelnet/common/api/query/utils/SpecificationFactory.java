package com.learning.reelnet.common.api.query.utils;

import org.springframework.data.jpa.domain.Specification;

import com.learning.reelnet.common.api.query.FilterParams;
import com.learning.reelnet.common.api.query.SearchParams;

import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecificationFactory {

    public static <T> Specification<T> buildSpecification(FilterParams filterParams, SearchParams searchParams) {
        Specification<T> filterSpec = null;
        Specification<T> searchSpec = null;
        
        if (filterParams != null && filterParams.hasFilters()) {
            filterSpec = buildFilterSpecification(filterParams);
        }
        
        if (searchParams != null && searchParams.hasSearch()) {
            searchSpec = buildSearchSpecification(searchParams);
        }
        
        return combineWithAnd(filterSpec, searchSpec);
    }
    
    private static <T> Specification<T> buildFilterSpecification(FilterParams filterParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            Map<String, Map<String, Object>> filters = filterParams.getFilters();
            
            for (Map.Entry<String, Map<String, Object>> entry : filters.entrySet()) {
                String field = entry.getKey();
                Map<String, Object> valueMap = entry.getValue();
                
                String operator = (String) valueMap.get("operator");
                Object value = valueMap.get("value");
                
                predicates.add(createPredicate(root, criteriaBuilder, field, operator, value));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    
    private static <T> Specification<T> buildSearchSpecification(SearchParams searchParams) {
        return (root, query, criteriaBuilder) -> {
            if (searchParams != null && searchParams.getQuery() != null && !searchParams.getQuery().isEmpty()) {
                List<Predicate> predicates = new ArrayList<>();
                
                for (String field : searchParams.getFields()) {
                    predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(field).as(String.class)),
                            "%" + searchParams.getQuery().toLowerCase() + "%"));
                }
                
                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
            
            return null;
        };
    }
    
    @SafeVarargs
    private static <T> Specification<T> combineWithAnd(Specification<T>... specifications) {
        Specification<T> result = Specification.where(null);
        for (Specification<T> spec : specifications) {
            if (spec != null) {
                result = result.and(spec);
            }
        }
        return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> Predicate createPredicate(Root<T> root, CriteriaBuilder cb,
            String propertyPath, String operator, Object value) {
        
        // Xử lý properties lồng nhau (nested)
        Path<Object> path;
        if (propertyPath.contains(".")) {
            String[] parts = propertyPath.split("\\.");
            Join<Object, Object> join = root.join(parts[0], JoinType.LEFT);
            
            for (int i = 1; i < parts.length - 1; i++) {
                join = join.join(parts[i], JoinType.LEFT);
            }
            path = join.get(parts[parts.length - 1]);
        } else {
            path = root.get(propertyPath);
        }
        
        // Kiểm tra null
        if (value == null) {
            return operator.equals("IS_NULL") ? cb.isNull(path) : 
                   operator.equals("IS_NOT_NULL") ? cb.isNotNull(path) : 
                   cb.conjunction();
        }
        
        // Xử lý dựa trên kiểu dữ liệu
        Class<?> pathType = path.getJavaType();
        
        // Xử lý cho Date
        if (value instanceof String && (pathType == java.util.Date.class || 
                pathType == java.time.LocalDate.class || 
                pathType == java.time.LocalDateTime.class)) {
            return handleDateComparison(cb, path, operator, (String)value, pathType);
        }
        
        // Xử lý cho Enum
        if (pathType.isEnum() && value instanceof String) {
            try {
                Enum<?> enumValue = Enum.valueOf((Class<Enum>)pathType, (String)value);
                value = enumValue;
            } catch (IllegalArgumentException e) {
                return cb.conjunction(); // Skip nếu giá trị enum không hợp lệ
            }
        }
        
        // Áp dụng operator
        switch (operator) {
            case "EQUALS":
                return cb.equal(path, value);
            case "NOT_EQUALS":
                return cb.notEqual(path, value);
            case "GREATER_THAN":
                return cb.greaterThan(path.as(Comparable.class), (Comparable)value);
            case "LESS_THAN":
                return cb.lessThan(path.as(Comparable.class), (Comparable)value);
            case "GREATER_THAN_EQUAL":
                return cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable)value);
            case "LESS_THAN_EQUAL":
                return cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable)value);
            case "LIKE":
                return cb.like(cb.lower(path.as(String.class)),
                        "%" + value.toString().toLowerCase() + "%");
            case "CONTAINS":
                return cb.like(cb.lower(path.as(String.class)),
                        "%" + value.toString().toLowerCase() + "%");
            case "STARTS_WITH":
                return cb.like(cb.lower(path.as(String.class)),
                        value.toString().toLowerCase() + "%");
            case "ENDS_WITH":
                return cb.like(cb.lower(path.as(String.class)),
                        "%" + value.toString().toLowerCase());
            case "IN":
                CriteriaBuilder.In<Object> inPredicate = cb.in(path);
                List<?> values = (List<?>) value;
                for (Object val : values) {
                    inPredicate.value(val);
                }
                return inPredicate;
            case "NOT_IN":
                return cb.not(path.in((List<?>) value));
            case "IS_NULL":
                return cb.isNull(path);
            case "IS_NOT_NULL":
                return cb.isNotNull(path);
            default:
                return cb.equal(path, value);
        }
    }
    
    // Thêm method hỗ trợ cho Date
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static <T> Predicate handleDateComparison(CriteriaBuilder cb, 
            Path<Object> path, String operator, String dateStr, Class<?> targetType) {
        try {
            // Chuyển đổi string thành đúng kiểu date
            Object dateValue;
            if (targetType == java.time.LocalDate.class) {
                dateValue = java.time.LocalDate.parse(dateStr);
            } else if (targetType == java.time.LocalDateTime.class) {
                dateValue = java.time.LocalDateTime.parse(dateStr);
            } else {
                // java.util.Date
                dateValue = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            }
            
            // So sánh với operator
            switch (operator) {
                case "EQUALS": 
                    return cb.equal(path, dateValue);
                case "GREATER_THAN":
                    return cb.greaterThan(path.as(Comparable.class), (Comparable)dateValue);
                case "LESS_THAN":
                    return cb.lessThan(path.as(Comparable.class), (Comparable)dateValue);
                case "GREATER_THAN_EQUAL":  
                    return cb.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable)dateValue);
                case "LESS_THAN_EQUAL":
                    return cb.lessThanOrEqualTo(path.as(Comparable.class), (Comparable)dateValue);
                case "BETWEEN":
                    String[] dateRange = dateStr.split("TO");
                    if (dateRange.length == 2) {
                        Object startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateRange[0]);
                        Object endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateRange[1]);
                        return cb.between(path.as(Comparable.class), (Comparable)startDate, (Comparable)endDate);
                    } else {
                        return cb.conjunction(); // Trả về true nếu parse lỗi
                    }
                case "NOT_BETWEEN":
                    String[] notBetweenRange = dateStr.split("TO");
                    if (notBetweenRange.length == 2) {
                        Object startDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(notBetweenRange[0]);
                        Object endDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(notBetweenRange[1]);
                        return cb.not(cb.between(path.as(Comparable.class), (Comparable)startDate, (Comparable)endDate));
                    } else {
                        return cb.conjunction(); // Trả về true nếu parse lỗi
                    }
                case "IS_NULL":
                    return cb.isNull(path);
                case "IS_NOT_NULL": 
                    return cb.isNotNull(path);
                case "IN":
                    CriteriaBuilder.In<Object> inPredicate = cb.in(path);
                    List<?> values = (List<?>) dateValue;
                    for (Object val : values) {
                        inPredicate.value(val);
                    }
                    return inPredicate;
                case "NOT_IN":
                    return cb.not(path.in((List<?>) dateValue));
                case "LIKE":
                    return cb.like(cb.lower(path.as(String.class)),
                            "%" + dateValue.toString().toLowerCase() + "%");
                case "CONTAINS":
                    return cb.like(cb.lower(path.as(String.class)),
                            "%" + dateValue.toString().toLowerCase() + "%");
                case "STARTS_WITH":
                    return cb.like(cb.lower(path.as(String.class)),
                            dateValue.toString().toLowerCase() + "%");
                case "ENDS_WITH":
                    return cb.like(cb.lower(path.as(String.class)),
                            "%" + dateValue.toString().toLowerCase());
                case "NOT_EQUALS":
                    return cb.notEqual(path, dateValue);
                default:
                    return cb.equal(path, dateValue);
            }
        } catch (Exception e) {
            return cb.conjunction(); // Trả về true nếu parse lỗi
        }
    }
}
