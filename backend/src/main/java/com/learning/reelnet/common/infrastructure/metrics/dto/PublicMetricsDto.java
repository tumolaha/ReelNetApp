package com.learning.reelnet.common.infrastructure.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Data Transfer Object for public metrics responses.
 * Standardizes the format of metrics data returned to clients.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicMetricsDto {

    /**
     * Timestamp when metrics were collected
     */
    private LocalDateTime timestamp;

    /**
     * Basic application metrics
     */
    private BasicMetrics basic;

    /**
     * Advanced metrics (optional)
     */
    private AdvancedMetrics advanced;

    /**
     * Request metrics by HTTP status code
     */
    private Map<String, Long> statusCodes;
    
    /**
     * Request metrics by endpoint
     */
    private Map<String, Long> endpointCalls;

    /**
     * Basic metrics data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicMetrics {
        private Long totalRequests;
        private Double errorRatePercent;
        private Double cpuUsagePercent;
        private Double avgResponseTimeMs;
        private Double uptime;
    }

    /**
     * Advanced metrics data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdvancedMetrics {
        private MemoryMetrics memory;
        private ThreadMetrics threads;
        private Long activeUsers;
        private Long totalSessions;
    }

    /**
     * Memory-related metrics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemoryMetrics {
        private Double usedMemoryMB;
        private Double maxMemoryMB;
        private Double usagePercent;
    }

    /**
     * Thread-related metrics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThreadMetrics {
        private Long threadCount;
        private Long peakThreadCount;
        private Long daemonThreadCount;
    }
} 