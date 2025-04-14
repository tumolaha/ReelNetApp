package com.learning.reelnet.common.infrastructure.web;

import com.learning.reelnet.common.domain.exceptions.BusinessRuleViolationException;
import com.learning.reelnet.common.domain.exceptions.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler to convert exceptions to standardized problem
 * details responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String PROBLEM_BASE_URL = "https://api.reelnet.com/problems";

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ProblemDetail> handleBusinessRuleViolation(
            BusinessRuleViolationException ex, WebRequest request) {

        log.warn("Business rule violation: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/business-rule-violation"))
                .title("Business Rule Violation")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(ex.getMessage())
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .properties(Map.of("errorCode", ex.getErrorCode()))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFound(
            EntityNotFoundException ex, WebRequest request) {

        log.info("Entity not found: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/not-found"))
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .properties(Map.of("errorCode", ex.getErrorCode()))
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problem);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(
            ConstraintViolationException ex, WebRequest request) {

        Map<String, Object> validationErrors = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (existing, replacement) -> existing));

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/validation-error"))
                .title("Validation Error")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("Validation failed for request parameters")
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .properties(Map.of("validationErrors", validationErrors))
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> validationErrors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/validation-error"))
                .title("Validation Error")
                .status(status.value())
                .detail("Validation failed for request body")
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .properties(Map.of("validationErrors", validationErrors))
                .build();

        return ResponseEntity
                .status(status)
                .body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/message-not-readable"))
                .title("Invalid Request Format")
                .status(status.value())
                .detail("The request body could not be read: " + ex.getMessage())
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex, WebRequest request) {
        log.error("Unhandled exception", ex);

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/internal-error"))
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("An unexpected error occurred")
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        Class<?> requiredType = ex.getRequiredType();
        String typeName = (requiredType != null) ? requiredType.getSimpleName() : "unknown";
        
        String detail = String.format(
                "Parameter '%s' with value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), typeName);

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/type-mismatch"))
                .title("Type Mismatch")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(detail)
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            @NonNull TypeMismatchException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status,
            @NonNull WebRequest request) {

        String detail = String.format(
                "Property '%s' with value '%s' could not be converted to required type",
                ex.getPropertyName(), ex.getValue());

        ProblemDetail problem = ProblemDetail.builder()
                .type(URI.create(PROBLEM_BASE_URL + "/type-mismatch"))
                .title("Type Mismatch")
                .status(status.value())
                .detail(detail)
                .instance(URI.create(((ServletWebRequest) request).getRequest().getRequestURI()))
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(problem);
    }
}