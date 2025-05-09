package com.learning.reelnet.modules.user.api.dto;

import java.time.Instant;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Tiêu chí tìm kiếm người dùng
 */
@Data
@Schema(description = "Tiêu chí tìm kiếm người dùng")
public class UserSearchCriteria {
    
    @Schema(description = "Email người dùng (tìm kiếm một phần)", example = "example.com")
    private String email;
    
    @Schema(description = "Tên người dùng (tìm kiếm một phần)", example = "john")
    private String name;
    
    @Schema(description = "Trạng thái hoạt động")
    private Boolean active;
    
    @Schema(description = "Trạng thái xác thực email")
    private Boolean emailVerified;
    
    @Schema(description = "ID vai trò để tìm người dùng có vai trò này")
    private UUID roleId;
    
    @Schema(description = "ID tenant (cho môi trường multi-tenant)")
    private String tenantId;
    
    @Schema(description = "Thời gian đăng nhập sau ngày")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant lastLoginAfter;
    
    @Schema(description = "Thời gian đăng nhập trước ngày")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant lastLoginBefore;
}