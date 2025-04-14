package com.learning.reelnet.common.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard API response with HATEOAS support.
 *
 * @param <T> The type of data contained in the response
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HateoasResponse<T> {

    /**
     * Data contained in the response.
     */
    private T data;
    
    /**
     * Links for HATEOAS support.
     */
    @Builder.Default
    private List<Link> links = new ArrayList<>();
    
    /**
     * Whether the request was successful.
     */
    private boolean success;
    
    /**
     * Response message.
     */
    private String message;
    
    /**
     * Timestamp of the response.
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    /**
     * Adds a link to this response.
     *
     * @param href The URL of the link
     * @param rel The relation type of the link
     * @param method The HTTP method to use with this link
     * @return This response
     */
    public HateoasResponse<T> addLink(String href, String rel, String method) {
        this.links.add(new Link(href, rel, method));
        return this;
    }
    
    /**
     * Creates a successful response with data.
     *
     * @param data The response data
     * @param <T> The type of data
     * @return A new HateoasResponse
     */
    public static <T> HateoasResponse<T> success(T data) {
        return HateoasResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }
    
    /**
     * Creates a successful response with data and a message.
     *
     * @param data The response data
     * @param message The success message
     * @param <T> The type of data
     * @return A new HateoasResponse
     */
    public static <T> HateoasResponse<T> success(T data, String message) {
        return HateoasResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }
    
    /**
     * Creates an error response with a message.
     *
     * @param message The error message
     * @param <T> The type of data
     * @return A new HateoasResponse
     */
    public static <T> HateoasResponse<T> error(String message) {
        return HateoasResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
    
    /**
     * Represents a hypermedia link.
     */
    @Data
    public static class Link {
        private final String href;
        private final String rel;
        private final String method;
    }
}