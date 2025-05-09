package com.learning.reelnet.modules.user.api.dto;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object cho thông tin người dùng.
 * Sử dụng cho API responses và requests liên quan đến người dùng.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Thông tin chi tiết của người dùng")
public class UserDTO {
    
    @Schema(description = "ID của người dùng (từ Auth0)", example = "auth0|123456789")
    private String id;
    
    @Schema(description = "Địa chỉ email của người dùng", example = "user@example.com")
    private String email;
    
    @Schema(description = "Tên hiển thị của người dùng", example = "John Doe")
    private String name;
    
    @Schema(description = "URL của ảnh đại diện", example = "https://example.com/avatar.jpg")
    private String picture;
    
    @Schema(description = "Thời điểm đăng nhập cuối cùng")
    private Instant lastLogin;
    
    @Schema(description = "Trạng thái xác thực email", example = "true")
    @JsonProperty("emailVerified")
    private boolean emailVerified;
    
    @Schema(description = "Trạng thái hoạt động của tài khoản", example = "true")
    private boolean active;
    
    @Schema(description = "Danh sách ID của các vai trò")
    private Set<UUID> roles;
    
    @Schema(description = "Danh sách ID của các quyền")
    private Set<UUID> permissions;
    
    @Schema(description = "Metadata do người dùng quản lý")
    private Map<String, Object> userMetadata;
    
    @Schema(description = "Metadata do ứng dụng quản lý")
    private Map<String, Object> appMetadata;
    
    @Schema(description = "Thời điểm đồng bộ dữ liệu cuối cùng từ Auth0")
    private Instant lastSyncTimestamp;
    
    @Schema(description = "ID của tenant trong môi trường multi-tenant", example = "tenant-123")
    private String tenantId;
    
    /**
     * Phiên bản rút gọn của UserDTO để hiển thị trong danh sách
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ListItem {
        private String id;
        private String email;
        private String name;
        private String picture;
        private boolean active;
        private Instant lastLogin;
    }
    
    /**
     * Chuyển đổi UserDTO đầy đủ sang phiên bản ListItem
     */
    public ListItem toListItem() {
        return ListItem.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .picture(this.picture)
                .active(this.active)
                .lastLogin(this.lastLogin)
                .build();
    }
    
    /**
     * Kiểm tra xem người dùng có vai trò cụ thể không
     * 
     * @param roleId ID của vai trò cần kiểm tra
     * @return true nếu người dùng có vai trò này
     */
    public boolean hasRole(UUID roleId) {
        return roles != null && roles.contains(roleId);
    }
    
    /**
     * Kiểm tra xem người dùng có quyền cụ thể không
     * 
     * @param permissionId ID của quyền cần kiểm tra
     * @return true nếu người dùng có quyền này
     */
    public boolean hasPermission(UUID permissionId) {
        return permissions != null && permissions.contains(permissionId);
    }
}