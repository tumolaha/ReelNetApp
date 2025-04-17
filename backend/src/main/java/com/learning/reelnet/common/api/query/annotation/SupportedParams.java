package com.learning.reelnet.common.api.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation đánh dấu các tham số query được hỗ trợ trong một API.
 * Sử dụng ở mức lớp hoặc phương thức để xác định các tham số query được phép.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface SupportedParams {
    
    /**
     * Danh sách các tham số pagination được hỗ trợ
     */
    String[] pagination() default {"page", "limit"};
    
    /**
     * Danh sách các tham số sort được hỗ trợ
     */
    String[] sort() default {"sort", "order"};
    
    /**
     * Danh sách các tham số search được hỗ trợ
     */
    String[] search() default {"q", "search", "search_mode", "search_operator", "search_fields"};
    
    /**
     * Danh sách các tham số filter được hỗ trợ
     * Mặc định chấp nhận các pattern như filter[field], filter[field][operation]
     */
    String[] filter() default {"filter"};
    
    /**
     * Danh sách các tham số field selection được hỗ trợ
     */
    String[] fields() default {"fields", "include", "exclude", "embed"};
    
    /**
     * Danh sách các trường được phép sắp xếp
     */
    String[] allowedSortFields() default {};
    
    /**
     * Danh sách các trường được phép lọc
     */
    String[] allowedFilterFields() default {};
    
    /**
     * Danh sách các trường được phép tìm kiếm
     */
    String[] allowedSearchFields() default {};
    
    /**
     * Hạn chế kích thước trang tối đa
     */
    int maxPageSize() default 100;
    
    /**
     * Cho phép các tham số không được định nghĩa
     */
    boolean allowUndefinedParams() default false;
} 