package com.cognixia.jump.movierating.data;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
	
	//Establish connection with database
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	//Close connection with database
	public void closeConnection() throws SQLException ;
	
	//Validates user against database
	public void validateUser(String email, String password);
	
	//Get all users
	public List<User> getAllUsers();
	
	//Get user given id
	public User getUserById(int id);
	
	//Add a new user
	public void addUser(String email, String password);
	
	//Update a user
	public void updateUser(String email, String password);
	
	//Remove a user
	public void removeUser(String email, String password);

}
