package com.learning.reelnet.common.model.enums;

import org.springframework.data.domain.Sort;

/**
 * Enum định nghĩa các hướng sắp xếp được hỗ trợ trong hệ thống.
 */
public enum SortDirection {
    /**
     * Sắp xếp tăng dần
     */
    ASCENDING("asc", "ASC"),
    
    /**
     * Sắp xếp giảm dần
     */
    DESCENDING("desc", "DESC");
    
    /**
     * Mã của hướng sắp xếp, sử dụng trong API
     */
    private final String code;
    
    /**
     * Ký hiệu SQL tương ứng
     */
    private final String sqlDirection;
    
    SortDirection(String code, String sqlDirection) {
        this.code = code;
        this.sqlDirection = sqlDirection;
    }
    
    /**
     * Lấy mã của hướng sắp xếp
     * 
     * @return Mã hướng sắp xếp
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Lấy ký hiệu SQL tương ứng
     * 
     * @return Ký hiệu SQL
     */
    public String getSqlDirection() {
        return sqlDirection;
    }
    
    /**
     * Tạo đối tượng Sort.Direction từ SortDirection
     * 
     * @return Đối tượng Sort.Direction tương ứng
     */
    public Sort.Direction toSpringDirection() {
        return this == ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
    
    /**
     * Tìm hướng sắp xếp theo mã
     * 
     * @param code Mã hướng sắp xếp ("asc", "desc")
     * @return Hướng sắp xếp tương ứng, mặc định là ASCENDING nếu không tìm thấy
     */
    public static SortDirection fromCode(String code) {
        if (code == null) {
            return ASCENDING;
        }
        
        for (SortDirection direction : SortDirection.values()) {
            if (direction.getCode().equalsIgnoreCase(code)) {
                return direction;
            }
        }
        
        // Xử lý các biến thể khác
        if ("-1".equals(code) || "descending".equalsIgnoreCase(code)) {
            return DESCENDING;
        }
        
        if ("1".equals(code) || "ascending".equalsIgnoreCase(code)) {
            return ASCENDING;
        }
        
        return ASCENDING; // Mặc định
    }
    
    /**
     * Tìm hướng sắp xếp từ một chuỗi, có hỗ trợ dấu trừ (-) ở đầu
     * 
     * @param fieldName Tên trường sắp xếp, có thể có dấu trừ (-) ở đầu
     * @return Hướng sắp xếp tương ứng (DESCENDING nếu có dấu -, ngược lại là ASCENDING)
     */
    public static SortDirection fromFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return ASCENDING;
        }
        
        return fieldName.startsWith("-") ? DESCENDING : ASCENDING;
    }
    
    /**
     * Lấy tên trường thực từ tên trường có thể có dấu -
     * 
     * @param fieldName Tên trường sắp xếp, có thể có dấu trừ (-) ở đầu
     * @return Tên trường thực (bỏ dấu - nếu có)
     */
    public static String getActualFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return "";
        }
        
        return fieldName.startsWith("-") ? fieldName.substring(1) : fieldName;
    }
} 