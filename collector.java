package com.monitor.collector;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

@Component
public class SystemMetricsCollector {
    private final MeterRegistry meterRegistry;
    private final OperatingSystemMXBean osBean;
    private final MemoryMXBean memoryBean;

    public SystemMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.osBean = ManagementFactory.getOperatingSystemMXBean();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
    }

    @Scheduled(fixedRate = 1000)
    public void collectCpuMetrics() {
        double cpuLoad = osBean.getSystemLoadAverage();
        meterRegistry.gauge("system.cpu.load", cpuLoad);
    }

    @Scheduled(fixedRate = 1000)
    public void collectMemoryMetrics() {
        long usedMemory = memoryBean.getHeapMemoryUsage().getUsed();
        long maxMemory = memoryBean.getHeapMemoryUsage().getMax();
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        meterRegistry.gauge("system.memory.usage.percent", memoryUsage);
    }
}

package com.monitor.collector;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;

@Component
public class NetworkMetricsCollector {
    private final MeterRegistry meterRegistry;
    private static final String[] HOSTS_TO_MONITOR = {"8.8.8.8", "1.1.1.1"};

    public NetworkMetricsCollector(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Scheduled(fixedRate = 5000)
    public void collectNetworkLatency() {
        for (String host : HOSTS_TO_MONITOR) {
            try {
                Instant start = Instant.now();
                InetAddress address = InetAddress.getByName(host);
                boolean reachable = address.isReachable(1000);
                Duration latency = Duration.between(start, Instant.now());
                
                if (reachable) {
                    meterRegistry.gauge("network.latency", latency.toMillis());
                }
            } catch (Exception e) {
                meterRegistry.counter("network.errors", "host", host).increment();
            }
        }
    }
}
