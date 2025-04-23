package com.learning.reelnet.interfaces.rest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.application.services.UserApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    
    private final UserApplicationService userService;
    
    /**
     * GET /users/me : Get currently authenticated user
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        log.debug("REST request to get current user");
        String auth0Id = jwt.getSubject();
        return userService.getUserByAuth0Id(auth0Id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    UserDTO user = userService.syncUserFromJwt(jwt);
                    return ResponseEntity.ok(user);
                });
    }
    
    /**
     * PUT /users/me : Update current user's profile
     */
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserDTO userDTO) {
        
        log.debug("REST request to update current user: {}", userDTO);
        String auth0Id = jwt.getSubject();
        
        return userService.getUserByAuth0Id(auth0Id)
                .map(user -> {
                    UserDTO updated = userService.updateUser(user.getId(), userDTO);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * PUT /users/me/settings : Update current user's settings
     */
    @PutMapping("/me/settings")
    public ResponseEntity<UserDTO> updateUserSettings(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody Map<String, Object> settings) {
        
        log.debug("REST request to update user settings");
        String auth0Id = jwt.getSubject();
        
        return userService.getUserByAuth0Id(auth0Id)
                .map(user -> {
                    UserDTO updated = userService.updateUserSettings(user.getId(), settings);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    /**
     * GET /users : Get all users (admin only)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get all Users");
        Page<UserDTO> page = userService.getAllUsers(pageable);
        return ResponseEntity.ok(page);
    }
    
    /**
     * GET /users/search : Search users (admin only)
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserDTO>> searchUsers(
            @RequestParam String query,
            Pageable pageable) {
        
        log.debug("REST request to search Users for query: {}", query);
        Page<UserDTO> page = userService.searchUsers(query, pageable);
        return ResponseEntity.ok(page);
    }
    
    /**
     * GET /users/creators : Get all content creators
     */
    @GetMapping("/creators")
    public ResponseEntity<Page<UserDTO>> getCreators(Pageable pageable) {
        log.debug("REST request to get all Creators");
        Page<UserDTO> page = userService.getCreators(pageable);
        return ResponseEntity.ok(page);
    }
    
    /**
     * GET /users/active : Get recently active users
     */
    @GetMapping("/active")
    public ResponseEntity<List<UserDTO>> getRecentlyActiveUsers(
            @RequestParam(defaultValue = "10") int limit) {
        
        log.debug("REST request to get recently active users");
        List<UserDTO> users = userService.getRecentlyActiveUsers(limit);
        return ResponseEntity.ok(users);
    }
    
    /**
     * GET /users/{id} : Get a specific user
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable UUID id) {
        log.debug("REST request to get User : {}", id);
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * POST /users/{id}/creator : Toggle creator status (admin only)
     */
    @PostMapping("/{id}/creator")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> toggleCreatorStatus(
            @PathVariable UUID id,
            @RequestParam boolean isCreator) {
        
        log.debug("REST request to set creator status for User {} to {}", id, isCreator);
        UserDTO result = userService.toggleCreatorStatus(id, isCreator);
        return ResponseEntity.ok(result);
    }
} 