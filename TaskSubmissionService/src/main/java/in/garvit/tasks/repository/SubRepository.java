package in.garvit.tasks.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import in.garvit.tasks.submissionModel.TaskSubmission;

@Repository
public interface SubRepository extends MongoRepository<TaskSubmission, String> {

	List<TaskSubmission> findByTaskIdOrderBySubmittedAtDesc(String taskId);

	Optional<TaskSubmission> findByTaskIdAndUserId(String taskId, String userId);
}
