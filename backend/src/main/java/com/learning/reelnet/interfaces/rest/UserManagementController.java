package com.learning.reelnet.interfaces.rest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.learning.reelnet.modules.user.api.dto.ReelNetUserDetails;
import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.api.dto.UserSearchCriteria;
import com.learning.reelnet.modules.user.application.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserManagementController {

    private final UserService userService;
    
    /**
     * Lấy thông tin người dùng hiện tại
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        
        String userId = principal.getName();
        log.debug("Getting current user info for: {}", userId);
        
        return userService.findUserById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Lấy thông tin details của người dùng đang đăng nhập
     */
    @GetMapping("/me/details")
    public ResponseEntity<ReelNetUserDetails> getCurrentUserDetails(
            @AuthenticationPrincipal JwtAuthenticationToken principal) {
        
        if (principal.getDetails() instanceof ReelNetUserDetails) {
            return ResponseEntity.ok((ReelNetUserDetails) principal.getDetails());
        }
        
        return ResponseEntity.notFound().build();
    }
    
    /**
     * Tìm kiếm users (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @ModelAttribute UserSearchCriteria criteria,
            Pageable pageable) {
        
        log.debug("Searching users with criteria: {}", criteria);
        return ResponseEntity.ok(userService.findUsers(criteria, pageable));
    }
    
    /**
     * Lấy thông tin user theo ID (admin only)
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        log.debug("Getting user by ID: {}", userId);
        
        return userService.findUserById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Refresh thông tin user từ Auth0 (admin only)
     */
    @PostMapping("/{userId}/refresh")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<UserDTO> refreshUserFromAuth0(@PathVariable String userId) {
        log.debug("Refreshing user data from Auth0 for user: {}", userId);
        
        UserDTO refreshedUser = userService.refreshUserFromAuth0(userId);
        return ResponseEntity.ok(refreshedUser);
    }
}
