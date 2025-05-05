package com.learning.reelnet.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
        
        // Explicitly set critical properties
        dataSource.addDataSourceProperty("prepareThreshold", "0");
        dataSource.addDataSourceProperty("preferQueryMode", "simple");
        dataSource.addDataSourceProperty("disableColumnSanitiser", "true");
        dataSource.addDataSourceProperty("reWriteBatchedInserts", "true");
        dataSource.setAutoCommit(false);
        
        return dataSource;
    }
}