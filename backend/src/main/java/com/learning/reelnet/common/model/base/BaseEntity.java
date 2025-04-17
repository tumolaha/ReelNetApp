package com.learning.reelnet.common.model.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Lớp cơ sở cho các entity trong hệ thống.
 * Cung cấp các trường chung cho tất cả các entity như id, thông tin audit.
 * 
 * @param <ID> Kiểu dữ liệu của khóa chính
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * ID tự tăng
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
    
    /**
     * Thời điểm tạo
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Người tạo
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    /**
     * Thời điểm cập nhật gần nhất
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Người cập nhật gần nhất
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    /**
     * Phiên bản để lạc quan khóa
     */
    @Version
    @Column(name = "version")
    private Long version;
    
    /**
     * Đánh dấu đã xóa hay chưa (soft delete)
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    /**
     * Override phương thức equals để so sánh entity theo ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BaseEntity<?> that = (BaseEntity<?>) o;
        
        return id != null && id.equals(that.id);
    }
    
    /**
     * Override phương thức hashCode để tạo mã băm theo ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
} 