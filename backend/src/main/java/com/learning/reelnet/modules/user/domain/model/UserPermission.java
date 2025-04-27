package com.learning.reelnet.modules.user.domain.model;

import java.util.UUID;

import com.learning.reelnet.common.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing a user permission in the system.
 * Maps users to their permissions from Auth0.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPermission extends BaseEntity<UUID> {
    
    /**
     * The user this permission belongs to
     */
    private User user;
    
    /**
     * The permission string (e.g., "read:profiles", "edit:content")
     */
    private String permission;
}
