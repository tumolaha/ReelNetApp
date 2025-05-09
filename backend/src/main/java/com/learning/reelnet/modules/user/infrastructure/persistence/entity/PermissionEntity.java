package com.learning.reelnet.modules.user.infrastructure.persistence.entity;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {

    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(unique = true)
    private String name;
    
    private String description;
    
    // Resource mà permission này áp dụng (ví dụ: "videos", "comments", "users")
    private String resource;
    
    // Action mà permission này cho phép (ví dụ: "read", "write", "delete")
    private String action;
    
    // Auth0 permission ID sử dụng cho đồng bộ hóa
    private String auth0PermissionId;
    
    // Mối quan hệ ngược lại với Role (nếu cần)
    @ManyToMany(mappedBy = "permissions")
    private Set<RoleEntity> roles;
}