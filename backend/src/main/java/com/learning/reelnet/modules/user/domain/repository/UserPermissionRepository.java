package com.learning.reelnet.modules.user.domain.repository;

import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.modules.user.domain.model.UserPermission;

/**
 * Repository for managing user permissions
 */
public interface UserPermissionRepository {
    
    /**
     * Find all permissions for a user
     * 
     * @param userId the user ID
     * @return set of user permissions
     */
    Set<UserPermission> findByUserId(UUID userId);
    
    /**
     * Find all permissions for a user by underscore format for JPA
     * 
     * @param userId the user ID
     * @return set of user permissions
     */
    Set<UserPermission> findByUser_Id(UUID userId);
    
    /**
     * Save a user permission
     * 
     * @param userPermission the permission to save
     * @return the saved permission
     */
    UserPermission save(UserPermission userPermission);
    
    /**
     * Delete a user permission
     * 
     * @param userPermission the permission to delete
     */
    void delete(UserPermission userPermission);
}
