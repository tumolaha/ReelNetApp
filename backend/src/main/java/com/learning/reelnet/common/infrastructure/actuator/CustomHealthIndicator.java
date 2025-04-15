package com.learning.reelnet.common.infrastructure.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom health indicator to check application-specific health.
 */
@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Add your health check logic here
        boolean applicationHealthy = checkApplicationHealth();
        
        if (applicationHealthy) {
            return Health.up()
                    .withDetail("description", "Application is healthy")
                    .build();
        } else {
            return Health.down()
                    .withDetail("description", "Application is not healthy")
                    .build();
        }
    }

    private boolean checkApplicationHealth() {
        // Implement your health check logic here
        // For example, check external dependencies, connections, etc.
        return true;
    }
}