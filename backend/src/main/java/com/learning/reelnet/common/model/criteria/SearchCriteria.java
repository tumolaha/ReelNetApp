package com.learning.reelnet.common.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Lớp đại diện cho một tiêu chí tìm kiếm.
 * Được sử dụng trong việc xây dựng các truy vấn động (dynamic queries) với JPA Criteria API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    /**
     * Khóa/tên trường để tìm kiếm
     */
    private String key;
    
    /**
     * Phép toán so sánh
     */
    private String operation;
    
    /**
     * Giá trị cần so sánh
     */
    private Object value;
    
    /**
     * Kiểu kết hợp (AND/OR) với các điều kiện tiếp theo
     */
    private String combinator;
    
    /**
     * Constructor với các tham số cơ bản
     * 
     * @param key Khóa/tên trường
     * @param operation Phép toán
     * @param value Giá trị
     */
    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.combinator = "AND"; // Mặc định sử dụng AND
    }
    
    /**
     * Kiểm tra xem có phải là phép toán bằng không
     * 
     * @return true nếu là phép toán bằng (=, :, ==)
     */
    public boolean isEquals() {
        return operation.equals("=") || operation.equals(":") || operation.equals("==");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán không bằng không
     * 
     * @return true nếu là phép toán không bằng (!=, <>)
     */
    public boolean isNotEquals() {
        return operation.equals("!=") || operation.equals("<>");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán lớn hơn không
     * 
     * @return true nếu là phép toán lớn hơn (>)
     */
    public boolean isGreaterThan() {
        return operation.equals(">");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán nhỏ hơn không
     * 
     * @return true nếu là phép toán nhỏ hơn (<)
     */
    public boolean isLessThan() {
        return operation.equals("<");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán lớn hơn hoặc bằng không
     * 
     * @return true nếu là phép toán lớn hơn hoặc bằng (>=)
     */
    public boolean isGreaterThanOrEquals() {
        return operation.equals(">=");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán nhỏ hơn hoặc bằng không
     * 
     * @return true nếu là phép toán nhỏ hơn hoặc bằng (<=)
     */
    public boolean isLessThanOrEquals() {
        return operation.equals("<=");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán like không
     * 
     * @return true nếu là phép toán like (~, LIKE)
     */
    public boolean isLike() {
        return operation.equalsIgnoreCase("LIKE") || operation.equals("~");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán in không
     * 
     * @return true nếu là phép toán in (IN)
     */
    public boolean isIn() {
        return operation.equalsIgnoreCase("IN");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán between không
     * 
     * @return true nếu là phép toán between (BETWEEN)
     */
    public boolean isBetween() {
        return operation.equalsIgnoreCase("BETWEEN");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán null không
     * 
     * @return true nếu là phép toán null (IS NULL)
     */
    public boolean isNull() {
        return operation.equalsIgnoreCase("IS NULL");
    }
    
    /**
     * Kiểm tra xem có phải là phép toán not null không
     * 
     * @return true nếu là phép toán not null (IS NOT NULL)
     */
    public boolean isNotNull() {
        return operation.equalsIgnoreCase("IS NOT NULL");
    }
    
    /**
     * Kiểm tra xem phép kết hợp có phải là AND không
     * 
     * @return true nếu là phép kết hợp AND
     */
    public boolean isAndCombinator() {
        return "AND".equalsIgnoreCase(combinator);
    }
    
    /**
     * Kiểm tra xem phép kết hợp có phải là OR không
     * 
     * @return true nếu là phép kết hợp OR
     */
    public boolean isOrCombinator() {
        return "OR".equalsIgnoreCase(combinator);
    }
} 