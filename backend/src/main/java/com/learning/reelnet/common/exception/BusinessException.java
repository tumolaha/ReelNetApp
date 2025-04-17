package com.learning.reelnet.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a business rule or constraint is violated.
 * This exception represents errors in the business domain, such as:
 * - Invalid operations
 * - Business rule violations
 * - Business constraint breaches
 * - Domain invariant violations
 */
public class BusinessException extends ApiException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "BUSINESS_ERROR";

    /**
     * Creates a new BusinessException with a message.
     *
     * @param message    The detailed error message
     * @param parameters Additional context parameters
     */
    public BusinessException(String message, Object... parameters) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.UNPROCESSABLE_ENTITY, parameters);
    }

    /**
     * Creates a new BusinessException with a custom error code.
     *
     * @param message    The detailed error message
     * @param errorCode  The unique error code
     * @param parameters Additional context parameters
     */
    public BusinessException(String message, String errorCode, Object... parameters) {
        super(message, errorCode, HttpStatus.UNPROCESSABLE_ENTITY, parameters);
    }

    /**
     * Creates a new BusinessException with a message and cause.
     *
     * @param message    The detailed error message
     * @param cause      The underlying cause
     * @param parameters Additional context parameters
     */
    public BusinessException(String message, Throwable cause, Object... parameters) {
        super(message, DEFAULT_ERROR_CODE, HttpStatus.UNPROCESSABLE_ENTITY, cause, parameters);
    }

    /**
     * Creates a new BusinessException for a business rule violation.
     *
     * @param violation  The business rule violation details
     * @param parameters Additional context parameters
     */
    public BusinessException(BusinessRuleViolation violation, Object... parameters) {
        super(
            formatRuleViolation(violation.getRuleName(), violation.getMessage()),
            DEFAULT_ERROR_CODE,
            HttpStatus.UNPROCESSABLE_ENTITY,
            parameters
        );
    }

    /**
     * Creates a new BusinessException for a business rule violation with custom error code.
     *
     * @param violation  The business rule violation details
     * @param errorCode  The unique error code
     * @param parameters Additional context parameters
     */
    public BusinessException(BusinessRuleViolation violation, String errorCode, Object... parameters) {
        super(
            formatRuleViolation(violation.getRuleName(), violation.getMessage()),
            errorCode,
            HttpStatus.UNPROCESSABLE_ENTITY,
            parameters
        );
    }

    /**
     * Creates a new BusinessException for a business rule violation with cause.
     *
     * @param violation  The business rule violation details
     * @param cause      The underlying cause
     * @param parameters Additional context parameters
     */
    public BusinessException(BusinessRuleViolation violation, Throwable cause, Object... parameters) {
        super(
            formatRuleViolation(violation.getRuleName(), violation.getMessage()),
            DEFAULT_ERROR_CODE,
            HttpStatus.UNPROCESSABLE_ENTITY,
            cause,
            parameters
        );
    }

    /**
     * Helper method to format rule violation messages consistently.
     */
    private static String formatRuleViolation(String ruleName, String message) {
        return String.format("Business rule '%s' violated: %s", ruleName, message);
    }

    /**
     * Helper class to encapsulate business rule violation details.
     */
    public static class BusinessRuleViolation {
        private final String ruleName;
        private final String message;

        public BusinessRuleViolation(String ruleName, String message) {
            this.ruleName = ruleName;
            this.message = message;
        }

        public String getRuleName() {
            return ruleName;
        }

        public String getMessage() {
            return message;
        }
    }
} 