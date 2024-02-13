package com.cognixia.jump.movierating;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cognixia.jump.movierating.dao.MovieRatingDaoImpl;
import com.cognixia.jump.movierating.exception.UserNotFoundException;
import java.sql.SQLException;
import com.cognixia.jump.movierating.dao.MovieRatingDao;
import com.cognixia.jump.movierating.data.Movie;
import java.util.List;

public class Menu {
	

	static MovieRatingDao mri = new MovieRatingDaoImpl();
	private static Scanner scanner = new Scanner(System.in);
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);


    public static void mainMenu() throws SQLException {
		try {
			mri.establishConnection();
		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("\nCould not connect to the Database, application cannot run at this time.");
		}
		;
		System.out.println("+========================================================+");
		System.out.println("| Main Menu:                                             |");
		System.out.println("+========================================================+");
		System.out.println("| 1. REGISTER                                            |");
		System.out.println("| 2. LOGIN                                               |");
		System.out.println("| 3. VIEW MOVIES                                         |");
		System.out.println("| 4. EXIT                                                |");
		System.out.println("+========================================================+");

		int choice = getChoice();

		switch (choice) {
		case 1:
			handleRegister();
			mainMenu();
			break;
		case 2:
			validateUser();
			break;
		case 3:
			viewMovies();
			break;
		case 4:
			mri.closeConnection();
			break;
		default:
			System.out.println("Invalid choice");
		}
	}

	private static int getChoice() {
		System.out.print("Selection: ");
		return scanner.nextInt();
	}

	static private void viewMovies() {
		List<Movie> movies = mri.getAllMovies();

		if (movies.isEmpty()) {
			System.out.println("No movies found.");
		} else {
			System.out.println("+========================================================+");
			System.out.println("| List of Movies                                         |");
			System.out.println("+========================================================+");
			for (Movie movie : movies) {
				System.out.println("ID: " + movie.getId() + ", Name: " + movie.getName());
			}
			System.out.println("+========================================================+");
		}
	}

	public void closeScanner() {
		scanner.close();
	}

	public static void ratingMenu(String movieName) throws SQLException {
		System.out.println("+========================================================+");
		System.out.println("| Rating Menu:                                           |");
		System.out.println("+========================================================+");
		System.out.println("| Movie: " + movieName + "                        |");
		System.out.println("|                                                        |");
		System.out.println("| Rating:                                                |");
		System.out.println("| 0. Really Bad                                          |");
		System.out.println("| 1. Bad                                                 |");
		System.out.println("| 2. Not Good                                            |");
		System.out.println("| 3. Okay                                                |");
		System.out.println("| 4. Good                                                |");
		System.out.println("| 5. Great                                               |");
		System.out.println("| 6. EXIT                                                |");
		System.out.println("+========================================================+");

		int choice = getChoice();

		switch (choice) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			loggedInMenu(1);
			break;
		default:
			System.out.println("Invalid choice");
		}
	}

	//function to validate user
	public static void validateUser() throws SQLException {
		try {

			mri.establishConnection();

		} catch (ClassNotFoundException | SQLException e1) {

			System.out.println("\nCould not connect to the Movie App Database, application cannot run at this time.");
		}

		// create temporary strings to hold the email,
		String email;

		String password;

		Scanner sc = new Scanner(System.in);

		// boolean to control while loop
		boolean isLoginValid = false;

		// prompt user for Email & Password
		do {

			System.out.println("\nPlease enter your email: ");
			email = sc.nextLine();

			System.out.println("Please enter your password: ");
			password = sc.nextLine();

			int userId;

			// Now pass that information to the DAO implementation class

			try {
				userId = mri.validateUser(email, password);
			} catch (UserNotFoundException e) {
				System.out.println("\n" + e.getMessage());
				userId = -1;
			}

			if (userId > 0) {

				Menu.loggedInMenu(userId);

				isLoginValid = true;

			} else {
				Menu.mainMenu();

				isLoginValid = true;

			}

		} while (!isLoginValid);

		return;

	}

	public static void loggedInMenu(int userId) throws SQLException {
		List<Movie> movies = mri.getAllMovies();
		double[] avgRatings = mri.getAverageRating();
		int[] numberOfRatings = mri.getNumberRatings();
		int exitChoice = movies.size() + 1;
		int number = 1;
		
		

		if (movies.isEmpty()) {
			System.out.println("No movies found.");
		} else {

			System.out.println("+=====================================================================================+");
			System.out.println("| Movie                                                    Avg. Rating   # of Ratings |");

			// Iterate through movies
			for(int i = 0; i < movies.size(); i++) {
			    String movieName = movies.get(i).getName();
			    String avgRating="";
			    
			    if(avgRatings[i] > 0)
			    {
			    	avgRating = String.format("%.2f", avgRatings[i]); // Format average rating to 2 decimal places
			    }
			    else
			    {
			    	avgRating = "N/A";
			    }
			    
			    String numRatings = String.valueOf(numberOfRatings[i]);
			    
			    // Calculate the space needed to align the columns
			    int spacesMovieName = Math.max(0, 54 - movieName.length()); // Adjust as needed
			    int spacesAvgRating = Math.max(0, 14 - avgRating.length()); // Adjust as needed
			    int spacesNumRatings = Math.max(0, 12 - numRatings.length()); // Adjust as needed
			    
			    // Construct the line with proper formatting
			    System.out.println("| " + number + ". " + movieName + " ".repeat(spacesMovieName) + avgRating + " ".repeat(spacesAvgRating) + numRatings + " ".repeat(spacesNumRatings) + " |");
			    number++;
			}

			System.out.println("| " + exitChoice + ". EXIT                                                                             |");
			System.out.println("+=====================================================================================+");






			
			
			int choice = getChoice();
			
			if(choice == exitChoice)
			{
				mainMenu();
			}
			else if (choice < exitChoice && choice > 0)
			{
				String movieName = movies.get(choice-1).getName();
				
				ratingMenu(movieName);
				
			}
			else
			{
				System.out.println("\nInvalid choice\n");
				loggedInMenu(userId);
			}
			

		}
	}
	
	

    public static boolean isValidEmail(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
	public static void handleRegister() {
		
		System.out.println("Enter email: ");
		scanner.nextLine();
		String email = scanner.nextLine();
	        
	        if (isValidEmail(email)) {
	        	
	            System.out.println("Enter password: ");
	            String password = scanner.nextLine();
	            mri.register(email, password);
	            
	        } else {
	            System.out.println("Invalid email address!");
	        }
	 
	}
}
