-- Create database movie_app
CREATE DATABASE movie_app;

-- Delete database movie_app
DROP DATABASE movie_app;

-- Use database movie_app
use movie_app;

-- Drop table user
drop table user;

-- Create table movie 
CREATE TABLE movie (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL
);

-- Create table user 
CREATE TABLE user (
  userid INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL DEFAULT 'guest@gmail.com',
  password VARCHAR(255) NOT NULL DEFAULT 'password',
  status enum('USER', 'ADMIN', 'GUEST') NOT NULL DEFAULT 'USER'
);

-- Create table movie_rating 
CREATE TABLE movie_rating (
  movieID INT REFERENCES movie(id),
  userID INT REFERENCES user(userid),
  rating INT CHECK (rating IN ('0', '1', '2', '3', '4', '5')),
  PRIMARY KEY (movieID, userID)
);

-- Create table favorites 
CREATE TABLE favorites (
	movieID INT REFERENCES movie(id),
	userID INT REFERENCES user(userid),
    PRIMARY KEY (movieID, userID)
  );
  
  -- Movie Inserts
use movie_app;
INSERT INTO movie (id, name) VALUES (1, 'The Shawshank Redemption');
INSERT INTO movie (id, name) VALUES (2, 'The Godfather');
INSERT INTO movie (id, name) VALUES (3, 'The Dark Knight');
INSERT INTO movie (id, name) VALUES (4, 'Schindler''s List');
INSERT INTO movie (id, name) VALUES (5, 'Pulp Fiction');
INSERT INTO movie (id, name) VALUES (6, 'The Lord of the Rings: The Return of the King');
