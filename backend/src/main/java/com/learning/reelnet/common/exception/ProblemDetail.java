package com.learning.reelnet.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Implementation of RFC 7807 Problem Details for HTTP APIs.
 * This is the standard error response format for the application.
 * See: https://tools.ietf.org/html/rfc7807
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    
    private static final String BASE_TYPE_URI = "https://api.reelnet.com/errors";
    
    /**
     * A URI reference that identifies the problem type.
     */
    private URI type;
    
    /**
     * A short, human-readable summary of the problem type.
     */
    private String title;
    
    /**
     * The HTTP status code.
     */
    private int status;
    
    /**
     * A human-readable explanation specific to this occurrence of the problem.
     */
    private String detail;
    
    /**
     * A URI reference that identifies the specific occurrence of the problem.
     */
    private URI instance;
    
    /**
     * The timestamp when the problem occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The application-specific error code.
     */
    private String errorCode;
    
    /**
     * Additional properties relevant to the problem.
     */
    private Map<String, Object> properties;

    /**
     * Creates a problem detail from an ApiException.
     */
    public static ProblemDetail fromException(ApiException ex) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/" + ex.getErrorCode().toLowerCase()))
            .title(HttpStatus.valueOf(ex.getHttpStatus().value()).getReasonPhrase())
            .detail(ex.getMessage())
            .status(ex.getHttpStatus().value())
            .errorCode(ex.getErrorCode())
            .timestamp(LocalDateTime.now())
            .properties(ex.getParameters() != null ? Map.of("parameters", ex.getParameters()) : null)
            .build();
    }

    /**
     * Creates a validation problem detail.
     */
    public static ProblemDetail forValidation(String title, String detail, Map<String, String> fieldErrors) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/validation"))
            .title(title)
            .detail(detail)
            .status(HttpStatus.BAD_REQUEST.value())
            .errorCode("VALIDATION_ERROR")
            .timestamp(LocalDateTime.now())
            .properties(Map.of("fieldErrors", fieldErrors))
            .build();
    }

    /**
     * Creates a business error problem detail.
     */
    public static ProblemDetail forBusinessError(String title, String detail, String errorCode) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/business"))
            .title(title)
            .detail(detail)
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .errorCode(errorCode)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Creates a not found problem detail.
     */
    public static ProblemDetail forNotFound(String detail) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/not-found"))
            .title("Resource Not Found")
            .detail(detail)
            .status(HttpStatus.NOT_FOUND.value())
            .errorCode("NOT_FOUND")
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Creates an unauthorized problem detail.
     */
    public static ProblemDetail forUnauthorized(String detail) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/unauthorized"))
            .title("Unauthorized Access")
            .detail(detail)
            .status(HttpStatus.UNAUTHORIZED.value())
            .errorCode("UNAUTHORIZED")
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Creates a forbidden problem detail.
     */
    public static ProblemDetail forForbidden(String detail) {
        return builder()
            .type(URI.create(BASE_TYPE_URI + "/forbidden"))
            .title("Access Denied")
            .detail(detail)
            .status(HttpStatus.FORBIDDEN.value())
            .errorCode("FORBIDDEN")
            .timestamp(LocalDateTime.now())
            .build();
    }
} 