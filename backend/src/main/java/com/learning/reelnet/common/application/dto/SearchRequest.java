package com.learning.reelnet.common.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for search requests that combine pagination, sorting, and filtering.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    
    /**
     * Pagination parameters.
     */
    @Builder.Default
    private PageRequest pagination = new PageRequest();
    
    /**
     * The search criteria.
     */
    @Builder.Default
    private List<SearchCriteria> criteria = new ArrayList<>();
    
    /**
     * Simple search query for free text search.
     */
    private String query;
    
    /**
     * Additional filters as key-value pairs.
     */
    @Builder.Default
    private Map<String, Object> filters = new HashMap<>();
    
    /**
     * Parse search criteria from a query string.
     * Format: "field1:value1 field2>value2 field3=value3"
     *
     * @param queryString the query string to parse
     * @return list of search criteria
     */
    public static List<SearchCriteria> parseQueryString(String queryString) {
        if (queryString == null || queryString.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<SearchCriteria> criteria = new ArrayList<>();
        
        // Split by spaces, but respect quoted strings
        boolean inQuotes = false;
        StringBuilder currentTerm = new StringBuilder();
        
        for (char c : queryString.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                currentTerm.append(c);
            } else if (c == ' ' && !inQuotes) {
                if (currentTerm.length() > 0) {
                    criteria.add(SearchCriteria.parse(currentTerm.toString()));
                    currentTerm.setLength(0);
                }
            } else {
                currentTerm.append(c);
            }
        }
        
        // Don't forget the last term
        if (currentTerm.length() > 0) {
            criteria.add(SearchCriteria.parse(currentTerm.toString()));
        }
        
        return criteria;
    }
    
    /**
     * Add a search criterion.
     *
     * @param field    the field name
     * @param operator the search operator
     * @param value    the search value
     * @return this search request
     */
    public SearchRequest addCriterion(String field, SearchCriteria.SearchOperator operator, Object value) {
        this.criteria.add(new SearchCriteria(field, operator, value));
        return this;
    }
    
    /**
     * Add a filter.
     *
     * @param field the field name
     * @param value the filter value
     * @return this search request
     */
    public SearchRequest addFilter(String field, Object value) {
        this.filters.put(field, value);
        return this;
    }
}