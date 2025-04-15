package com.learning.reelnet.common.infrastructure.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * Service for extracting user information from Auth0 JWTs.
 */
@Service
@Slf4j
public class Auth0UserService {

    /**
     * Gets the current authenticated user's Auth0 ID.
     *
     * @return the Auth0 user ID, or empty if not authenticated
     */
    public Optional<String> getCurrentUserId() {
        return getClaim("sub", String.class);
    }

    /**
     * Gets the current authenticated user's email.
     *
     * @return the user's email, or empty if not available
     */
    public Optional<String> getCurrentUserEmail() {
        return getClaim("email", String.class);
    }

    /**
     * Gets the current authenticated user's name.
     *
     * @return the user's name, or empty if not available
     */
    public Optional<String> getCurrentUserName() {
        return getClaim("name", String.class);
    }

    /**
     * Gets the current authenticated user's picture URL.
     *
     * @return the user's picture URL, or empty if not available
     */
    public Optional<String> getCurrentUserPicture() {
        return getClaim("picture", String.class);
    }

    /**
     * Gets the current authenticated user's nickname.
     *
     * @return the user's nickname, or empty if not available
     */
    public Optional<String> getCurrentUserNickname() {
        return getClaim("nickname", String.class);
    }

    /**
     * Gets a specific claim from the JWT token.
     *
     * @param <T> the type of the claim
     * @param name the name of the claim
     * @param type the class of the claim value
     * @return the claim value, or empty if not available
     */
    public <T> Optional<T> getClaim(String name, Class<T> type) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof Jwt)) {
                return Optional.empty();
            }

            Jwt jwt = (Jwt) principal;
            return Optional.ofNullable(jwt.getClaim(name));
        } catch (Exception e) {
            log.error("Error retrieving claim {}: {}", name, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Gets all permissions assigned to the current user from the JWT token.
     *
     * @return an array of permissions, or empty array if not available
     */
    public String[] getCurrentUserPermissions() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return new String[0];
            }

            Object principal = authentication.getPrincipal();
            if (!(principal instanceof Jwt)) {
                return new String[0];
            }

            Jwt jwt = (Jwt) principal;
            Map<String, Object> permissions = jwt.getClaim("permissions");
            
            if (permissions == null) {
                return new String[0];
            }
            
            return permissions.keySet().toArray(new String[0]);
        } catch (Exception e) {
            log.error("Error retrieving permissions: {}", e.getMessage());
            return new String[0];
        }
    }

    /**
     * Checks if the current user has a specific permission.
     *
     * @param permission the permission to check
     * @return true if the user has the permission, false otherwise
     */
    public boolean hasPermission(String permission) {
        String[] permissions = getCurrentUserPermissions();
        for (String p : permissions) {
            if (p.equals(permission)) {
                return true;
            }
        }
        return false;
    }
} 