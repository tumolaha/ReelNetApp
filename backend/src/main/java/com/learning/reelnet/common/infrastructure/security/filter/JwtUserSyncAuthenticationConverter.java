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

import com.learning.reelnet.modules.user.api.dto.ReelNetUserDetails;
import com.learning.reelnet.modules.user.application.services.UserSynchronizationService;
import com.learning.reelnet.modules.user.domain.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUserSyncAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserSynchronizationService userSyncService;
    
    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 1. Lấy thông tin cơ bản từ JWT
        String userId = jwt.getSubject();
        log.info("Converting JWT to Authentication Token for user: {}", userId);

        try {
            // 2. Đồng bộ và lấy thông tin user
            User user = userSyncService.ensureUserIsSynchronized(userId, jwt);
            
            // 3. Tạo authorities từ roles và permissions của user
            Collection<GrantedAuthority> authorities = getAuthoritiesFromUser(user);
            
            // 4. Tạo đối tượng CustomUserDetails để chứa thông tin user
            ReelNetUserDetails userDetails = new ReelNetUserDetails(user);
            
            // 5. Tạo và trả về token xác thực
            JwtAuthenticationToken token = new JwtAuthenticationToken(jwt, authorities, userId);
            token.setDetails(userDetails);
            return token;
        } catch (Exception e) {
            log.error("Error synchronizing user from JWT: {}", e.getMessage(), e);
            
            // Nếu xảy ra lỗi, vẫn trả về token xác thực cơ bản với thông tin từ JWT
            Collection<GrantedAuthority> fallbackAuthorities = extractAuthoritiesFromJwt(jwt);
            return new JwtAuthenticationToken(jwt, fallbackAuthorities, userId);
        }
    }
    
    /**
     * Lấy authorities từ user entity
     */
    private Collection<GrantedAuthority> getAuthoritiesFromUser(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Thêm roles
        if (user.getRoles() != null) {
            for (UUID role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
        }
        
        // Thêm permissions
        if (user.getPermissions() != null) {
            for (UUID permission : user.getPermissions()) {
                authorities.add(new SimpleGrantedAuthority("PERMISSION_" + permission));
            }
        }
        
        return authorities;
    }
    
    /**
     * Trích xuất authorities từ JWT trong trường hợp fallback
     */
    private Collection<GrantedAuthority> extractAuthoritiesFromJwt(Jwt jwt) {
        // Lấy permissions từ claim
        Collection<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions == null) {
            return Collections.emptyList();
        }
        
        return permissions.stream()
            .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.toUpperCase()))
            .collect(Collectors.toList());
    }
}
