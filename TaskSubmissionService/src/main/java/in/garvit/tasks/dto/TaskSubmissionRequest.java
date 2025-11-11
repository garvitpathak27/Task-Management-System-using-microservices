package in.garvit.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskSubmissionRequest(
		String taskId,
		String content) {
}
