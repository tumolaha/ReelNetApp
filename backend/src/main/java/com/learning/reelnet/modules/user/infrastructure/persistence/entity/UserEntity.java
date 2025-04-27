package com.learning.reelnet.modules.user.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * JPA Entity for User in the database.
 * Maps to the users table defined in V1__Create_Users_Table.sql
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity<UUID> {
    
    @Column(name = "auth0_id", nullable = false, unique = true)
    private String auth0Id;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "picture_url")
    private String pictureUrl;
    
    @Column(name = "locale")
    private String locale;
    
    @Column(name = "role")
    private String role;
    
    @Column(name = "is_creator")
    private Boolean isCreator;
    
    @Column(name = "settings", columnDefinition = "jsonb")
    private String settings;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
