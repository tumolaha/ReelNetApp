package com.learning.reelnet.modules.user.api.dto;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.modules.user.domain.model.User;

import lombok.Getter;

@Getter
public class ReelNetUserDetails {
    
    private final String id;
    private final String email;
    private final String name;
    private final String picture;
    private final boolean emailVerified;
    private final boolean active;
    private final Set<UUID> roleIds;
    private final Set<UUID> permissionIds;
    private final Map<String, Object> userMetadata;
    private final Map<String, Object> appMetadata;
    private final Map<String, Object> applicationData;
    private final String tenantId;
    
    public ReelNetUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.picture = user.getPicture();
        this.emailVerified = user.isEmailVerified();
        this.active = user.isActive();
        this.roleIds = user.getRoles();
        this.permissionIds = user.getPermissions();
        this.userMetadata = user.getUserMetadata();
        this.appMetadata = user.getAppMetadata();
        this.applicationData = user.getApplicationData();
        this.tenantId = user.getTenantId();
    }
    
    public boolean hasRole(UUID roleId) {
        return roleIds != null && roleIds.contains(roleId);
    }
    
    public boolean hasPermission(UUID permissionId) {
        return permissionIds != null && permissionIds.contains(permissionId);
    }
}
