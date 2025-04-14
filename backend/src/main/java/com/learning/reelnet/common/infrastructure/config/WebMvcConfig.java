package com.learning.reelnet.common.infrastructure.config;

import com.learning.reelnet.common.infrastructure.web.SearchParamResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configuration for Spring MVC.
 * Adds custom argument resolvers and other web-related configurations.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final SearchParamResolver searchParamResolver;

    public WebMvcConfig(SearchParamResolver searchParamResolver) {
        this.searchParamResolver = searchParamResolver;
    }
    @Override
    public void addArgumentResolvers(@NonNull List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(searchParamResolver);
    }
    
}