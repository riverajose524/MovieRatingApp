package com.cognixia.jump.movierating.dao;

import java.sql.Statement;
import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cognixia.jump.movierating.connection.ConnectionManager;
import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.data.User;
import com.cognixia.jump.movierating.data.UserStatus;
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
	
	public Optional<User> getUserById(int userId) {
	    Optional<User> user = Optional.empty();

	    String sql = "SELECT * FROM user WHERE userid = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            int id = resultSet.getInt("userid");
	            String email = resultSet.getString("email");
	            String password = resultSet.getString("password");
	            String statusString = resultSet.getString("status"); // Assuming status is stored as a string in the database
	            UserStatus status = UserStatus.valueOf(statusString.toUpperCase()); 
	            

	            User foundUser = new User(email, password);
	            
	            foundUser.setId(id);
	            foundUser.setStatus(status);
	           
	            
	            user = Optional.of(foundUser);
	        }
	    } catch (SQLException e) {
	        System.out.println("A SQL exception has occurred while retrieving the user by ID.");
	        System.out.println(e.getMessage());
	    }

	    return user;
	}


	@Override
	public List<Movie> getAllFavoriteMovies(int userId) {
	    List<Movie> movieList = new ArrayList<>();
	    String sql = "SELECT movie.id, movie.name FROM movie " +
	                 "JOIN favorites ON movie.id = favorites.movieID " +
	                 "WHERE favorites.userID = ?";
	    
	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, userId);
	        ResultSet resultSet = preparedStatement.executeQuery();
	        
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String name = resultSet.getString("name");
	            Movie movie = new Movie(id, name);
	            movieList.add(movie);
	        }
	    } catch (SQLException e) {
	        System.out.println("A SQL exception has occurred while retrieving favorite movies.");
	        System.out.println(e.getMessage());
	        movieList = new ArrayList<>();
	    }
	    
	    return movieList;
	}


	@Override
	public void rateMovie(int userId, int selectedMovieId, int rating) {
		
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO movie_rating (movieID, userID, rating) VALUES (?,?,?)")){

			pstmt.setInt(1, selectedMovieId);
			pstmt.setInt(2,  userId);
			pstmt.setInt(3, rating);

				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie rating succesfully entered.");
	            }
		}catch (SQLIntegrityConstraintViolationException e) { // catch the duplicate entry exception
			updateMovieRating(userId,selectedMovieId,rating);
		}
		
	
			catch (SQLException e) {
				
		        
		        
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
	public void favorMovie(int userId, int selectedMovieId) {
		
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT INTO favorites (movieID, userID) VALUES (?,?)")){

			pstmt.setInt(1, selectedMovieId);
			pstmt.setInt(2,  userId);
				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie was succesfully favored.");
	            }
		}catch (SQLIntegrityConstraintViolationException e) { // catch the duplicate entry exception
			updateMovieFavorite(userId,selectedMovieId);
		}
		
	
			catch (SQLException e) {
				
		        
		        
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
	public void register(String email, String password, UserStatus userStatus) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"INSERT into user(email,password, status) values(?,?,?)")){
			
			String userStatusAsString = "";
			
			if(userStatus == userStatus.GUEST)
			{
				userStatusAsString = "GUEST";
				
			}
			else if (userStatus == userStatus.USER)
			{
				userStatusAsString = "USER";
			}
			System.out.println("User status:"+userStatusAsString);
			pstmt.setString(1, email);
			pstmt.setString(2,  password);
			pstmt.setString(3, userStatusAsString);
			
			int count = pstmt.executeUpdate();

            if (count > 0 && userStatus == userStatus.USER) {
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
	public void updateMovieFavorite(int userId, int selectedMovieId) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE favorites set where movieID=? and userID=?")){
			
			pstmt.setInt(1,  selectedMovieId);
			pstmt.setInt(2, userId);
				
			int count = pstmt.executeUpdate();
			
			 if (count > 0) {
	                System.out.println("Movie favorite succesfully updated.");
	            }
			} catch (SQLException e) {
				System.out.println("A SQL exception has occured for the database while updating movie rating, the following exception was given.");
		        System.out.println(e.getMessage());
			}
				
		
	}


	@Override
	public boolean deleteRating(int userId, int movieId) {
        try {
            // Implementation for deleting a movie rating
            String sql = "DELETE FROM movie_rating WHERE movieID = ? and userID=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, movieId);
            statement.setInt(2, userId);
          
            int deletedRows = statement.executeUpdate();
            if( deletedRows > 0) {
            	System.out.println("Rating succesfully deleted.");
            }
        } catch (SQLException e) {
            System.out.println("You haven't rated this movie yet.");
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
	
	@Override
	public void updateMovie(String movieName, int movieId) {
		
		try (PreparedStatement pstmt = connection.prepareStatement(
				"UPDATE movie set name = ? where id = ?")){
				
			pstmt.setString(1, movieName);
			pstmt.setInt(2, movieId);
			
			int count = pstmt.executeUpdate();

            if (count > 0) {
                System.out.println("The Movie:  " + movieName +" succesfully updated.");
            }
		} catch (SQLException e) {
			System.out.println("A SQL exception has occured for the database while adding a new user, the following exception was given.");
	        System.out.println(e.getMessage());
		}
		
	}
	

	@Override
	public List<List<MovieRating>>  getRatedMoviesByUser(int userId) {
		
		   List<List<MovieRating>> ratedMovies = new ArrayList<>();

		try (PreparedStatement preparedStatement = connection.prepareStatement(
		        "SELECT movie.id, movie.name, movie_rating.rating " +
		        "FROM movie_rating INNER JOIN movie ON movie_rating.movieID = movie.id " +
		        "WHERE movie_rating.userID = ?")) {
		    preparedStatement.setInt(1, userId);
		    try (ResultSet resSet = preparedStatement.executeQuery()) {
		    	int i=0;
		    	 while (resSet.next()) {
		    		 int id=resSet.getInt("id");
		    		 String movieName = resSet.getString("name");
		    	     int rating = resSet.getInt("rating");
		    	     List<MovieRating> movieRatings = new ArrayList<>();
		                movieRatings.add(new MovieRating(id, rating));
		                ratedMovies.add(movieRatings);
		    	     System.out.printf("| %d. %-57s  %-21d |\n",i+1, movieName, rating);
		    	     i++;
		    	 }
		    }
		} catch (SQLException e) {
			 System.out.println("A SQL exception has occurred while retrieving rated movies list by user.");
		     System.out.println(e.getMessage());
		}
		return ratedMovies;
       
	}

	@Override
	public UserStatus getUserStatus(int userId) {
	    UserStatus userStatus = UserStatus.USER; // Default to regular user status
	    try {
	        // Create a PreparedStatement to query the database for the user's status
	        String sql = "SELECT status FROM user WHERE userid = ?";
	        PreparedStatement statement = connection.prepareStatement(sql);
	        statement.setInt(1, userId);
	        // Execute the query
	        ResultSet resultSet = statement.executeQuery();
	        // Check if a result is returned
	        if (resultSet.next()) {
	            // Retrieve the user's status from the result set
	            String statusString = resultSet.getString("status");
	           
	            // Map the status string to the corresponding enum value
	            userStatus = UserStatus.valueOf(statusString);
	        }
	       
	        // Close resources
	        resultSet.close();
	        statement.close();
	    } catch (SQLException e) {
	        // Handle any SQL exceptions
	        System.out.println("Error retrieving user status: " + e.getMessage());
	    }
	    return userStatus;
	}
	@Override
	public void addMovie(String movieName) {
	    // Check if the movie already exists
	    if (movieExists(movieName)) {
	        System.out.println("Movie already exists.");
	        return;
	    }
	    String sql = "INSERT INTO movie (id, name) VALUES (?, ?)";
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        // Assuming id is auto-incrementing, so set it to null
	        statement.setNull(1, Types.INTEGER);
	        statement.setString(2, movieName);
	        statement.executeUpdate();
	        System.out.println("Movie added successfully.");
	    } catch (SQLException e) {
	        System.out.println("An error occurred while adding the movie: " + e.getMessage());
	    }       	
	}

	@Override
	public boolean deleteMovie(int movieId) {
	    String sql = "DELETE FROM movie WHERE id = ?";

	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setInt(1, movieId);
	        int rowsDeleted = statement.executeUpdate();

	        if (rowsDeleted > 0) {
	            System.out.println("Movie deleted successfully.");
	            return true;
	        } else {
	            System.out.println("No movie found with the given ID.");
	            return false;
	        }
	    } catch (SQLException e) {
	        System.out.println("An error occurred while deleting the movie: " + e.getMessage());
	        return false;
	    }
	}

	@Override
	public boolean movieExists(String movieName) {
	    String sql = "SELECT COUNT(*) AS count FROM movie WHERE name = ?";
	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
	        statement.setString(1, movieName);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            int count = resultSet.getInt("count");
	            return count > 0;
	        }
	    }catch (SQLException e) {
	        System.out.println("An error occurred while adding the movie: " + e.getMessage());
	        return false;
	    }
	    
		return false;
	}

}