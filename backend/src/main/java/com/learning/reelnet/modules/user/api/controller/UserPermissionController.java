package com.learning.reelnet.modules.user.api.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reelnet.modules.user.api.dto.UserPermissionDTO;
import com.learning.reelnet.modules.user.application.services.UserPermissionService;
import com.learning.reelnet.modules.user.domain.model.UserPermission;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST API controller for user permissions
 */
@RestController
@RequestMapping("/api/users/{userId}/permissions")
@RequiredArgsConstructor
@Slf4j
public class UserPermissionController {

    private final UserPermissionService userPermissionService;

    /**
     * Get all permissions for a user
     * 
     * @param userId the user ID
     * @return list of permissions
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or @securityService.validateResourceAccess(#userId)")
    public ResponseEntity<List<UserPermissionDTO>> getUserPermissions(@PathVariable UUID userId) {
        log.debug("Getting permissions for user ID: {}", userId);
        
        List<UserPermissionDTO> permissions = userPermissionService.getUserPermissions(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(permissions);
    }
      /**
     * Map domain model to DTO
     * 
     * @param permission the domain model
     * @return the DTO
     */
    private UserPermissionDTO mapToDto(UserPermission permission) {
        return new UserPermissionDTO(permission.getId(), permission.getUser().getId(), permission.getPermission());
    }
}
