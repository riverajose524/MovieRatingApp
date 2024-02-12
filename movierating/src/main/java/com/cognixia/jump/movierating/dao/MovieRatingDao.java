package com.cognixia.jump.movierating.dao;

import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.data.User;

public interface MovieRatingDao {
	
	 // needed for later so we make sure that the connection manager gets called
    public void establishConnection() throws ClassNotFoundException, SQLException;

    // as well, this method will help with closing the connection
    public void closeConnection() throws SQLException;
    
    public boolean userLogIn(String email, String password);
    
    public List<Movie> getAllMovies();
    
    public MovieRating rateMovie(Movie selectedMovie, int rating);
    
    public void register(String email, String password);
    
    public void updateMovieRating(User user,Movie selectedMovie, int rating);
    
}
