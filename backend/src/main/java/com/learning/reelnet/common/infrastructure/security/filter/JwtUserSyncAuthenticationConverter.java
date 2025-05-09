package com.learning.reelnet.common.infrastructure.security.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.learning.reelnet.common.infrastructure.security.exception.UserSynchronizationException;
import com.learning.reelnet.modules.user.api.dto.ReelNetUserDetails;
import com.learning.reelnet.modules.user.application.services.UserSynchronizationService;
import com.learning.reelnet.modules.user.domain.model.User;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;

/**
 * Converts JWT to Authentication and synchronizes user information with Auth0
 */
@Component
@Slf4j
public class JwtUserSyncAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    
    private final UserSynchronizationService userSyncService;
    private final MeterRegistry meterRegistry;
    
    private final Timer syncTimer;
    private final Counter syncSuccessCounter;
    private final Counter syncFailureCounter;
    private final Counter fallbackCounter;
    
    /**
     * Constructor with metrics
     */
    public JwtUserSyncAuthenticationConverter(UserSynchronizationService userSyncService, MeterRegistry meterRegistry) {
        this.userSyncService = userSyncService;
        this.meterRegistry = meterRegistry;
        
        // Initialize metrics
        this.syncTimer = Timer.builder("auth.user_sync.time")
                .description("User synchronization time")
                .register(meterRegistry);
        
        this.syncSuccessCounter = Counter.builder("auth.user_sync.success")
                .description("Number of successful user synchronizations")
                .register(meterRegistry);
        
        this.syncFailureCounter = Counter.builder("auth.user_sync.failure")
                .description("Number of failed user synchronizations")
                .register(meterRegistry);
        
        this.fallbackCounter = Counter.builder("auth.user_sync.fallback")
                .description("Number of authentications using fallback")
                .register(meterRegistry);
    }
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String userId = jwt.getSubject();
        log.debug("Converting JWT to Authentication for user: {}", userId);
        
        User user = null;
        Collection<GrantedAuthority> authorities;
        ReelNetUserDetails userDetails = null;
        
        try {
            // Measure synchronization time
            user = syncTimer.record(() -> userSyncService.ensureUserIsSynchronized(userId, jwt));
            
            if (user != null) {
                // Get authorities from database user
                authorities = getAuthoritiesFromUser(user);
                userDetails = new ReelNetUserDetails(user);
                syncSuccessCounter.increment();
                
                log.debug("User synchronized successfully: {}", userId);
            } else {
                // Fallback: get authorities from JWT if synchronization fails
                authorities = extractAuthoritiesFromJwt(jwt);
                fallbackCounter.increment();
                
                log.warn("User sync returned null, using fallback from JWT for: {}", userId);
            }
        } catch (Exception e) {
            // Handle synchronization error
            log.error("Error synchronizing user {}: {}", userId, e.getMessage(), e);
            syncFailureCounter.increment();
            
            // Fallback to JWT permissions
            authorities = extractAuthoritiesFromJwt(jwt);
            
            // Log specific error
            if (e instanceof UserSynchronizationException) {
                log.warn("User synchronization failed: {}", e.getMessage());
            } else {
                log.error("Unexpected error during user synchronization", e);
            }
        }
        
        // Create authentication token with user information
        JwtAuthenticationToken authToken = new JwtAuthenticationToken(jwt, authorities, userId);
        
        // Attach user details to token
        if (userDetails != null) {
            authToken.setDetails(userDetails);
        }
        
        return authToken;
    }
    
    /**
     * Get authorities from user entity
     */
    private Collection<GrantedAuthority> getAuthoritiesFromUser(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Add roles
        if (user.getRoles() != null) {
            for (UUID role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        
        // Add permissions
        if (user.getPermissions() != null) {
            for (UUID permission : user.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission));
            }
        }
        
        return authorities;
    }
    
    /**
     * Extract authorities from JWT in fallback scenario
     */
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // Get permissions from claim
        Collection<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions == null) {
            return Collections.emptyList();
        }
        
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Get roles from Auth0
        String namespace = "https://reelnet.com/";
        Collection<String> roles = jwt.getClaimAsStringList(namespace + "roles");
        if (roles != null) {
            authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toList()));
        }
        
        // Get permissions from Auth0
        authorities.addAll(permissions.stream()
            .map(permission -> new SimpleGrantedAuthority("PERMISSION_" + permission.toUpperCase()))
            .collect(Collectors.toList()));
            
        return authorities;
    }
}