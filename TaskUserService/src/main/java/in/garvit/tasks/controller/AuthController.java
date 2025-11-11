package in.garvit.tasks.controller;

import in.garvit.tasks.exception.UserException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import in.garvit.tasks.request.LoginRequest;
import in.garvit.tasks.request.SignupRequest;
import in.garvit.tasks.response.AuthResponse;
import in.garvit.tasks.service.UserService;
import in.garvit.tasks.taskSecurityConfig.JwtProvider;
import in.garvit.tasks.usermodel.User;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private static final String CIRCUIT_BREAKER_NAME = "userService";
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);

	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;

	public AuthController(UserService userService,
		AuthenticationManager authenticationManager,
		JwtProvider jwtProvider) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
	}

	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "createUserFallback")
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest request) throws UserException {
		log.info("Processing signup request for email {}", request.getEmail());
		String rawPassword = request.getPassword();
		User createdUser = userService.register(request);
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(createdUser.getEmail(), rawPassword));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse(token, "Register success", true);
		return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<AuthResponse> createUserFallback(SignupRequest request, Throwable throwable) {
		log.error("Signup fallback triggered for email {}", request.getEmail(), throwable);
		AuthResponse authResponse = new AuthResponse(null,
			"User registration failed due to a temporary issue.", false);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authResponse);
	}

	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "signinFallback")
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("Processing signin request for email {}", loginRequest.getEmail());
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse(token, "Login success", true);
		return ResponseEntity.ok(authResponse);
	}

	@SuppressWarnings("unused")
	private ResponseEntity<AuthResponse> signinFallback(LoginRequest loginRequest, Throwable throwable) {
		log.error("Signin fallback triggered for email {}", loginRequest.getEmail(), throwable);
		AuthResponse authResponse = new AuthResponse(null,
			"Login failed due to a temporary issue.", false);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(authResponse);
	}
}
