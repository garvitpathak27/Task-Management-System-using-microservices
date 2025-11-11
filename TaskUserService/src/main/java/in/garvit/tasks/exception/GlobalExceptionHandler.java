package in.garvit.tasks.exception;

import in.garvit.tasks.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse> handleUserException(UserException ex) {
		log.warn("User exception: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ApiResponse(ex.getMessage(), false));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
			.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
			.findFirst()
			.orElse("Validation failed");
		log.warn("Validation error: {}", message);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ApiResponse(message, false));
	}

	@ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
	public ResponseEntity<ApiResponse> handleAuthenticationException(Exception ex) {
		log.warn("Authentication error: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ApiResponse("Authentication failed", false));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
		log.error("Unexpected error", ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ApiResponse("Unexpected error occurred", false));
	}
}
