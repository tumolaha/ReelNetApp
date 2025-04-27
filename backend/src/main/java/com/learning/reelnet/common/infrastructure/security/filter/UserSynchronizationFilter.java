package com.learning.reelnet.common.infrastructure.security.filter;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learning.reelnet.modules.user.application.services.UserSyncServiceEnhanced;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter đơn giản hơn để đồng bộ người dùng từ JWT token
 * Chạy cuối cùng trong chuỗi filter để đảm bảo token đã được xác thực
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE) // Chạy sau tất cả các filter khác
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationFilter extends OncePerRequestFilter {

    private final UserSyncServiceEnhanced userSyncService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Thực hiện chuỗi filter trước
            filterChain.doFilter(request, response);
            
            // Kiểm tra xác thực sau khi request đã được xử lý
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                
                // Log chi tiết để debug
                log.info("UserSynchronizationFilter: Authentication found, principal type: {}", 
                        principal != null ? principal.getClass().getName() : "null");
                
                // Kiểm tra nếu principal là JWT
                if (principal instanceof Jwt jwt) {
                    log.info("UserSynchronizationFilter: JWT found, subject: {}", jwt.getSubject());
                    try {
                        // Đồng bộ người dùng
                        userSyncService.syncUser(jwt);
                        log.info("UserSynchronizationFilter: User successfully synchronized with subject: {}", jwt.getSubject());
                    } catch (Exception e) {
                        log.error("UserSynchronizationFilter: Error synchronizing user: {}", e.getMessage(), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("UserSynchronizationFilter: Unexpected error: {}", e.getMessage(), e);
            throw e;
        }
    }
}
