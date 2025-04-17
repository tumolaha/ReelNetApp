package com.learning.reelnet.common.api.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * Ghi nhật ký cho các yêu cầu API.
 * Bộ tư vấn này chặn các yêu cầu đến và ghi lại thông tin quan trọng
 * như ID yêu cầu, loại yêu cầu, đường dẫn, IP người dùng và nội dung.
 */
@Slf4j
@ControllerAdvice
public class RequestLoggingAdvice extends RequestBodyAdviceAdapter {

    private final HttpServletRequest request;

    /**
     * Khởi tạo lớp tư vấn với HttpServletRequest
     *
     * @param request Đối tượng HttpServletRequest hiện tại
     */
    public RequestLoggingAdvice( HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Xác định xem lớp tư vấn này có hỗ trợ phương thức hiện tại hay không
     *
     * @param methodParameter Tham số phương thức đang được kiểm tra
     * @param targetType Loại mục tiêu
     * @param converterType Loại bộ chuyển đổi
     * @return true nếu tư vấn hỗ trợ phương thức này
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter#supports(MethodParameter, Type, Class)
     */
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
    

    /**
     * Xử lý sau khi đọc phần thân yêu cầu
     *
     * @param body Đối tượng đã được chuyển đổi
     * @param inputMessage Thông điệp đầu vào HTTP
     * @param parameter Tham số phương thức
     * @param targetType Loại mục tiêu
     * @param converterType Loại bộ chuyển đổi
     * @return Đối tượng đã xử lý
     * 
     * 
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter#afterBodyRead(Object, HttpInputMessage, MethodParameter, Type, Class)
     */
    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage,
                               @NonNull MethodParameter parameter, @NonNull Type targetType,
                               @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // Kiểm tra xem body có phải là một chuỗi hay không        
        // Tạo ID duy nhất cho yêu cầu
        String requestId = UUID.randomUUID().toString();
        
        // Ghi nhật ký thông tin yêu cầu
        log.info("Request ID: {}, Method: {}, Path: {}, Client IP: {}, Body: {}",
                requestId,
                request.getMethod(),
                request.getRequestURI(),
                getClientIp(),
                body);
        
        // Lưu ID yêu cầu vào thuộc tính yêu cầu để có thể truy cập sau này
        request.setAttribute("requestId", requestId);
        
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

    /**
     * Lấy địa chỉ IP của khách hàng, xử lý các proxy
     *
     * @return Địa chỉ IP của khách hàng
     */
    private String getClientIp() {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // Lấy địa chỉ IP đầu tiên trong chuỗi X-Forwarded-For
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
} 