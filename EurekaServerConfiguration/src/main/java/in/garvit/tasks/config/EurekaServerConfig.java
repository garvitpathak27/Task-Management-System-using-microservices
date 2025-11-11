package in.garvit.tasks.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Eureka Server
 * Contains beans and configurations for the Eureka server
 * 
 * @author garvitpathak27
 */
@Configuration("customEurekaServerConfig")
public class EurekaServerConfig {

    /**
     * Custom info contributor for actuator info endpoint
     */
    @Bean
    public InfoContributor customInfoContributor() {
        return new InfoContributor() {
            @Override
            public void contribute(Info.Builder builder) {
                Map<String, Object> details = new HashMap<>();
                details.put("service", "Eureka Service Discovery Server");
                details.put("version", "1.0.0");
                details.put("author", "garvitpathak27");
                details.put("startup-time", LocalDateTime.now());
                details.put("description", "Service registry for Task Management Microservices");
                
                builder.withDetail("application", details);
            }
        };
    }
}