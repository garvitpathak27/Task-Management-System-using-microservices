package in.garvit.tasks.service;

import java.util.List;

import in.garvit.tasks.enums.TaskStatus;
import in.garvit.tasks.taskModel.Task;

public interface TaskService {
	Task create(Task task, String requestRole) throws Exception;
	
	Task getTaskById(String id) throws Exception;
	
	List<Task> getAllTasks(TaskStatus taskStatus, String sortByDeadline, String sortByCreatedAt);
	
	Task updateTask(String id, Task updatedTask, String userId) throws Exception;
	
	void deleteTask(String id) throws Exception;
	
	Task assignedToUser(String userId, String id) throws Exception;
	
	List<Task> assignedUsersTask(String userId, TaskStatus taskStatus, String sortByDeadline, String sortByCreatedAt);
	
	Task completeTask(String taskId) throws Exception;
	
	

}
