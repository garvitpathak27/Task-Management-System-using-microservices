package in.garvit.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SubmissionStatusUpdateRequest(
		String status,
		String content) {
}
