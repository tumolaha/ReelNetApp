package com.learning.reelnet.common.infrastructure.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Implementation of RFC 7807 Problem Details for HTTP APIs.
 * See: https://tools.ietf.org/html/rfc7807
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    
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
     * Additional properties relevant to the problem.
     */
    private Map<String, Object> properties;
}