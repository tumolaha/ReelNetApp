package com.learning.reelnet.common.infrastructure.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "system-resources")
public class SystemResourcesEndpoint {

    @ReadOperation
    public Map<String, Object> getSystemResources() {
        Map<String, Object> resources = new HashMap<>();
        
        // Memory information
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        Map<String, Object> memoryInfo = new HashMap<>();
        memoryInfo.put("heapMemoryUsage", memoryBean.getHeapMemoryUsage());
        memoryInfo.put("nonHeapMemoryUsage", memoryBean.getNonHeapMemoryUsage());
        resources.put("memory", memoryInfo);
        
        // CPU information
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        Map<String, Object> cpuInfo = new HashMap<>();
        cpuInfo.put("availableProcessors", osBean.getAvailableProcessors());
        cpuInfo.put("systemLoadAverage", osBean.getSystemLoadAverage());
        resources.put("cpu", cpuInfo);
        
        // Thread information
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Map<String, Object> threadInfo = new HashMap<>();
        threadInfo.put("threadCount", threadBean.getThreadCount());
        threadInfo.put("daemonThreadCount", threadBean.getDaemonThreadCount());
        threadInfo.put("peakThreadCount", threadBean.getPeakThreadCount());
        threadInfo.put("totalStartedThreadCount", threadBean.getTotalStartedThreadCount());
        resources.put("threads", threadInfo);
        
        // Runtime
        Map<String, Object> runtime = new HashMap<>();
        runtime.put("startTime", ManagementFactory.getRuntimeMXBean().getStartTime());
        runtime.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime());
        resources.put("runtime", runtime);
        
        return resources;
    }
}