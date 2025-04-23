package com.learning.reelnet.interfaces.rest;

import com.learning.reelnet.common.api.response.ApiResponse;
import com.learning.reelnet.common.infrastructure.metrics.PublicMetricsService;
import com.learning.reelnet.common.infrastructure.metrics.dto.PublicMetricsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for exposing selected application metrics to users.
 * Provides a safe, filtered view of metrics without exposing sensitive system information.
 */
@RestController
@RequestMapping("/public/metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public Metrics", description = "API endpoints for public application metrics")
public class PublicMetricsController {

    private final PublicMetricsService publicMetricsService;

    /**
     * Get basic application metrics in a user-friendly format.
     * This endpoint is accessible without authentication.
     *
     * @return API Response containing basic metrics
     */
    @GetMapping
    @Operation(summary = "Get basic application metrics", 
               description = "Returns basic application metrics such as request count, error rate, and system load")
    public ApiResponse<PublicMetricsDto> getBasicMetrics() {
        log.debug("REST request to get basic metrics");
        PublicMetricsDto metrics = publicMetricsService.getSafeMetrics();
        return ApiResponse.success(metrics, "Basic metrics retrieved successfully");
    }

    /**
     * Get detailed metrics for dashboards.
     * This endpoint is more detailed but still contains only safe, non-sensitive information.
     *
     * @return API Response containing detailed metrics for dashboards
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get detailed metrics for dashboards", 
               description = "Returns more detailed metrics suitable for user dashboards")
    public ApiResponse<PublicMetricsDto> getDashboardMetrics() {
        log.debug("REST request to get dashboard metrics");
        PublicMetricsDto metrics = publicMetricsService.getDashboardMetrics();
        return ApiResponse.success(metrics, "Dashboard metrics retrieved successfully");
    }
} 