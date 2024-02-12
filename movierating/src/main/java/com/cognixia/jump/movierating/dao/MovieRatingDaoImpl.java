package com.cognixia.jump.movierating.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.movierating.connection.ConnectionManager;
import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;

public class MovieRatingDaoImpl implements MovieRatingDao{

	private Connection connection = null;
	
	@Override
	public void establishConnection() throws ClassNotFoundException, SQLException {
		
		if(connection == null) {
			connection = ConnectionManager.getConnection();
		}
	}
	
	@Override
	public void closeConnection() throws SQLException {
		connection.close();
	}

	@Override
	public boolean userLogIn(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Movie> getAllMovies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MovieRating rateMovie(Movie selectedMovie, int rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(String username, String password) {
		// TODO Auto-generated method stub
		
	}

}
