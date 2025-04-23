package com.learning.reelnet.modules.user.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for transferring user data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private UUID id;
    private String email;
    private String displayName;
    private String pictureUrl;
    private String locale;
    private String role;
    private boolean creator;
    private String settings;
    private LocalDateTime lastLogin;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 