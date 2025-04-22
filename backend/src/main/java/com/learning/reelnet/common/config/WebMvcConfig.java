package com.learning.reelnet.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.learning.reelnet.common.api.query.converter.FilterParamsConverter;
import com.learning.reelnet.common.api.query.converter.SearchParamsConverter;

/**
 * Configuration for Spring MVC.
 * Adds content negotiation and other web-related configurations.
 * Note: CORS configuration is handled separately in CorsConfig.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configures content negotiation for the application.
     * 
     * @param configurer the content negotiation configurer
     */
    @Override
    public void configureContentNegotiation(@NonNull ContentNegotiationConfigurer configurer) {
        configurer
                .favorParameter(false)
                .ignoreAcceptHeader(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new FilterParamsConverter());
        registry.addConverter(new SearchParamsConverter());
    }
}