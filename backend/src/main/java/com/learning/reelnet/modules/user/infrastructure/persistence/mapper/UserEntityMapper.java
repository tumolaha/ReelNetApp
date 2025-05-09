package com.learning.reelnet.modules.user.infrastructure.persistence.mapper;

import java.util.HashMap;

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
                if (user == null) return null;
                
                return UserEntity.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .picture(user.getPicture())
                                .lastLogin(user.getLastLogin())
                                .lastSyncTimestamp(user.getLastSyncTimestamp())
                                .userMetadata(user.getUserMetadata())
                                .appMetadata(user.getAppMetadata())
                                .roles(user.getRoles())
                                .permissions(user.getPermissions())
                                .active(user.isActive())
                                .emailVerified(user.isEmailVerified())
                                .applicationData(user.getApplicationData() != null ? 
                                        user.getApplicationData() : new HashMap<>())
                                .tenantId(user.getTenantId())
                                .build();
        }

        /**
         * Converts a UserEntity JPA entity to a User domain model.
         * 
         * @param entity The UserEntity JPA entity to convert
         * @return The converted User domain model
         */
        public User toDomain(UserEntity entity) {
                if (entity == null) return null;
                
                return User.builder()
                                .id(entity.getId())
                                .email(entity.getEmail())
                                .name(entity.getName())
                                .picture(entity.getPicture())
                                .lastLogin(entity.getLastLogin())
                                .lastSyncTimestamp(entity.getLastSyncTimestamp())
                                .userMetadata(entity.getUserMetadata())
                                .appMetadata(entity.getAppMetadata())
                                .roles(entity.getRoles())
                                .permissions(entity.getPermissions())
                                .active(entity.isActive())
                                .emailVerified(entity.isEmailVerified())
                                .applicationData(entity.getApplicationData())
                                .tenantId(entity.getTenantId())
                                .build();
        }
}
