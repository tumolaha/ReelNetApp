package com.learning.reelnet.common.infrastructure.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;

/**
 * AOP aspect to handle @RateLimit annotations on controller methods.
 * Extracts the appropriate key based on the annotation settings and 
 * applies rate limiting before allowing the method to execute.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    
    private final RateLimitService rateLimitService;
    
    /**
     * Around advice for methods annotated with @RateLimit.
     * Intercepts the call, applies rate limiting, and either allows
     * the call to proceed or throws an exception.
     */
    @Around("@annotation(com.learning.reelnet.common.infrastructure.ratelimit.RateLimit)")
    public Object rateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
        String limitType = rateLimitAnnotation.type();
        RateLimit.KeySource keySource = rateLimitAnnotation.keySource();
        
        String key = generateKey(keySource);
        
        if (rateLimitService.allowRequest(key, limitType)) {
            return joinPoint.proceed();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS, 
                    "Rate limit exceeded. Please try again later."
            );
        }
    }
    
    /**
     * Generate a key for rate limiting based on the key source.
     * 
     * @param keySource The source to use for the key
     * @return The generated key
     */
    private String generateKey(RateLimit.KeySource keySource) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        
        HttpServletRequest request = attributes.getRequest();
        String ip = getClientIp(request);
        
        switch (keySource) {
            case IP:
                return ip;
            case PRINCIPAL:
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                return (auth != null && auth.getName() != null) ? auth.getName() : "anonymous";
            case COMBINED:
                Authentication combinedAuth = SecurityContextHolder.getContext().getAuthentication();
                String principal = (combinedAuth != null && combinedAuth.getName() != null) 
                        ? combinedAuth.getName() : "anonymous";
                return ip + ":" + principal;
            default:
                return ip;
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
} 