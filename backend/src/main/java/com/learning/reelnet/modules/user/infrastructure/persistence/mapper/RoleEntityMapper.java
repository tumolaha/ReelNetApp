package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.domain.model.Permission;
import com.learning.reelnet.modules.user.domain.model.Role;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.PermissionEntity;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.RoleEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoleEntityMapper {

    private final PermissionEntityMapper permissionMapper;
    
    public Role toDomain(RoleEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Set<Permission> permissions = null;
        if (entity.getPermissions() != null) {
            permissions = entity.getPermissions().stream()
                .map(permissionMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        return Role.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .permissions(permissions)
            .auth0RoleId(entity.getAuth0RoleId())
            .build();
    }
    
    public RoleEntity toEntity(Role domain) {
        if (domain == null) {
            return null;
        }
        
        Set<PermissionEntity> permissions = null;
        if (domain.getPermissions() != null) {
            permissions = domain.getPermissions().stream()
                .map(permissionMapper::toEntity)
                .collect(Collectors.toSet());
        }
        
        return RoleEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            .permissions(permissions)
            .auth0RoleId(domain.getAuth0RoleId())
            .build();
    }
}
