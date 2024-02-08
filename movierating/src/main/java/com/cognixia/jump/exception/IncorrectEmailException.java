package com.cognixia.jump.exception;

public class IncorrectEmailException extends Exception {
	
	private static final long serialVersionUID = 1L;

		//construct the exception that is thrown when incorrect email is entered
		public IncorrectEmailException(String email) {
			
			super("The following: " + email + " is not a correctly written email");
		}

}
