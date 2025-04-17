package com.learning.reelnet.common.model.base;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp cơ sở cho các DTO trong hệ thống.
 * Cung cấp các trường chung cho tất cả các DTO như id, thông tin audit.
 * 
 * @param <ID> Kiểu dữ liệu của khóa chính
 */
@Getter
@Setter
public abstract class BaseDto<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * ID của đối tượng
     */
    private ID id;
    
    /**
     * Thời điểm tạo
     */
    private LocalDateTime createdAt;
    
    /**
     * Người tạo
     */
    private String createdBy;
    
    /**
     * Thời điểm cập nhật gần nhất
     */
    private LocalDateTime updatedAt;
    
    /**
     * Người cập nhật gần nhất
     */
    private String updatedBy;
    
    /**
     * Đánh dấu đã xóa hay chưa (soft delete)
     */
    private Boolean deleted = false;
} 