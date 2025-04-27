package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;
/**
 * Mapper class for converting between User domain model and UserEntity JPA entity.
 * This supports the separation of concerns between domain logic and persistence.
 */
@Component
public class UserEntityMapper {

    /**
     * Converts a JPA UserEntity to the User domain model
     * 
     * @param entity the JPA entity to convert
     * @return the corresponding domain model
     */
    public User toDomainModel(UserEntity entity) {
        if (entity == null) return null;
        
        User user = new User();
        user.setId(entity.getId());
        user.setAuth0Id(entity.getAuth0Id());
        user.setEmail(entity.getEmail());
        user.setDisplayName(entity.getDisplayName());
        user.setPictureUrl(entity.getPictureUrl());
        user.setLocale(entity.getLocale());
        user.setRole(entity.getRole());
        user.setIsCreator(entity.getIsCreator());
        user.setSettings(entity.getSettings());
        user.setLastLogin(entity.getLastLogin());
        user.setIsActive(entity.getIsActive());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        user.setCreatedBy(entity.getCreatedBy());
        user.setUpdatedBy(entity.getUpdatedBy());
        
        return user;
    }
    
    /**
     * Converts a User domain model to a JPA UserEntity
     * 
     * @param user the domain model to convert
     * @return the corresponding JPA entity
     */
    public UserEntity toEntity(User user) {
        if (user == null) return null;
        
        UserEntity entity = UserEntity.builder()
                .auth0Id(user.getAuth0Id())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .pictureUrl(user.getPictureUrl())
                .locale(user.getLocale())
                .role(user.getRole())
                .isCreator(user.getIsCreator())
                .settings(user.getSettings())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .build();
        
        // Copy audit fields
        if (user.getId() != null) {
            entity.setId(user.getId());
        } else {
            entity.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt() : LocalDateTime.now());
        }
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setCreatedBy(user.getCreatedBy());
        entity.setUpdatedBy(user.getUpdatedBy());
        
        return entity;
    }
}
