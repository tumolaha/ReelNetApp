package com.learning.reelnet.common.infrastructure.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;

/**
 * JPA configuration for the application.
 * This class configures JPA repositories, auditing, and transaction management.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.learning.reelnet")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    /**
     * Bean that provides the current auditor (user) for JPA auditing.
     * This implementation uses the Spring Security context to determine the current user.
     *
     * @return the auditor provider
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // In a real application, this would get the current user from Spring Security
            // For now, we return a placeholder value
            return Optional.of("system");
        };
    }
}