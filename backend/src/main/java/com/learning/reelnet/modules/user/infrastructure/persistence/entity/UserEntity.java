package com.learning.reelnet.modules.user.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    private String id;  // Auth0 user_id
    
    @Column(unique = true)
    private String email;
    
    private String name;
    private String picture;
    
    private Instant lastLogin;
    private Instant lastSyncTimestamp;
    
    // Thông tin từ Auth0 được lưu trữ dưới dạng JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> userMetadata;
    
    // Thông tin bổ sung của ứng dụng được lưu trữ dưới dạng JSON
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> appMetadata;
    
    // Mối quan hệ với roles và permissions
    @ElementCollection
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private Set<UUID> roles;
    
    @ElementCollection
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission_id")
    private Set<UUID> permissions;
    
    // Trạng thái người dùng trong hệ thống
    private boolean active;
    private boolean emailVerified;
    
    // Thông tin bổ sung của ứng dụng
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> applicationData;
    
    // Thông tin về tenant nếu có multi-tenancy
    private String tenantId;
}