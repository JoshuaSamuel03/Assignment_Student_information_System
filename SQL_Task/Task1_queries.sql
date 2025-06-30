/*Create the database named "SISDB"*/

CREATE DATABASE SISDB;
USE SISDB;

/*Define the schema for the Students, Courses, Enrollments, Teacher, and Payments tables based on the provided schema. Write SQL scripts to create the mentioned tables with appropriate data types, constraints, and relationships.
a. Students
b. Courses
c. Enrollments
d. Teacher
e. Payments

Create appropriate Primary Key and Foreign Key constraints for referential integrity*/

CREATE TABLE Students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    date_of_birth DATE,
    email VARCHAR(100),
    phone_number VARCHAR(15)
);
CREATE TABLE Teacher (
    teacher_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    email VARCHAR(100)
);
CREATE TABLE Courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100),
    credits INT,
    teacher_id INT,
    FOREIGN KEY (teacher_id) REFERENCES Teacher(teacher_id)
);
CREATE TABLE Enrollments (
    enrollment_id INT PRIMARY KEY,
    student_id INT,
    course_id INT,
    enrollment_date DATE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);
CREATE TABLE Payments (
    payment_id INT PRIMARY KEY,
    student_id INT,
    amount DECIMAL(10, 2),
    payment_date DATE,
    FOREIGN KEY (student_id) REFERENCES Students(student_id)
);

/*Create an ERD (Entity Relationship Diagram) for the database.*/
/* REFER er_diagram.png*/

/*Insert at least 10 sample records into each of the following tables.
i. Students
ii. Courses
iii. Enrollments
iv. Teacher
v. Payments*/

INSERT INTO Students VALUES
(1, 'Siddharth', 'Menon', '2001-03-12', 'siddharth.menon@example.com', '9876543210'),
(2, 'Ritika', 'Agarwal', '2000-07-25', 'ritika.agarwal@example.com', '8765432109'),
(3, 'Raj', 'Verma', '2002-01-05', 'raj.verma@example.com', '7654321098'),
(4, 'Anjali', 'Mehra', '1999-11-22', 'anjali.mehra@example.com', '6543210987'),
(5, 'Kunal', 'Deshmukh', '2001-05-14', 'kunal.deshmukh@example.com', '5432109876'),
(6, 'Neha', 'Raman', '2000-12-30', 'neha.raman@example.com', '4321098765'),
(7, 'Aakash', 'Sharma', '2002-08-19', 'aakash.sharma@example.com', '3210987654'),
(8, 'Sneha', 'Kapoor', '1998-10-09', 'sneha.kapoor@example.com', '2109876543'),
(9, 'Manoj', 'Joshi', '2003-04-01', 'manoj.joshi@example.com', '1098765432'),
(10, 'Priya', 'Singh', '2001-06-27', 'priya.singh@example.com', '9988776655');

INSERT INTO Teacher VALUES
(1, 'Aditya', 'Kulkarni', 'aditya.kulkarni@faculty.in'),
(2, 'Neha', 'Raman', 'neha.raman@faculty.in'),
(3, 'Ramesh', 'Naidu', 'ramesh.naidu@faculty.in'),
(4, 'Sonal', 'Iyer', 'sonal.iyer@faculty.in'),
(5, 'Arvind', 'Mehta', 'arvind.mehta@faculty.in'),
(6, 'Kavita', 'Gupta', 'kavita.gupta@faculty.in'),
(7, 'Suresh', 'Thakur', 'suresh.thakur@faculty.in'),
(8, 'Priya', 'Banerjee', 'priya.banerjee@faculty.in'),
(9, 'Vikram', 'Malik', 'vikram.malik@faculty.in'),
(10, 'Deepa', 'Chopra', 'deepa.chopra@faculty.in');

INSERT INTO Courses VALUES
(201, 'Linear Algebra', 3, 1),
(202, 'Thermodynamics', 4, 2),
(203, 'Organic Chemistry', 3, 3),
(204, 'Genetics', 4, 4),
(205, 'Data Structures', 3, 5),
(206, 'British Literature', 2, 6),
(207, 'World History', 2, 7),
(208, 'Earth Sciences', 3, 8),
(209, 'Modern Art', 2, 9),
(210, 'Public Policy', 3, 10);

INSERT INTO Enrollments VALUES
(101, 1, 201, '2025-02-01'),
(102, 2, 202, '2025-02-02'),
(103, 3, 203, '2025-02-03'),
(104, 4, 204, '2025-02-04'),
(105, 5, 205, '2025-02-05'),
(106, 6, 206, '2025-02-06'),
(107, 7, 207, '2025-02-07'),
(108, 8, 208, '2025-02-08'),
(109, 9, 209, '2025-02-09'),
(110, 10, 210, '2025-02-10');

INSERT INTO Payments VALUES
(201, 1, 5600.00, '2025-01-15'),
(202, 2, 5700.00, '2025-01-16'),
(203, 3, 5900.00, '2025-01-17'),
(204, 4, 5500.00, '2025-01-18'),
(205, 5, 5800.00, '2025-01-19'),
(206, 6, 5750.00, '2025-01-20'),
(207, 7, 5650.00, '2025-01-21'),
(208, 8, 5850.00, '2025-01-22'),
(209, 9, 5900.00, '2025-01-23'),
(210, 10, 6000.00, '2025-01-24');
