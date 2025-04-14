package com.learning.reelnet.common.domain.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Base abstract class for entities which will hold common auditing and
 * identification information.
 * 
 * @param <ID> The type of the identifier
 */
@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier of the entity.
     * Each subclass should define its own ID field with appropriate strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    /**
     * Universal Unique Identifier that can be used for distributed systems.
     * This is particularly useful for event sourcing and microservices.
     */
    @Column(name = "uuid", updatable = false, nullable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

    /**
     * Version used for optimistic locking.
     */
    @Version
    @Column(name = "version")
    private Long version = 0L;

    /**
     * Creation timestamp.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * Last update timestamp.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    /**
     * Creator of the record.
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * Last modifier of the record.
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Flag to indicate if the entity is deleted (soft delete).
     */
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * Timestamp when the entity was deleted.
     */
    @Column(name = "deleted_at")
    private Instant deletedAt;

    /**
     * Who deleted the entity.
     */
    @Column(name = "deleted_by")
    private String deletedBy;

    /**
     * Marks this entity as deleted by setting the deleted flag and timestamp.
     *
     * @param deletedBy The user who deleted the entity
     */
    public void markAsDeleted(String deletedBy) {
        this.deleted = true;
        this.deletedAt = Instant.now();
        this.deletedBy = deletedBy;
    }

    /**
     * Checks if this entity is new (not yet persisted).
     *
     * @return true if the entity is new, false otherwise
     */
    @Transient
    public boolean isNew() {
        return id == null;
    }
    
    /**
     * Returns a JSON-friendly representation of the entity for logging purposes.
     * Override this in subclasses to provide more detailed representation.
     *
     * @return A string representation suitable for logging
     */
    @Transient
    public String toLogString() {
        return String.format("%s(id=%s, uuid=%s)", 
            this.getClass().getSimpleName(), id, uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        BaseEntity<?> that = (BaseEntity<?>) o;
        
        // If both entities have IDs, compare them
        if (this.id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        
        // If at least one entity doesn't have an ID, compare UUIDs
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        // Use ID if available, otherwise use UUID
        return id != null ? Objects.hash(id) : Objects.hash(uuid);
    }

    /**
     * Pre-persist hook to set initial values.
     * This method is called automatically before the entity is persisted.
     */
    @PrePersist
    protected void onPrePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = Instant.now();
        }
    }

    /**
     * Pre-update hook to update the last modified timestamp.
     * This method is called automatically before the entity is updated.
     */
    @PreUpdate
    protected void onPreUpdate() {
        updatedAt = Instant.now();
    }
}