package com.learning.reelnet.modules.user.application.services;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.modules.user.domain.model.UserPermission;
import com.learning.reelnet.modules.user.domain.repository.UserPermissionRepository;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing user permissions
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserPermissionService {
    
    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;
    
    /**
     * Get all permissions for a user
     * 
     * @param userId the user ID
     * @return set of permissions
     */
    @Transactional(readOnly = true)
    public Set<UserPermission> getUserPermissions(UUID userId) {
        // Verify user exists
        userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return userPermissionRepository.findByUserId(userId);
    }
}
