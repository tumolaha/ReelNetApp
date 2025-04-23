package com.learning.reelnet.modules.user.application.mapper;

import org.springframework.stereotype.Component;

import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.domain.model.User;

/**
 * Mapper component to convert between User entity and UserDTO
 */
@Component
public class UserMapper {

    /**
     * Convert a User entity to UserDTO
     * @param user the user entity
     * @return the user DTO
     */
    public UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .pictureUrl(user.getPictureUrl())
                .locale(user.getLocale())
                .role(user.getRole())
                .creator(user.isCreator())
                .settings(user.getSettings())
                .lastLogin(user.getLastLogin())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    /**
     * Convert UserDTO to User entity
     * @param dto the user DTO
     * @return the user entity
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return User.builder()
                .email(dto.getEmail())
                .displayName(dto.getDisplayName())
                .pictureUrl(dto.getPictureUrl())
                .locale(dto.getLocale())
                .role(dto.getRole())
                .creator(dto.isCreator())
                .settings(dto.getSettings())
                .lastLogin(dto.getLastLogin())
                .active(dto.isActive())
                .build();
    }
} 