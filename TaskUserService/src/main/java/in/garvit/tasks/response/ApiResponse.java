package in.garvit.tasks.response;

import java.time.Instant;

public class ApiResponse {
	private String message;
	private boolean status;
	private Instant timestamp = Instant.now();

	public ApiResponse() {
	}

	public ApiResponse(String message, boolean status) {
		this.message = message;
		this.status = status;
		this.timestamp = Instant.now();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}
}
