package com.learning.reelnet.modules.user.domain.model;

public enum UserRole {
    USER,
    PREMIUM_USER,
    ADMIN,
    SUPER_ADMIN;

    public String getValue() {
        return this.name();
    }
}