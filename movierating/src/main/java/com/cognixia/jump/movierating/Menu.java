package com.cognixia.jump.movierating;

import java.util.Scanner;

import com.cognixia.jump.movierating.dao.MovieRatingDaoImpl;
import com.cognixia.jump.movierating.exception.UserNotFoundException;
import java.sql.SQLException;

public class Menu {

	private static Scanner scanner = new Scanner(System.in);

	private static MovieRatingDaoImpl movieRatingDao = new MovieRatingDaoImpl();
	
	public static void mainMenu() {
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
			// Handle register
			break;
		case 2:
			Menu.validateUser();
			break;
		case 3:
			// Handle view movies
			break;
		case 4:
			// Handle exit
			break;
		default:
			System.out.println("Invalid choice");
		}
	}

	private static int getChoice() {
		System.out.print("Selection: ");
		return scanner.nextInt();
	}

	public void closeScanner() {
		scanner.close();
	}

	// function to validate user
	public static void validateUser() {

		try {

			movieRatingDao.establishConnection();

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
				userId = movieRatingDao.validateUser(email, password);
			} catch (UserNotFoundException e) {
				System.out.println("\n" + e.getMessage());
				userId = -1;
			}

			if (userId > 0) {

				Menu.loggedInMenu(userId);

				isLoginValid = true;
				
			} else {
				System.out.println("\n**************************************");
				System.out.println("************ Login Failed ************");
				System.out.println("**************************************\n");
				
				Menu.mainMenu();
				
				isLoginValid = true;
				
				
			}

		} while (!isLoginValid);

		return;

	}
	
	
	public static void loggedInMenu(int userId) {
		
		System.out.println("User with id " + userId + " logged in");

	}
	
	
	
	
	
}
