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
 * Filter to synchronize user data from JWT tokens after successful
 * authentication.
 * This filter runs after the JWT token has been validated and authenticated.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 300) // Đặt sau BearerTokenAuthenticationFilter với order cao hơn
@RequiredArgsConstructor
@Slf4j
public class TokenSynchronizationFilter extends OncePerRequestFilter {

    private final UserSyncServiceEnhanced userSyncService;

    @Override    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Cho phép request đi qua filter chain trước để đảm bảo BearerTokenAuthenticationFilter đã xử lý
        filterChain.doFilter(request, response);
        
        // Sau đó mới lấy authentication đã được xác thực từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Kiểm tra nếu đã xác thực và principal là Jwt
        log.info("TokenSynchronizationFilter: Checking authentication in SecurityContext AFTER filter chain");
        
        if (authentication != null) {
            // Log chi tiết về authentication để debug
            log.info("TokenSynchronizationFilter: Authentication: isAuthenticated={}, principalType={}, authorities={}",
                authentication.isAuthenticated(),
                authentication.getPrincipal() != null ? authentication.getPrincipal().getClass().getName() : "null",
                authentication.getAuthorities());
            
            // Nếu có authentication và principal là JWT, thì đồng bộ người dùng
            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt jwt) {
                try {
                    log.info("TokenSynchronizationFilter: Synchronizing user data for JWT subject: {}", jwt.getSubject());
                    userSyncService.syncUser(jwt);
                    log.info("TokenSynchronizationFilter: Successfully synchronized user data for JWT subject: {}", 
                            jwt.getSubject());
                } catch (Exception e) {
                    log.error("TokenSynchronizationFilter: Error synchronizing user data: {}", e.getMessage(), e);
                    // Không throw exception để không ảnh hưởng đến request
                }
            } else {
                log.info("TokenSynchronizationFilter: Authentication present but not a JWT token");
            }        } else {
            log.info("TokenSynchronizationFilter: No authentication found in SecurityContext after filter chain");
        }
    }@Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Log trước khi quyết định bỏ qua filter hay không
        log.info("TokenSynchronizationFilter: Checking if should filter request: {}", request.getServletPath());
        
        // Bỏ qua các request static hoặc không yêu cầu xác thực
        String path = request.getServletPath();
        
        // Không cần đồng bộ người dùng cho các endpoint công khai và tài nguyên tĩnh
        boolean isPublicEndpoint = path.startsWith("/actuator") || 
                path.startsWith("/swagger-ui") || 
                path.startsWith("/api-docs") || 
                path.equals("/") ||
                path.startsWith("/api/public") ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".html") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".svg") ||
                path.endsWith(".ico");
        
        // Sửa lỗi: Không nên kiểm tra header Authorization ở đây vì có thể bỏ sót những request có token
        // Chúng ta sẽ để filter luôn được gọi cho các request không phải endpoint công khai
        boolean shouldSkip = isPublicEndpoint;
        
        log.info("TokenSynchronizationFilter: Should skip filtering: {} for path: {}", shouldSkip, path);
        return shouldSkip;
    }
}
