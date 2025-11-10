package in.garvit.tasks.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home Controller for Task Service
 * Provides basic health check and welcome endpoints
 */
@RestController
public class HomeController {

	/**
	 * Welcome endpoint for Task Service
	 * @return welcome message
	 */
	@GetMapping("/tasks")
	public ResponseEntity<String> home() {
		return new ResponseEntity<>("Welcome to Task Service", HttpStatus.OK);
	}
}
