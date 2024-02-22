package com.cognixia.jump.movierating;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cognixia.jump.movierating.dao.MovieRatingDaoImpl;
import com.cognixia.jump.movierating.exception.UserNotFoundException;
import java.sql.SQLException;
import com.cognixia.jump.movierating.dao.MovieRatingDao;
import com.cognixia.jump.movierating.data.Movie;
import com.cognixia.jump.movierating.data.MovieRating;
import com.cognixia.jump.movierating.data.User;
import com.cognixia.jump.movierating.data.UserStatus;

import java.util.List;
import java.util.Optional;

public class Menu {

	static MovieRatingDao mri = new MovieRatingDaoImpl();
	private static Scanner scanner = new Scanner(System.in);
	private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
	private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
	public static final String RED = "\033[0;31m";
	public static final String CYAN = "\u001B[36m";
	public static final String RESET = "\u001B[0m";
	public static final String PURPLE = "\u001B[35m";
	public static final String YELLOW = "\u001B[33m";
	public static final String GREEN = "\u001B[32m";

	public static void mainMenu() throws SQLException {
		try {
			mri.establishConnection();
		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("\nCould not connect to the Database, application cannot run at this time.");
		}
		;
		System.out.println(GREEN + "+========================================================+");
		System.out.println("| Main Menu:                                             |");
		System.out.println("+========================================================+");
		System.out.println("| 1. REGISTER                                            |");
		System.out.println("| 2. LOGIN                                               |");
		System.out.println("| 3. GUEST LOGIN                                         |");
		System.out.println("| 4. VIEW MOVIES                                         |");
		System.out.println("| 5. EXIT                                                |");
		System.out.println("+========================================================+"+ RESET);

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
			handleGuestLogin();
			break;
		case 4:
			viewMovies();
			break;
		case 5:
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

	public static void viewMovies() throws SQLException {
		List<Movie> movies = mri.getAllMovies();

		if (movies.isEmpty()) {
			System.out.println("No movies found.");
		} else {
			System.out.println("+========================================================+");
			System.out.println("| List of Movies                                         |");
			System.out.println("+========================================================+");
			int number = 1;
			for (Movie movie : movies) {
				System.out.println(YELLOW+number + ": MOVIE NAME: " + movie.getName()+RESET);
				number++;
			}
			int exitChoice = movies.size() + 1;
			System.out.println(YELLOW + exitChoice +  ":" + RED + " EXIT" + RESET );
			System.out.println("+========================================================+");
			int choice = getChoice();

			if (choice == exitChoice) {
				mainMenu();
			} else {
				System.out.println("Invalid choice");
			}
		}
	}

	public static void closeScanner() {
		scanner.close();
	}

	public static void ratingMenu(String movieName, int userID, int movieID) throws SQLException {

		Optional<User> user = mri.getUserById(userID);
		String name = "";

		int movieNameLength = movieName.length();
		int totalWidth = 56; // Total width of the line, including characters for "| Movie: " and " |"
		int remainingWidth = totalWidth - 9; // Subtracting characters for "| Movie: " to get the remaining width
		String padding = ""; // Initialize padding string

		// Calculate the number of spaces needed for padding
		int paddingSpaces = remainingWidth - movieNameLength;

		// Generate the padding string
		for (int i = 0; i < paddingSpaces; i++) {
			padding += " ";
		}

		System.out.println(CYAN+"+========================================================+");
		System.out.println("| Rating Menu:                                           |");
		System.out.println("+========================================================+");
		System.out.println("| Movie: " + movieName + padding + " |");
		System.out.println("|                                                        |");
		System.out.println("| Rating:                                                |");
		System.out.println("| 0. Really Bad                                          |");
		System.out.println("| 1. Bad                                                 |");
		System.out.println("| 2. Not Good                                            |");
		System.out.println("| 3. Okay                                                |");
		System.out.println("| 4. Good                                                |");
		System.out.println("| 5. Great                                               |" + RESET);

		if (user.get().getStatus() == UserStatus.USER) {
			System.out.println("| 6. FAVOR                                               |");
			System.out.println("| 7. DELETE                                              |");
			System.out.println("| 8. EXIT                                                |");
			System.out.println("+========================================================+");

			int choice = getChoice();

			switch (choice) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				mri.rateMovie(userID, movieID, choice);
				loggedInMenu(userID);
				break;
			case 6:
				mri.favorMovie(userID, movieID);
				loggedInMenu(userID);
				break;
			case 7:
				mri.deleteRating(userID, movieID);
				loggedInMenu(userID);
				break;
			case 8:
				loggedInMenu(1);
				break;
			default:
				System.out.println("Invalid choice");
			}

		} else if (user.get().getStatus() == UserStatus.ADMIN) {
			System.out.println("| 6. FAVOR                                               |");
			System.out.println("| 7. UPDATE                                              |");
			System.out.println("| 8. EXIT                                                |");
			System.out.println("+========================================================+");

			int choice = getChoice();

			switch (choice) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				mri.rateMovie(userID, movieID, choice);
				loggedInMenu(userID);
				break;
			case 6:
				mri.favorMovie(userID, movieID);
				loggedInMenu(userID);
				break;
			case 7:
				System.out.println("Enter the movie name you wish to update the movie to");
				scanner.nextLine(); // Consume newline character
				name = scanner.nextLine();
				mri.updateMovie(name, movieID);
				loggedInMenu(userID);

				break;
			case 8:
				loggedInMenu(userID);
			default:
				System.out.println("Invalid choice");
			}

		} else {
			System.out.println("| 6. EXIT                                                |");
			System.out.println("+========================================================+");

			int choice = getChoice();

			switch (choice) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
				mri.rateMovie(userID, movieID, choice);
				loggedInMenu(userID);
				break;
			case 6:
				loggedInMenu(userID);
				break;
			default:
				System.out.println("Invalid choice");
			}
		}

	}

	// function to validate user
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

				loggedInMenu(userId);

				isLoginValid = true;

			} else {
				Menu.mainMenu();

				isLoginValid = true;

			}

		} while (!isLoginValid);

		return;

	}

	public static void FavoriteMoviesMenu(int userId) throws SQLException {
		List<Movie> movies = mri.getAllFavoriteMovies(userId);

		if (movies.isEmpty()) {
			System.out.println("No movies found.");
			loggedInMenu(userId);
		} else {
			System.out.println("+========================================================+");
			System.out.println("| List of Favorite Movies                                |");
			System.out.println("+========================================================+");
			int number = 1;
			for (Movie movie : movies) {
				System.out.println(number + ": MOVIE NAME: " + movie.getName());
				number++;
			}
			int exitChoice = movies.size() + 1;
			System.out.println(exitChoice + ": EXIT");
			System.out.println("+========================================================+");
			int choice = getChoice();

			if (choice == exitChoice) {
				loggedInMenu(userId);
			} else {
				System.out.println("Invalid choice");
			}
		}
	}

	public static void loggedInMenu(int userId) throws SQLException {
		UserStatus userStatus = mri.getUserStatus(userId);
		List<Movie> movies = mri.getAllMovies();
		double[] avgRatings = mri.getAverageRating();
		int[] numberOfRatings = mri.getNumberRatings();
		int exitChoice = movies.size() + 1;
		int number = 1;
		int movieId = -1;
		String movieName = "";

		if (movies.isEmpty()) {
			System.out.println("No movies found.");
		} else {

			System.out
					.println("+=====================================================================================+");
			System.out
					.println("| Movie                                                    Avg. Rating   # of Ratings |");

			// Iterate through movies
			for (int i = 0; i < movies.size(); i++) {
				movieName = movies.get(i).getName();

				String avgRating = "";

				if (avgRatings[i] > 0) {
					avgRating = String.format("%.2f", avgRatings[i]); // Format average rating to 2 decimal places
				} else {
					avgRating = "N/A";
				}

				String numRatings = String.valueOf(numberOfRatings[i]);

				// Calculate the space needed to align the columns
				int spacesMovieName = Math.max(0, 54 - movieName.length()); // Adjust as needed
				int spacesAvgRating = Math.max(0, 14 - avgRating.length()); // Adjust as needed
				int spacesNumRatings = Math.max(0, 12 - numRatings.length()); // Adjust as needed

				// Construct the line with proper formatting
				// works with older version
//				System.out.println("| " + number + ". " + movieName + " ".repeat(spacesMovieName) + avgRating
//						+ " ".repeat(spacesAvgRating) + numRatings + " ".repeat(spacesNumRatings) + " |");

				// works with new java version
				System.out.println(YELLOW + "| " + number + ". " + movieName + String.format("%" + spacesMovieName + "s", "")
						+ avgRating + String.format("%" + spacesAvgRating + "s", "") + numRatings
						+ String.format("%" + spacesNumRatings + "s", "") + " |"+RESET);
				number++;
			}

			boolean isAdmin = userStatus == UserStatus.ADMIN;

			// Additional menu options for admin
			if (isAdmin) {
				System.out.println("| " + PURPLE + exitChoice
						+  ". ADD MOVIE                                                                         |"+RESET);
				System.out.println("| " + (exitChoice + 1)
						+ ". FAVORITES                                                                        |"+ RESET);
				System.out.println("| " + (exitChoice + 2)
						+ ". YOUR MOVIE RATINGS                                                               |"+RESET);
				System.out.println("| " + PURPLE + (exitChoice + 3)
						+ ". DELETE MOVIE                                                                      |"+RESET);
				System.out.println("| " + RED+ (exitChoice + 4)
						+ ". EXIT                                                                            |"+RESET);

				System.out.println(
						"+=====================================================================================+");

				int choice = getChoice();

				if (choice == (exitChoice + 2)) {
					ratedMoviesByUser(userId);
				} else if (choice == (exitChoice + 4)) {
					mainMenu();
				} else if (choice == exitChoice + 1) {
					FavoriteMoviesMenu(userId);
				} else if (choice == exitChoice + 3) { // ADMIN DELETE MOVIE
					System.out.println("Enter the movie ID you wish to delete:");
					int movieIdToDelete = scanner.nextInt();
					scanner.nextLine();
					boolean deleteSuccess = mri.deleteMovie(movieIdToDelete);
					if (deleteSuccess) {
						System.out.println("Movie with ID: " + movieIdToDelete + " deleted successfully.");
					} else {
						System.out.println("Failed to delete the movie with ID " + movieIdToDelete);
					}
					loggedInMenu(userId);
				} else if (choice == exitChoice) { // ADMIN ADD MOVIE
					System.out.println("Enter the name of the movie you wish to add:");
					scanner.nextLine();
					movieName = scanner.nextLine();
					mri.addMovie(movieName);
					loggedInMenu(userId);
				} else if (choice < exitChoice && choice > 0) {
					movieName = movies.get(choice - 1).getName();
					movieId = movies.get(choice - 1).getId();

					ratingMenu(movieName, userId, movieId);

				} else {

					System.out.println("\nInvalid choice\n");
					loggedInMenu(userId);
				}

			} else if (userStatus == UserStatus.USER) {
				System.out.println("| " + exitChoice
						+ ". FAVORITES                                                                        |");
				System.out.println("| " + (exitChoice + 1)
						+ ". YOUR MOVIE RATINGS                                                               |");
				System.out.println("| " + (exitChoice + 2)
						+ ". EXIT                                                                             |");
				System.out.println(
						"+=====================================================================================+");

				int choice = getChoice();

				if (choice == (exitChoice + 1)) {
					ratedMoviesByUser(userId);
				} else if (choice == (exitChoice + 2)) {
					mainMenu();
				} else if (choice == exitChoice) {
					FavoriteMoviesMenu(userId);
				} else if (choice < exitChoice && choice > 0) {
					movieName = movies.get(choice - 1).getName();
					movieId = movies.get(choice - 1).getId();

					ratingMenu(movieName, userId, movieId);
				} else {

					System.out.println("\nInvalid choice\n");
					loggedInMenu(userId);
				}
			} else {
				System.out.println("| " + exitChoice
						+ ". EXIT                                                                             |");
				System.out.println(
						"+=====================================================================================+");

				int choice = getChoice();

				if (choice == (exitChoice)) {
					mainMenu();
				} else if (choice < exitChoice && choice > 0) {
					movieName = movies.get(choice - 1).getName();
					movieId = movies.get(choice - 1).getId();

					ratingMenu(movieName, userId, movieId);
				} else {

					System.out.println("\nInvalid choice\n");
					loggedInMenu(userId);
				}
			}

		}
	}

	private static void ratedMoviesByUser(int userId) throws SQLException {
		
		System.out.println("+=====================================================================================+");
		System.out.println("| Movie                                                         Rating                |");

		mri.getRatedMoviesByUser(userId);
		loggedInMenu(userId);		
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
			mri.register(email, password, UserStatus.USER);

		} else {
			System.out.println("Invalid email address!");
		}

	}

	public static void handleGuestLogin() throws SQLException {

		String email = "guestUser@email.com";
		String password = "pw123";
		boolean isLoginValid = false;

		mri.register(email, password, UserStatus.GUEST);

		try {

			mri.establishConnection();

		} catch (ClassNotFoundException | SQLException e1) {

			System.out.println("\nCould not connect to the Movie App Database, application cannot run at this time.");
		}

		// prompt user for Email & Password
		do {

			int userId;
			// Now pass that information to the DAO implementation class

			try {
				userId = mri.validateUser(email, password);
			} catch (UserNotFoundException e) {
				System.out.println("\n" + e.getMessage());
				userId = -1;
			}

			if (userId > 0) {

				loggedInMenu(userId);

				isLoginValid = true;

			} else {
				Menu.mainMenu();

				isLoginValid = true;

			}

		} while (!isLoginValid);

		return;

	}
}