package com.learning.reelnet.common.model.criteria;

/**
 * Enum định nghĩa các phép toán lọc được hỗ trợ trong hệ thống.
 * Sử dụng trong việc xây dựng các tiêu chí lọc động.
 */
public enum FilterOperation {
    /**
     * Phép toán bằng (equals)
     */
    EQUALS("eq", "="),
    
    /**
     * Phép toán không bằng (not equals)
     */
    NOT_EQUALS("ne", "!="),
    
    /**
     * Phép toán lớn hơn (greater than)
     */
    GREATER_THAN("gt", ">"),
    
    /**
     * Phép toán nhỏ hơn (less than)
     */
    LESS_THAN("lt", "<"),
    
    /**
     * Phép toán lớn hơn hoặc bằng (greater than or equals)
     */
    GREATER_THAN_OR_EQUALS("gte", ">="),
    
    /**
     * Phép toán nhỏ hơn hoặc bằng (less than or equals)
     */
    LESS_THAN_OR_EQUALS("lte", "<="),
    
    /**
     * Phép toán trong tập giá trị (in)
     */
    IN("in", "IN"),
    
    /**
     * Phép toán không trong tập giá trị (not in)
     */
    NOT_IN("nin", "NOT IN"),
    
    /**
     * Phép toán trong khoảng (between)
     */
    BETWEEN("between", "BETWEEN"),
    
    /**
     * Phép toán so khớp mẫu (like)
     */
    LIKE("like", "LIKE"),
    
    /**
     * Phép toán kiểm tra tồn tại (exists)
     */
    EXISTS("exists", "EXISTS"),
    
    /**
     * Phép toán từ thời điểm (from date)
     */
    FROM("from", ">="),
    
    /**
     * Phép toán đến thời điểm (to date)
     */
    TO("to", "<="),
    
    /**
     * Phép toán chứa (contains)
     */
    CONTAINS("contains", "LIKE"),
    
    /**
     * Phép toán bắt đầu bằng (starts with)
     */
    STARTS_WITH("startsWith", "LIKE"),
    
    /**
     * Phép toán kết thúc bằng (ends with)
     */
    ENDS_WITH("endsWith", "LIKE"),
    
    /**
     * Phép toán là null (is null)
     */
    IS_NULL("isNull", "IS NULL"),
    
    /**
     * Phép toán không null (is not null)
     */
    IS_NOT_NULL("isNotNull", "IS NOT NULL");
    
    /**
     * Mã của phép toán, sử dụng trong API
     */
    private final String code;
    
    /**
     * Ký hiệu SQL tương ứng
     */
    private final String sqlOperator;
    
    FilterOperation(String code, String sqlOperator) {
        this.code = code;
        this.sqlOperator = sqlOperator;
    }
    
    /**
     * Lấy mã của phép toán
     * 
     * @return Mã phép toán
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
     * Tìm phép toán theo mã
     * 
     * @param code Mã phép toán
     * @return Phép toán tương ứng
     * @throws IllegalArgumentException nếu không tìm thấy phép toán phù hợp
     */
    public static FilterOperation fromCode(String code) {
        for (FilterOperation operation : FilterOperation.values()) {
            if (operation.getCode().equalsIgnoreCase(code)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy phép toán với mã: " + code);
    }
    
    /**
     * Tìm phép toán theo ký hiệu SQL
     * 
     * @param sqlOperator Ký hiệu SQL
     * @return Phép toán tương ứng
     * @throws IllegalArgumentException nếu không tìm thấy phép toán phù hợp
     */
    public static FilterOperation fromSqlOperator(String sqlOperator) {
        for (FilterOperation operation : FilterOperation.values()) {
            if (operation.getSqlOperator().equalsIgnoreCase(sqlOperator)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy phép toán với ký hiệu SQL: " + sqlOperator);
    }
} 