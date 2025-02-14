Attendance Management System

Description

A brief description of your project goes here. Explain what the project does and its main features.

Installation

Clone the repository:

git clone https://github.com/YourUsername/YourRepo.git

Navigate to the project directory:

cd YourRepo

Set up Git user details (if necessary for this repository only):

git config user.email "your-email@example.com"
git config user.name "Your Name"

Usage

Provide instructions on how to use the project, including commands or setup steps.

MySQL Setup for Attendance Management System Project In Java

Creating the database

-- Creating database
CREATE DATABASE attendance;

Selecting the database

-- Selecting the database
USE attendance;

Creating the user table

-- User Table
CREATE TABLE user(id INT PRIMARY KEY, username VARCHAR(25), name VARCHAR(25), password VARCHAR(25), prio INT);

Creating the class table

-- Class Table
CREATE TABLE class(id INT PRIMARY KEY, name VARCHAR(25));

Creating the student table

-- Student Table
CREATE TABLE students(id INT PRIMARY KEY, name VARCHAR(25), class VARCHAR(10));

Creating the teacher’s table

-- Teachers Table
CREATE TABLE teachers(id INT PRIMARY KEY, name VARCHAR(25));

Creating the attendance table

-- Attend Table
CREATE TABLE attend(stid INT, dt DATE, status VARCHAR(15), class VARCHAR(15));

Adding the First user to get logged in

-- Creating admin user
INSERT INTO user VALUES(1, 'admin', 'Admin', 'admin', 1);

Contributing

Fork the repository.

Create a new branch: git checkout -b feature-branch

Make your changes and commit them: git commit -m 'Add new feature'

Push to the branch: git push origin feature-branch

Open a Pull Request.


