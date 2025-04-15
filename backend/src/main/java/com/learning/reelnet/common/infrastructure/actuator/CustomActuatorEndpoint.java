package com.learning.reelnet.common.infrastructure.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom actuator endpoint to provide application-specific information.
 */
@Component
@Endpoint(id = "application-status")
public class CustomActuatorEndpoint {

    @ReadOperation
    public Map<String, Object> getApplicationStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("currentTime", System.currentTimeMillis());
        
        // Add system information
        Map<String, Object> system = new HashMap<>();
        system.put("javaVersion", System.getProperty("java.version"));
        system.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        system.put("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "MB");
        system.put("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        
        status.put("system", system);
        return status;
    }
}