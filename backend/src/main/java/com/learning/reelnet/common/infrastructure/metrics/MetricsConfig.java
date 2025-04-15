package com.learning.reelnet.common.infrastructure.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Configuration for application metrics.
 */
@Configuration
public class MetricsConfig {

    private final MeterRegistry meterRegistry;
    private final AtomicInteger activeUsers = new AtomicInteger(0);
    private final AtomicInteger pendingJobs = new AtomicInteger(0);

    public MetricsConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    /**
     * Creates a TimedAspect bean for the @Timed annotation.
     */
    @Bean
    public TimedAspect timedAspect() {
        return new TimedAspect(meterRegistry);
    }

    /**
     * Counter for API request errors.
     */
    @Bean
    public Counter apiErrorCounter() {
        return Counter.builder("api.errors.count")
                .description("Count of API errors")
                .register(meterRegistry);
    }

    /**
     * Timer for API request duration.
     */
    @Bean
    public Timer apiRequestTimer() {
        return Timer.builder("api.request.duration")
                .description("API request duration")
                .register(meterRegistry);
    }
    
    /**
     * Gauge for active users count.
     */
    @Bean
    public Gauge activeUsersGauge() {
        return Gauge.builder("app.users.active", activeUsers, AtomicInteger::get)
                .description("Number of active users")
                .register(meterRegistry);
    }
    
    /**
     * Gauge for pending background jobs.
     */
    @Bean
    public Gauge pendingJobsGauge() {
        return Gauge.builder("app.jobs.pending", pendingJobs, AtomicInteger::get)
                .description("Number of pending background jobs")
                .register(meterRegistry);
    }
    
    // Methods to update the gauges
    public void incrementActiveUsers() {
        activeUsers.incrementAndGet();
    }
    
    public void decrementActiveUsers() {
        activeUsers.decrementAndGet();
    }
    
    public void setPendingJobs(int count) {
        pendingJobs.set(count);
    }
}