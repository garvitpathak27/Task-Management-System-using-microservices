package in.garvit.tasks.controller;

import in.garvit.tasks.enums.TaskStatus;
import in.garvit.tasks.exception.UnauthorizedActionException;
import in.garvit.tasks.service.TaskService;
import in.garvit.tasks.service.UserService;
import in.garvit.tasks.taskModel.Task;
import in.garvit.tasks.taskModel.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Task Management
 * Handles all task-related operations including CRUD operations
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

	private final TaskService taskService;
	private final UserService userService;

	@Autowired
	public TaskController(TaskService taskService, UserService userService) {
		this.taskService = taskService;
		this.userService = userService;
	}

	/**
	 * Create a new task (Admin only)
	 * @param task the task to create
	 * @param jwt authorization token
	 * @return created task
	 * @throws Exception if JWT is missing or user is not admin
	 */
	@PostMapping
	public ResponseEntity<Task> createTask(@RequestBody Task task, @RequestHeader("Authorization") String jwt) {
		UserDTO user = userService.getUserProfileHandler(jwt);
		ensureAdmin(user);
		Task createdTask = taskService.create(task, user.getRole());
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}

	/**
	 * Get a task by ID
	 * @param id task ID
	 * @param jwt authorization token
	 * @return task if found
	 * @throws Exception if JWT is missing or task not found
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable String id, @RequestHeader("Authorization") String jwt) {
		userService.getUserProfileHandler(jwt);
		Task task = taskService.getTaskById(id);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	/**
	 * Get all tasks assigned to the authenticated user
	 * @param jwt authorization token
	 * @param status filter by task status (optional)
	 * @param sortByDeadline sort by deadline - "asc" or "desc" (optional)
	 * @param sortByCreatedAt sort by creation date - "asc" or "desc" (optional)
	 * @return list of assigned tasks
	 * @throws Exception if JWT is missing
	 */
	@GetMapping("/user")
	public ResponseEntity<List<Task>> getAssignedUsersTask(
			@RequestHeader("Authorization") String jwt,
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) String sortByDeadline,
			@RequestParam(required = false) String sortByCreatedAt) throws Exception {
		UserDTO user = userService.getUserProfileHandler(jwt);
		List<Task> tasks = taskService.assignedUsersTask(user.getId(), status, sortByDeadline, sortByCreatedAt);
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}

	/**
	 * Get all tasks with optional filtering and sorting
	 * @param jwt authorization token
	 * @param status filter by task status (optional)
	 * @param sortByDeadline sort by deadline - "asc" or "desc" (optional)
	 * @param sortByCreatedAt sort by creation date - "asc" or "desc" (optional)
	 * @return list of all tasks
	 * @throws Exception if JWT is missing
	 */
	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks(
			@RequestHeader("Authorization") String jwt,
			@RequestParam(required = false) TaskStatus status,
			@RequestParam(required = false) String sortByDeadline,
			@RequestParam(required = false) String sortByCreatedAt) {
		userService.getUserProfileHandler(jwt);
		List<Task> tasks = taskService.getAllTasks(status, sortByDeadline, sortByCreatedAt);
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}

	/**
	 * Assign a task to a specific user
	 * @param id task ID
	 * @param userId user ID to assign the task to
	 * @param jwt authorization token
	 * @return updated task
	 * @throws Exception if JWT is missing or task/user not found
	 */
	@PutMapping("/{id}/user/{userId}/assigned")
	public ResponseEntity<Task> assignedTaskToUser(
			@PathVariable String id,
			@PathVariable String userId,
			@RequestHeader("Authorization") String jwt) {
		UserDTO requester = userService.getUserProfileHandler(jwt);
		ensureAdmin(requester);
		Task task = taskService.assignedToUser(userId, id);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	/**
	 * Update an existing task
	 * @param id task ID
	 * @param req task update request
	 * @param jwt authorization token
	 * @return updated task
	 * @throws Exception if JWT is missing or task not found
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(
			@PathVariable String id,
			@RequestBody Task req,
			@RequestHeader("Authorization") String jwt) {
		UserDTO requester = userService.getUserProfileHandler(jwt);
		Task existingTask = taskService.getTaskById(id);
		if (!isAdmin(requester) && !requester.getId().equals(existingTask.getAssignedUserId())) {
			throw new UnauthorizedActionException("Only the assigned user or an administrator can update this task");
		}
		Task task = taskService.updateTask(id, req);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	/**
	 * Delete a task by ID
	 * @param id task ID
	 * @return no content response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable String id, @RequestHeader("Authorization") String jwt) {
		UserDTO requester = userService.getUserProfileHandler(jwt);
		ensureAdmin(requester);
		taskService.deleteTask(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	/**
	 * Mark a task as complete
	 * @param id task ID
	 * @return updated task with DONE status
	 * @throws Exception if task not found
	 */
	@PutMapping("/{id}/complete")
	public ResponseEntity<Task> completeTask(@PathVariable String id, @RequestHeader("Authorization") String jwt) {
		UserDTO requester = userService.getUserProfileHandler(jwt);
		Task existingTask = taskService.getTaskById(id);
		if (!isAdmin(requester) && !requester.getId().equals(existingTask.getAssignedUserId())) {
			throw new UnauthorizedActionException("Only the assigned user or an administrator can complete this task");
		}
		Task task = taskService.completeTask(id);
		return new ResponseEntity<>(task, HttpStatus.OK);
	}

	private void ensureAdmin(UserDTO user) {
		if (!isAdmin(user)) {
			throw new UnauthorizedActionException("Administrator privileges are required to perform this action");
		}
	}

	private boolean isAdmin(UserDTO user) {
		return user != null && "ROLE_ADMIN".equalsIgnoreCase(user.getRole());
	}
}
