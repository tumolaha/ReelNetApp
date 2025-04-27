package com.learning.reelnet.modules.user.application.services;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.model.UserPermission;
import com.learning.reelnet.modules.user.domain.repository.UserPermissionRepository;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for synchronizing user data from Auth0 to local database
 * Enhanced version with better client credentials token support
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceEnhanced {

    private final UserRepository userRepository;
    private final UserPermissionRepository userPermissionRepository;

    /**
     * Synchronizes user data from Auth0 JWT token to local database
     * Creates or updates user based on Auth0 ID
     *
     * @param jwt the JWT token containing user information
     * @return the synchronized user
     */
    @Transactional
    public User syncUser(Jwt jwt) {
        try {
            log.info("UserSyncService: Begin syncUser - JWT Claims: {}", jwt.getClaims().keySet());
            
            String auth0Id = jwt.getSubject();
            
            // Kiểm tra loại token
            boolean isClientCredential = isClientCredentialsToken(jwt);
            log.info("UserSyncService: Token type: {}, isClientCredential: {}", 
                    jwt.getClaimAsString("gty"), isClientCredential);
            
            // Trích xuất thông tin từ token với các giá trị mặc định phù hợp
            String email = getEmailFromToken(jwt, isClientCredential, auth0Id);
            String name = getNameFromToken(jwt, isClientCredential, auth0Id);
            String picture = getPictureFromToken(jwt, isClientCredential);
            String locale = getLocaleFromToken(jwt, isClientCredential);

            log.info("UserSyncService: Processing user with Auth0 ID: {}, Email: {}", auth0Id, email);

            // Kiểm tra người dùng đã tồn tại chưa
            boolean userExists = userRepository.existsByAuth0Id(auth0Id);
            log.info("UserSyncService: User exists in database: {}", userExists);

            User user = userRepository.findByAuth0Id(auth0Id)
                    .orElseGet(() -> {
                        log.info("UserSyncService: Creating new user with Auth0 ID: {}", auth0Id);
                        return User.builder()
                                .auth0Id(auth0Id)
                                .email(email)
                                .displayName(name)
                                .pictureUrl(picture)
                                .locale(locale)
                                .isActive(true)
                                .isCreator(isClientCredential) // Client thường là content creator
                                .settings("{}") // JSON object rỗng hợp lệ
                                .lastLogin(LocalDateTime.now())
                                .build();
                    });

            // Cập nhật thông tin cho người dùng hiện có
            user.setEmail(email);
            user.setDisplayName(name);
            user.setPictureUrl(picture);
            user.setLocale(locale);
            user.setLastLogin(LocalDateTime.now());
            
            // Đảm bảo settings không null
            if (user.getSettings() == null) {
                user.setSettings("{}");
            }
            
            // Xác định vai trò dựa trên loại token
            String role = getRoleFromToken(jwt, isClientCredential);
            user.setRole(role);

            log.info("UserSyncService: Synchronizing user data for Auth0 ID: {}, Email: {}, Name: {}, Role: {}", 
                    auth0Id, email, name, role);
            User savedUser = userRepository.save(user);
            log.info("UserSyncService: User saved with ID: {}, Auth0ID: {}", savedUser.getId(), savedUser.getAuth0Id());

            // Đồng bộ quyền từ token
            syncUserPermissionsFromToken(savedUser, jwt, isClientCredential);

            return savedUser;
        } catch (Exception e) {
            log.error("UserSyncService: Error synchronizing user: {}", e.getMessage(), e);
            throw e; // Rethrow the exception to be handled by the caller
        }
    }

    /**
     * Kiểm tra xem token có phải là client credentials không
     */
    private boolean isClientCredentialsToken(Jwt jwt) {
        String grantType = jwt.getClaimAsString("gty");
        String auth0Id = jwt.getSubject();
        return "client-credentials".equals(grantType) || (auth0Id != null && auth0Id.endsWith("@clients"));
    }

    /**
     * Lấy email từ token, với giá trị mặc định phù hợp
     */
    private String getEmailFromToken(Jwt jwt, boolean isClientCredential, String auth0Id) {
        String email = jwt.getClaimAsString("email");
        
        if (email == null || email.isEmpty()) {
            if (isClientCredential) {
                // Email cho client credentials với định dạng đặc biệt
                String clientId = auth0Id.replace("@clients", "");
                return clientId + "@api.reelnet.app";
            } else {
                // Email cho người dùng thông thường
                return auth0Id + "@user.reelnet.app";
            }
        }
        
        return email;
    }

    /**
     * Lấy tên hiển thị từ token, với giá trị mặc định phù hợp
     */
    private String getNameFromToken(Jwt jwt, boolean isClientCredential, String auth0Id) {
        String name = jwt.getClaimAsString("name");
        
        if (name == null || name.isEmpty()) {
            if (isClientCredential) {
                // Tên hiển thị cho client API
                String clientId = auth0Id.replace("@clients", "");
                return "API Client: " + clientId;
            } else {
                // Tên hiển thị cho người dùng thông thường
                return "User: " + auth0Id;
            }
        }
        
        return name;
    }

    /**
     * Lấy URL ảnh đại diện từ token, với giá trị mặc định phù hợp
     */
    private String getPictureFromToken(Jwt jwt, boolean isClientCredential) {
        String picture = jwt.getClaimAsString("picture");
        
        if (picture == null || picture.isEmpty()) {
            if (isClientCredential) {
                // URL ảnh đại diện cho client API
                return "https://cdn.auth0.com/client-avatars/default-client-avatar.png";
            } else {
                // URL ảnh đại diện cho người dùng thông thường
                return "https://cdn.auth0.com/avatars/default-user-avatar.png";
            }
        }
        
        return picture;
    }

    /**
     * Lấy locale từ token, với giá trị mặc định phù hợp
     */
    private String getLocaleFromToken(Jwt jwt, boolean isClientCredential) {
        String locale = jwt.getClaimAsString("locale");
        
        if (locale == null || locale.isEmpty()) {
            return "en-US"; // Giá trị mặc định
        }
        
        return locale;
    }

    /**
     * Lấy vai trò từ token, với xử lý đặc biệt cho client credentials
     */
    @SuppressWarnings("unchecked")
    private String getRoleFromToken(Jwt jwt, boolean isClientCredential) {
        // Client credentials luôn có vai trò SERVICE
        if (isClientCredential) {
            log.info("UserSyncService: Assigned SERVICE role to client credentials token");
            return "SERVICE";
        }
        
        // Xử lý token người dùng thông thường
        Object rolesObj = jwt.getClaim("roles");
        log.info("UserSyncService: Roles claim in token: {}", rolesObj);
        
        if (rolesObj == null) {
            return "USER"; // Vai trò mặc định
        }
        
        if (rolesObj instanceof Collection) {
            Collection<String> roles = (Collection<String>) rolesObj;
            if (!roles.isEmpty()) {
                // Ưu tiên vai trò ADMIN nếu có
                if (roles.contains("ADMIN")) {
                    return "ADMIN";
                }
                // Nếu không có ADMIN, lấy vai trò đầu tiên
                return roles.iterator().next();
            }
        } else if (rolesObj instanceof String) {
            String roles = (String) rolesObj;
            if (!roles.isEmpty()) {
                String[] roleArray = roles.split(",");
                if (roleArray.length > 0) {
                    return roleArray[0].trim();
                }
            }
        }
        
        return "USER"; // Vai trò mặc định
    }

    /**
     * Đồng bộ quyền người dùng từ token JWT
     */
    private void syncUserPermissionsFromToken(User user, Jwt jwt, boolean isClientCredential) {
        // Lấy quyền hiện tại
        Set<UserPermission> currentPermissions = userPermissionRepository.findByUserId(user.getId());
        Set<String> currentPermissionNames = currentPermissions.stream()
                .map(UserPermission::getPermission)
                .collect(Collectors.toSet());
        
        log.info("UserSyncService: Current user permissions: {}", currentPermissionNames);
        
        // Lấy quyền mới từ token
        Set<String> newPermissions = getPermissionsFromToken(jwt, isClientCredential);
        log.info("UserSyncService: New permissions from token: {}", newPermissions);
        
        // Thêm quyền mới
        for (String permission : newPermissions) {
            if (!currentPermissionNames.contains(permission)) {
                UserPermission userPermission = new UserPermission();
                userPermission.setUser(user);
                userPermission.setPermission(permission);
                userPermissionRepository.save(userPermission);
                log.info("UserSyncService: Added permission '{}' to user {}", permission, user.getAuth0Id());
            }
        }
        
        // Xóa quyền không còn trong token
        for (UserPermission permission : currentPermissions) {
            if (!newPermissions.contains(permission.getPermission())) {
                userPermissionRepository.delete(permission);
                log.info("UserSyncService: Removed permission '{}' from user {}", 
                        permission.getPermission(), user.getAuth0Id());
            }
        }
        
        log.info("UserSyncService: User permission sync completed for user: {}, Total permissions: {}", 
                user.getAuth0Id(), newPermissions.size());
    }

    /**
     * Lấy quyền từ token JWT, xử lý khác nhau cho client và người dùng
     */
    @SuppressWarnings("unchecked")
    private Set<String> getPermissionsFromToken(Jwt jwt, boolean isClientCredential) {
        Set<String> permissions = new HashSet<>();
        
        if (isClientCredential) {
            // Client credentials: quyền từ scope
            String scope = jwt.getClaimAsString("scope");
            if (scope != null && !scope.isEmpty()) {
                // Phân tích scope thành các quyền riêng lẻ
                String[] scopes = scope.split(" ");
                for (String s : scopes) {
                    permissions.add(s.trim());
                }
            }
            
            // Thêm quyền đặc biệt cho client
            permissions.add("api:access");
            permissions.add("client:authenticated");
            
            log.info("UserSyncService: Extracted {} permissions from client scope", permissions.size());
        } else {
            // Token người dùng: quyền từ permissions claim
            Object permissionsObj = jwt.getClaim("permissions");
            
            if (permissionsObj instanceof Collection) {
                permissions.addAll((Collection<String>) permissionsObj);
            } else if (permissionsObj instanceof String) {
                String permissionsStr = (String) permissionsObj;
                if (!permissionsStr.isEmpty()) {
                    String[] permissionArray = permissionsStr.split(",");
                    for (String permission : permissionArray) {
                        permissions.add(permission.trim());
                    }
                }
            }
            
            // Thêm quyền cơ bản cho người dùng
            permissions.add("user:authenticated");
            
            log.info("UserSyncService: Extracted {} permissions from user token", permissions.size());
        }
        
        return permissions;
    }
}
