package com.learning.reelnet.common.api.advice;

import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.api.response.ErrorResponse;
import com.learning.reelnet.common.exception.ApiException;
import com.learning.reelnet.common.exception.ValidationException;
import com.learning.reelnet.common.exception.ResourceNotFoundException;
import com.learning.reelnet.common.exception.ValidationException.ValidationError;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Xử lý toàn cục các ngoại lệ phát sinh trong ứng dụng.
 * Chuyển đổi các ngoại lệ thành phản hồi API có cấu trúc và nhất quán.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý các ngoại lệ API
     * 
     * @param ex Ngoại lệ API cần xử lý
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API chứa thông tin lỗi
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
            ex.getHttpStatus().value(),
            ex.getMessage(),
            ex.getErrorCode(), 
            request.getRequestURI()
        );
        
        log.error("API exception: {}", ex.toString());
        
        return new ResponseEntity<>(ApiResponse.error(errorResponse), ex.getHttpStatus());
    }
    
    /**
     * Xử lý ngoại lệ xác thực (ValidationException)
     * 
     * @param ex Ngoại lệ xác thực cần xử lý
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API chứa thông tin lỗi xác thực
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationException(ValidationException ex, HttpServletRequest request) {
        Map<String, String> validationErrors = ex.getValidationErrors().stream()
            .collect(Collectors.toMap(ValidationError::getField, ValidationError::getMessage));
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            ex.getErrorCode(),
            request.getRequestURI()
        );
        errorResponse.setValidationErrors(validationErrors);
        
        log.warn("Validation exception: {}", ex.toString());
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ không tìm thấy tài nguyên
     * 
     * @param ex Ngoại lệ không tìm thấy tài nguyên
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với thông báo lỗi phù hợp
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Object> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            ex.getErrorCode(),
            request.getRequestURI()
        );
        
        log.warn("Resource not found: {}", ex.toString());
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ từ chối truy cập
     * 
     * @param ex Ngoại lệ từ chối truy cập
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với thông báo từ chối truy cập
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Object> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.FORBIDDEN.value(),
            "Bạn không có quyền truy cập tài nguyên này",
            "ACCESS_DENIED",
            request.getRequestURI()
        );
        
        log.warn("Access denied: {}", ex.getMessage());
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý các lỗi xác thực từ Spring Validation
     * 
     * @param ex Ngoại lệ xác thực từ Spring
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với danh sách lỗi xác thực
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Dữ liệu không hợp lệ",
            "VALIDATION_ERROR",
            request.getRequestURI()
        );
        errorResponse.setValidationErrors(errors);
        
        log.warn("Validation error: {}", errors);
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ vi phạm ràng buộc
     * 
     * @param ex Ngoại lệ ràng buộc
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với danh sách lỗi ràng buộc
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String path = violation.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            errors.put(field, violation.getMessage());
        });
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Dữ liệu vi phạm ràng buộc",
            "CONSTRAINT_VIOLATION",
            request.getRequestURI()
        );
        errorResponse.setValidationErrors(errors);
        
        log.warn("Constraint violation: {}", errors);
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ thiếu tham số yêu cầu
     * 
     * @param ex Ngoại lệ thiếu tham số
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với thông báo lỗi phù hợp
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Thiếu tham số bắt buộc: " + ex.getParameterName(),
            "MISSING_PARAMETER",
            request.getRequestURI()
        );
        
        log.warn("Missing parameter: {}", ex.getParameterName());
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ không thể đọc được thông điệp HTTP
     * 
     * @param ex Ngoại lệ không thể đọc được thông điệp
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với thông báo lỗi phù hợp
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.BAD_REQUEST.value(),
            "Nội dung yêu cầu không hợp lệ hoặc có định dạng sai",
            "INVALID_REQUEST_BODY",
            request.getRequestURI()
        );
        
        log.warn("Invalid request body: {}", ex.getMessage());
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ tham số đối số không khớp kiểu
     * 
     * @param ex Ngoại lệ kiểu tham số không khớp
     * @param request Yêu cầu HTTP hiện tại
     * @param of TODO
     * @return Phản hồi API với thông báo lỗi phù hợp
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request, ErrorResponse of) {
        
        ErrorResponse errorResponse = of;
        
        String requiredType = "appropriate type";
        if (ex.getRequiredType() != null) {
            Class<?> type = ex.getRequiredType();
            requiredType = (type != null && type.getSimpleName() != null) ? type.getSimpleName() : "unknown type";
        }
        log.warn("Type mismatch: Parameter '{}' should be {}", ex.getName(), requiredType);
        
        return ApiResponse.error(errorResponse);
    }
    
    /**
     * Xử lý ngoại lệ chung cho tất cả các ngoại lệ không được xử lý cụ thể
     * 
     * @param ex Ngoại lệ cần xử lý
     * @param request Yêu cầu HTTP hiện tại
     * @return Phản hồi API với thông báo lỗi chung
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Object> handleGlobalException(Exception ex, HttpServletRequest request) {
        String requestId = (String) request.getAttribute("requestId");
        
        ErrorResponse errorResponse = ErrorResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Đã xảy ra lỗi trong hệ thống. Vui lòng thử lại sau hoặc liên hệ hỗ trợ.",
            "INTERNAL_SERVER_ERROR",
            request.getRequestURI()
        );
        errorResponse.setRequestId(requestId);
        
        // Ghi nhật ký lỗi chi tiết cho mục đích gỡ lỗi
        log.error("Unhandled exception: ", ex);
        
        return ApiResponse.error(errorResponse);
    }
} 