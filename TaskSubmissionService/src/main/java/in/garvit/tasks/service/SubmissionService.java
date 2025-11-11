package in.garvit.tasks.service;

import java.util.List;

import in.garvit.tasks.dto.SubmissionStatusUpdateRequest;
import in.garvit.tasks.dto.TaskSubmissionRequest;
import in.garvit.tasks.submissionModel.TaskSubmission;

public interface SubmissionService {

	TaskSubmission submitTask(TaskSubmissionRequest request, String jwt);

	TaskSubmission getTaskSubmissionById(String submissionId);

	List<TaskSubmission> getAllTaskSubmissions();

	List<TaskSubmission> getTaskSubmissionByTaskId(String taskId);

	TaskSubmission updateSubmission(String submissionId,
			SubmissionStatusUpdateRequest updateRequest,
			String jwt);
}
