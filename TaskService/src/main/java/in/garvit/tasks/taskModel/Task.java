package in.garvit.tasks.taskModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import in.garvit.tasks.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Task Entity
 * 
 * Represents a task in the Task Management System.
 * This entity is persisted in MongoDB with collection name "Tasks".
 * 
 * @author garvitpathak27
 */
@Data
@Document(collection = "Tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
	
	/**
	 * Unique identifier for the task (MongoDB ObjectId)
	 */
	@Id
	private String id;
	
	/**
	 * Title of the task
	 */
	private String title;
	
	/**
	 * Detailed description of the task
	 */
	private String description;
	
	/**
	 * URL to an image associated with the task (optional)
	 */
	private String imageUrl;
	
	/**
	 * ID of the user to whom the task is assigned (optional)
	 */
	private String assignedUserId;
	
	/**
	 * Current status of the task (PENDING, ASSIGNED, or DONE)
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private TaskStatus status;
	
	/**
	 * Deadline for task completion
	 */
	private LocalDateTime deadline;
	
	/**
	 * Timestamp when the task was created
	 */
	private LocalDateTime createAt;
	
	/**
	 * List of tags associated with the task for categorization
	 */
	private List<String> tags = new ArrayList<>();
}

