package in.garvit.tasks.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import in.garvit.tasks.enums.TaskStatus;
import in.garvit.tasks.exception.TaskNotFoundException;
import in.garvit.tasks.exception.UnauthorizedActionException;
import in.garvit.tasks.repository.TaskRepository;
import in.garvit.tasks.taskModel.Task;

@Service
public class TaskServiceImplementation implements TaskService {

	private final TaskRepository taskRepository;

	public TaskServiceImplementation(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public Task create(Task task, String requestRole) {
		if (!"ROLE_ADMIN".equalsIgnoreCase(requestRole)) {
			throw new UnauthorizedActionException("Only administrators can create tasks");
		}

		if (!StringUtils.hasText(task.getTitle())) {
			throw new IllegalArgumentException("Task title is required");
		}

		if (task.getTags() == null) {
			task.setTags(new ArrayList<>());
		}

		task.setStatus(TaskStatus.PENDING);
		task.setCreateAt(LocalDateTime.now());

		return taskRepository.save(task);
	}

	@Override
	public Task getTaskById(String id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
	}

	@Override
	public List<Task> getAllTasks(TaskStatus taskStatus, String sortByDeadline, String sortByCreatedAt) {
		List<Task> allTasks = taskRepository.findAll();
		return filterAndSortTasks(allTasks, taskStatus, sortByDeadline, sortByCreatedAt);
	}

	@Override
	public Task updateTask(String id, Task updatedTask) {
		Task existingTask = getTaskById(id);
		
		if (updatedTask.getTitle() != null) {
			existingTask.setTitle(updatedTask.getTitle());
		}
		if (updatedTask.getImageUrl() != null) {
			existingTask.setImageUrl(updatedTask.getImageUrl());
		}
		if (updatedTask.getDescription() != null) {
			existingTask.setDescription(updatedTask.getDescription());
		}
		if (updatedTask.getStatus() != null) {
			existingTask.setStatus(updatedTask.getStatus());
		}
		if (updatedTask.getDeadline() != null) {
			existingTask.setDeadline(updatedTask.getDeadline());
		}
		if (updatedTask.getTags() != null) {
			existingTask.setTags(updatedTask.getTags());
		}

		return taskRepository.save(existingTask);
	}

	@Override
	public void deleteTask(String id) {
		Task task = getTaskById(id);
		taskRepository.delete(task);
	}

	@Override
	public Task assignedToUser(String userId, String taskId) {
		Task task = getTaskById(taskId);
		task.setAssignedUserId(userId);
		task.setStatus(TaskStatus.ASSIGNED);

		return taskRepository.save(task);
	}

	@Override
	public List<Task> assignedUsersTask(String userId, TaskStatus status, String sortByDeadline, String sortByCreatedAt) {
		List<Task> allTasks = taskRepository.findByAssignedUserId(userId);
		return filterAndSortTasks(allTasks, status, sortByDeadline, sortByCreatedAt);
	}

	@Override
	public Task completeTask(String taskId) {
		Task task = getTaskById(taskId);
		task.setStatus(TaskStatus.DONE);
		return taskRepository.save(task);
	}

	private List<Task> filterAndSortTasks(List<Task> tasks, TaskStatus status, String sortByDeadline, String sortByCreatedAt) {
		List<Task> filteredTasks = tasks.stream()
				.filter(task -> status == null || task.getStatus() == status)
				.collect(Collectors.toList());

		Comparator<Task> comparator = resolveComparator(sortByDeadline, sortByCreatedAt);
		if (comparator != null) {
			filteredTasks.sort(comparator);
		}
		return filteredTasks;
	}

	private Comparator<Task> resolveComparator(String sortByDeadline, String sortByCreatedAt) {
		if (StringUtils.hasText(sortByDeadline)) {
			boolean ascending = sortByDeadline.equalsIgnoreCase("asc");
			Comparator<LocalDateTime> base = ascending ? Comparator.naturalOrder() : Comparator.reverseOrder();
			return Comparator.comparing(Task::getDeadline, Comparator.nullsLast(base));
		}

		if (StringUtils.hasText(sortByCreatedAt)) {
			boolean ascending = sortByCreatedAt.equalsIgnoreCase("asc");
			Comparator<LocalDateTime> base = ascending ? Comparator.naturalOrder() : Comparator.reverseOrder();
			return Comparator.comparing(Task::getCreateAt, Comparator.nullsLast(base));
		}

		return null;
	}
}
