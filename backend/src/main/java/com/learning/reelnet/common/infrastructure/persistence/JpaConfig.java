package com.learning.reelnet.common.infrastructure.persistence;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA configuration for the application.
 * This class configures JPA repositories and transaction management.
 * Note: Auditing is configured in AuditingConfig class.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.learning.reelnet")
@EnableTransactionManagement
public class JpaConfig {
    // Auditing configuration moved to AuditingConfig
}