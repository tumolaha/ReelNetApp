package com.learning.reelnet.modules.user.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain model representing a user in the system.
 * This entity stores user information synced from Auth0.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    
    private String id; // Auth0 user ID
    private String email;
    private String name;
    private String picture;
    private String nickname;
    private String locale;

    @Builder.Default
    private Set<UUID> roles = new HashSet<>();
    @Builder.Default
    private Set<UUID> permissions = new HashSet<>();
    private LocalDateTime lastSyncedWithAuth0;
    private String auth0UpdatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private boolean emailVerified;
    private boolean blocked;
    private String userMetadata;
    private String appMetadata;
}
