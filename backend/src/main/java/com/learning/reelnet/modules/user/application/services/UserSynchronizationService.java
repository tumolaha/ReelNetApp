package com.learning.reelnet.modules.user.application.services;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.learning.reelnet.modules.user.api.dto.Auth0UserDTO;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.RoleRepository;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;
import com.learning.reelnet.modules.user.domain.model.Role;
import com.learning.reelnet.common.infrastructure.security.exception.UserSynchronizationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Auth0ManagementClient auth0Client;
    
    /**
     * Đồng bộ thông tin người dùng từ Auth0 JWT
     */
    @Transactional
    @Cacheable(value = "userSyncCache", key = "#userId + '-' + #jwt.getClaim('iat')", 
           unless = "#result == null", cacheManager = "cacheManager")
    public User ensureUserIsSynchronized(String userId, Jwt jwt) {
        log.debug("Synchronizing user: {}", userId);
        
        // 1. Kiểm tra trong DB
        User existingUser = userRepository.findById(userId).orElse(null);
        
        // 2. Kiểm tra nếu cần cập nhật
        if (existingUser != null) {
            Long tokenIat = jwt.getClaim("iat");
            if (!needsUpdate(existingUser, tokenIat)) {
                log.debug("User {} does not require update (token iat: {}, last sync: {})", 
                    userId, tokenIat, existingUser.getLastSyncTimestamp());
                return existingUser;
            }
        }
        
        try {
            // 3. Lấy thông tin từ Auth0 API
            Auth0UserDTO auth0User = auth0Client.getUserInfo(userId);
            List<Map<String, Object>> auth0Roles = auth0Client.getUserRoles(userId);
            List<Map<String, Object>> auth0Permissions = auth0Client.getUserPermissions(userId);
            
            // 4. Tạo mới hoặc cập nhật user từ dữ liệu Auth0
            User user = existingUser != null ? existingUser : new User();
            
            return updateUserWithAuth0Data(user, auth0User, auth0Roles, auth0Permissions, jwt);
            
        } catch (Exception e) {
            log.error("Error synchronizing user {}: {}", userId, e.getMessage(), e);
            if (existingUser != null) {
                return existingUser; // Return old user if sync fails
            }
            throw new UserSynchronizationException("Unable to synchronize user information from Auth0", e);
        }
    }
    
    /**
     * Kiểm tra xem user có cần cập nhật dựa trên token iat
     */
    private boolean needsUpdate(User user, Long tokenIat) {
        if (user.getLastSyncTimestamp() == null) {
            return true;
        }
        
        // Tính thời gian token được phát hành dưới dạng epoch seconds
        long lastSyncEpoch = user.getLastSyncTimestamp().getEpochSecond();
        
        // Token phát hành sau lần đồng bộ cuối -> Cần cập nhật
        return tokenIat > lastSyncEpoch;
    }
    
    /**
     * Cập nhật thông tin người dùng từ dữ liệu Auth0
     */
    @Transactional
    protected User updateUserWithAuth0Data(
            User user, 
            Auth0UserDTO auth0User, 
            List<Map<String, Object>> auth0Roles,
            List<Map<String, Object>> auth0Permissions,
            Jwt jwt) {
        
        // 1. Cập nhật thông tin cơ bản
        if (user.getId() == null) {
            user.setId(auth0User.getUser_id());
        }
        
        user.setEmail(auth0User.getEmail());
        user.setName(auth0User.getName());
        user.setPicture(auth0User.getPicture());
        user.setLastLogin(auth0User.getLastLogin());
        user.setEmailVerified(auth0User.isEmailVerified());
        user.setActive(!auth0User.isBlocked());
        
        // 2. Cập nhật metadata
        user.setUserMetadata(auth0User.getUserMetadata());
        user.setAppMetadata(auth0User.getAppMetadata());
        
        // 3. Cập nhật roles và permissions từ Auth0
        Set<UUID> roleIds = mapRolesFromAuth0(auth0Roles);
        Set<UUID> permissionIds = mapPermissionsFromAuth0(auth0Permissions);
        
        // 4. Ghi đè roles và permissions hiện có
        user.setRoles(roleIds);
        user.setPermissions(permissionIds);
        
        // 5. Cập nhật thời gian đồng bộ
        user.setLastSyncTimestamp(Instant.now());
        
        // 6. Xử lý dữ liệu tùy chỉnh từ token nếu có
        String namespace = "https://reelnet.com/";
        Map<String, Object> appData = jwt.getClaim(namespace + "app_data");
        if (appData != null) {
            user.setApplicationData(appData);
        }
        
        // 7. Xử lý tenant ID nếu có
        String tenantId = jwt.getClaimAsString(namespace + "tenant_id");
        if (tenantId != null) {
            user.setTenantId(tenantId);
        }
        
        // 8. Lưu vào database
        return userRepository.save(user);
    }
    
    /**
     * Chuyển đổi Auth0 roles sang UUID trong hệ thống
     */
    private Set<UUID> mapRolesFromAuth0(List<Map<String, Object>> auth0Roles) {
        if (auth0Roles == null || auth0Roles.isEmpty()) {
            return Collections.emptySet();
        }
        
        // Lấy tất cả roles từ database để map với Auth0 roles
        Map<String, Role> rolesByName = roleRepository.findAllByNameIn(
            auth0Roles.stream()
                .map(role -> (String)role.get("name"))
                .collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(Role::getName, role -> role));
        
        // Ánh xạ roles từ Auth0 sang local roles
        return auth0Roles.stream()
            .map(role -> {
                String roleName = (String)role.get("name");
                Role localRole = rolesByName.get(roleName);
                return localRole != null ? localRole.getId() : null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }
    
    /**
     * Chuyển đổi Auth0 permissions sang UUID trong hệ thống
     */
    private Set<UUID> mapPermissionsFromAuth0(List<Map<String, Object>> auth0Permissions) {
        // Tương tự như mapRolesFromAuth0, nhưng cho permissions
        // Code tương tự cho permissions
        if (auth0Permissions == null || auth0Permissions.isEmpty()) {
            return Collections.emptySet();
        }

        // Thực hiện tương tự như với roles
        return Collections.emptySet();
    }
    
    /**
     * Xóa cache khi cần làm mới thông tin người dùng
     */
    @CacheEvict(value = "userSyncCache", key = "#userId + '-*'", cacheManager = "cacheManager")
    public void invalidateSyncCache(String userId) {
        log.debug("Invalidating sync cache for user: {}", userId);
        auth0Client.invalidateUserCache(userId);
    }
}