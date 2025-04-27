package com.learning.reelnet.modules.user.infrastructure.persistence.data;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.reelnet.modules.user.infrastructure.persistence.entity.UserPermissionEntity;

/**
 * Spring Data JPA repository for UserPermissionEntity.
 */
public interface SpringDataUserPermissionRepository extends JpaRepository<UserPermissionEntity, UUID> {
    
    /**
     * Find all permissions for a specific user
     * 
     * @param userId the user ID
     * @return set of permission entities
     */
    Set<UserPermissionEntity> findByUser_Id(UUID userId);
      /**
     * Delete permissions by user ID
     * 
     * @param userId the user ID
     */
    void deleteByUser_Id(UUID userId);
    
    /**
     * Delete permissions by user ID - alternative method
     * 
     * @param userId the user ID
     * @deprecated Use deleteByUser_Id instead
     */
    @Deprecated
    default void deleteByUserId(UUID userId) {
        deleteByUser_Id(userId);
    }
    
    /**
     * Check if a permission exists for a user
     * 
     * @param userId the user ID
     * @param permission the permission string
     * @return true if the permission exists
     */
    boolean existsByUser_IdAndPermission(UUID userId, String permission);
}
