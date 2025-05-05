package com.learning.reelnet.common.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
public class HibernateConfig {

    @Bean
    @Profile("dev")
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            // Disable prepared statements
            hibernateProperties.put("hibernate.jdbc.use_get_generated_keys", "false");
            // Improve batching efficiency
            hibernateProperties.put("hibernate.order_inserts", "true");
            hibernateProperties.put("hibernate.order_updates", "true");
            // Connection handling
            hibernateProperties.put("hibernate.connection.provider_disables_autocommit", "true");
            // Prevent dialect from generating problematic prepared statements
            hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
            hibernateProperties.put("hibernate.query.in_clause_parameter_padding", "true");
        };
    }
}