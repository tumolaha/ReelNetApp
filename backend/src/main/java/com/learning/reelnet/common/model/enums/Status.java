package com.learning.reelnet.common.model.enums;

/**
 * Enum định nghĩa các trạng thái chung được sử dụng trong hệ thống.
 */
public enum Status {
    /**
     * Trạng thái hoạt động
     */
    ACTIVE("active", "Hoạt động"),
    
    /**
     * Trạng thái không hoạt động
     */
    INACTIVE("inactive", "Không hoạt động"),
    
    /**
     * Trạng thái tạm khóa
     */
    SUSPENDED("suspended", "Tạm khóa"),
    
    /**
     * Trạng thái chờ duyệt
     */
    PENDING("pending", "Chờ duyệt"),
    
    /**
     * Trạng thái đã duyệt
     */
    APPROVED("approved", "Đã duyệt"),
    
    /**
     * Trạng thái từ chối
     */
    REJECTED("rejected", "Từ chối"),
    
    /**
     * Trạng thái nháp
     */
    DRAFT("draft", "Nháp"),
    
    /**
     * Trạng thái đã xuất bản
     */
    PUBLISHED("published", "Đã xuất bản"),
    
    /**
     * Trạng thái đã xóa
     */
    DELETED("deleted", "Đã xóa"),
    
    /**
     * Trạng thái đã hoàn thành
     */
    COMPLETED("completed", "Đã hoàn thành"),
    
    /**
     * Trạng thái đã hủy
     */
    CANCELLED("cancelled", "Đã hủy"),
    
    /**
     * Trạng thái đang xử lý
     */
    PROCESSING("processing", "Đang xử lý"),
    
    /**
     * Trạng thái đã hết hạn
     */
    EXPIRED("expired", "Đã hết hạn");
    
    /**
     * Mã của trạng thái
     */
    private final String code;
    
    /**
     * Mô tả của trạng thái
     */
    private final String description;
    
    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * Lấy mã của trạng thái
     * 
     * @return Mã trạng thái
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Lấy mô tả của trạng thái
     * 
     * @return Mô tả trạng thái
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Tìm trạng thái theo mã
     * 
     * @param code Mã trạng thái
     * @return Trạng thái tương ứng, null nếu không tìm thấy
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
     * Kiểm tra xem trạng thái hiện tại có phải là trạng thái hoạt động không
     * 
     * @return true nếu là trạng thái hoạt động
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * Kiểm tra xem trạng thái hiện tại có phải là trạng thái không hoạt động hoặc đã xóa không
     * 
     * @return true nếu là trạng thái không hoạt động hoặc đã xóa
     */
    public boolean isInactiveOrDeleted() {
        return this == INACTIVE || this == DELETED;
    }
    
    /**
     * Kiểm tra xem trạng thái hiện tại có phải là trạng thái đang xử lý không
     * 
     * @return true nếu là trạng thái đang xử lý
     */
    public boolean isProcessing() {
        return this == PROCESSING || this == PENDING;
    }
    
    /**
     * Kiểm tra xem trạng thái hiện tại có phải là trạng thái đã hoàn thành không
     * 
     * @return true nếu là trạng thái đã hoàn thành
     */
    public boolean isCompleted() {
        return this == COMPLETED || this == APPROVED;
    }
} 