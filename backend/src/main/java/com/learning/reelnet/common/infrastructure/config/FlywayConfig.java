package com.learning.reelnet.common.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Flyway configuration to handle PostgreSQL connection pooler issues.
 */
@Configuration
public class FlywayConfig {

    @Autowired
    private Environment env;
    
    @Autowired
    private DataSource dataSource;

    /**
     * Creates a custom Flyway instance with special configuration to avoid
     * prepared statement issues with PostgreSQL connection poolers.
     */
    @Bean(initMethod = "migrate")
    @Primary
    @ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "true", matchIfMissing = true)
    public Flyway flyway() {
        try {
            // Get the database URL from environment
            String url = env.getProperty("spring.datasource.url");
            String username = env.getProperty("spring.datasource.username");
            String password = env.getProperty("spring.datasource.password");
            
            // Append parameters to disable prepared statements if not already present
            if (!url.contains("prepareThreshold=0")) {
                url = url + (url.contains("?") ? "&" : "?") + "prepareThreshold=0";
            }
            // Create JDBC properties to avoid prepared statement issues
            Map<String, String> jdbcProps = new HashMap<>();
            jdbcProps.put("prepareThreshold", "0");
            jdbcProps.put("preferQueryMode", "simple");
            jdbcProps.put("autosave", "never");
            
            // Create a direct connection to bypass connection pool for Flyway
            return Flyway.configure()
                .dataSource(url, username, password)
                .baselineOnMigrate(true)
                .outOfOrder(false)
                .validateOnMigrate(false)
                .cleanDisabled(true)
                .jdbcProperties(jdbcProps)
                .cleanDisabled(true)
                .load();
        } catch (Exception e) {
            // If configuration fails, fallback to simpler config
            return Flyway.configure()
                .dataSource(dataSource)
                .baselineOnMigrate(true)
                .load();
        }
    }
    
}