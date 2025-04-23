package com.learning.reelnet.modules.user.domain.model;

import com.learning.reelnet.common.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_auth0_id", columnList = "auth0_id", unique = true),
        @Index(name = "idx_user_email", columnList = "email", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity<UUID> {

    /**
     * Auth0 user ID (e.g., "auth0|12345678")
     */
    @Column(name = "auth0_id", unique = true, nullable = false)
    private String auth0Id;

    /**
     * Email address
     */
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    /**
     * Display name
     */
    @Column(name = "display_name")
    private String displayName;

    /**
     * URL to the user's profile picture (from Auth0)
     */
    @Column(name = "picture_url")
    private String pictureUrl;

    /**
     * User's preferred language
     */
    @Column(name = "locale")
    private String locale;

    /**
     * User's role in the system (can be extended to use a separate role entity)
     */
    @Column(name = "role")
    private String role;

    /**
     * Whether the user is a content creator
     */
    @Column(name = "is_creator")
    private boolean creator;

    /**
     * Settings or preferences in JSON format
     */
    @Column(name = "settings", columnDefinition = "jsonb")
    private String settings;

    /**
     * Last login timestamp (for activity tracking)
     */
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /**
     * Account status
     */
    @Column(name = "is_active", nullable = false)
    private boolean active;

    /**
     * User roles
     */
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    /**
     * User permissions
     */
    /*
    @ElementCollection
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> permissions;
    */

    /**
     * Login count
     */
    @Column(name = "login_count")
    private Integer loginCount;

    /**
     * User metadata
     */
    @Column(name = "metadata", columnDefinition = "jsonb")
    private String metadata;

    @PrePersist
    protected void onCreate() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
        if (getLoginCount() == null) {
            setLoginCount(0);
        }

    }

    public void incrementLoginCount() {
        this.loginCount = this.loginCount == null ? 1 : this.loginCount + 1;
        this.lastLogin = LocalDateTime.now();
    }
}