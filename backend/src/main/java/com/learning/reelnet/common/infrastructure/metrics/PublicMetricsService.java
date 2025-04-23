package com.learning.reelnet.common.infrastructure.metrics;

import com.learning.reelnet.common.infrastructure.metrics.dto.PublicMetricsDto;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service to safely expose selected metrics for public consumption.
 * This service filters sensitive metrics and only exposes non-sensitive data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PublicMetricsService {

    private final MeterRegistry meterRegistry;

    /**
     * Get basic, safe application metrics for public display
     * 
     * @return DTO with basic metrics
     */
    public PublicMetricsDto getSafeMetrics() {
        PublicMetricsDto.BasicMetrics basicMetrics = PublicMetricsDto.BasicMetrics.builder()
                .totalRequests(getHttpRequestCount())
                .errorRatePercent(getErrorRate())
                .cpuUsagePercent(getSystemLoad())
                .avgResponseTimeMs(getAverageResponseTime())
                .uptime(getUptime())
                .build();
        
        return PublicMetricsDto.builder()
                .timestamp(LocalDateTime.now())
                .basic(basicMetrics)
                .statusCodes(getStatusCodes())
                .build();
    }
    
    /**
     * Get more detailed metrics for dashboards, still filtered for safety
     */
    public PublicMetricsDto getDashboardMetrics() {
        PublicMetricsDto basicMetrics = getSafeMetrics();
        
        Map<String, Double> memoryInfo = getMemoryUsage();
        
        PublicMetricsDto.MemoryMetrics memoryMetrics = PublicMetricsDto.MemoryMetrics.builder()
                .usedMemoryMB(memoryInfo.get("usedMB"))
                .maxMemoryMB(memoryInfo.get("maxMB"))
                .usagePercent(memoryInfo.get("usagePercentage"))
                .build();
        
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        PublicMetricsDto.ThreadMetrics threadMetrics = PublicMetricsDto.ThreadMetrics.builder()
                .threadCount((long) threadBean.getThreadCount())
                .peakThreadCount((long) threadBean.getPeakThreadCount())
                .daemonThreadCount((long) threadBean.getDaemonThreadCount())
                .build();
        
        PublicMetricsDto.AdvancedMetrics advancedMetrics = PublicMetricsDto.AdvancedMetrics.builder()
                .memory(memoryMetrics)
                .threads(threadMetrics)
                .activeUsers(getActiveUserCount())
                .totalSessions(getTotalSessions())
                .build();
        
        basicMetrics.setAdvanced(advancedMetrics);
        basicMetrics.setEndpointCalls(getRequestsByEndpoint());
        
        return basicMetrics;
    }
    
    /**
     * Get total HTTP request count
     */
    private Long getHttpRequestCount() {
        try {
            return meterRegistry.find("http.server.requests")
                   .timer()
                   .count();
        } catch (Exception e) {
            log.warn("Error getting HTTP request count: {}", e.getMessage());
            return 0L;
        }
    }
    
    /**
     * Calculate error rate based on HTTP requests
     */
    private Double getErrorRate() {
        try {
            Long totalRequests = meterRegistry.find("http.server.requests").timer().count();
            Long errorRequests = meterRegistry.find("http.server.requests")
                    .tags("status", "5xx", "status", "4xx")
                    .timer()
                    .count();
            
            if (totalRequests == 0) {
                return 0.0;
            }
            
            return (errorRequests * 100.0) / totalRequests;
        } catch (Exception e) {
            log.warn("Error calculating error rate: {}", e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Get system CPU load
     */
    private Double getSystemLoad() {
        try {
            return meterRegistry.find("system.cpu.usage")
                   .gauge()
                   .value() * 100; // Convert to percentage
        } catch (Exception e) {
            log.warn("Error getting system load: {}", e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Get average response time
     */
    private Double getAverageResponseTime() {
        try {
            return meterRegistry.find("http.server.requests")
                   .timer()
                   .mean(java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.warn("Error getting average response time: {}", e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Get uptime in hours
     */
    private Double getUptime() {
        try {
            return (double) ManagementFactory.getRuntimeMXBean().getUptime() / (1000 * 60 * 60);
        } catch (Exception e) {
            log.warn("Error getting uptime: {}", e.getMessage());
            return 0.0;
        }
    }
    
    /**
     * Get active user count (estimated from metrics if available)
     */
    private Long getActiveUserCount() {
        try {
            return 0L; // Replace with actual implementation if you track active users
        } catch (Exception e) {
            log.warn("Error getting active user count: {}", e.getMessage());
            return 0L;
        }
    }
    
    /**
     * Get total sessions count
     */
    private Long getTotalSessions() {
        try {
            return 0L; // Replace with actual implementation if you track sessions
        } catch (Exception e) {
            log.warn("Error getting total sessions: {}", e.getMessage());
            return 0L;
        }
    }
    
    /**
     * Get status code distribution
     */
    private Map<String, Long> getStatusCodes() {
        Map<String, Long> statusCounts = new HashMap<>();
        
        try {
            // Get metrics for 2xx responses
            Long status2xx = meterRegistry.find("http.server.requests")
                           .tags("status", "200", "status", "201", "status", "204")
                           .timer()
                           .count();
            statusCounts.put("2xx", status2xx);
            
            // Get metrics for 4xx responses
            Long status4xx = meterRegistry.find("http.server.requests")
                           .tags("status", "400", "status", "401", "status", "403", "status", "404")
                           .timer()
                           .count();
            statusCounts.put("4xx", status4xx);
            
            // Get metrics for 5xx responses
            Long status5xx = meterRegistry.find("http.server.requests")
                           .tags("status", "500", "status", "502", "status", "503")
                           .timer()
                           .count();
            statusCounts.put("5xx", status5xx);
        } catch (Exception e) {
            log.warn("Error getting status code metrics: {}", e.getMessage());
        }
        
        return statusCounts;
    }
    
    /**
     * Get request counts by endpoint (only for selected endpoints)
     */
    private Map<String, Long> getRequestsByEndpoint() {
        Map<String, Long> resultsByEndpoint = new HashMap<>();
        
        try {
            Collection<Meter> meters = meterRegistry.find("http.server.requests").meters();
            
            for (Meter meter : meters) {
                String uri = null;
                for (Tag tag : meter.getId().getTags()) {
                    if (tag.getKey().equals("uri")) {
                        uri = tag.getValue();
                        break;
                    }
                }
                
                // Only include specific API endpoints, skip actuator and admin endpoints
                if (uri != null && uri.startsWith("/v1/") && !uri.contains("admin")) {
                    Long count = resultsByEndpoint.getOrDefault(uri, 0L);
                    count += meterRegistry.get("http.server.requests")
                              .tag("uri", uri)
                              .timer()
                              .count();
                    resultsByEndpoint.put(uri, count);
                }
            }
        } catch (Exception e) {
            log.warn("Error getting requests by endpoint: {}", e.getMessage());
        }
        
        return resultsByEndpoint;
    }
    
    /**
     * Get memory usage information
     */
    private Map<String, Double> getMemoryUsage() {
        Map<String, Double> memoryInfo = new HashMap<>();
        
        try {
            Double used = meterRegistry.find("jvm.memory.used")
                         .gauge()
                         .value() / (1024 * 1024); // Convert to MB
                         
            Double max = meterRegistry.find("jvm.memory.max")
                        .gauge()
                        .value() / (1024 * 1024); // Convert to MB
            
            memoryInfo.put("usedMB", used);
            memoryInfo.put("maxMB", max);
            memoryInfo.put("usagePercentage", (used / max) * 100);
        } catch (Exception e) {
            log.warn("Error getting memory usage: {}", e.getMessage());
            memoryInfo.put("usedMB", 0.0);
            memoryInfo.put("maxMB", 0.0);
            memoryInfo.put("usagePercentage", 0.0);
        }
        
        return memoryInfo;
    }
} 