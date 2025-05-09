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
            // Clear previous cached data for this user if we've had issues
            for (int retry = 0; retry < 2; retry++) {
                try {
                    return userCache.get(userId);
                } catch (Exception e) {
                    if (retry == 0) {
                        // On first error, invalidate cache and token and retry once
                        log.warn("First attempt to get user info failed, invalidating cache and retrying: {}", e.getMessage());
                        userCache.invalidate(userId);
                        this.managementApiToken = null;
                        this.tokenExpiresAt = null;
                    } else {
                        // On second error, propagate the exception
                        log.error("Error fetching user info from Auth0 after retry: {}", e.getMessage(), e);
                        throw e;
                    }
                }
            }
            
            // This should never be reached due to the throw in the catch block
            throw new RuntimeException("Failed to get user info from Auth0 after retries");
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
        try {
            ensureManagementApiToken();

            String url = String.format("https://%s/api/v2/users/%s", domain, userId);
            HttpHeaders headers = createAuthHeaders();

            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Auth0UserDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, Auth0UserDTO.class);

            return response.getBody();
        } catch (HttpClientErrorException e) {
            handleApiError(e, userId);
            // handleApiError will now throw exceptions rather than retry
            // so this line should never be reached
            throw new RuntimeException("Failed to fetch user from Auth0", e);
        } catch (Exception e) {
            log.error("Unexpected error fetching user from Auth0: {}", e.getMessage(), e);
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
                // Don't call fetchUserInfoFromAuth0 recursively to avoid nesting
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Auth0 API rate limit reached", e);
        } else if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // Token hết hạn - làm mới và thử lại
            log.warn("Auth0 Management API token expired. Refreshing...");
            // Invalidate token but don't call API again here
            this.managementApiToken = null;
            this.tokenExpiresAt = null;
            throw new RuntimeException("Auth0 token unauthorized, token has been invalidated for refresh", e);
        } else {
            log.error("Auth0 API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Auth0 API error: " + e.getStatusCode(), e);
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
            // Fix the duration calculation (was using wrong order of parameters)
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

            log.info("Requesting new Auth0 Management API token");
            
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
            if (responseBody != null) {
                // Clear previous token first
                this.managementApiToken = null;
                this.tokenExpiresAt = null;
                
                // Store the new token
                managementApiToken = (String) responseBody.get("access_token");
                Integer expiresIn = (Integer) responseBody.get("expires_in");

                // Validate token before storing
                if (managementApiToken == null || managementApiToken.isEmpty()) {
                    throw new RuntimeException("Auth0 returned an empty management token");
                }

                // Lưu thời gian hết hạn chính xác với buffer ngắn hơn (30 giây)
                tokenExpiresAt = Instant.now().plusSeconds(expiresIn - 30); 
                
                // Thêm log debug chi tiết về thời gian hết hạn
                log.info("Acquired new Auth0 Management API token, expires in {} seconds", expiresIn);
                log.debug("Token will expire at: {}, current time: {}, buffer: 30 seconds", 
                    tokenExpiresAt, Instant.now());
                
                // Validate the token immediately by making a test API call
                verifyToken();
            } else {
                log.error("Empty response body when requesting Auth0 Management API token");
                throw new RuntimeException("Failed to acquire Auth0 Management API token: empty response");
            }
        } catch (Exception e) {
            log.error("Failed to acquire Auth0 Management API token: {}", e.getMessage(), e);
            // Reset token state on error
            managementApiToken = null;
            tokenExpiresAt = null;
            throw new RuntimeException("Failed to acquire Auth0 Management API token", e);
        } finally {
            tokenLock.unlock();
        }
    }
    
    /**
     * Verify the token is valid by making a test API call
     */
    @SuppressWarnings("rawtypes")
    private void verifyToken() {
        try {
            // Make a simple API call that doesn't require much resources
            String url = String.format("https://%s/api/v2/clients", domain);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(managementApiToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Only request page 0 with 1 item to minimize data
            url += "?page=0&per_page=1";
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<List> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, List.class);
            
            // If we get here, the token is valid
            if (response.getStatusCode().is2xxSuccessful()) {
                log.debug("Auth0 Management API token verified successfully");
            } else {
                log.warn("Auth0 Management API token verification returned non-success status: {}", 
                        response.getStatusCode());
                // Still consider it an error
                throw new RuntimeException("Auth0 token verification failed with status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Auth0 Management API token verification failed: {}", e.getMessage(), e);
            // Invalidate the token since verification failed
            this.managementApiToken = null;
            this.tokenExpiresAt = null;
            throw new RuntimeException("Auth0 Management API token verification failed", e);
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
