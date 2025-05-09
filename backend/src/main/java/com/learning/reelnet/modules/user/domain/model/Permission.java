package com.learning.reelnet.modules.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    private UUID id;
    private String name;
    private String description;
    private String resource;
    private String action;
    private String auth0PermissionId;
    
    // Instead of directly referencing entities, we reference other domain models
    // This would typically be a Set<Role> but would need Role domain model
    private Set<UUID> roleIds;
}
