package com.cognixia.jump.movierating.data;

public class MovieRating {
	
	private int movieID;
	private int userID;
	private int rating;
	
	public MovieRating(int movieID, int userID, int rating) {
		super();
		this.movieID = movieID;
		this.userID = userID;
		this.rating = rating;
	}
	
	public int getMovieID() {
		return movieID;
	}
	
	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	@Override
	public String toString() {
		return "MovieRating [movieID=" + movieID + ", userID=" + userID + ", rating=" + rating + "]";
	}
	

}
