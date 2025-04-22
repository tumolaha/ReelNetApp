package com.learning.reelnet.common.model.enums;

/**
 * Enum defining common statuses used in the system.
 */
public enum Status {
    /**
     * Active status
     */
    ACTIVE("active", "Hoạt động"),
    
    /**
     * Inactive status
     */
    INACTIVE("inactive", "Không hoạt động"),
    
    /**
     * Suspended status
     */
    SUSPENDED("suspended", "Tạm khóa"),
    
    /**
     * Pending approval status
     */
    PENDING("pending", "Chờ duyệt"),
    
    /**
     * Approved status
     */
    APPROVED("approved", "Đã duyệt"),
    
    /**
     * Rejected status
     */
    REJECTED("rejected", "Từ chối"),
    
    /**
     * Draft status
     */
    DRAFT("draft", "Nháp"),
    
    /**
     * Published status
     */
    PUBLISHED("published", "Đã xuất bản"),
    
    /**
     * Deleted status
     */
    DELETED("deleted", "Đã xóa"),
    
    /**
     * Completed status
     */
    COMPLETED("completed", "Đã hoàn thành"),
    
    /**
     * Cancelled status
     */
    CANCELLED("cancelled", "Đã hủy"),
    
    /**
     * Processing status
     */
    PROCESSING("processing", "Đang xử lý"),
    
    /**
     * Expired status
     */
    EXPIRED("expired", "Đã hết hạn");
    
    /**
     * Status code
     */
    private final String code;
    
    /**
     * Status description
     */
    private final String description;
    
    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Get the status code
     * 
     * @return Status code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Get the status description
     * 
     * @return Status description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Find status by code
     * 
     * @param code Status code
     * @return Corresponding status, null if not found
     */
    public static Status fromCode(String code) {
        if (code == null) {
            return null;
        }
        
        for (Status status : Status.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        
        return null;
    }
    
    /**
     * Check if current status is active
     * 
     * @return true if status is active
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * Check if current status is inactive or deleted
     * 
     * @return true if status is inactive or deleted
     */
    public boolean isInactiveOrDeleted() {
        return this == INACTIVE || this == DELETED;
    }
    
    /**
     * Check if current status is in processing state
     * 
     * @return true if status is in processing
     */
    public boolean isProcessing() {
        return this == PROCESSING || this == PENDING;
    }
    
    /**
     * Check if current status is completed
     * 
     * @return true if status is completed
     */
    public boolean isCompleted() {
        return this == COMPLETED || this == APPROVED;
    }
}