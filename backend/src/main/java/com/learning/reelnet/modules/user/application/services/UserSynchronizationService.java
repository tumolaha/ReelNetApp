package com.learning.reelnet.modules.user.application.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.modules.user.api.dto.Auth0UserDTO;
import com.learning.reelnet.modules.user.domain.exception.UserNotFoundException;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSynchronizationService {

    private final UserRepository userRepository;
    private final Auth0ManagementClient auth0Client;

    @Value("${user.sync.minimum-interval-hours:24}")
    private int syncIntervalHours;

    @Value("${user.sync.create-if-not-exists:true}")
    private boolean createIfNotExists;

    /**
     * Đảm bảo user đã được đồng bộ từ Auth0
     */
    @Transactional
    public User ensureUserIsSynchronized(String userId, Jwt jwt) {
        Optional<User> optionalUser = userRepository.findById(userId);
        log.info("Ensuring user is synchronized: {}", optionalUser);
        if (optionalUser.isEmpty()) {
            if (createIfNotExists) {
                log.info("User not found in local database, creating new user from Auth0: {}", userId);
                return createUserFromAuth0(userId);
            } else {
                throw new UserNotFoundException("User not found in local database: " + userId);
            }
        }

        User user = optionalUser.get();
        if (shouldUpdateUser(user, jwt)) {
            return updateUserFromAuth0(user);
        }

        return updateLastLogin(user);
    }

    /**
     * Tạo user mới từ Auth0
     */
    @Transactional
    public User createUserFromAuth0(String userId) {
        Auth0UserDTO auth0User = auth0Client.getUserInfo(userId);
        User newUser = mapAuth0UserToLocalUser(auth0User);

        // Lấy roles và permissions từ Auth0
        enrichUserWithRolesAndPermissions(newUser);

        // Cập nhật thông tin đồng bộ
        newUser.setLastSyncedWithAuth0(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        newUser.setAuth0UpdatedAt(auth0User.getUpdated_at());
        newUser.setLastLogin(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        log.info("Created new local user from Auth0: {}", userId);
        return userRepository.save(newUser);
    }

    /**
     * Cập nhật user từ Auth0
     */
    @Transactional
    public User updateUserFromAuth0(User user) {
        Auth0UserDTO auth0User = auth0Client.getUserInfo(user.getId());

        // Cập nhật thông tin cơ bản
        user.setEmail(auth0User.getEmail());
        user.setName(auth0User.getName());
        user.setPicture(auth0User.getPicture());
        user.setNickname(auth0User.getNickname());
        user.setLocale(auth0User.getLocale());
        user.setEmailVerified(auth0User.isEmail_verified());
        user.setBlocked(auth0User.isBlocked());

        // Cập nhật metadata
        user.setUserMetadata(auth0User.getMetadataAsJson(auth0User.getUserMetadata()));
        user.setAppMetadata(auth0User.getMetadataAsJson(auth0User.getAppMetadata()));

        // Cập nhật roles và permissions (tùy vào yêu cầu)
        enrichUserWithRolesAndPermissions(user);

        // Cập nhật thông tin đồng bộ
        user.setLastSyncedWithAuth0(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        user.setAuth0UpdatedAt(auth0User.getUpdated_at());

        log.info("Updated local user from Auth0: {}", user.getId());
        return userRepository.save(user);
    }

    /**
     * Cập nhật thời gian đăng nhập cuối cùng
     */
    @Transactional
    public User updateLastLogin(User user) {
        user.setLastLogin(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        return userRepository.save(user);
    }

    /**
     * Kiểm tra xem có cần cập nhật thông tin user không
     */
    private boolean shouldUpdateUser(User user, Jwt jwt) {
        // Chưa từng đồng bộ
        if (user.getLastSyncedWithAuth0() == null) {
            return true;
        }

        // Đã quá thời gian định kỳ cần đồng bộ
        LocalDateTime lastSync = user.getLastSyncedWithAuth0();
        if (ChronoUnit.HOURS.between(lastSync, LocalDateTime.now()) >= syncIntervalHours) {
            return true;
        }

        // Thông tin đã được cập nhật trên Auth0 sau lần đồng bộ cuối cùng
        String updatedAt = jwt.getClaimAsString("updated_at");
        if (updatedAt != null && !updatedAt.equals(user.getAuth0UpdatedAt())) {
            return true;
        }
        return false;
    }

    /**
     * Lấy thông tin roles và permissions từ Auth0
     */
    private void enrichUserWithRolesAndPermissions(User user) {
        try {
            // Lấy roles từ Auth0
            List<Map<String, Object>> roles = auth0Client.getUserRoles(user.getId());
            Set<UUID> roleNames = roles.stream()
                    .map(role -> (UUID) role.get("name"))
                    .collect(Collectors.toSet());
            user.setRoles(roleNames);

            // Lấy permissions từ Auth0
            List<Map<String, Object>> permissions = auth0Client.getUserPermissions(user.getId());
            Set<UUID> permissionNames = permissions.stream()
                    .map(permission -> (UUID) permission.get("permission_name"))
                    .collect(Collectors.toSet());
            user.setPermissions(permissionNames);
        } catch (Exception e) {
            log.error("Failed to fetch roles and permissions for user: {}", user.getId(), e);
        }
    }

    /**
     * Chuyển đổi từ Auth0User sang User entity
     */
    private User mapAuth0UserToLocalUser(Auth0UserDTO auth0User) {
        User user = new User();
        user.setId(auth0User.getUserId());
        user.setEmail(auth0User.getEmail());
        user.setName(auth0User.getName());
        user.setPicture(auth0User.getPicture());
        user.setNickname(auth0User.getNickname());
        user.setLocale(auth0User.getLocale());
        user.setEmailVerified(auth0User.isEmail_verified());
        user.setBlocked(auth0User.isBlocked());
        user.setUserMetadata(auth0User.getMetadataAsJson(auth0User.getUserMetadata()));
        user.setAppMetadata(auth0User.getMetadataAsJson(auth0User.getAppMetadata()));
        return user;
    }
}
