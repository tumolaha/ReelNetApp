package com.learning.reelnet.modules.user.application.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learning.reelnet.modules.user.api.dto.UserDTO;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserApplicationService {

    private final UserRepository userRepository;
    
    /**
     * Lấy thông tin User thông qua Auth0 ID
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByAuth0Id(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(this::toDTO);
    }
    
    /**
     * Lấy thông tin User hiện tại thông qua Auth0 ID
     * Sử dụng trong các controller để lấy thông tin người dùng hiện tại
     */
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser(String auth0Id) {
        return userRepository.findByAuth0Id(auth0Id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found: " + auth0Id));
    }
    
    /**
     * Chuyển đổi từ Domain Model sang DTO
     */
    private UserDTO toDTO(User user) {
        if (user == null) return null;
        
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .pictureUrl(user.getPictureUrl())
                .locale(user.getLocale())
                .role(user.getRole())
                .creator(user.getIsCreator() != null && user.getIsCreator())
                .build();
    }
}
