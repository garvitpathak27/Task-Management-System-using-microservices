package in.garvit.tasks.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.garvit.tasks.dto.SubmissionStatusUpdateRequest;
import in.garvit.tasks.dto.TaskSubmissionRequest;
import in.garvit.tasks.dto.TaskSubmissionResponse;
import in.garvit.tasks.service.SubmissionService;
import in.garvit.tasks.submissionModel.TaskSubmission;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/submissions")
@RequiredArgsConstructor
public class SubController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseEntity<TaskSubmissionResponse> createSubmission(
            @RequestBody TaskSubmissionRequest request,
            @RequestHeader("Authorization") String jwt) {
        TaskSubmission created = submissionService.submitTask(request, jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskSubmissionResponse.from(created));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<TaskSubmissionResponse> getSubmission(@PathVariable String submissionId) {
        TaskSubmission submission = submissionService.getTaskSubmissionById(submissionId);
        return ResponseEntity.ok(TaskSubmissionResponse.from(submission));
    }

    @GetMapping
    public ResponseEntity<List<TaskSubmissionResponse>> getAllSubmissions() {
        List<TaskSubmissionResponse> submissions = submissionService.getAllTaskSubmissions().stream()
            .map(TaskSubmissionResponse::from)
            .toList();
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskSubmissionResponse>> getTaskSubmissions(@PathVariable String taskId) {
        List<TaskSubmissionResponse> submissions = submissionService.getTaskSubmissionByTaskId(taskId).stream()
            .map(TaskSubmissionResponse::from)
            .toList();
        return ResponseEntity.ok(submissions);
    }

    @PutMapping("/{submissionId}")
    public ResponseEntity<TaskSubmissionResponse> updateSubmission(
            @PathVariable String submissionId,
            @RequestBody SubmissionStatusUpdateRequest updateRequest,
            @RequestHeader("Authorization") String jwt) {
        TaskSubmission updated = submissionService.updateSubmission(submissionId, updateRequest, jwt);
        return ResponseEntity.ok(TaskSubmissionResponse.from(updated));
    }
}

