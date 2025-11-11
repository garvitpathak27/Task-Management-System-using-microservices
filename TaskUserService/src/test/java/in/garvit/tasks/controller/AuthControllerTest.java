package in.garvit.tasks.controller;

import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.request.LoginRequest;
import in.garvit.tasks.request.SignupRequest;
import in.garvit.tasks.response.AuthResponse;
import in.garvit.tasks.service.UserService;
import in.garvit.tasks.taskSecurityConfig.JwtProvider;
import in.garvit.tasks.usermodel.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtProvider jwtProvider;

	@InjectMocks
	private AuthController authController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void register_ReturnsToken() throws UserException {
		SignupRequest request = new SignupRequest("Jane Doe", "jane@example.com", "password123", "ROLE_USER", "1234567890");
		User createdUser = new User();
		createdUser.setEmail(request.getEmail());
		Authentication authentication = new UsernamePasswordAuthenticationToken(createdUser.getEmail(), request.getPassword());

		when(userService.register(any(SignupRequest.class))).thenReturn(createdUser);
		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(jwtProvider.generateToken(authentication)).thenReturn("jwt-token");

		ResponseEntity<AuthResponse> response = authController.register(request);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Register success", response.getBody().getMessage());
		assertEquals("jwt-token", response.getBody().getJwt());
	}

	@Test
	void signin_ReturnsToken() {
		LoginRequest request = new LoginRequest("jane@example.com", "password123");
		Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(jwtProvider.generateToken(authentication)).thenReturn("jwt-token");

		ResponseEntity<AuthResponse> response = authController.signin(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Login success", response.getBody().getMessage());
	}

	@Test
	void signin_InvalidCredentials_Propagates() {
		LoginRequest request = new LoginRequest("jane@example.com", "wrong-password");

		when(authenticationManager.authenticate(any()))
			.thenThrow(new BadCredentialsException("Invalid credentials"));

		assertThrows(BadCredentialsException.class, () -> authController.signin(request));
	}
}

