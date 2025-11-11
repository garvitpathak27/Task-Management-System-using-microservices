package in.garvit.tasks.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.response.ApiResponse;
import in.garvit.tasks.response.UserResponse;
import in.garvit.tasks.service.UserService;
import in.garvit.tasks.usermodel.User;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private static final String CIRCUIT_BREAKER_NAME = "userService";
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackForGetUserProfile")
	@GetMapping("/profile")
	public ResponseEntity<UserResponse> getUserProfile(@RequestHeader("Authorization") String jwt) throws UserException {
		log.debug("Fetching user profile using JWT");
		User user = userService.findUserProfileByJwt(jwt);
		return ResponseEntity.ok(UserResponse.from(user));
	}

	@SuppressWarnings("unused")
	private ResponseEntity<UserResponse> fallbackForGetUserProfile(String jwt, Throwable throwable) {
		log.error("Fallback triggered while fetching user profile", throwable);
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}

	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackForFindUserById")
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponse> findUserById(@PathVariable String userId,
			@RequestHeader("Authorization") String jwt) throws UserException {
		log.info("Fetching user with id {}", userId);
		User user = userService.findUserById(userId);
		return ResponseEntity.ok(UserResponse.from(user));
	}

	@SuppressWarnings("unused")
	private ResponseEntity<UserResponse> fallbackForFindUserById(String userId, String jwt, Throwable throwable) {
		log.error("Fallback triggered while fetching user {}", userId, throwable);
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
	}

	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackForFindAllUsers")
	@GetMapping
	public ResponseEntity<List<UserResponse>> findAllUsers(@RequestHeader("Authorization") String jwt) {
		log.info("Fetching all users");
		List<UserResponse> users = userService.findAllUsers().stream()
			.map(UserResponse::from)
			.collect(Collectors.toList());
		return ResponseEntity.ok(users);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<List<UserResponse>> fallbackForFindAllUsers(String jwt, Throwable throwable) {
		log.error("Fallback triggered while fetching all users", throwable);
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(List.of());
	}

	@GetMapping("/health")
	public ResponseEntity<ApiResponse> getUsersHealth() {
		ApiResponse response = new ApiResponse("User service is available", true);
		return ResponseEntity.ok(response);
	}
}
