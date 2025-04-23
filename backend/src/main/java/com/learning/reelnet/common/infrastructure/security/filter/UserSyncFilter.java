package com.learning.reelnet.common.infrastructure.security.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.learning.reelnet.modules.user.application.services.UserApplicationService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter that automatically synchronizes user information from Auth0
 * It runs after successful authentication and before controller methods.
 * Optimized for performance with cached user sync status.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
@RequiredArgsConstructor
@Slf4j
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserApplicationService userService;
    
    // Cache lưu thời điểm đồng bộ gần nhất của từng user
    private final ConcurrentHashMap<String, LocalDateTime> lastSyncTimeMap = new ConcurrentHashMap<>();
    
    // Thời gian tối thiểu giữa các lần đồng bộ (mặc định: 30 phút)
    private final Duration MIN_SYNC_INTERVAL = Duration.ofMinutes(30);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // Log thông tin Authorization header để debug
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                log.debug("Authorization header: {}", maskToken(authHeader));
            } else {
                log.debug("No Authorization header found");
            }
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                Jwt jwt = jwtAuth.getToken();
                String userId = jwt.getSubject();
                
                // Log thông tin JWT để debug
                log.debug("JWT Subject: {}", jwt.getSubject());
                log.debug("JWT Issuer: {}", jwt.getIssuer());
                log.debug("JWT Expiration: {}", jwt.getExpiresAt());
                
                // Chỉ đồng bộ cho API requests và khi cần thiết
                if (isApiRequest(request) && shouldSyncUser(userId)) {
                    log.debug("Synchronizing user from JWT for: {}", userId);
                    userService.syncUserFromJwt(jwt);
                    
                    // Cập nhật thời gian đồng bộ gần nhất
                    lastSyncTimeMap.put(userId, LocalDateTime.now());
                }
            } else if (authentication != null) {
                log.debug("Authentication is not JwtAuthenticationToken: {}", authentication.getClass().getName());
            } else {
                log.debug("No authentication found in SecurityContext");
            }
        } catch (Exception e) {
            log.error("Error synchronizing user from JWT", e);
            // Tiếp tục filter chain ngay cả khi đồng bộ thất bại
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * Ẩn thông tin nhạy cảm trong token
     */
    private String maskToken(String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (token.length() > 10) {
                return "Bearer " + token.substring(0, 5) + "..." + token.substring(token.length() - 5);
            }
        }
        return authHeader;
    }
    
    /**
     * Kiểm tra xem có nên đồng bộ user không dựa trên thời gian đồng bộ gần nhất
     */
    private boolean shouldSyncUser(String userId) {
        // Nếu chưa từng đồng bộ, cần đồng bộ ngay
        if (!lastSyncTimeMap.containsKey(userId)) {
            return true;
        }
        
        // Tính thời gian từ lần đồng bộ gần nhất
        LocalDateTime lastSync = lastSyncTimeMap.get(userId);
        Duration timeSinceLastSync = Duration.between(lastSync, LocalDateTime.now());
        
        // Chỉ đồng bộ nếu đã qua khoảng thời gian MIN_SYNC_INTERVAL
        return timeSinceLastSync.compareTo(MIN_SYNC_INTERVAL) > 0;
    }
    
    /**
     * Kiểm tra xem request có phải API request hay không (loại trừ tài nguyên tĩnh)
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Bỏ qua đồng bộ cho static resources, Swagger UI, etc.
        return !path.contains("/v3/api-docs") && 
               !path.contains("/swagger-ui") && 
               !path.contains("/actuator/") &&
               !isStaticResource(path);
    }
    
    /**
     * Kiểm tra xem đường dẫn có phải tài nguyên tĩnh hay không
     */
    private boolean isStaticResource(String path) {
        return path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".html") ||
               path.endsWith(".ico") ||
               path.endsWith(".png") ||
               path.endsWith(".jpg") ||
               path.endsWith(".jpeg") ||
               path.endsWith(".svg") ||
               path.endsWith(".woff") ||
               path.endsWith(".woff2") ||
               path.endsWith(".ttf");
    }
} 