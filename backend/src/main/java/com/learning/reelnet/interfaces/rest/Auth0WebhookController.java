package com.learning.reelnet.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.reelnet.modules.user.application.services.UserSynchronizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@RestController
@RequestMapping("/api/public/webhooks/auth0")
@RequiredArgsConstructor
@Slf4j
public class Auth0WebhookController {

    private final UserSynchronizationService syncService;
    
    @Value("${auth0.webhook.signing-secret}")
    private String signingSecret;
    
    /**
     * Xử lý webhook từ Auth0 khi user thay đổi
     */
    @PostMapping
    public ResponseEntity<?> handleAuth0Webhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader("X-Auth0-Signature") String signature) {
        
        // 1. Xác thực webhook request từ Auth0
        if (!verifySignature(payload, signature)) {
            log.warn("Invalid Auth0 webhook signature");
            return ResponseEntity.badRequest().body("Invalid signature");
        }
        
        try {
            // 2. Xử lý các sự kiện từ Auth0
            String eventType = (String) payload.get("type");
            Map<String, Object> userData = (Map<String, Object>) payload.get("user");
            String userId = (String) userData.get("user_id");
            
            log.info("Received Auth0 webhook: {} for user {}", eventType, userId);
            
            // 3. Xóa cache và cập nhật user tương ứng
            switch (eventType) {
                case "user.created":
                case "user.updated":
                case "user.profile_updated":
                case "user.roles.updated": 
                case "user.permissions.updated":
                    syncService.invalidateSyncCache(userId);
                    break;
                case "user.deleted":
                    // Xử lý xóa user nếu cần
                    break;
                default:
                    log.debug("Ignored Auth0 event type: {}", eventType);
            }
            
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            log.error("Error processing Auth0 webhook: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error processing webhook");
        }
    }
    
    /**
     * Xác thực chữ ký từ Auth0 webhook
     */
    private boolean verifySignature(Map<String, Object> payload, String signature) {
        // Implement signature verification logic here
        // Thường sử dụng HMAC để xác thực
        return true; // Placeholder
    }
}
