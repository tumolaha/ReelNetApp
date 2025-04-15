package com.learning.reelnet.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.Arrays;

import com.learning.reelnet.common.ApplicationConstants.Profiles;

/**
 * Configuration for environment variables and properties.
 * Supports loading environment variables from .env file and system environment.
 */
@Configuration
public class EnvConfig {

    private final Environment environment;

    public EnvConfig(Environment environment) {
        this.environment = environment;
        logActiveProfiles();
    }

    /**
     * Logs the active profiles for debugging purposes.
     */
    private void logActiveProfiles() {
        String[] activeProfiles = environment.getActiveProfiles();
        if (activeProfiles.length > 0) {
            System.out.println("Active profiles: " + Arrays.toString(activeProfiles));
        } else {
            System.out.println("No active profiles set, using default configuration");
        }
    }

    /**
     * Configures property sources with environment-specific .env file if it exists.
     *
     * @return the property sources placeholder configurer
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setIgnoreResourceNotFound(true);
        
        // Determine which .env file to load based on active profile
        String activeProfile = System.getProperty("spring.profiles.active", "dev");
        
        // Try to load profile-specific .env file if it exists
        File envFile;
        if (Profiles.PRODUCTION.equals(activeProfile)) {
            envFile = new File(".env.production");
            if (envFile.exists()) {
                configurer.setLocation(new FileSystemResource(envFile));
                System.out.println("Loading environment variables from .env.production file");
                return configurer;
            }
        } else {
            // Default to development for any non-production profile
            envFile = new File(".env.development");
            if (envFile.exists()) {
                configurer.setLocation(new FileSystemResource(envFile));
                System.out.println("Loading environment variables from .env.development file");
                return configurer;
            }
        }
        
        // Fall back to .env file if profile-specific ones don't exist
        envFile = new File(".env");
        if (envFile.exists()) {
            configurer.setLocation(new FileSystemResource(envFile));
            System.out.println("Loading environment variables from .env file");
        } else {
            System.out.println("No .env file found, using system environment variables");
        }
        
        return configurer;
    }
} 