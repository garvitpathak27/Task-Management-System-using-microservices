package in.garvit.tasks.service;

import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.request.SignupRequest;
import in.garvit.tasks.usermodel.User;

import java.util.List;

public interface UserService {

	User register(SignupRequest request) throws UserException;

	User findUserProfileByJwt(String jwt) throws UserException;

	User findUserByEmail(String email) throws UserException;

	User findUserById(String userId) throws UserException;

	List<User> findAllUsers();
}

