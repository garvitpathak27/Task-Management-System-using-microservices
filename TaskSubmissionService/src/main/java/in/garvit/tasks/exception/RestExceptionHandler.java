package in.garvit.tasks.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import in.garvit.tasks.exception.model.ErrorResponse;

@RestControllerAdvice
public class RestExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
		return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(DuplicateSubmissionException.class)
	public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateSubmissionException ex) {
		return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
	}

	@ExceptionHandler(InvalidSubmissionStatusException.class)
	public ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidSubmissionStatusException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
	public ResponseEntity<ErrorResponse> handleValidation(Exception ex) {
		String message = "Validation failed";
		if (ex instanceof MethodArgumentNotValidException manv) {
			var fieldError = manv.getBindingResult().getFieldError();
			if (fieldError != null) {
				message = fieldError.getDefaultMessage();
			}
		}
		return buildResponse(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
		ErrorResponse body = new ErrorResponse(status.value(), message, LocalDateTime.now());
		return ResponseEntity.status(status).body(body);
	}
}
