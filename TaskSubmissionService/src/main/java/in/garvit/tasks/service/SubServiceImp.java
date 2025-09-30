package in.garvit.tasks.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.garvit.tasks.repository.SubRepository;
import in.garvit.tasks.submissionModel.TaskDTO;
import in.garvit.tasks.submissionModel.TaskSubmission;

@Service
public class SubServiceImp implements SubmissionService {

	@Autowired
	private SubRepository subRepository;

	@Autowired
	private  TaskService taskService;

	@Override
	public TaskSubmission submitTask(String taskId, String githubLink, String userId, String jwt) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskSubmission getTaskSubmissionById(String submissionId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSubmission> getAllTaskSubmissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TaskSubmission> getTaskSubmissionByTaskId(String taskId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TaskSubmission acceptDeclineSubmission(String id, String status) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override public TaskSubmission submitTask(String taskId, String githubLink,
	 * String userId, String jwt) throws Exception { // TODO Auto-generated method
	 * stub return null; }
	 */
	/*
	 * @Override public TaskSubmission getTaskSubmissionById(String submissionId)
	 * throws Exception { // TODO Auto-generated method stub return null; }
	 */

	/*
	 * @Override public List<TaskSubmission> getAllTaskSubmissions() { // TODO
	 * Auto-generated method stub return null; }
	 */

	/*
	 * @Override public List<TaskSubmission> getTaskSubmissionByTaskId(String
	 * taskId) { // TODO Auto-generated method stub return null; }
	 */

	/*
	 * @Override public TaskSubmission acceptDeclineSubmission(String id, String
	 * status) throws Exception { // TODO Auto-generated method stub return null; }
	 */
	










	

}
