package in.garvit.tasks.exception;

/**
 * Exception thrown when a user attempts to perform an action they are not authorized for.
 */
public class UnauthorizedActionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Create a new UnauthorizedActionException with the provided message.
	 *
	 * @param message description of the unauthorized action
	 */
	public UnauthorizedActionException(String message) {
		super(message);
	}
}
