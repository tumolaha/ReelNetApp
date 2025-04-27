package com.learning.reelnet.common.infrastructure.security.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Converter that extracts roles and permissions from Auth0 JWT tokens 
 * and converts them to Spring Security authorities.
 */
@Component
@Slf4j
public class Auth0JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
    
    private static final String ROLES_CLAIM = "roles";
    private static final String PERMISSIONS_CLAIM = "permissions";
    private static final String ROLE_PREFIX = "ROLE_";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractAuthorities(jwt);
        log.debug("Extracted {} authorities from JWT token", authorities.size());
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(defaultConverter.convert(jwt));
        
        // Extract roles
        authorities.addAll(extractRoles(jwt));
        
        // Extract permissions
        authorities.addAll(extractPermissions(jwt));
        
        return authorities;
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Object roles = jwt.getClaim(ROLES_CLAIM);
        
        if (roles == null) {
            return Collections.emptyList();
        }
        
        Collection<String> rolesList;
        
        if (roles instanceof Collection) {
            rolesList = (Collection<String>) roles;
        } else if (roles instanceof String) {
            String rolesString = (String) roles;
            rolesList = List.of(rolesString.split(","));
        } else if (roles instanceof Map) {
            Map<String, Object> rolesMap = (Map<String, Object>) roles;
            rolesList = rolesMap.keySet();
        } else {
            log.warn("Unexpected roles claim format: {}", roles.getClass());
            return Collections.emptyList();
        }
        
        return rolesList.stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private Collection<GrantedAuthority> extractPermissions(Jwt jwt) {
        Object permissions = jwt.getClaim(PERMISSIONS_CLAIM);
        
        if (permissions == null) {
            return Collections.emptyList();
        }
        
        Collection<String> permissionsList;
        
        if (permissions instanceof Collection) {
            permissionsList = (Collection<String>) permissions;
        } else if (permissions instanceof String) {
            String permissionsString = (String) permissions;
            permissionsList = List.of(permissionsString.split(","));
        } else if (permissions instanceof Map) {
            Map<String, Object> permissionsMap = (Map<String, Object>) permissions;
            permissionsList = permissionsMap.keySet();
        } else {
            log.warn("Unexpected permissions claim format: {}", permissions.getClass());
            return Collections.emptyList();
        }
        
        return permissionsList.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
