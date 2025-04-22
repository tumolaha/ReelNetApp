package com.learning.reelnet.common.infrastructure.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter to apply rate limiting to all API requests.
 * This filter extracts the client IP and applies rate limits
 * based on the request path.
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;
    private final RateLimitProperties properties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        // Skip rate limiting if disabled
        if (!properties.isEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract client IP address
        String clientIp = getClientIp(request);
        
        // Determine rate limit type based on request path
        String limitType = determineLimitType(request.getRequestURI());
        
        // Check if the request is allowed
        boolean allowed = rateLimitService.allowRequest(clientIp, limitType);
        
        if (allowed) {
            // Add rate limit headers to response
            long remainingTokens = rateLimitService.getRemainingTokens(clientIp, limitType);
            response.addHeader("X-RateLimit-Remaining", String.valueOf(remainingTokens));
            
            filterChain.doFilter(request, response);
        } else {
            // Reject the request with 429 Too Many Requests
            log.warn("Rate limit exceeded for IP: {}, path: {}", clientIp, request.getRequestURI());
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Rate limit exceeded. Please try again later.\"}");
        }
    }
    
    /**
     * Extract the client IP address from the request, handling proxies.
     *
     * @param request The HTTP request
     * @return The client IP address
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Get the first IP in the chain, which is the original client
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    /**
     * Determine the rate limit type based on the request URI.
     * This can be customized based on your API structure.
     *
     * @param uri The request URI
     * @return The rate limit type to apply
     */
    private String determineLimitType(String uri) {
        if (uri.startsWith("/api/v1/auth")) {
            return "auth";
        } else if (uri.startsWith("/api")) {
            return "api";
        }
        return "default";
    }
} 