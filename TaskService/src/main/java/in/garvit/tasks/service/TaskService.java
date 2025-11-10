package in.garvit.tasks.service;

import java.util.List;

import in.garvit.tasks.enums.TaskStatus;
import in.garvit.tasks.taskModel.Task;

/**
 * Task Service Interface
 * 
 * Defines all operations related to task management including CRUD operations,
 * task assignment, filtering, and sorting.
 * 
 * @author garvitpathak27
 */
public interface TaskService {
	
	/**
	 * Create a new task (Admin only)
	 * 
	 * @param task the task to create
	 * @param requestRole the role of the requesting user
	 * @return the created task
	 * @throws Exception if user is not an admin
	 */
	Task create(Task task, String requestRole);
	
	/**
	 * Get a task by its ID
	 * 
	 * @param id the task ID
	 * @return the task if found
	 * @throws Exception if task is not found
	 */
	Task getTaskById(String id);
	
	/**
	 * Get all tasks with optional filtering and sorting
	 * 
	 * @param taskStatus filter by task status (optional)
	 * @param sortByDeadline sort by deadline - "asc" or "desc" (optional)
	 * @param sortByCreatedAt sort by creation date - "asc" or "desc" (optional)
	 * @return list of tasks
	 */
	List<Task> getAllTasks(TaskStatus taskStatus, String sortByDeadline, String sortByCreatedAt);
	
	/**
	 * Update an existing task
	 * 
	 * @param id the task ID
	 * @param updatedTask the task with updated information
	 * @param userId the ID of the user performing the update
	 * @return the updated task
	 * @throws Exception if task is not found
	 */
	Task updateTask(String id, Task updatedTask);
	
	/**
	 * Delete a task by ID
	 * 
	 * @param id the task ID
	 * @throws Exception if task is not found
	 */
	void deleteTask(String id);
	
	/**
	 * Assign a task to a specific user
	 * 
	 * @param userId the ID of the user to assign the task to
	 * @param id the task ID
	 * @return the updated task with assigned user
	 * @throws Exception if task or user is not found
	 */
	Task assignedToUser(String userId, String id);
	
	/**
	 * Get all tasks assigned to a specific user with optional filtering and sorting
	 * 
	 * @param userId the ID of the user
	 * @param taskStatus filter by task status (optional)
	 * @param sortByDeadline sort by deadline - "asc" or "desc" (optional)
	 * @param sortByCreatedAt sort by creation date - "asc" or "desc" (optional)
	 * @return list of tasks assigned to the user
	 */
	List<Task> assignedUsersTask(String userId, TaskStatus taskStatus, String sortByDeadline, String sortByCreatedAt);
	
	/**
	 * Mark a task as complete
	 * 
	 * @param taskId the task ID
	 * @return the updated task with DONE status
	 * @throws Exception if task is not found
	 */
	Task completeTask(String taskId);
}

