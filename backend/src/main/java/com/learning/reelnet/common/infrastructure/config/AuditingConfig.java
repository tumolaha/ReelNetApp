package com.learning.reelnet.common.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.learning.reelnet.common.infrastructure.security.utils.SecurityUtils;

import java.util.Optional;

/**
 * Configuration for JPA auditing.
 * Sets up auditing for created/modified dates and users.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditingConfig {

    /**
     * Provides the current auditor (user) for entity auditing.
     *
     * @return the auditor provider
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(SecurityUtils.getCurrentUsername()).or(() -> Optional.of("system"));
    }
}