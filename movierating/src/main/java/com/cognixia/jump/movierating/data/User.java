package com.cognixia.jump.movierating.data;

//User Model Class
public class User {

	

	// User's id
	private int id;
	
	//User's email
	private String email;
	
	//User's password
	private String password;
	
	private UserStatus status;

	//Constructs the user object
	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	//Get the user's id
	public int getId() {
		return id;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", status=" + status + "]";
	}
	
	
}
