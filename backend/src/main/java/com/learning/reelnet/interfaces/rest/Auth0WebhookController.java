package com.learning.reelnet.interfaces.rest;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.interfaces.dto.WebhookPayload;
import com.learning.reelnet.modules.user.application.services.Auth0ManagementClient;
import com.learning.reelnet.modules.user.application.services.UserSynchronizationService;
import com.learning.reelnet.modules.user.domain.model.User;
import com.learning.reelnet.modules.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/webhooks/auth0")
@Slf4j
@RequiredArgsConstructor
public class Auth0WebhookController {
    private final UserRepository userRepository;
    private final UserSynchronizationService userSyncService;
    private final Auth0ManagementClient auth0Client;
    
    @Value("${auth0.webhook.signing-secret}")
    private String signingSecret;
    
    /**
     * Xử lý webhook từ Auth0
     */
    @PostMapping
    public ResponseEntity<Void> handleAuth0Webhook(
            @RequestBody WebhookPayload payload,
            @RequestHeader(name = "X-Auth0-Signature", required = false) String signature) {
        
        // Xác thực webhook (Production)
        if (signature != null && !verifySignature(payload, signature)) {
            log.warn("Invalid webhook signature received");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            log.info("Received Auth0 webhook: {}", payload.getType());
            String userId = extractUserIdFromPayload(payload);
            
            if (userId == null) {
                return ResponseEntity.ok().build();
            }
            
            // Xử lý webhook dựa trên loại sự kiện
            switch (payload.getType()) {
                case "user.created":
                    handleUserCreated(userId);
                    break;
                case "user.updated":
                    handleUserUpdated(userId);
                    break;
                case "user.deleted":
                    handleUserDeleted(userId);
                    break;
                default:
                    log.info("Unhandled event type: {}", payload.getType());
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error processing Auth0 webhook: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Xử lý sự kiện user.created
     */
    private void handleUserCreated(String userId) {
        // Xóa cache nếu có
        auth0Client.invalidateUserCache(userId);
        
        // Không tạo user ngay - sẽ được tạo khi user truy cập hệ thống
        log.info("Received user.created event for: {}", userId);
    }
    
    /**
     * Xử lý sự kiện user.updated
     */
    private void handleUserUpdated(String userId) {
        // Xóa cache
        auth0Client.invalidateUserCache(userId);
        
        // Kiểm tra user có tồn tại trong DB không
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            // Cập nhật thông tin user
            userSyncService.updateUserFromAuth0(optionalUser.get());
            log.info("Updated local user after Auth0 update: {}", userId);
        }
    }
    
    /**
     * Xử lý sự kiện user.deleted
     */
    private void handleUserDeleted(String userId) {
        // Xóa cache
        auth0Client.invalidateUserCache(userId);
        
        // Kiểm tra user có tồn tại trong DB không
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBlocked(true);
            userRepository.save(user);
            log.info("Marked user as deleted from Auth0: {}", userId);
        }
    }
    
    /**
     * Trích xuất userId từ payload
     */
    private String extractUserIdFromPayload(WebhookPayload payload) {
        // Trích xuất user_id từ payload tùy theo cấu trúc
        return payload.getUserId();
    }
    
    /**
     * Xác thực chữ ký webhook
     */
    private boolean verifySignature(WebhookPayload payload, String signature) {
        try {
            // Convert payload to JSON string
            String data = new ObjectMapper().writeValueAsString(payload);
            
            // Tính HMAC signature
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(signingSecret.getBytes(), "HmacSHA256");
            hmac.init(secretKey);
            String calculatedSignature = Base64.getEncoder()
                .encodeToString(hmac.doFinal(data.getBytes()));
            
            return MessageDigest.isEqual(calculatedSignature.getBytes(), signature.getBytes());
        } catch (Exception e) {
            log.error("Error verifying webhook signature", e);
            return false;
        }
    }
}
