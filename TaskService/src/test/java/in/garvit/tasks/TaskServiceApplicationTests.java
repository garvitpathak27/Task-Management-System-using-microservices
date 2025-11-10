package in.garvit.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for Task Service Application
 * 
 * This test class verifies that the Spring application context loads successfully.
 * It serves as a smoke test to ensure basic application configuration is correct.
 * 
 * @author garvitpathak27
 */
@SpringBootTest
@ActiveProfiles("test")
class TaskServiceApplicationTests {

	/**
	 * Test that the application context loads without errors
	 * This verifies that all beans are properly configured and dependencies are satisfied
	 */
	@Test
	void contextLoads() {
		// This test will fail if the application context cannot be loaded
		// It validates:
		// - All @Component, @Service, @Repository, @Controller beans are properly configured
		// - All dependencies can be resolved
		// - Configuration properties are valid
	}
}

