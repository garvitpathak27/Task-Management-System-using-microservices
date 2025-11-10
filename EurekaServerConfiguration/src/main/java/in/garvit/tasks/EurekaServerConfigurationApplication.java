package in.garvit.tasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * Eureka Server Configuration Application
 * 
 * This microservice acts as the service registry for the Task Management System.
 * It provides service discovery capabilities for all microservices in the system.
 * 
 * Features:
 * - Service Registration and Discovery
 * - Health Monitoring
 * - Load Balancing Support
 * - Circuit Breaker Integration
 * - Distributed Tracing with Zipkin
 * 
 * @author garvitpathak27
 * @version 1.0.0
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerConfigurationApplication {

	@PostConstruct
	public void init() {
		// Set default timezone to UTC for consistency across all microservices
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Eureka Server starting with UTC timezone...");
	}

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(EurekaServerConfigurationApplication.class);
		
		// Set system properties for better performance
		System.setProperty("spring.output.ansi.enabled", "always");
		System.setProperty("logging.pattern.console", "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(${LOG_LEVEL_PATTERN:-%5p}) %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n${LOG_EXCEPTION_CONVERSION_WORD:-%wEx}");
		
		// Add shutdown hook for graceful shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("Eureka Server is shutting down gracefully...");
		}));
		
		application.run(args);
	}
}
