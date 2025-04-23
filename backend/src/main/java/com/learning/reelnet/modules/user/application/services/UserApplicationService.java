package com.learning.reelnet.modules.user.application.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.application.mapper.UserMapper;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing users and synchronizing with Auth0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserApplicationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    
    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }
    
    /**
     * Get user by Auth0 ID
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(userMapper::toDto);
    }
    
    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto);
    }
    
    /**
     * Get all users (paginated)
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }
    
    /**
     * Get all content creators (paginated)
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> getCreators(Pageable pageable) {
        return userRepository.findByCreatorTrue(pageable)
                .map(userMapper::toDto);
    }
    
    /**
     * Search users
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> searchUsers(String searchTerm, Pageable pageable) {
        return userRepository.searchUsers(searchTerm, pageable)
                .map(userMapper::toDto);
    }
    
    /**
     * Get recently active users
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getRecentlyActiveUsers(int limit) {
        return userRepository.findRecentlyActiveUsers(limit)
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    
    /**
     * Create or update user from JWT
     * This is the key method that synchronizes data from Auth0 with our database
     * Optimized to minimize unnecessary database writes
     */
    @Transactional
    public UserDTO syncUserFromJwt(Jwt jwt) {
        String auth0Id = jwt.getSubject();
        Map<String, Object> claims = jwt.getClaims();
        
        log.debug("Synchronizing user data from JWT for user: {}", auth0Id);
        
        Optional<User> existingUserOpt = userRepository.findByAuth0Id(auth0Id);
        
        // Nếu người dùng không tồn tại, tạo mới
        if (existingUserOpt.isEmpty()) {
            User newUser = createUserFromClaims(auth0Id, claims);
            newUser = userRepository.save(newUser);
            return userMapper.toDto(newUser);
        }
        
        // Người dùng đã tồn tại, chỉ cập nhật khi cần thiết
        User existingUser = existingUserOpt.get();
        boolean needsUpdate = false;
        
        // Kiểm tra các trường thông tin cần cập nhật
        String displayName = claims.getOrDefault("name", "").toString();
        if (!displayName.equals(existingUser.getDisplayName())) {
            existingUser.setDisplayName(displayName);
            needsUpdate = true;
        }
        
        String pictureUrl = claims.getOrDefault("picture", "").toString();
        if (!pictureUrl.equals(existingUser.getPictureUrl())) {
            existingUser.setPictureUrl(pictureUrl);
            needsUpdate = true;
        }
        
        String locale = claims.getOrDefault("locale", "").toString();
        if (!locale.equals(existingUser.getLocale())) {
            existingUser.setLocale(locale);
            needsUpdate = true;
        }
        
        // Cập nhật lastLogin khi cần thiết (ví dụ: nếu last_login quá cũ hoặc null)
        LocalDateTime now = LocalDateTime.now();
        if (existingUser.getLastLogin() == null || 
            existingUser.getLastLogin().plusHours(1).isBefore(now)) {
            existingUser.setLastLogin(now);
            needsUpdate = true;
        }
        
        // Kiểm tra và cập nhật roles từ Auth0 nếu có thay đổi
        boolean roleChanged = checkAndUpdateRoles(existingUser, claims);
        if (roleChanged) {
            needsUpdate = true;
        }
        
        // Chỉ lưu vào DB nếu có thay đổi
        if (needsUpdate) {
            log.debug("User data has changed, updating database for user: {}", auth0Id);
            existingUser = userRepository.save(existingUser);
        } else {
            log.debug("No changes detected for user: {}, skipping database update", auth0Id);
        }
        
        return userMapper.toDto(existingUser);
    }
    
    /**
     * Kiểm tra và cập nhật roles từ Auth0
     * @return true nếu có thay đổi, false nếu không
     */
    @SuppressWarnings("unchecked")
    private boolean checkAndUpdateRoles(User user, Map<String, Object> claims) {
        // Lấy roles từ Auth0
        Object roles = claims.get("https://reelnet.com/roles");
        
        if (roles instanceof List<?>) {
            List<String> rolesList = (List<String>) roles;
            
            boolean isAdmin = rolesList.contains("admin");
            boolean isCreator = rolesList.contains("teacher") || rolesList.contains("creator");
            
            // Xác định role mới dựa trên Auth0
            String newRole;
            if (isAdmin) {
                newRole = "ADMIN";
            } else if (isCreator) {
                newRole = "CREATOR";
            } else {
                newRole = "USER";
            }
            
            // Kiểm tra xem có thay đổi không
            boolean roleChanged = !newRole.equals(user.getRole());
            boolean creatorStatusChanged = isCreator != user.isCreator();
            
            // Cập nhật nếu có thay đổi
            if (roleChanged) {
                user.setRole(newRole);
            }
            
            if (creatorStatusChanged) {
                user.setCreator(isCreator);
            }
            
            return roleChanged || creatorStatusChanged;
        }
        
        // Không có roles claim hoặc không phải List, giữ nguyên
        return false;
    }
    
    /**
     * Update user information
     */
    @Transactional
    public UserDTO updateUser(UUID id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(user -> {
                    // Avoid updating sensitive fields
                    if (userDTO.getDisplayName() != null) {
                        user.setDisplayName(userDTO.getDisplayName());
                    }
                    if (userDTO.getLocale() != null) {
                        user.setLocale(userDTO.getLocale());
                    }
                    if (userDTO.getSettings() != null) {
                        user.setSettings(userDTO.getSettings());
                    }
                    
                    // Only admins should update these fields via separate methods
                    //user.setRole(userDTO.getRole());
                    //user.setCreator(userDTO.isCreator());
                    //user.setActive(userDTO.isActive());
                    
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
    
    /**
     * Update user settings
     */
    @Transactional
    public UserDTO updateUserSettings(UUID id, Map<String, Object> settings) {
        return userRepository.findById(id)
                .map(user -> {
                    try {
                        user.setSettings(objectMapper.writeValueAsString(settings));
                        return userMapper.toDto(userRepository.save(user));
                    } catch (JsonProcessingException e) {
                        log.error("Error serializing user settings", e);
                        throw new RuntimeException("Error updating user settings");
                    }
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
    
    /**
     * Toggle content creator status
     */
    @Transactional
    public UserDTO toggleCreatorStatus(UUID id, boolean isCreator) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setCreator(isCreator);
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }
    
    /**
     * Create a new user from JWT claims
     */
    private User createUserFromClaims(String auth0Id, Map<String, Object> claims) {
        User user = new User();
        user.setAuth0Id(auth0Id);
        user.setEmail(claims.getOrDefault("email", "").toString());
        user.setActive(true);
        user.setLastLogin(LocalDateTime.now());
        
        // Default settings
        try {
            user.setSettings(objectMapper.writeValueAsString(Map.of(
                "notifications", true,
                "darkMode", false,
                "language", claims.getOrDefault("locale", "en")
            )));
        } catch (JsonProcessingException e) {
            log.error("Error serializing default settings", e);
        }
        
        updateUserFromClaims(user, claims);
        
        return user;
    }
    
    /**
     * Update user fields from JWT claims
     */
    private void updateUserFromClaims(User user, Map<String, Object> claims) {
        // Update basic profile information
        user.setDisplayName(claims.getOrDefault("name", "").toString());
        user.setPictureUrl(claims.getOrDefault("picture", "").toString());
        user.setLocale(claims.getOrDefault("locale", "").toString());
        
        // Handle roles/permissions from Auth0
        handleAuth0Roles(user, claims);
    }
    
    /**
     * Process Auth0 roles and permissions
     * This handles the Auth0 roles and permissions claims and maps them to our system
     */
    @SuppressWarnings("unchecked")
    private void handleAuth0Roles(User user, Map<String, Object> claims) {
        // Get roles from Auth0 (using custom namespace if configured)
        Object roles = claims.get("https://reelnet.com/roles");
        
        if (roles instanceof List<?>) {
            List<String> rolesList = (List<String>) roles;
            
            // Check for admin role
            if (rolesList.contains("admin")) {
                user.setRole("ADMIN");
            } 
            // Check for teacher/creator role 
            else if (rolesList.contains("teacher") || rolesList.contains("creator")) {
                user.setRole("CREATOR");
                user.setCreator(true);
            }
            // Default role
            else {
                user.setRole("USER");
            }
        } else {
            // Default to USER if no roles claim
            if (user.getRole() == null) {
                user.setRole("USER");
            }
        }
    }
}
