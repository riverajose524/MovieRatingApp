package com.cognixia.jump.exception;

public class UserNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	//construct the exception that is thrown when user is not found
	public UserNotFoundException(String email) {
		
		super("User with " + email + " could not be found");
	}


}
