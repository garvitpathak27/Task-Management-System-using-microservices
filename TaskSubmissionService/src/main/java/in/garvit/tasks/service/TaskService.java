package in.garvit.tasks.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import in.garvit.tasks.submissionModel.TaskDTO;


@FeignClient(name = "TASK-SERVICE")
public interface TaskService {


	@GetMapping("/api/tasks/{id}")
	TaskDTO getTaskById(@PathVariable String id, @RequestHeader("Authorization") String jwt);


	@PutMapping("/api/tasks/{id}/complete")
	TaskDTO completeTask(@PathVariable String id, @RequestHeader("Authorization") String jwt);

}
