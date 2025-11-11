package in.garvit.tasks.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import in.garvit.tasks.response.ApiResponse;

@RestController
public class HomeController {
	
	@GetMapping
	public ResponseEntity<ApiResponse> homeController(){
		ApiResponse res = new ApiResponse("Welcome To Task Management Microservice Project", true);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/users")
	public ResponseEntity<ApiResponse> userHomeController(){
		ApiResponse res = new ApiResponse("Welcome To Task Management User Service", true);
		return ResponseEntity.ok(res);
	}
}
