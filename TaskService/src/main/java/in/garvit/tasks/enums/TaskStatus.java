package in.garvit.tasks.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Task Status Enumeration
 * 
 * Represents the various states a task can be in throughout its lifecycle.
 * 
 * @author garvitpathak27
 */
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TaskStatus {
	
	/**
	 * Task has been created but not yet assigned to any user
	 */
	PENDING("PENDING"),
	
	/**
	 * Task has been assigned to a user but not yet completed
	 */
	ASSIGNED("ASSIGNED"),
	
	/**
	 * Task has been completed
	 */
	DONE("DONE");

	private final String name;

	/**
	 * Constructor for TaskStatus enum
	 * @param name the string representation of the status
	 */
	TaskStatus(String name) {
		this.name = name;
	}

	/**
	 * Get the string representation of the status
	 * @return the status name
	 */
	public String getName() {
		return name;
	}
}
