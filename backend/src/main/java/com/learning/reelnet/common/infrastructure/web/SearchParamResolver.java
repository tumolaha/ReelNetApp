package com.learning.reelnet.common.infrastructure.web;

import com.learning.reelnet.common.application.dto.PageRequest;
import com.learning.reelnet.common.application.dto.SearchCriteria;
import com.learning.reelnet.common.application.dto.SearchRequest;
import org.springframework.lang.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolver for SearchRequest from request parameters.
 * Allows controllers to receive a SearchRequest object filled from query
 * parameters.
 */
@Component
public class SearchParamResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.getParameterType().equals(SearchRequest.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter,
            @org.springframework.lang.Nullable ModelAndViewContainer mavContainer,
            @NonNull NativeWebRequest webRequest,
            @org.springframework.lang.Nullable WebDataBinderFactory binderFactory) {
        // Initialize search request
        SearchRequest searchRequest = new SearchRequest();

        // Extract pagination parameters
        Integer page = parseIntegerParam(webRequest.getParameter("page"), 0);
        Integer size = parseIntegerParam(webRequest.getParameter("size"), 20);

        // Extract sort parameters
        List<String> sort = new ArrayList<>();
        String sortParam = webRequest.getParameter("sort");
        if (sortParam != null && !sortParam.trim().isEmpty()) {
            for (String s : sortParam.split(",")) {
                sort.add(s.trim());
            }
        }

        // Set pagination params
        searchRequest.setPagination(PageRequest.builder()
                .page(page)
                .size(size)
                .sort(sort)
                .build());

        // Extract search criteria
        String query = webRequest.getParameter("q");
        searchRequest.setQuery(query);

        // Extract filter parameters (all parameters starting with "filter.")
        Map<String, Object> filters = new HashMap<>();
        Map<String, String[]> parameterMap = webRequest.getParameterMap();

        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();

            if (paramName.startsWith("filter.") && entry.getValue().length > 0) {
                String fieldName = paramName.substring("filter.".length());
                filters.put(fieldName, entry.getValue()[0]);
            } else if (paramName.startsWith("criteria.") && entry.getValue().length > 0) {
                // Extract explicit search criteria
                String criteriaStr = entry.getValue()[0];
                searchRequest.getCriteria().add(SearchCriteria.parse(criteriaStr));
            }
        }

        searchRequest.setFilters(filters);

        return searchRequest;
    }

    private Integer parseIntegerParam(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}