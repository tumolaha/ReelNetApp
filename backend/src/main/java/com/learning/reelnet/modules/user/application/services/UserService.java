package com.learning.reelnet.modules.user.application.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.modules.user.api.dto.Auth0UserDTO;
import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.api.dto.UserSearchCriteria;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final Auth0ManagementClient auth0Client;
    private final UserSynchronizationService syncService;
    
    /**
     * Tìm user theo ID (cached)
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> findUserById(String userId) {
        return userRepository.findById(userId)
            .map(this::mapToUserDTO);
    }
    
    /**
     * Tìm kiếm users với phân trang, hiệu suất cao
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findUsers(UserSearchCriteria criteria, Pageable pageable) {
        Page<User> usersPage = userRepository.findByCriteria(criteria, pageable);
        return usersPage.map(this::mapToUserDTO);
    }

    /**
     * Tìm users theo email (partial match) với phân trang
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findUsersByEmailContaining(String emailPart, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCase(emailPart, pageable)
            .map(this::mapToUserDTO);
    }
    
    /**
     * Kích hoạt cập nhật thông tin user từ Auth0
     */
    @Transactional
    public UserDTO refreshUserFromAuth0(String userId) {
        // 1. Xóa cache
        syncService.invalidateSyncCache(userId);
        
        // 2. Cập nhật từ Auth0
        try {
            auth0Client.invalidateUserCache(userId);
            
            // 3. Cập nhật dữ liệu local từ Auth0
            User updatedUser = userRepository.findById(userId)
                .map(user -> {
                    Auth0UserDTO auth0User = auth0Client.getUserInfo(userId);
                    List<Map<String, Object>> roles = auth0Client.getUserRoles(userId);
                    List<Map<String, Object>> permissions = auth0Client.getUserPermissions(userId);
                    
                    return syncService.updateUserWithAuth0Data(
                        user, auth0User, roles, permissions, null);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                
            return mapToUserDTO(updatedUser);
        } catch (Exception e) {
            log.error("Error refreshing user data from Auth0: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to refresh user data", e);
        }
    }
    
    /**
     * Chuyển đổi từ User entity sang UserDTO
     */
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .picture(user.getPicture())
            .lastLogin(user.getLastLogin())
            .emailVerified(user.isEmailVerified())
            .active(user.isActive())
            .roles(user.getRoles())
            .permissions(user.getPermissions())
            .userMetadata(user.getUserMetadata())
            .appMetadata(user.getAppMetadata())
            .lastSyncTimestamp(user.getLastSyncTimestamp())
            .tenantId(user.getTenantId())
            .build();
    }
}