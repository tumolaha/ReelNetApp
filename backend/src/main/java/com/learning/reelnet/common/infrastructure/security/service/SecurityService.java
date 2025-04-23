package com.learning.reelnet.common.infrastructure.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.learning.reelnet.common.exception.ForbiddenException;
import com.learning.reelnet.common.exception.UnauthorizedAccessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getSubject();
        }
        throw new UnauthorizedAccessException("User not authenticated");
    }

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.getAuthorities().stream()
                   .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public void validateResourceAccess(String resourceOwnerId) {
        String currentUserId = getCurrentUserId();
        if (!currentUserId.equals(resourceOwnerId) && !hasRole("ADMIN")) {
            throw new ForbiddenException("Access denied to this resource");
        }
    }
}
