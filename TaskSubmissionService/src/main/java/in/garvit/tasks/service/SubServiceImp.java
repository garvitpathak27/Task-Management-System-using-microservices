package in.garvit.tasks.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import feign.FeignException;
import in.garvit.tasks.dto.SubmissionStatusUpdateRequest;
import in.garvit.tasks.dto.TaskSubmissionRequest;
import in.garvit.tasks.exception.DuplicateSubmissionException;
import in.garvit.tasks.exception.InvalidSubmissionStatusException;
import in.garvit.tasks.exception.ResourceNotFoundException;
import in.garvit.tasks.exception.UnauthorizedSubmissionActionException;
import in.garvit.tasks.repository.SubRepository;
import in.garvit.tasks.submissionModel.TaskDTO;
import in.garvit.tasks.submissionModel.TaskSubmission;
import in.garvit.tasks.submissionModel.UserDTO;
import in.garvit.tasks.submissionModel.enums.SubmissionStatus;
import in.garvit.tasks.submissionModel.enums.TaskStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubServiceImp implements SubmissionService {

	private static final Logger log = LoggerFactory.getLogger(SubServiceImp.class);

	private final SubRepository subRepository;
	private final TaskService taskService;
	private final UserService userService;

	@Override
	@SuppressWarnings("null")
	public TaskSubmission submitTask(TaskSubmissionRequest request, String jwt) {
		Objects.requireNonNull(request, "Request payload must not be null");
		Objects.requireNonNull(jwt, "Authorization token must not be null");
		validateSubmissionRequest(request);
		UserDTO requester = userService.getUserProfileHandler(jwt);
		TaskDTO task = fetchTask(request.taskId(), jwt);

		if (task.getAssignedUserId() != null && !task.getAssignedUserId().equals(requester.getId())) {
			throw new UnauthorizedSubmissionActionException("Only the assigned user can submit work for this task");
		}

		if (TaskStatus.DONE.equals(task.getStatus())) {
			throw new InvalidSubmissionStatusException("Task is already completed; submissions are closed");
		}

		subRepository.findByTaskIdAndUserId(request.taskId(), requester.getId())
			.ifPresent(existing -> {
				throw new DuplicateSubmissionException("A submission already exists for this task and user");
			});

		TaskSubmission submission = TaskSubmission.builder()
				.taskId(request.taskId())
				.userId(requester.getId())
				.content(request.content().trim())
				.build();

		log.info("Creating submission for task {} by user {}", request.taskId(), requester.getId());
		TaskSubmission saved = subRepository.save(submission);
		return Objects.requireNonNull(saved, "Submission persistence returned null");
	}

	@Override
	@SuppressWarnings("null")
	public TaskSubmission getTaskSubmissionById(String submissionId) {
		Objects.requireNonNull(submissionId, "Submission id must not be null");
		return subRepository.findById(submissionId)
			.orElseThrow(() -> new ResourceNotFoundException("Submission not found: " + submissionId));
	}

	@Override
	public List<TaskSubmission> getAllTaskSubmissions() {
		return subRepository.findAll().stream()
			.sorted(Comparator.comparing(TaskSubmission::getSubmittedAt).reversed())
			.toList();
	}

	@Override
	public List<TaskSubmission> getTaskSubmissionByTaskId(String taskId) {
		return subRepository.findByTaskIdOrderBySubmittedAtDesc(taskId);
	}

	@Override
	public TaskSubmission updateSubmission(String submissionId,
			SubmissionStatusUpdateRequest updateRequest,
			String jwt) {
		Objects.requireNonNull(updateRequest, "Update request must not be null");
		Objects.requireNonNull(jwt, "Authorization token must not be null");
		validateStatusUpdate(updateRequest);
		TaskSubmission submission = getTaskSubmissionById(submissionId);
		UserDTO requester = userService.getUserProfileHandler(jwt);
		SubmissionStatus newStatus = parseStatus(updateRequest.status());

		boolean isOwner = submission.getUserId().equals(requester.getId());
		boolean isAdmin = requester.getRole() != null && requester.getRole().equalsIgnoreCase("ROLE_ADMIN");

		if (!isAdmin && !isOwner) {
			throw new UnauthorizedSubmissionActionException("You do not have permission to update this submission");
		}

		if (!isAdmin && newStatus != SubmissionStatus.PENDING) {
			throw new UnauthorizedSubmissionActionException("Only administrators can approve or reject submissions");
		}

		if (updateRequest.content() != null && !updateRequest.content().isBlank()) {
			submission.refreshContent(updateRequest.content().trim());
		}

		submission.markStatus(newStatus);

		TaskSubmission saved = subRepository.save(submission);

		if (newStatus == SubmissionStatus.APPROVED) {
			completeTask(submission.getTaskId(), jwt);
		}

		return saved;
	}

	private SubmissionStatus parseStatus(String raw) {
		try {
			return SubmissionStatus.valueOf(raw.trim().toUpperCase(Locale.ROOT));
		} catch (Exception ex) {
			throw new InvalidSubmissionStatusException("Unsupported submission status: " + raw);
		}
	}

	private TaskDTO fetchTask(String taskId, String jwt) {
		try {
			return taskService.getTaskById(taskId, jwt);
		} catch (FeignException.NotFound ex) {
			throw new ResourceNotFoundException("Task not found: " + taskId);
		} catch (FeignException ex) {
			log.error("Failed to fetch task {}: {}", taskId, ex.getMessage());
			throw new InvalidSubmissionStatusException("Unable to retrieve task details at this time");
		}
	}

	private void completeTask(String taskId, String jwt) {
		try {
			taskService.completeTask(taskId, jwt);
		} catch (FeignException ex) {
			log.warn("Failed to mark task {} as complete after submission approval: {}", taskId, ex.getMessage());
		}
	}

	private void validateSubmissionRequest(TaskSubmissionRequest request) {
		if (request.taskId() == null || request.taskId().isBlank()) {
			throw new IllegalArgumentException("taskId is required");
		}
		if (request.content() == null || request.content().isBlank()) {
			throw new IllegalArgumentException("content is required");
		}
	}

	private void validateStatusUpdate(SubmissionStatusUpdateRequest updateRequest) {
		if (updateRequest.status() == null || updateRequest.status().isBlank()) {
			throw new IllegalArgumentException("status is required");
		}
	}
}
