package com.learning.reelnet.modules.user.infrastructure.persistence.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RedisHash("user")
public class RedisUserEntity {
    @Id
    private String id;
    
    @Indexed
    private String email;
    
    private String name;
    private String picture;
    private String nickname;
    private String locale;
    
    private Set<UUID> roles = new HashSet<>();
    private Set<UUID> permissions = new HashSet<>();
    
    private Date lastSyncedWithAuth0;
    private String auth0UpdatedAt;
    
    private Date createdAt;
    private Date updatedAt;
    private Date lastLogin;
    private boolean emailVerified;
    private boolean blocked;
    
    private String userMetadata;
    private String appMetadata;
} 