# Movie Rating Console Application

MovieRatingApp is a Java-based project of a console application that connects to a MySQL database called `movie_app` with JDBC and implements the DAO pattern.
The project aims to provide a platform for movie lovers to rate, favorite watched movies and discover new ones to watch.

## Prerequisites

To run this project, you will need to have the following installed on your system:

- MySQL 8.0 or later
- Java and an IDE
- JDBC driver jar file

## Installation

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE.
3. Create DB in preferred DBMS, sql script provided with the name `Movie-App.sql` within the resources folder.
4. Configure the database connection settings in the `ConnectionManager.java` file which can be found in the package with the path `com.cognixia.jump.movierating.connection`.
5. Add the external JDBC jar file to the project.  
6. Run the project.

## Features

- User registration
- User authentication
- Rate movie menu with rate, update, delete functionallity
- View list of movies with avg. ratings and # ratings for each movie (user feature)
- Admin can add, update and delete movies
- Guests can rate movies
  
