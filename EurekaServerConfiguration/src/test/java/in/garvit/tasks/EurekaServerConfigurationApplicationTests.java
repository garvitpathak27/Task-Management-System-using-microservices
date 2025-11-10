package in.garvit.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "management.zipkin.tracing.endpoint="
})
class EurekaServerConfigurationApplicationTests {

	@Test
	void contextLoads() {
		// Test that the Spring context loads successfully
		// This test ensures all beans are properly configured
	}

	@Test
	void applicationStartsSuccessfully() {
		// Test that the Eureka server starts without any configuration errors
		// The @SpringBootTest annotation ensures the full application context is loaded
	}
}
