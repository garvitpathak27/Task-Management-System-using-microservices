package in.garvit.tasks.controller;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;

/**
 * Custom health indicator for Eureka Server
 * Provides detailed health information about the Eureka server status
 * 
 * @author garvitpathak27
 */
@Component("customEurekaHealthIndicator")
public class EurekaHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();
        
        try {
            // Check if Eureka server context is available
            if (EurekaServerContextHolder.getInstance() != null && 
                EurekaServerContextHolder.getInstance().getServerContext() != null) {
                
                PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance()
                    .getServerContext().getRegistry();
                
                if (registry != null) {
                    // Get registry information
                    long numberOfRegisteredInstances = registry.getNumOfRenewsInLastMin();
                    int numOfKnownApps = registry.getApplications().size();
                    
                    builder.up()
                        .withDetail("status", "Eureka Server is running")
                        .withDetail("renewsInLastMin", numberOfRegisteredInstances)
                        .withDetail("numOfKnownApps", numOfKnownApps)
                        .withDetail("selfPreservationMode", registry.isSelfPreservationModeEnabled());
                } else {
                    builder.down().withDetail("reason", "Registry not available");
                }
            } else {
                builder.down().withDetail("reason", "Eureka server context not available");
            }
        } catch (Exception e) {
            builder.down()
                .withDetail("reason", "Health check failed")
                .withDetail("exception", e.getMessage());
        }
        
        return builder.build();
    }
}