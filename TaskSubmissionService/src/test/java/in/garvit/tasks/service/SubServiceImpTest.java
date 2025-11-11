package in.garvit.tasks.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import in.garvit.tasks.dto.SubmissionStatusUpdateRequest;
import in.garvit.tasks.dto.TaskSubmissionRequest;
import in.garvit.tasks.exception.DuplicateSubmissionException;
import in.garvit.tasks.exception.ResourceNotFoundException;
import in.garvit.tasks.exception.UnauthorizedSubmissionActionException;
import in.garvit.tasks.repository.SubRepository;
import in.garvit.tasks.submissionModel.TaskDTO;
import in.garvit.tasks.submissionModel.TaskSubmission;
import in.garvit.tasks.submissionModel.UserDTO;
import in.garvit.tasks.submissionModel.enums.TaskStatus;

@SuppressWarnings("null")
class SubServiceImpTest {

	@Mock
	private SubRepository subRepository;

	@Mock
	private TaskService taskService;

	@Mock
	private UserService userService;

	@InjectMocks
	private SubServiceImp subServiceImp;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void submitTask_whenValidRequest_savesSubmission() {
		String jwt = "token";
		TaskSubmissionRequest request = new TaskSubmissionRequest("task-1", "https://github.com/example/repo");
		UserDTO user = new UserDTO("user-1", "Test User", "test@example.com", "ROLE_USER", "9876543210");
		TaskDTO task = new TaskDTO("task-1", "Task", "desc", null, "user-1", TaskStatus.ASSIGNED,
				LocalDateTime.now().plusDays(1), LocalDateTime.now(), null);
		TaskSubmission saved = TaskSubmission.builder()
				.id("submission-1")
				.taskId("task-1")
				.userId("user-1")
				.content("https://github.com/example/repo")
				.build();

		given(userService.getUserProfileHandler(jwt)).willReturn(user);
		given(taskService.getTaskById(request.taskId(), jwt)).willReturn(task);
		given(subRepository.findByTaskIdAndUserId(request.taskId(), user.getId())).willReturn(Optional.empty());
		given(subRepository.save(any(TaskSubmission.class))).willReturn(saved);

		TaskSubmission result = subServiceImp.submitTask(request, jwt);

		assertEquals(saved.getId(), result.getId());
		verify(subRepository).save(any(TaskSubmission.class));
	}

	@Test
	void submitTask_whenDuplicateSubmission_throwsConflict() {
		String jwt = "token";
		TaskSubmissionRequest request = new TaskSubmissionRequest("task-1", "https://github.com/example/repo");
		UserDTO user = new UserDTO("user-1", "Test User", "test@example.com", "ROLE_USER", "9876543210");
		TaskDTO task = new TaskDTO("task-1", "Task", "desc", null, "user-1", TaskStatus.ASSIGNED,
				LocalDateTime.now().plusDays(1), LocalDateTime.now(), null);

		given(userService.getUserProfileHandler(jwt)).willReturn(user);
		given(taskService.getTaskById(request.taskId(), jwt)).willReturn(task);
		given(subRepository.findByTaskIdAndUserId(request.taskId(), user.getId()))
			.willReturn(Optional.of(new TaskSubmission()));

		assertThrows(DuplicateSubmissionException.class, () -> subServiceImp.submitTask(request, jwt));
	}

	@Test
	void updateSubmission_whenNotOwnerOrAdmin_throwsUnauthorized() {
		String jwt = "token";
		SubmissionStatusUpdateRequest updateRequest = new SubmissionStatusUpdateRequest("APPROVED", null);
		TaskSubmission submission = TaskSubmission.builder()
				.id("submission-1")
				.taskId("task-1")
				.userId("other-user")
				.content("https://github.com/example/repo")
				.build();
		UserDTO requester = new UserDTO("user-1", "Test User", "test@example.com", "ROLE_USER", "9876543210");

		given(subRepository.findById(submission.getId())).willReturn(Optional.of(submission));
		given(userService.getUserProfileHandler(jwt)).willReturn(requester);

		assertThrows(UnauthorizedSubmissionActionException.class,
				() -> subServiceImp.updateSubmission(submission.getId(), updateRequest, jwt));
	}

	@Test
	void updateSubmission_whenNotFound_throwsResourceNotFound() {
		String jwt = "token";
		SubmissionStatusUpdateRequest updateRequest = new SubmissionStatusUpdateRequest("APPROVED", null);

		given(subRepository.findById("missing")).willReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class,
				() -> subServiceImp.updateSubmission("missing", updateRequest, jwt));
	}

	@Test
	void submitTask_whenTaskNotFound_throwsResourceNotFound() {
		String jwt = "token";
		TaskSubmissionRequest request = new TaskSubmissionRequest("task-404", "https://github.com/example/repo");
		UserDTO user = new UserDTO("user-1", "Test User", "test@example.com", "ROLE_USER", "9876543210");

		given(userService.getUserProfileHandler(jwt)).willReturn(user);
		given(taskService.getTaskById(eq("task-404"), eq(jwt))).willThrow(createFeignNotFound());

		assertThrows(ResourceNotFoundException.class, () -> subServiceImp.submitTask(request, jwt));
	}

	private FeignException.NotFound createFeignNotFound() {
		Request request = Request.create(Request.HttpMethod.GET, "/api/tasks/404", java.util.Collections.emptyMap(), null,
				new RequestTemplate());
		return new FeignException.NotFound("not found", request, null, null);
	}
}
