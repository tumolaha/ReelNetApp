package com.learning.reelnet.common.config;

import me.paulschwarz.springdotenv.DotenvPropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {
    private static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);
    private final ConfigurableEnvironment environment;

    @Autowired
    public EnvConfig(ConfigurableEnvironment environment) {
        this.environment = environment;
        // Now it's safe to use environment
        DotenvPropertySource.addToEnvironment(environment);
    }
    
    @PostConstruct
    public void logActiveProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "default";
        logger.info("Active profile: {}", activeProfile);
        logger.info("Loading environment variables from .env and .env.{}", activeProfile);

        // Log a few key properties to verify loading worked
        logger.info("Server port: {}", environment.getProperty("SERVER_PORT"));
        logger.info("Context path: {}", environment.getProperty("SERVER_SERVLET_CONTEXT_PATH"));
    }
}