package in.garvit.tasks.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.garvit.tasks.dto.TaskSubmissionResponse;
import in.garvit.tasks.service.SubmissionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HomeController {

	private final SubmissionService submissionService;

	@GetMapping("/submissions")
	public ResponseEntity<List<TaskSubmissionResponse>> listSubmissions() {
		List<TaskSubmissionResponse> submissions = submissionService.getAllTaskSubmissions().stream()
			.map(TaskSubmissionResponse::from)
			.toList();
		return ResponseEntity.ok(submissions);
	}
}
