package in.garvit.tasks.taskModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Data Transfer Object used for interactions with the User Service.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String mobile;
}
