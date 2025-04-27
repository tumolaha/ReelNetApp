package com.learning.reelnet.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.learning.reelnet.modules.user.application.services.UserSyncServiceEnhanced;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Configuration for JWT authentication handling.
 * Syncs user data from Auth0 to local database on successful authentication.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationConfig implements AuthenticationSuccessHandler {

    private final UserSyncServiceEnhanced userSyncService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // Check if the authentication principal is an instance of Jwt
        // and synchronize user data if it is
        log.debug("Authentication successful for user: {}", authentication.getName());
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            try {
                userSyncService.syncUser(jwt);
                log.debug("Successfully synchronized user data for JWT subject: {}", jwt.getSubject());
            } catch (Exception e) {
                log.error("Error synchronizing user data: {}", e.getMessage(), e);
            }
        }
    }
}
