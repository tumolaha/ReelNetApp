package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ khi truy cập bị cấm.
 * Được sử dụng khi người dùng không có quyền truy cập vào tài nguyên yêu cầu.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    /**
     * Mã lỗi
     */
    private String errorCode;

    /**
     * Constructor mặc định
     */
    public ForbiddenException() {
        super("Truy cập bị cấm");
    }

    /**
     * Constructor với thông báo
     *
     * @param message Thông báo lỗi
     */
    public ForbiddenException(String message) {
        super(message);
    }

    /**
     * Constructor với thông báo và mã lỗi
     *
     * @param message Thông báo lỗi
     * @param errorCode Mã lỗi
     */
    public ForbiddenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor với thông báo và nguyên nhân
     *
     * @param message Thông báo lỗi
     * @param cause Nguyên nhân
     */
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor với thông báo, mã lỗi và nguyên nhân
     *
     * @param message Thông báo lỗi
     * @param errorCode Mã lỗi
     * @param cause Nguyên nhân
     */
    public ForbiddenException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Lấy mã lỗi
     *
     * @return Mã lỗi
     */
    public String getErrorCode() {
        return errorCode;
    }
} 