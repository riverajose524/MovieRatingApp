package com.cognixia.jump.movierating;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.cognixia.jump.movierating.dao.MovieRatingDao;
import com.cognixia.jump.movierating.dao.MovieRatingDaoImpl;
import com.cognixia.jump.movierating.data.Movie;

public class Menu {

    private Scanner scanner;
    MovieRatingDao mri = new MovieRatingDaoImpl();

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void mainMenu() {
    	
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
                // register
            	
                break;
            case 2:
                // login
                break;
            case 3:
                // view movies
            	viewMovies();
                break;
            case 4:
                // Exit
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
    
    private void viewMovies() {
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


public void ratingMenu(String movieName) {
  System.out.println("+========================================================+");
  System.out.println("| Rating Menu:                                           |");
  System.out.println("+========================================================+");
  System.out.println("| Movie: " + movieName + "                                     |");
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
    private int getChoice() {
        System.out.print("Selection: ");
        return scanner.nextInt();
    }

    public void closeScanner() {
        scanner.close();
    }
}
