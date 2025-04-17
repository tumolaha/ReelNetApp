package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Ngoại lệ khi yêu cầu không hợp lệ.
 * Được sử dụng khi dữ liệu đầu vào không đáp ứng các yêu cầu nghiệp vụ.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Mã lỗi
     */
    private String errorCode;

    /**
     * Constructor mặc định
     */
    public BadRequestException() {
        super("Yêu cầu không hợp lệ");
    }

    /**
     * Constructor với thông báo
     *
     * @param message Thông báo lỗi
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Constructor với thông báo và mã lỗi
     *
     * @param message Thông báo lỗi
     * @param errorCode Mã lỗi
     */
    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor với thông báo và nguyên nhân
     *
     * @param message Thông báo lỗi
     * @param cause Nguyên nhân
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor với thông báo, mã lỗi và nguyên nhân
     *
     * @param message Thông báo lỗi
     * @param errorCode Mã lỗi
     * @param cause Nguyên nhân
     */
    public BadRequestException(String message, String errorCode, Throwable cause) {
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