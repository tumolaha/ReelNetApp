package com.learning.reelnet.common.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.lang.NonNull;

@Component
@RequiredArgsConstructor
public class HttpMetricsFilter extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Record request count by path and method
        Counter.builder("http.requests.total")
                .tag("path", path)
                .tag("method", method)
                .register(meterRegistry)
                .increment();

        // Measure request duration
        long start = System.nanoTime();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.nanoTime() - start;

            // Record response time by path, method and status
            Timer.builder("http.requests.duration")
                    .tag("path", path)
                    .tag("method", method)
                    .tag("status", String.valueOf(response.getStatus()))
                    .register(meterRegistry)
                    .record(duration, TimeUnit.NANOSECONDS);

            // Count responses by status
            Counter.builder("http.responses.total")
                    .tag("path", path)
                    .tag("method", method)
                    .tag("status", String.valueOf(response.getStatus()))
                    .register(meterRegistry)
                    .increment();
        }
    }
}