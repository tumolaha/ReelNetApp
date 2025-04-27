package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.domain.model.UserPermission;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserPermissionEntity;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mapper for converting between UserPermission domain model and UserPermissionEntity JPA entity.
 */
@Component
@AllArgsConstructor
public class UserPermissionEntityMapper {
    private final UserEntityMapper userEntityMapper;
    
    /**
     * Convert a domain model to a JPA entity
     * 
     * @param domain the domain model
     * @return the JPA entity
     */
    public UserPermissionEntity toEntity(UserPermission domain) {
        if (domain == null) {
            return null;
        }
          UserPermissionEntity entity = new UserPermissionEntity();
        if (domain.getId() == null) {
            entity.setId(UUID.randomUUID());
        } else {
            entity.setId(domain.getId());        }
        
        entity.setUser(userEntityMapper.toEntity(domain.getUser()));
        entity.setPermission(domain.getPermission());
        
        // Set audit fields
        LocalDateTime now = LocalDateTime.now();
        if (domain.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        } else {
            entity.setCreatedAt(domain.getCreatedAt());
        }
        
        entity.setUpdatedAt(now);
        
        return entity;
    }
    
    /**
     * Convert a JPA entity to a domain model
     * 
     * @param entity the JPA entity
     * @return the domain model
     */
    public UserPermission toDomainModel(UserPermissionEntity entity) {
        if (entity == null) {
            return null;
        }
          UserPermission domain = new UserPermission();
        domain.setId(entity.getId());
        domain.setUser(userEntityMapper.toDomainModel(entity.getUser()));
        domain.setPermission(entity.getPermission());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        
        return domain;
    }
}
