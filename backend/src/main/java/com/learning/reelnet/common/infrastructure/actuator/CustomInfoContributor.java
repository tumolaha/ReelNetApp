package com.learning.reelnet.common.infrastructure.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomInfoContributor implements InfoContributor {

    @Autowired(required = false)
    private DataSource dataSource;

    @Override
    public void contribute(Info.Builder builder) {
        Map<String, Object> details = new HashMap<>();
        details.put("runtime", getRuntimeInfo());
        details.put("database", getDatabaseInfo());
        details.put("system", getSystemInfo());
        
        builder.withDetail("reelnet", details);
    }
    
    private Map<String, Object> getRuntimeInfo() {
        Map<String, Object> details = new HashMap<>();
        details.put("processors", Runtime.getRuntime().availableProcessors());
        details.put("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB");
        details.put("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        details.put("javaVersion", System.getProperty("java.version"));
        return details;
    }
    
    private Map<String, Object> getDatabaseInfo() {
        Map<String, Object> details = new HashMap<>();
        if (dataSource != null) {
            try (Connection conn = dataSource.getConnection()) {
                DatabaseMetaData metaData = conn.getMetaData();
                details.put("databaseProduct", metaData.getDatabaseProductName());
                details.put("databaseVersion", metaData.getDatabaseProductVersion());
                details.put("driverName", metaData.getDriverName());
                details.put("driverVersion", metaData.getDriverVersion());
            } catch (Exception e) {
                details.put("error", "Could not fetch database info: " + e.getMessage());
            }
        } else {
            details.put("status", "No datasource available");
        }
        return details;
    }
    
    private Map<String, Object> getSystemInfo() {
        Map<String, Object> details = new HashMap<>();
        details.put("os", System.getProperty("os.name"));
        details.put("osVersion", System.getProperty("os.version"));
        details.put("osArch", System.getProperty("os.arch"));
        details.put("user", System.getProperty("user.name"));
        return details;
    }
}