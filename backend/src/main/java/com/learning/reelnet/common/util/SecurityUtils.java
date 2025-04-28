package com.learning.reelnet.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.exception.UnauthorizedAccessException;
import com.learning.reelnet.modules.user.api.dto.ReelNetUserDetails;
import com.learning.reelnet.modules.user.domain.model.User;

@Component
public class SecurityUtils {

    /**
     * Lấy thông tin người dùng hiện tại từ SecurityContext
     */
    public static ReelNetUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User not authenticated");
        }
        
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            Object principal = jwtAuth.getPrincipal();
            
            if (principal instanceof Jwt) {
                // Lấy JWT token và trích xuất thông tin
                Jwt jwt = (Jwt) principal;
                return new ReelNetUserDetails(extractBasicUserInfo(jwt));
            } 
            
            if (jwtAuth.getDetails() instanceof ReelNetUserDetails) {
                return (ReelNetUserDetails) jwtAuth.getDetails();
            }
        }
        
        throw new UnauthorizedAccessException("Unable to extract user details");
    }
    
    /**
     * Lấy userId của người dùng hiện tại
     */
    public static String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedAccessException("User not authenticated");
        }
        
        return authentication.getName();
    }
    
    /**
     * Trích xuất thông tin cơ bản từ JWT
     */
    private static User extractBasicUserInfo(Jwt jwt) {
        User user = new User();
        user.setId(jwt.getSubject());
        user.setEmail(jwt.getClaimAsString("email"));
        user.setName(jwt.getClaimAsString("name"));
        user.setPicture(jwt.getClaimAsString("picture"));
        return user;
    }
}
