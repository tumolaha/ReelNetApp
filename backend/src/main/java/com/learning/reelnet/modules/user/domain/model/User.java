package com.learning.reelnet.modules.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Domain model representing a user in the system.
 * This entity stores user information synced from Auth0.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<UUID> {
    
    /**
     * The Auth0 ID of the user (sub claim from JWT)
     */
    private String auth0Id;
    
    /**
     * The user's email address
     */
    private String email;
    
    /**
     * The user's display name
     */
    private String displayName;
    
    /**
     * URL to the user's profile picture
     */
    private String pictureUrl;
    
    /**
     * The user's locale setting
     */
    private String locale;
    
    /**
     * The user's role in the system
     */
    private String role;
    
    /**
     * Flag indicating whether the user is a content creator
     */
    private Boolean isCreator;
    
    /**
     * User settings stored as JSON
     */
    private String settings;
    
    /**
     * Timestamp of the user's last login
     */
    private LocalDateTime lastLogin;
    
    /**
     * Flag indicating whether the user account is active
     */
    private Boolean isActive;
}
