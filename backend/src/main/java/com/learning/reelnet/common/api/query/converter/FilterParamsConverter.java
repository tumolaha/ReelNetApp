package com.learning.reelnet.common.api.query.converter;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.reelnet.common.api.query.FilterParams;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterParamsConverter implements Converter<String, FilterParams> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public FilterParams convert(String source) {
        try {
            if (source == null || source.isEmpty()) {
                return new FilterParams();
            }
            return objectMapper.readValue(source, FilterParams.class);
        } catch (Exception e) {
            log.warn("Error converting filter params: {}", e.getMessage());
            return new FilterParams();
        }
    }
} 