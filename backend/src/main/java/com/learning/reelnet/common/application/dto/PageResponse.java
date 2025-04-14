package com.learning.reelnet.common.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A standard DTO for pagination responses.
 * This class encapsulates the result of a paginated query, including the content
 * and pagination metadata.
 *
 * @param <T> the type of elements in the content
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /**
     * The content of the current page.
     */
    private List<T> content;

    /**
     * The total number of elements across all pages.
     */
    private long totalElements;

    /**
     * The total number of pages.
     */
    private int totalPages;

    /**
     * The current page number (0-based).
     */
    private int number;

    /**
     * The number of elements in the current page.
     */
    private int size;

    /**
     * Whether the current page is the first page.
     */
    private boolean first;

    /**
     * Whether the current page is the last page.
     */
    private boolean last;

    /**
     * Whether the current page has a next page.
     */
    private boolean hasNext;

    /**
     * Whether the current page has a previous page.
     */
    private boolean hasPrevious;

    /**
     * Creates a new PageResponse instance based on the content and total elements.
     *
     * @param content the content of the current page
     * @param totalElements the total number of elements across all pages
     * @param pageRequest the original page request
     * @param <T> the type of elements in the content
     * @return a new PageResponse instance
     */
    public static <T> PageResponse<T> of(List<T> content, long totalElements, PageRequest pageRequest) {
        int totalPages = pageRequest.getSize() > 0 ? 
                (int) Math.ceil((double) totalElements / pageRequest.getSize()) : 0;
        
        int page = pageRequest.getPage();
        boolean isFirst = page == 0;
        boolean isLast = page >= totalPages - 1;
        
        return new PageResponse<>(
                content,
                totalElements,
                totalPages,
                page,
                content.size(),
                isFirst,
                isLast,
                !isLast,
                !isFirst
        );
    }

    /**
     * Creates an empty PageResponse.
     *
     * @param <T> the type of elements in the content
     * @return an empty PageResponse
     */
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(
                List.of(),
                0,
                0,
                0,
                0,
                true,
                true,
                false,
                false
        );
    }
}