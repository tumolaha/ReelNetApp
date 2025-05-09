package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.domain.model.Permission;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.PermissionEntity;

@Component
public class PermissionEntityMapper {

    public Permission toDomain(PermissionEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Permission.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .resource(entity.getResource())
            .action(entity.getAction())
            .auth0PermissionId(entity.getAuth0PermissionId())
            .build();
    }
    
    public PermissionEntity toEntity(Permission domain) {
        if (domain == null) {
            return null;
        }
        
        return PermissionEntity.builder()
            .id(domain.getId())
            .name(domain.getName())
            .description(domain.getDescription())
            .resource(domain.getResource())
            .action(domain.getAction())
            .auth0PermissionId(domain.getAuth0PermissionId())
            .build();
    }
}
