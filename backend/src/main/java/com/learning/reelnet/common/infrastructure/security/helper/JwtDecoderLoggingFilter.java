package com.learning.reelnet.common.infrastructure.security.helper;

import java.io.IOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Lọc để ghi nhật ký JWT chi tiết cho mục đích gỡ lỗi.
 * Ghi lại thông tin header, cấu trúc token và lỗi có thể xảy ra.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class JwtDecoderLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            // Ghi nhật ký chi tiết cho các yêu cầu API
            if (isApiRequest(request)) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    logTokenInfo(token);
                } else {
                    log.debug("JWT Debug: Authorization header missing or not in Bearer format");
                }
            }
        } catch (Exception e) {
            log.error("JWT Debug: Error analyzing JWT token", e);
        }
        
        // Tiếp tục chuỗi lọc
        filterChain.doFilter(request, response);
    }
    
    /**
     * Ghi nhật ký thông tin JWT để gỡ lỗi
     */
    private void logTokenInfo(String token) {
        try {
            // Kiểm tra định dạng token
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                log.error("JWT Debug: Token does not have 3 parts (header.payload.signature)");
                return;
            }
            
            log.debug("JWT Debug: Token structure is valid (3 parts)");
            log.debug("JWT Debug: Header length: {}", parts[0].length());
            log.debug("JWT Debug: Payload length: {}", parts[1].length());
            log.debug("JWT Debug: Signature length: {}", parts[2].length());
            
            // Không giải mã token để tránh ghi nhật ký thông tin nhạy cảm
            // Chỉ kiểm tra cấu trúc
            
        } catch (Exception e) {
            log.error("JWT Debug: Error parsing token", e);
        }
    }
    
    /**
     * Kiểm tra xem request có phải API request hay không
     */
    private boolean isApiRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // Loại trừ các tài nguyên tĩnh, Swagger UI, v.v.
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