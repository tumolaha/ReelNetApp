package com.learning.reelnet.modules.user.api.dto;

import java.util.Set;
import java.util.UUID;

import com.learning.reelnet.modules.user.domain.model.User;

public class ReelNetUserDetails {
    private final User user;

    public ReelNetUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getName() {
        return user.getName();
    }

    public String getPicture() {
        return user.getPicture();
    }

    public Set<UUID> getRoles() {
        return user.getRoles();
    }
}
