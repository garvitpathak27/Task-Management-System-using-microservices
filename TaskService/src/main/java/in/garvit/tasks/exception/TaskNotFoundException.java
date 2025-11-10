package in.garvit.tasks.exception;

/**
 * Custom exception thrown when a task is not found
 */
public class TaskNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with message
	 * @param message the exception message
	 */
	public TaskNotFoundException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and cause
	 * @param message the exception message
	 * @param cause the cause of the exception
	 */
	public TaskNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
