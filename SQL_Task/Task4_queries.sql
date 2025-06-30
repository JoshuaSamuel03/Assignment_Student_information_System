/* Write an SQL query to calculate the average number of students enrolled in each course. Use aggregate functions and subqueries to achieve this. */

SELECT AVG(student_count) AS avg_enrollment
FROM (
  SELECT course_id, COUNT(student_id) AS student_count
  FROM Enrollments
  GROUP BY course_id
) AS course_counts;

/* Identify the student(s) who made the highest payment. Use a subquery to find the maximum payment amount and then retrieve the student(s) associated 
with that amount. */

SELECT s.student_id, s.first_name, s.last_name, p.amount
FROM Payments p
JOIN Students s ON p.student_id = s.student_id
WHERE p.amount = (
  SELECT MAX(amount) FROM Payments
);

/* Retrieve a list of courses with the highest number of enrollments. Use subqueries to find the course(s) with the maximum enrollment count. */

SELECT c.course_id, c.course_name, COUNT(e.student_id) AS enrollment_count
FROM Courses c
JOIN Enrollments e ON c.course_id = e.course_id
GROUP BY c.course_id, c.course_name
HAVING COUNT(e.student_id) = (
  SELECT MAX(enrollment_total) FROM (
    SELECT COUNT(student_id) AS enrollment_total
    FROM Enrollments
    GROUP BY course_id
  ) AS enroll_counts
);

/* Calculate the total payments made to courses taught by each teacher. Use subqueries to sum payments for each teacher's courses. */

SELECT t.teacher_id, t.first_name, t.last_name,
  (
    SELECT SUM(p.amount)
    FROM Payments p
    JOIN Enrollments e ON p.student_id = e.student_id
    JOIN Courses c2 ON e.course_id = c2.course_id
    WHERE c2.teacher_id = t.teacher_id
  ) AS total_payment
FROM Teacher t;

/* Identify students who are enrolled in all available courses. Use subqueries to compare a student's enrollments with the total number of courses. */

SELECT s.student_id, s.first_name, s.last_name
FROM Students s
WHERE NOT EXISTS (
  SELECT course_id FROM Courses
  EXCEPT
  SELECT course_id FROM Enrollments e WHERE e.student_id = s.student_id
);

/* Retrieve the names of teachers who have not been assigned to any courses. Use subqueries to find teachers with no course assignments. */

SELECT teacher_id, first_name, last_name
FROM Teacher
WHERE teacher_id NOT IN (
  SELECT DISTINCT teacher_id FROM Courses WHERE teacher_id IS NOT NULL
);

/* Calculate the average age of all students. Use subqueries to calculate the age of each student based on their date of birth. */

SELECT AVG(age) AS avg_age
FROM (
  SELECT TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) AS age
  FROM Students
) AS student_ages;

/* Identify courses with no enrollments. Use subqueries to find courses without enrollment records. */

SELECT course_id, course_name
FROM Courses
WHERE course_id NOT IN (
  SELECT DISTINCT course_id FROM Enrollments
);

/* Calculate the total payments made by each student for each course they are enrolled in. Use subqueries and aggregate functions to sum payments. */

SELECT s.student_id, s.first_name, s.last_name, c.course_name, 
  (
    SELECT SUM(p.amount)
    FROM Payments p
    WHERE p.student_id = s.student_id
  ) AS total_payment
FROM Students s
JOIN Enrollments e ON s.student_id = e.student_id
JOIN Courses c ON e.course_id = c.course_id
GROUP BY s.student_id, c.course_id;

/* Identify students who have made more than one payment. Use subqueries and aggregate functions to count payments per student and filter for those with 
counts greater than one. */

SELECT s.student_id, s.first_name, s.last_name
FROM Students s
JOIN Payments p ON s.student_id = p.student_id
GROUP BY s.student_id, s.first_name, s.last_name
HAVING COUNT(p.payment_id) > 1;

/* Write an SQL query to calculate the total payments made by each student. Join the "Students" table with the "Payments" table and use GROUP BY to 
calculate the sum of payments for each student. */

SELECT s.student_id, s.first_name, s.last_name, SUM(p.amount) AS total_payment
FROM Students s
JOIN Payments p ON s.student_id = p.student_id
GROUP BY s.student_id, s.first_name, s.last_name;

/* Retrieve a list of course names along with the count of students enrolled in each course. Use JOIN operations between the "Courses" table and the
"Enrollments" table and GROUP BY to count enrollments. */

SELECT c.course_name, COUNT(e.student_id) AS student_count
FROM Courses c
LEFT JOIN Enrollments e ON c.course_id = e.course_id
GROUP BY c.course_name;

/* Calculate the average payment amount made by students. Use JOIN operations between the "Students" table and the "Payments" table and GROUP BY to 
calculate the average. */
SELECT s.student_id, s.first_name, s.last_name, AVG(p.amount) AS avg_payment
FROM Students s
JOIN Payments p ON s.student_id = p.student_id
GROUP BY s.student_id, s.first_name, s.last_name;
