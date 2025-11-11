package in.garvit.tasks.response;

import in.garvit.tasks.usermodel.User;
import java.util.List;
import java.util.Objects;

public record UserResponse(
	String id,
	String fullName,
	String email,
	String role,
	String mobile,
	List<Long> completedTasks
) {

	public static UserResponse from(User user) {
		Objects.requireNonNull(user, "user must not be null");
		List<Long> completed = user.getCompletedTasks() == null ? List.of() : List.copyOf(user.getCompletedTasks());
		return new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole(), user.getMobile(), completed);
	}
}
