package web.cms.springmvc.service;

import java.io.File;
import java.util.List;

import web.cms.springmvc.model.User;

public interface FileService {
	
	File findByName(String name);
	
	
	User findById(int id);
	
	User findByUsername(String username);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserByUsername(String username);

	List<User> findAllUsers(); 
	
	boolean isUsernameUnique(Integer id, String username);



}
