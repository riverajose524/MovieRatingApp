package com.cognixia.jump.movierating.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
        try {
            String sql = "INSERT INTO movie_ratings (movie_id, rating) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, selectedMovie.getId()); // Set movie ID
            statement.setInt(2, rating); // Set rating
            // Execute the SQL statement
            statement.executeUpdate();
            // Construct and return the MovieRating object
            return new MovieRating(selectedMovie, rating);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
	}

	@Override
	public void register(String username, String password) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean deleteRating(Movie selectedMovie) {
        try {
            // Implementation for deleting a movie rating
            String sql = "DELETE FROM movie_ratings WHERE movie_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, selectedMovie.getId());
            int deletedRows = statement.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return false;
	}

	@Override
	public boolean updateRating(Movie selectedMovie, int newRating) {
		// TODO Auto-generated method stub
		return false;
	}

}
