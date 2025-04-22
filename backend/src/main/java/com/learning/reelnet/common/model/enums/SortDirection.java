package com.learning.reelnet.common.model.enums;

import org.springframework.data.domain.Sort;

/**
 * Enum defining the sort directions supported in the system.
 */
public enum SortDirection {
    /**
     * Ascending sort order
     */
    ASCENDING("asc", "ASC"),
    
    /**
     * Descending sort order
     */
    DESCENDING("desc", "DESC");
    
    /**
     * Code of the sort direction, used in API
     */
    private final String code;
    
    /**
     * Corresponding SQL notation
     */
    private final String sqlDirection;
    
    SortDirection(String code, String sqlDirection) {
        this.code = code;
        this.sqlDirection = sqlDirection;
    }
    
    /**
     * Get the sort direction code
     * 
     * @return Sort direction code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the corresponding SQL notation
     * 
     * @return SQL notation
     */
    public String getSqlDirection() {
        return sqlDirection;
    }
    
    /**
     * Create a Sort.Direction object from SortDirection
     * 
     * @return Corresponding Sort.Direction object
     */
    public Sort.Direction toSpringDirection() {
        return this == ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
    
    /**
     * Find sort direction by code
     * 
     * @param code Sort direction code ("asc", "desc")
     * @return Corresponding sort direction, defaults to ASCENDING if not found
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
        
        // Handle other variants
        if ("-1".equals(code) || "descending".equalsIgnoreCase(code)) {
            return DESCENDING;
        }
        
        if ("1".equals(code) || "ascending".equalsIgnoreCase(code)) {
            return ASCENDING;
        }
        
        return ASCENDING; // Default
    }
    
    /**
     * Find sort direction from a string, supporting minus sign (-) prefix
     * 
     * @param fieldName Sort field name, may have a minus sign (-) prefix
     * @return Corresponding sort direction (DESCENDING if has - prefix, otherwise ASCENDING)
     */
    public static SortDirection fromFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return ASCENDING;
        }
        
        return fieldName.startsWith("-") ? DESCENDING : ASCENDING;
    }
    
    /**
     * Get the actual field name from a field name that may have a minus sign
     * 
     * @param fieldName Sort field name, may have a minus sign (-) prefix
     * @return Actual field name (without the - prefix if present)
     */
    public static String getActualFieldName(String fieldName) {
        if (fieldName == null || fieldName.isEmpty()) {
            return "";
        }
        
        return fieldName.startsWith("-") ? fieldName.substring(1) : fieldName;
    }
}