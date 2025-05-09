package com.learning.reelnet.modules.user.domain.model;

import java.util.Set;
import java.util.UUID;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    private UUID id;
    
    private String name;
    
    private String description;
    
    private Set<Permission> permissions;
    
    private String auth0RoleId;
}
