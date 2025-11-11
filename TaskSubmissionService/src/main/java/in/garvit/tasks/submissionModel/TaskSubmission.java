package in.garvit.tasks.submissionModel;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import in.garvit.tasks.submissionModel.enums.SubmissionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "taskSubmission")
@CompoundIndex(def = "{'taskId': 1, 'userId': 1}")
public class TaskSubmission {

	@Id
	private String id;

	@Indexed
	private String taskId;

	@Indexed
	private String userId;

	@Builder.Default
	private SubmissionStatus status = SubmissionStatus.PENDING;

	private String content;

	@Builder.Default
	private LocalDateTime submittedAt = LocalDateTime.now();

	@LastModifiedDate
	private LocalDateTime updatedAt;

	public void markStatus(SubmissionStatus newStatus) {
		this.status = newStatus;
		this.updatedAt = LocalDateTime.now();
	}

	public void refreshContent(String newContent) {
		this.content = newContent;
		this.updatedAt = LocalDateTime.now();
	}
}
