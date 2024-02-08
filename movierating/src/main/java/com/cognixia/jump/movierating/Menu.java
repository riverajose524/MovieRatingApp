package com.cognixia.jump.movierating;

import java.util.List;
import java.util.Scanner;

import com.cognixia.jump.movierating.data.Movie;

public class Menu {

    private Scanner scanner;

    public Menu() {
        this.scanner = new Scanner(System.in);
    }

    public void mainMenu() {
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
                // Handle login
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


    private int getChoice() {
        System.out.print("Selection: ");
        return scanner.nextInt();
    }

    public void closeScanner() {
        scanner.close();
    }
}
