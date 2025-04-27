package com.learning.reelnet.modules.user.api.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for UserPermission
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPermissionDTO {
    
    /**
     * The permission ID
     */
    private UUID id;
    
    /**
     * The user ID
     */
    private UUID userId;
    
    /**
     * The permission string
     */
    private String permission;
}
