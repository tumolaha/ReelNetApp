package com.learning.reelnet.common.api.query.converter;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.common.api.query.SearchParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchParamsConverter implements Converter<String, SearchParams> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public SearchParams convert(String source) {
        try {
            if (source == null || source.isEmpty()) {
                return new SearchParams();
            }
            return objectMapper.readValue(source, SearchParams.class);
        } catch (Exception e) {
            log.warn("Error converting search params: {}", e.getMessage());
            return new SearchParams();
        }
    }
} 