package in.garvit.tasks.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import in.garvit.tasks.taskModel.UserDTO;

/**
 * Feign Client for User Service Communication
 * 
 * This interface provides methods to communicate with the User Service microservice.
 * It uses OpenFeign for declarative REST client implementation.
 * 
 * @author garvitpathak27
 */
@FeignClient(name = "USER-SERVICE", path = "/api/users")
public interface UserService {

	/**
	 * Get user profile information using JWT token
	 * 
	 * @param jwt JWT authentication token
	 * @return UserDTO containing user profile information
	 */
	@GetMapping("/profile")
	UserDTO getUserProfileHandler(@RequestHeader("Authorization") String jwt);
}

