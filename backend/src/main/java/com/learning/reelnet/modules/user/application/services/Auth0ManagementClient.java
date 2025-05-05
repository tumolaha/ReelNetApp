package com.learning.reelnet.modules.user.application.services;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Value;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.learning.reelnet.modules.user.api.dto.Auth0UserDTO;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Auth0ManagementClient {

    @Value("${auth0.domain}")
    private String domain;

    // Thay đổi properties
    @Value("${auth0.management.clientId}")
    private String clientId;

    @Value("${auth0.management.clientSecret}")
    private String clientSecret;

    // Sử dụng audience dành riêng cho Management API
    @Value("${auth0.management.audience}")
    private String audience;

    @Value("${auth0.management.api.rate-limit:10}")
    private int rateLimit;

    private final RestTemplate restTemplate;
    private final LoadingCache<String, Auth0UserDTO> userCache;

    // Token management
    private String managementApiToken;
    private Instant tokenExpiresAt;
    private final ReentrantLock tokenLock = new ReentrantLock();

    public Auth0ManagementClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        // Khởi tạo cache
        this.userCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
                .build(key -> fetchUserInfoFromAuth0(key));
    }

    @PostConstruct
    public void init() {
        log.info("Auth0 Domain: {}", domain);
        log.info("Auth0 Client ID: {}", clientId);
        log.info("Auth0 Management Client ID: {}", audience);
    }

    /**
     * Lấy thông tin người dùng từ Auth0 (với cache)
     */
    public Auth0UserDTO getUserInfo(String userId) {
        try {
            return userCache.get(userId);
        } catch (Exception e) {
            log.error("Error fetching user info from Auth0: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get user info from Auth0", e);
        }
    }

    /**
     * Lấy roles của người dùng từ Auth0
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, Object>> getUserRoles(String userId) {
        String url = String.format("https://%s/api/v2/users/%s/roles", domain, userId);
        HttpHeaders headers = createAuthHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, List.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching user roles from Auth0: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user roles", e);
        }
    }

    /**
     * Lấy permissions của người dùng từ Auth0
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, Object>> getUserPermissions(String userId) {
        String url = String.format("https://%s/api/v2/users/%s/permissions", domain, userId);
        HttpHeaders headers = createAuthHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, List.class);

            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching user permissions from Auth0: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch user permissions", e);
        }
    }

    /**
     * Invalidate cache cho một user cụ thể
     */
    public void invalidateUserCache(String userId) {
        userCache.invalidate(userId);
    }

    /**
     * Gọi API Auth0 để lấy thông tin user
     */
    private Auth0UserDTO fetchUserInfoFromAuth0(String userId) {
        ensureManagementApiToken();

        String url = String.format("https://%s/api/v2/users/%s", domain, userId);
        HttpHeaders headers = createAuthHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Auth0UserDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Auth0UserDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            handleApiError(e, userId);
            // Nếu không phục hồi được, ném ngoại lệ
            throw new RuntimeException("Failed to fetch user from Auth0", e);
        }
    }

    /**
     * Xử lý lỗi từ Auth0 API
     */
    private void handleApiError(HttpClientErrorException e, String userId) {
        if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            // Rate limit - thử lại sau
            log.warn("Auth0 API rate limit reached. Retrying...");
            try {
                Thread.sleep(1000);
                fetchUserInfoFromAuth0(userId);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // Token hết hạn - làm mới và thử lại
            log.warn("Auth0 Management API token expired. Refreshing...");
            this.managementApiToken = null;
            fetchUserInfoFromAuth0(userId);
        } else {
            log.error("Auth0 API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
        }
    }

    /**
     * Đảm bảo có token hợp lệ cho Management API
     */
    private boolean isTokenValid() {
        // Fix token validation method
        if (managementApiToken == null || tokenExpiresAt == null) {
            log.debug("Token validation failed - token or expiration is null");
            return false;
        }
        
        Instant now = Instant.now();
        boolean isValid = now.isBefore(tokenExpiresAt);
        
        if (!isValid) {
            log.debug("Token expired: current={}, expires={}, diff={}ms", 
                now, tokenExpiresAt, 
                java.time.Duration.between(now, tokenExpiresAt).toMillis());
        }
        
        return isValid;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void ensureManagementApiToken() {
        if (isTokenValid()) {
            return;
        }

        tokenLock.lock();
        try {
            // Double-check after lock
            if (isTokenValid()) {
                return;
            }

            // Call API for new token
            String url = String.format("https://%s/oauth/token", domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> requestBody = Map.of(
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "audience", audience,
                    "grant_type", "client_credentials");

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, Map.class);

            Map<String, Object> responseBody = response.getBody();
            managementApiToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");

            // Store expiration time correctly
            tokenExpiresAt = Instant.now().plusSeconds(expiresIn - 300);
            
            log.info("Acquired new Auth0 Management API token, expires in {} seconds", expiresIn);
            log.debug("Token will expire at: {}", tokenExpiresAt);
        } catch (Exception e) {
            log.error("Failed to acquire Auth0 Management API token", e);
            // Reset token state on error
            managementApiToken = null;
            tokenExpiresAt = null;
        } finally {
            tokenLock.unlock();
        }
    }

    private HttpHeaders createAuthHeaders() {
        ensureManagementApiToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(managementApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
