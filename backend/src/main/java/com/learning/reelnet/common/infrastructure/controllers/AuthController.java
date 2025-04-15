package com.learning.reelnet.common.infrastructure.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reelnet.common.infrastructure.security.service.Auth0UserService;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for authentication-related endpoints.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final Auth0UserService auth0UserService;

    /**
     * Gets the current user's profile information.
     *
     * @param jwt the JWT token of the authenticated user
     * @return the user profile information
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> userInfo = new HashMap<>();
        
        if (jwt != null) {
            userInfo.put("userId", auth0UserService.getCurrentUserId().orElse(null));
            userInfo.put("email", auth0UserService.getCurrentUserEmail().orElse(null));
            userInfo.put("name", auth0UserService.getCurrentUserName().orElse(null));
            userInfo.put("picture", auth0UserService.getCurrentUserPicture().orElse(null));
            userInfo.put("nickname", auth0UserService.getCurrentUserNickname().orElse(null));
            userInfo.put("permissions", auth0UserService.getCurrentUserPermissions());
            userInfo.put("isAuthenticated", true);
        } else {
            userInfo.put("isAuthenticated", false);
        }
        
        return ResponseEntity.ok(userInfo);
    }

    /**
     * Public endpoint that doesn't require authentication.
     *
     * @return a simple message
     */
    @GetMapping("/public")
    public ResponseEntity<Map<String, String>> publicEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint that doesn't require authentication");
        return ResponseEntity.ok(response);
    }

    /**
     * Protected endpoint that requires authentication.
     *
     * @return a simple message
     */
    @GetMapping("/protected")
    public ResponseEntity<Map<String, String>> protectedEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a protected endpoint that requires authentication");
        return ResponseEntity.ok(response);
    }
} 