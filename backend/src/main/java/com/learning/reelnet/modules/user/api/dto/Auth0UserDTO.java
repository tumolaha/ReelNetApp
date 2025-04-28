package com.learning.reelnet.modules.user.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Auth0UserDTO {
    private String user_id;
    private String name;
    private String email;
    private String picture;
    private String nickname;
    private String given_name;
    private String family_name;
    private String locale;
    private boolean email_verified;
    private boolean blocked;
    private String updated_at;
    private String created_at;
    private List<Auth0Identity> identities;
    
    @JsonProperty("app_metadata")
    private Map<String, Object> appMetadata;
    
    @JsonProperty("user_metadata")
    private Map<String, Object> userMetadata;
    
    // Helper methods
    public String getUserId() {
        return user_id;
    }
    
    public String getMetadataAsJson(Map<String, Object> metadata) {
        try {
            return new ObjectMapper().writeValueAsString(metadata);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
class Auth0Identity {
    private String connection;
    private String provider;
    private String user_id;
}
