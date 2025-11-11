package in.garvit.tasks.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.garvit.tasks.submissionModel.TaskSubmission;

public record TaskSubmissionResponse(
		String id,
		String taskId,
		String userId,
		String status,
		String content,
		@JsonProperty("createdAt") LocalDateTime submittedAt,
		@JsonProperty("updatedAt") LocalDateTime updatedAt) {

	public static TaskSubmissionResponse from(TaskSubmission submission) {
		return new TaskSubmissionResponse(
				submission.getId(),
				submission.getTaskId(),
				submission.getUserId(),
				submission.getStatus().name().toLowerCase(),
				submission.getContent(),
				submission.getSubmittedAt(),
				submission.getUpdatedAt());
	}
}
