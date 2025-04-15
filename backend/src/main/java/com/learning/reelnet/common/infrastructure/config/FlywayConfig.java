package com.learning.reelnet.common.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

/**
 * Configuration for Flyway database migrations.
 * This ensures proper initialization of Flyway and avoids conflicts.
 */
@Configuration
public class FlywayConfig {

    private final EnvironmentVariables env;

    public FlywayConfig(Environment environment, EnvironmentVariables env) {
        this.env = env;
    }

    /**
     * Custom FlywayMigrationInitializer bean to ensure proper initialization.
     * This bean will only be created if flyway is enabled via the 'spring.flyway.enabled' property.
     * 
     * @param flyway the Flyway instance
     * @return a FlywayMigrationInitializer
     */
    @Bean
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
    public FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
        // Return a no-op initializer to override the default one
        return new FlywayMigrationInitializer(flyway, (f) -> {
            // Do nothing, we'll manually migrate later if needed
        });
    }

    /**
     * Bean to manually run Flyway migrations after the EntityManagerFactory is initialized.
     * This helps avoid conflicts between Hibernate's schema update and Flyway migrations.
     * 
     * @param flyway the Flyway instance
     * @return a boolean indicating if migration was performed
     */
    @Bean
    @DependsOn("entityManagerFactory")
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true")
    public boolean runFlywayMigrations(Flyway flyway) {
        // Only run migrations if enabled and we have the necessary DB configuration
        if (env.has("SPRING_DATASOURCE_URL") && env.has("SPRING_DATASOURCE_USERNAME")) {
            try {
                flyway.migrate();
                return true;
            } catch (Exception e) {
                // Log error but don't prevent application startup
                System.err.println("Error running Flyway migrations: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
} 