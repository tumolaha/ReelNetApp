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
 * Base class for entities in the system.
 * Provides common fields for all entities such as id, audit information.
 * 
 * @param <ID> Data type of the primary key
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * UUID-based ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "UUID")
    private ID id;
    
    /**
     * Creation timestamp
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * Created by user
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    /**
     * Last update timestamp
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Last updated by user
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    /**
     * Version for optimistic locking
     */
    @Version
    @Column(name = "version")
    private Long version = 0L;
    
    /**
     * Flag for soft delete
     */
    @Column(name = "deleted", nullable = false)
    private Boolean deleted = false;
    
    /**
     * Override equals method to compare entities by ID
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BaseEntity<?> that = (BaseEntity<?>) o;
        
        return id != null && id.equals(that.id);
    }
    
    /**
     * Override hashCode method to generate hash code based on ID
     */
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}