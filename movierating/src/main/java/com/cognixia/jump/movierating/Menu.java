package com.cognixia.jump.movierating;
import java.util.Scanner;
import com.cognixia.jump.movierating.dao.MovieRatingDaoImpl;
import com.cognixia.jump.movierating.exception.UserNotFoundException;
import java.sql.SQLException;
import com.cognixia.jump.movierating.dao.MovieRatingDao;
import com.cognixia.jump.movierating.data.Movie;
import java.util.List;

public class Menu {

    //private Scanner scanner;
    static MovieRatingDao mri = new MovieRatingDaoImpl();
    private static Scanner scanner = new Scanner(System.in);


    public static void mainMenu() {
    	
		try {
			mri.establishConnection();
		} catch (ClassNotFoundException | SQLException e1) {
			System.out.println("\nCould not connect to the Database, application cannot run at this time.");
		};
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
			viewMovies();
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

	

public void ratingMenu(String movieName) {
  System.out.println("+========================================================+");
  System.out.println("| Rating Menu:                                           |");
  System.out.println("+========================================================+");
  System.out.println("| Movie: " + movieName + "                               |");
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
          
          break;
      default:
          System.out.println("Invalid choice");
  }
}

	//function to validate user
	public static void validateUser() {
		
	
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
		
		System.out.println("\n+========================================================+");
		System.out.println("| Movie              Avg. Rating            # of Ratings |");
		System.out.println("+========================================================+");
	}
	
	
	
	
	
}
