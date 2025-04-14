package com.learning.reelnet.common.domain.exceptions;

/**
 * Exception thrown when a business rule is violated in the domain.
 * Business rules represent constraints or invariants that must be maintained
 * for the system to be in a valid state.
 */
public class BusinessRuleViolationException extends DomainException {

    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_ERROR_CODE = "BUSINESS_RULE_VIOLATION";

    /**
     * Creates a new BusinessRuleViolationException with the specified rule name and message.
     *
     * @param ruleName The name of the business rule that was violated
     * @param message A detailed message explaining the violation
     */
    public BusinessRuleViolationException(String ruleName, String message) {
        super(
            String.format("Business rule '%s' violated: %s", ruleName, message),
            DEFAULT_ERROR_CODE,
            Severity.ERROR,
            ruleName, message
        );
    }
    
    /**
     * Creates a new BusinessRuleViolationException with the specified rule name, message, and cause.
     *
     * @param ruleName The name of the business rule that was violated
     * @param message A detailed message explaining the violation
     * @param cause The underlying cause of the exception
     */
    public BusinessRuleViolationException(String ruleName, String message, Throwable cause) {
        super(
            String.format("Business rule '%s' violated: %s", ruleName, message),
            DEFAULT_ERROR_CODE,
            Severity.ERROR,
            cause,
            ruleName, message
        );
    }
    
    /**
     * Creates a new BusinessRuleViolationException with a custom error code.
     *
     * @param ruleName The name of the business rule that was violated
     * @param message A detailed message explaining the violation
     * @param errorCode A custom error code for this violation
     */
    public BusinessRuleViolationException(String ruleName, String message, String errorCode) {
        super(
            String.format("Business rule '%s' violated: %s", ruleName, message),
            errorCode,
            Severity.ERROR,
            ruleName, message
        );
    }
    
    /**
     * Creates a new BusinessRuleViolationException with a custom error code and severity.
     *
     * @param ruleName The name of the business rule that was violated
     * @param message A detailed message explaining the violation
     * @param errorCode A custom error code for this violation
     * @param severity The severity level of the exception
     */
    public BusinessRuleViolationException(String ruleName, String message, String errorCode, Severity severity) {
        super(
            String.format("Business rule '%s' violated: %s", ruleName, message),
            errorCode,
            severity,
            ruleName, message
        );
    }
}