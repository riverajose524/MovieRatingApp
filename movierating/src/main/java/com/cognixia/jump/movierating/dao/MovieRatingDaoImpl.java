package com.cognixia.jump.movierating.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.cognixia.jump.movierating.connection.ConnectionManager;
import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.exception.UserNotFoundException;

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
	public int validateUser(String email, String password) throws UserNotFoundException {
		
		
				//first verify if the email entered exists in the database, throw custom exception otherwise
				String sql = "SELECT * FROM user WHERE email = ?";
				
				try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
					preparedStatement.setString(1, email);
					
					ResultSet resultSet = preparedStatement.executeQuery();
					if(resultSet.next() == false) {
						throw new UserNotFoundException(email);
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				//once the email is verified, check if they entered the right password
				sql = "SELECT * FROM user WHERE email = ? AND password = ?";
				try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
					preparedStatement.setString(1, email);
					preparedStatement.setString(2, password);
					
					
					ResultSet resultSet = preparedStatement.executeQuery();
					String sqlEmail = "";
					String sqlPassword = "";
					
					//if the entry matching the email and password was found
					if(resultSet.next()) {
						sqlEmail = resultSet.getString("email");
						sqlPassword = resultSet.getString("password");
						
						//comparing case sensitivity in java
						if(email.equals(sqlEmail) && password.equals(sqlPassword)) {
							return resultSet.getInt("user_id");
						}
					}
					
				}
				catch(SQLException e){
					e.printStackTrace();
				}
				
				//returns -1 if the password is wrong
				return -1;
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
