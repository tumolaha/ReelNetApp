package com.learning.reelnet.common.model.enums;

/**
 * Enum defining filter condition combination operators.
 * Used when multiple filter conditions need to be combined.
 */
public enum CombinationOperator {
    /**
     * AND operator - all conditions must be satisfied
     */
    AND("and", "AND"),
    
    /**
     * OR operator - at least one condition must be satisfied
     */
    OR("or", "OR");
    
    /**
     * Code of the operator
     */
    private final String code;
    
    /**
     * Corresponding SQL notation
     */
    private final String sqlOperator;
    
    CombinationOperator(String code, String sqlOperator) {
        this.code = code;
        this.sqlOperator = sqlOperator;
    }
    
    /**
     * Get the operator code
     * 
     * @return Operator code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the corresponding SQL notation
     * 
     * @return SQL notation
     */
    public String getSqlOperator() {
        return sqlOperator;
    }
    
    /**
     * Find combination operator by code
     * 
     * @param code Operator code
     * @return Corresponding operator, defaults to AND if not found
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
        
        return AND; // Default to AND if not found
    }
}