package com.learning.reelnet.common.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import com.learning.reelnet.common.ApplicationConstants.Profiles;

/**
 * Setup environment files at application startup.
 * Helps ensure the correct .env file is used by copying from .env.development or .env.production
 */
@Configuration
public class EnvironmentFileSetup {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentFileSetup.class);
    private final Environment environment;

    public EnvironmentFileSetup(Environment environment) {
        this.environment = environment;
    }

    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        String[] activeProfiles = environment.getActiveProfiles();
        logger.info("Application started with profiles: {}", Arrays.toString(activeProfiles));
        
        setupEnvironmentFiles(activeProfiles);
    }
    
    private void setupEnvironmentFiles(String[] activeProfiles) {
        boolean isProduction = Arrays.stream(activeProfiles)
                .anyMatch(profile -> Profiles.PRODUCTION.equals(profile));
        
        try {
            Path sourceEnvPath;
            File envDevFile = new File(".env.development");
            File envProdFile = new File(".env.production");
            File destEnvFile = new File(".env");
            
            if (isProduction && envProdFile.exists()) {
                sourceEnvPath = Paths.get(".env.production");
                logger.info("Using production environment file");
            } else if (envDevFile.exists()) {
                sourceEnvPath = Paths.get(".env.development");
                logger.info("Using development environment file");
            } else {
                logger.warn("No environment files found. Using system environment variables.");
                return;
            }
            
            // Copy the appropriate env file to .env
            Files.copy(sourceEnvPath, destEnvFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.info("Successfully copied {} to .env", sourceEnvPath);
            
        } catch (IOException e) {
            logger.error("Failed to set up environment files", e);
        }
    }
} 