package com.cognixia.jump.movierating.data;

//User Model Class
public class User {

	// User's id
	private int id;
	
	//User's email
	private String email;
	
	//User's password
	private String password;

	//Constructs the user object
	public User(int id, String email, String password) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
	}

	//Get the user's id
	public int getId() {
		return id;
	}

	//Set the user's id
	public void setId(int id) {
		this.id = id;
	}

	//Get the user's email
	public String getEmail() {
		return email;
	}

	//Set the user's email
	public void setEmail(String email) {
		this.email = email;
	}

	//Get the user's password
	public String getPassword() {
		return password;
	}

	//Set the user's password
	public void setPassword(String password) {
		this.password = password;
	}

	//Converts user object into a String object
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + "]";
	}
	
	
}
