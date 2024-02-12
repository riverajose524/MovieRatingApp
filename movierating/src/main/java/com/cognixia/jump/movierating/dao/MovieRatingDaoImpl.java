package com.cognixia.jump.movierating.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cognixia.jump.movierating.connection.ConnectionManager;
import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.data.User;

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
	public boolean userLogIn(String email, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Movie> getAllMovies() {
		
		List<Movie> movieList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
                ResultSet resSet = statement.executeQuery("select * from movie")) {
            while (resSet.next()) {
                int id = resSet.getInt("id");
                String name = resSet.getString("name");
                
                Movie movie = new Movie(id, name);
                movieList.add(movie);
            }
        } catch (SQLException e) {
            System.out.println(
                    "A SQL exception has occured for the database while retrieving all movies, the following exception message was given.");
            System.out.println(e.getMessage());
            movieList = new ArrayList<>();
        }

        return movieList;
	}

	@Override
	public MovieRating rateMovie(Movie selectedMovie, int rating) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void register(String email, String password) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT into user(email,password) values(?,?)")){
			
			pstmt.setString(1, email);
			pstmt.setString(2,  password);
			
			int count = pstmt.executeUpdate();

            if (count > 0) {
                System.out.println("User "+email+" succesfully added.");
            }
		} catch (SQLException e) {
			System.out.println("A SQL exception has occured for the database while adding a new user, the following exception was given.");
	        System.out.println(e.getMessage());
		}
		
	}

	@Override
	public void updateMovieRating(User user, Movie selectedMovie, int rating) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE movie_rating set rating=? where movieID=? and userID=?")){
			
			pstmt.setInt(1, rating);
			pstmt.setInt(2,  selectedMovie.getId());
			pstmt.setInt(3, user.getId());
				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie rating succesfully updated.");
	            }
			} catch (SQLException e) {
				System.out.println("A SQL exception has occured for the database while updating movie rating, the following exception was given.");
		        System.out.println(e.getMessage());
			}
				
		
	}

}
