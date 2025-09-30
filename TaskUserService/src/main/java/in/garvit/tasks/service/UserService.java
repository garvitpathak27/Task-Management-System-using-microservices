package in.garvit.tasks.service;

import in.garvit.tasks.exception.UserException;
import in.garvit.tasks.usermodel.User;

import java.util.List;


public interface UserService {

     
     public List<User> getAllUser()  throws UserException;
     
     public User findUserProfileByJwt(String jwt) throws UserException;
 	
 	public User findUserByEmail(String email) throws UserException;
 	
 	public User findUserById(String userId) throws UserException;

 	public List<User> findAllUsers();
     
     
    	 
     }

