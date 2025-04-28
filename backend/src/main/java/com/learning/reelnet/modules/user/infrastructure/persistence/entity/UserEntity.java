package com.learning.reelnet.modules.user.infrastructure.persistence.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Id;

/**
 * JPA Entity for User in the database.
 * Maps to the users table defined in V1__Create_Users_Table.sql
 */
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(unique = true, nullable = false)
    private String id; // Auth0 user ID
    
    @Column(nullable = false)
    private String email;
    
    private String name;
    private String picture;
    private String nickname;
    private String locale;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    private Set<UUID> roles = new HashSet<>();
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    private Set<UUID> permissions = new HashSet<>();
    
    // Thông tin đồng bộ
    private Date lastSyncedWithAuth0;
    private String auth0UpdatedAt;
    
    // Audit fields
    @CreatedDate
    private Date createdAt;
    
    @LastModifiedDate
    private Date updatedAt;
    
    private Date lastLogin;
    private boolean emailVerified;
    private boolean blocked;
    
    // Dữ liệu tùy chỉnh ứng dụng
    @Column(columnDefinition = "TEXT")
    private String userMetadata;
    
    @Column(columnDefinition = "TEXT")
    private String appMetadata;
}
