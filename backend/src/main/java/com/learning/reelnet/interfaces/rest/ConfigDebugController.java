package com.learning.reelnet.interfaces.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/config-debug")
public class ConfigDebugController {
    
    @Value("${server.servlet.context-path}")
    private String contextPath;
    
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;
    
    @GetMapping
    public Map<String, String> getConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("contextPath", contextPath);
        config.put("swaggerPath", swaggerPath);
        config.put("expectedSwaggerUrl", contextPath + swaggerPath);
        return config;
    }
}
