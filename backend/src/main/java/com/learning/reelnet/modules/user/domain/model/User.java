package com.learning.reelnet.modules.user.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    private String id;  // Auth0 user_id
    private String email;
    private String name;
    private String picture;
    
    private Instant lastLogin;
    private Instant lastSyncTimestamp;
    
    private Map<String, Object> userMetadata;
    private Map<String, Object> appMetadata;
    
    // Roles and permissions
    private Set<UUID> roles;
    private Set<UUID> permissions;
    
    // User status
    private boolean active;
    private boolean emailVerified;
    
    // Additional application data
    private Map<String, Object> applicationData;
    
    // Multi-tenancy info
    private String tenantId;
}
