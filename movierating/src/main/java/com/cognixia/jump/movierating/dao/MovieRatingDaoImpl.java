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
							return resultSet.getInt("userid");
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
	public void rateMovie(int userId, int selectedMovieId, int rating) {
		
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO movie_rating (movieID, userID, rating) VALUES (?,?,?)")){
			
			pstmt.setInt(1, rating);
			pstmt.setInt(2,  selectedMovieId);
			pstmt.setInt(3, userId);
				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie rating succesfully updated.");
	            }
			} catch (SQLException e) {
				System.out.println("A SQL exception has occured for the database while updating movie rating, the following exception was given.");
		        System.out.println(e.getMessage());
			}
				
//        try {
//            String sql = "INSERT INTO movie_rating (movieID, userID, rating) VALUES (?,?,?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setInt(1, selectedMovieId); // Set movie ID
//            statement.setInt(2,userId);
//            statement.setInt(3, rating); // Set; rating
//            // Execute the SQL statement
//            int count=statement.executeUpdate();
//            if (count > 0) {
//                System.out.println("Rating success.");
//            }
//            // Construct and return the MovieRating object
//            
//            return new MovieRating(selectedMovieId, userId,rating);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
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
	public void updateMovieRating(int userId, int selectedMovieId, int rating) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE movie_rating set rating=? where movieID=? and userID=?")){
			
			pstmt.setInt(1, rating);
			pstmt.setInt(2,  selectedMovieId);
			pstmt.setInt(3, userId);
				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie rating succesfully updated.");
	            }
			} catch (SQLException e) {
				System.out.println("A SQL exception has occured for the database while updating movie rating, the following exception was given.");
		        System.out.println(e.getMessage());
			}
				
		
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
	public double[] getAverageRating() {

		double[] avgRatings = null; // Initialize avgRatings
	    List<Double> ratingList = new ArrayList<>(); // Initialize a list to store ratings

	    try (Statement statement = connection.createStatement();
	         ResultSet resSet = statement.executeQuery("SELECT AVG(movie_rating.rating) AS avg_rating\r\n"
	         		+ "FROM movie\r\n"
	         		+ "LEFT JOIN movie_rating on movie.id=movie_rating.movieID\r\n"
	         		+ "group by movie.id;")) {
	        while (resSet.next()) {
	            double avgRating = resSet.getDouble("avg_rating");
	            ratingList.add(avgRating); // Add average rating to the list
	        }

	        // Convert the list to double array
	        avgRatings = new double[ratingList.size()];
	        for (int i = 0; i < ratingList.size(); i++) {
	            avgRatings[i] = ratingList.get(i);
	        }
	    } catch (SQLException e) {
	        System.out.println("A SQL exception has occurred while retrieving average ratings.");
	        System.out.println(e.getMessage());
	    }

	    return avgRatings;
	}

	@Override
	public int[] getNumberRatings() {
		
		int[] numRatings = null; // Initialize avgRatings
	    List<Integer> ratingList = new ArrayList<>(); // Initialize a list to store ratings

	    try (Statement statement = connection.createStatement();
	         ResultSet resSet = statement.executeQuery("SELECT count(movie_rating.rating) AS num_ratings\r\n"
	         		+ "FROM movie\r\n"
	         		+ "LEFT JOIN movie_rating on movie.id=movie_rating.movieID\r\n"
	         		+ "group by movie.id;")) {
	        while (resSet.next()) {
	            int numRating = resSet.getInt("num_ratings");
	            ratingList.add(numRating); // Add average rating to the list
	        }

	        // Convert the list to double array
	        numRatings = new int[ratingList.size()];
	        for (int i = 0; i < ratingList.size(); i++) {
	            numRatings[i] = ratingList.get(i);
	        }
	    } catch (SQLException e) {
	        System.out.println("A SQL exception has occurred while retrieving number ratings.");
	        System.out.println(e.getMessage());
	    }

	    return numRatings;
	}

	@Override
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		 if (connection == null) {
	            connection = ConnectionManager.getConnection();
	        }
	        return connection;
	}

}
