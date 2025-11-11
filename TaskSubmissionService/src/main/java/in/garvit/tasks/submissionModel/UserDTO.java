package in.garvit.tasks.submissionModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

	private String id;
	private String fullName;
	private String email;
	private String role;
	private String mobile;
}
