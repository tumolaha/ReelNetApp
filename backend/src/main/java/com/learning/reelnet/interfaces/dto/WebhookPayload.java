package com.learning.reelnet.interfaces.dto;

public class WebhookPayload {
    private String type;
    private String userId;
    
    // Getters and setters
    public String getType() {
        return type;
    }
    
    public String getUserId() {
        return userId;
    }
    
    // Additional fields based on Auth0 webhook structure
}
