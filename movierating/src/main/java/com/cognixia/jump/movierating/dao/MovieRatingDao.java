package com.cognixia.jump.movierating.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.data.User;
import com.cognixia.jump.movierating.exception.UserNotFoundException;


public interface MovieRatingDao {
	
	 // needed for later so we make sure that the connection manager gets called
    public void establishConnection() throws ClassNotFoundException, SQLException;

    // as well, this method will help with closing the connection
    public void closeConnection() throws SQLException;
    
    public Connection getConnection()throws ClassNotFoundException, SQLException;
    
    public int validateUser(String username, String password) throws UserNotFoundException;

    
    public List<Movie> getAllMovies();
    
    public MovieRating rateMovie(Movie selectedMovie, int rating);
    
    public void register(String email, String password);
    
    public void updateMovieRating(User user,Movie selectedMovie, int rating);
    
    // Deletes the rating of a movie
    boolean deleteRating(Movie selectedMovie);
    
    public double[] getAverageRating();
    
    public int[] getNumberRatings();
    
    
}
