package com.cognixia.jump.movierating.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.User;
import com.cognixia.jump.movierating.data.UserStatus;
import com.cognixia.jump.movierating.exception.UserNotFoundException;


public interface MovieRatingDao {
	
	 // needed for later so we make sure that the connection manager gets called
    public void establishConnection() throws ClassNotFoundException, SQLException;

    // as well, this method will help with closing the connection
    public void closeConnection() throws SQLException;
    
    public Connection getConnection()throws ClassNotFoundException, SQLException;
    
    public int validateUser(String username, String password) throws UserNotFoundException;

    public List<Movie> getAllMovies();
    
    public void rateMovie(int userId, int selectedMovieId, int rating);
    
    public void register(String email, String password,UserStatus userStatus);
    
	public void updateMovieRating(int userId, int selectedMovieId, int rating);
    
    boolean deleteRating(Movie selectedMovie);
    
    public double[] getAverageRating();
    
    public int[] getNumberRatings();
    
	public List<Movie> getAllFavoriteMovies(int userId);
	
	public void updateMovieFavorite(int userId, int selectedMovieId);
	
	public void favorMovie(int userId, int selectedMovieId);
	
	public void updateMovie(String movieName, int movieId);
	
	public Optional<User> getUserById(int userId);
    
    public void getRatedMoviesByUser(int userId);
}
