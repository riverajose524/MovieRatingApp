package com.cognixia.jump.movierating.data;

import java.sql.SQLException;
import java.util.List;

public interface MovieDao {
	
	
	public void establishConnection() throws ClassNotFoundException, SQLException;
	public void closeConnection() throws SQLException ;
	
	List<Movie> getAllMovies();
}
