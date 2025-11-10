package in.garvit.tasks.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import in.garvit.tasks.taskModel.Task;

import java.util.List;

/**
 * Task Repository Interface
 * 
 * Spring Data MongoDB repository for Task entity.
 * Provides standard CRUD operations and custom query methods for task persistence.
 * 
 * @author garvitpathak27
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, String> {
	
	/**
	 * Find all tasks assigned to a specific user
	 * 
	 * Custom query method that searches tasks by assigned user ID.
	 * Spring Data MongoDB automatically implements this method based on the method name.
	 * 
	 * @param userId the ID of the user whose tasks to retrieve
	 * @return list of tasks assigned to the specified user
	 */
	List<Task> findByAssignedUserId(String userId);
}

