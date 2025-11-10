package in.garvit.tasks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for Task Service
 * Handles all exceptions thrown by controllers and services
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handle generic exceptions
	 * @param ex the exception
	 * @param request the web request
	 * @return error response
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleGlobalException(
			Exception ex, WebRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("error", "Internal Server Error");

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Handle TaskNotFoundException
	 * @param ex the exception
	 * @param request the web request
	 * @return error response
	 */
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleTaskNotFoundException(
			TaskNotFoundException ex, WebRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("error", "Task Not Found");

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

	/**
	 * Handle IllegalArgumentException
	 * @param ex the exception
	 * @param request the web request
	 * @return error response
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
			IllegalArgumentException ex, WebRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Bad Request");

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle UnauthorizedActionException
	 * @param ex the exception
	 * @param request the web request
	 * @return error response
	 */
	@ExceptionHandler(UnauthorizedActionException.class)
	public ResponseEntity<Map<String, Object>> handleUnauthorizedActionException(
			UnauthorizedActionException ex, WebRequest request) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.FORBIDDEN.value());
		body.put("error", "Forbidden");

		return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
	}
}
