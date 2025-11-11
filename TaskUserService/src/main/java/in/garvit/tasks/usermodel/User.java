package in.garvit.tasks.usermodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class User {

	@Id
	private String id;
	private String fullName;
	private String email;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private String role = "ROLE_CUSTOMER";
	private String mobile;
	private List<Long> completedTasks = new ArrayList<>();

	public User() {
		// Default constructor
	}

	public User(String id, String fullName, String email, String password, String role, String mobile,
		List<Long> completedTasks) {
		this.id = id;
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.role = role != null ? role : this.role;
		this.mobile = mobile;
		this.completedTasks = completedTasks != null ? new ArrayList<>(completedTasks) : new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role != null ? role : this.role;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<Long> getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(List<Long> completedTasks) {
		this.completedTasks = completedTasks != null ? new ArrayList<>(completedTasks) : new ArrayList<>();
	}
}
