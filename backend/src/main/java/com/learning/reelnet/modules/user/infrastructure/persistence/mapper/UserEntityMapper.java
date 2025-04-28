package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper class for converting between User domain model and UserEntity JPA
 * entity.
 * This supports the separation of concerns between domain logic and
 * persistence.
 */
@Component
public class UserEntityMapper {

    /**
     * Converts a User domain model to a UserEntity JPA entity.
     * 
     * @param user The User domain model to convert
     * @return The converted UserEntity JPA entity
     */
    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());
        entity.setPicture(user.getPicture());
        entity.setNickname(user.getNickname());
        entity.setLocale(user.getLocale());
        entity.setPermissions(user.getPermissions());
        entity.setLastSyncedWithAuth0(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setAuth0UpdatedAt(user.getAuth0UpdatedAt());
        entity.setRoles(user.getRoles());
        entity.setCreatedAt(Date.from(user.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setUpdatedAt(Date.from(user.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setLastLogin(Date.from(user.getLastLogin().atZone(ZoneId.systemDefault()).toInstant()));
        entity.setEmailVerified(user.isEmailVerified());
        entity.setBlocked(user.isBlocked());
        entity.setUserMetadata(user.getUserMetadata());
        entity.setAppMetadata(user.getAppMetadata());

        return entity;
    }

    /**
     * Converts a UserEntity JPA entity to a User domain model.
     * 
     * @param entity The UserEntity JPA entity to convert
     * @return The converted User domain model
     */
    public User toDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .picture(entity.getPicture())
                .nickname(entity.getNickname())
                .locale(entity.getLocale())
                .permissions(entity.getPermissions())
                .roles(entity.getRoles())
                .lastSyncedWithAuth0(
                        entity.getLastSyncedWithAuth0().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .auth0UpdatedAt(entity.getAuth0UpdatedAt())
                .createdAt(entity.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .updatedAt(entity.getUpdatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .lastLogin(entity.getLastLogin().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .emailVerified(entity.isEmailVerified())
                .blocked(entity.isBlocked())
                .userMetadata(entity.getUserMetadata())
                .appMetadata(entity.getAppMetadata())
                .build();
    }
}
