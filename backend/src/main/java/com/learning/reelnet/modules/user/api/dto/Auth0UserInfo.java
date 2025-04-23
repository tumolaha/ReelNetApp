package com.learning.reelnet.modules.user.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth0UserInfo {

    @JsonProperty("sub")
    private String sub;  // Auth0 user ID

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("name")
    private String name;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("locale")
    private String locale;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("email")
    private String email;

    @JsonProperty("email_verified")
    private Boolean emailVerified;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("permissions")
    private List<String> permissions;

    @JsonProperty("user_metadata")
    private Map<String, Object> userMetadata;

    @JsonProperty("app_metadata")
    private Map<String, Object> appMetadata;

    // Helper methods to access nested metadata
    public Object getUserMetadataValue(String key) {
        return userMetadata != null ? userMetadata.get(key) : null;
    }

    public Object getAppMetadataValue(String key) {
        return appMetadata != null ? appMetadata.get(key) : null;
    }

    // Helper method to check if user has a specific role
    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }

    // Helper method to check if user has a specific permission
    public boolean hasPermission(String permission) {
        return permissions != null && permissions.contains(permission);
    }
}
