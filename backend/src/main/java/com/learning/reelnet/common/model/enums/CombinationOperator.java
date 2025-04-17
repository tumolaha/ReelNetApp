package com.learning.reelnet.common.model.enums;

/**
 * Enum định nghĩa các toán tử kết hợp điều kiện lọc.
 * Sử dụng khi cần kết hợp nhiều điều kiện lọc với nhau.
 */
public enum CombinationOperator {
    /**
     * Toán tử VÀ - tất cả các điều kiện phải thỏa mãn
     */
    AND("and", "AND"),
    
    /**
     * Toán tử HOẶC - chỉ cần một điều kiện thỏa mãn
     */
    OR("or", "OR");
    
    /**
     * Mã của toán tử
     */
    private final String code;
    
    /**
     * Ký hiệu SQL tương ứng
     */
    private final String sqlOperator;
    
    CombinationOperator(String code, String sqlOperator) {
        this.code = code;
        this.sqlOperator = sqlOperator;
    }
    
    /**
     * Lấy mã của toán tử
     * 
     * @return Mã toán tử
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Lấy ký hiệu SQL tương ứng
     * 
     * @return Ký hiệu SQL
     */
    public String getSqlOperator() {
        return sqlOperator;
    }
    
    /**
     * Tìm toán tử kết hợp theo mã
     * 
     * @param code Mã toán tử
     * @return Toán tử tương ứng, mặc định là AND nếu không tìm thấy
     */
    public static CombinationOperator fromCode(String code) {
        if (code == null) {
            return AND;
        }
        
        for (CombinationOperator operator : CombinationOperator.values()) {
            if (operator.getCode().equalsIgnoreCase(code)) {
                return operator;
            }
        }
        
        return AND; // Mặc định là AND nếu không tìm thấy
    }
} 