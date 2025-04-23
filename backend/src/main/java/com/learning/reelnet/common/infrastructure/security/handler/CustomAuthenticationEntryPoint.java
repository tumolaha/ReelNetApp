package com.learning.reelnet.common.infrastructure.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.api.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom authentication entry point to handle unauthorized access.
 * Returns a standardized API response when authentication fails.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        
        log.warn("Authentication failed: {}", authException.getMessage());
        
        // Xác định thông báo lỗi và mã lỗi dựa trên loại ngoại lệ
        String message = "Xác thực không thành công";
        String errorCode = "AUTHENTICATION_FAILED";
        
        if (authException instanceof InvalidBearerTokenException) {
            if (authException.getMessage().contains("audience")) {
                message = "Token không được phát hành cho audience cần thiết";
                errorCode = "INVALID_TOKEN_AUDIENCE";
            } else if (authException.getMessage().contains("expired")) {
                message = "Token đã hết hạn";
                errorCode = "EXPIRED_TOKEN";
            } else if (authException.getMessage().contains("signature")) {
                message = "Chữ ký token không hợp lệ";
                errorCode = "INVALID_SIGNATURE";
            } else {
                message = "Token không hợp lệ";
                errorCode = "INVALID_TOKEN";
            }
        } else if (authException.getMessage().contains("Bad credentials")) {
            message = "Thông tin đăng nhập không chính xác";
            errorCode = "INVALID_CREDENTIALS";
        } else if (authException.getMessage().contains("insufficient authentication")) {
            message = "Thiếu thông tin xác thực";
            errorCode = "MISSING_AUTHENTICATION";
        }
        
        // Tạo response lỗi theo định dạng API của ứng dụng
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.UNAUTHORIZED.value(),
            message,
            errorCode,
            request.getRequestURI()
        );
        
        ApiResponse<Object> apiResponse = ApiResponse.error(errorResponse);
        
        // Thiết lập response
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        // Ghi response 
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
} 