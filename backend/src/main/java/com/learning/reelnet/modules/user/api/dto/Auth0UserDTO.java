package com.learning.reelnet.modules.user.api.dto;

import java.time.Instant;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Auth0UserDTO {
    
    private String user_id;
    private String email;
    private String name;
    private String nickname;
    private String picture;
    
    @JsonProperty("user_metadata")
    private Map<String, Object> userMetadata;
    
    @JsonProperty("app_metadata")
    private Map<String, Object> appMetadata;
    
    @JsonProperty("email_verified")
    private boolean emailVerified;
    
    @JsonProperty("created_at")
    private Instant createdAt;
    
    @JsonProperty("updated_at")
    private Instant updatedAt;
    
    @JsonProperty("last_login")
    private Instant lastLogin;
    
    @JsonProperty("last_ip")
    private String lastIp;
    
    @JsonProperty("logins_count")
    private Integer loginsCount;
    
    private boolean blocked;
    
    @JsonProperty("identities")
    private List<Map<String, Object>> identities;
    
    // Auth0 Connection Info
    private String connection;
}
