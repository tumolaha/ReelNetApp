package com.learning.reelnet.common.api.query.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation đánh dấu một tham số của phương thức controller là một query parameter.
 * Giúp xử lý tự động binding các HTTP query parameters vào đối tượng.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface QueryParam {
    
    /**
     * Tên của tham số trong URL (mặc định là tên của biến)
     */
    String name() default "";
    
    /**
     * Mô tả của tham số
     */
    String description() default "";
    
    /**
     * Giá trị mặc định của tham số
     */
    String defaultValue() default "";
    
    /**
     * Tham số này có bắt buộc không
     */
    boolean required() default false;
    
    /**
     * Định dạng của tham số (ví dụ: "date", "datetime", "email")
     */
    String format() default "";
    
    /**
     * Giá trị nhỏ nhất (đối với số)
     */
    String min() default "";
    
    /**
     * Giá trị lớn nhất (đối với số)
     */
    String max() default "";
    
    /**
     * Mẫu regex để xác thực
     */
    String pattern() default "";
    
    /**
     * Danh sách các giá trị hợp lệ
     */
    String[] allowedValues() default {};
} 