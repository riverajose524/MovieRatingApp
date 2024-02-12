-- Create database movie_app
CREATE DATABASE movie_app;

-- Use database movie_app
use movie_app;

-- Drop tables
drop table user;

-- Create table movie 
CREATE TABLE movie (
  id INT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- Create table user 
CREATE TABLE user (
  userid INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

-- Create table movie_rating 
CREATE TABLE movie_rating (
  movieID INT REFERENCES movie(id),
  userID INT REFERENCES user(userid),
  rating INT CHECK (rating IN ('0', '1', '2', '3', '4', '5')),
  PRIMARY KEY (movieID, userID)
);
