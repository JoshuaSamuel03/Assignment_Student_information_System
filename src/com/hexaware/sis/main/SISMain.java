package com.hexaware.sis.main;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.hexaware.sis.service.ISISService;
import com.hexaware.sis.service.SISServiceImpl;

public class SISMain {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ISISService sis = new SISServiceImpl();

    public static void main(String[] args) {
        System.out.println("Welcome to Student Information System");
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Assign Teacher to Course");
        System.out.println("3. Record Student Payment");
        System.out.println("4. View Enrollments by Student");
        System.out.println("5. View Courses by Teacher");
        System.out.println("6. Generate Enrollment Report");
        System.out.println("7. Generate Payment Report");
        System.out.println("8. Statistics of Course");
        System.out.println("9. Exit");
        System.out.print("Enter choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        try {
        	switch (choice) {
            case 1:
                enrollStudent();
                break;
            case 2:
                assignTeacher();
                break;
            case 3:
                recordPayment();
                break;
            case 4:
                viewEnrollments();
                break;
            case 5:
                viewCoursesByTeacher();
                break;
            case 6:
                generateEnrollmentReportForCourse();
                break;
            case 7:
                generatePaymentReport();
                break;
            case 8:
                courseStatistics();
                break;
            case 9:
                System.out.println("Goodbye!");
                return;
            default:
                System.out.println("Invalid choice.");
                break;
        	}
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void enrollStudent() throws Exception {
        System.out.println("Enter Student Details");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("Date of Birth (yyyy-mm-dd): ");
        Date dob = Date.valueOf(scanner.nextLine().trim());
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine().trim();

        List<String> courseList = sis.getAllCoursesForDisplay();
        System.out.println("\nAvailable Courses:");
        courseList.forEach(System.out::println);

        System.out.print("\nEnter Course Code or Course Name to Enroll: ");
        String input = scanner.nextLine().trim();

        String result = sis.enrollStudentByCourseSelection(firstName, lastName, dob, email, phone, input);
        System.out.println(result);
    }

    private static void assignTeacher() throws Exception {
        System.out.print("Enter teacher first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter teacher last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter teacher email: ");
        String email = scanner.nextLine();

        System.out.println("Courses:");
        sis.getAllCoursesForDisplay().forEach(System.out::println);

        System.out.print("Enter Course Code or Name to assign teacher: ");
        String courseCode = scanner.nextLine();

        String result = sis.assignTeacherToCourse(firstName, lastName, email, courseCode);
        System.out.println(result);
    }

    private static void recordPayment() throws Exception {
        System.out.print("Enter student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter payment amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        System.out.print("Enter payment date (yyyy-MM-dd): ");
        Date paymentDate = Date.valueOf(scanner.nextLine());

        List<String> unpaidCourses = sis.getUnpaidEnrolledCourses(studentId);
        List<String> displayList;

        if (!unpaidCourses.isEmpty()) {
            System.out.println("Courses you're enrolled in but haven't paid for:");
            displayList = unpaidCourses;
        } else {
            System.out.println("No unpaid enrolled courses found.");
            System.out.println("\nAvailable Courses:");
            displayList = sis.getAllCoursesForDisplay();
        }

        displayList.forEach(c -> System.out.println("- " + c));

        System.out.print("Enter Course Code or Course Name to pay for: ");
        String courseInput = scanner.nextLine().trim();

        String result = sis.recordStudentPayment(studentId, amount, paymentDate, courseInput);
        System.out.println(result);
    }


    private static void viewEnrollments() throws Exception {
        System.out.print("Enter student ID: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        List<String> enrollments = sis.getEnrollmentsByStudent(studentId);
        System.out.println("Enrollments:");
        enrollments.forEach(System.out::println);
    }

    private static void viewCoursesByTeacher() throws Exception {
        System.out.print("Enter teacher ID: ");
        int teacherId = Integer.parseInt(scanner.nextLine());

        List<String> courses = sis.getCoursesByTeacher(teacherId);
        System.out.println("Courses Assigned:");
        courses.forEach(System.out::println);
    }

    private static void generateEnrollmentReportForCourse() throws Exception {
        List<String> courseList = sis.getAllCoursesForDisplay();
        System.out.println("\n📚 Available Courses:");
        courseList.forEach(System.out::println);

        System.out.print("\nEnter course name or course code: ");
        String courseInput = scanner.nextLine().trim();

        List<String> report = sis.getEnrollmentReportForCourse(courseInput);
        if (report.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            System.out.println("Enrollment Report:");
            report.forEach(System.out::println);
        }
    }


    private static void generatePaymentReport() throws Exception {
        System.out.print("Enter student ID to generate payment report: ");
        int studentId = Integer.parseInt(scanner.nextLine());

        List<String> report = sis.generatePaymentReport(studentId);
        System.out.println("Payment Report:");
        if (report.isEmpty()) {
            System.out.println("No payments found for student ID: " + studentId);
        } else {
            report.forEach(System.out::println);
        }
    }

    private static void courseStatistics() throws Exception {
        List<String> courseList = sis.getAllCoursesForDisplay();
        System.out.println("\nAvailable Courses:");
        courseList.forEach(System.out::println);

        System.out.print("\nEnter Course Code or Course Name: ");
        String courseInput = scanner.nextLine().trim();

        int courseId = sis.getCourseIdByInput(courseInput);
        if (courseId == -1) {
            System.out.println("Course not found.");
            return;
        }

        int count = sis.getEnrollmentCount(courseId);
        double total = sis.getTotalPaymentsForCourse(courseId);
        List<String> students = sis.getStudentsEnrolledInCourse(courseId);

        System.out.println("Course Statistics:");
        System.out.println("Enrolled Students Count: " + count);
        System.out.println("Total Payments: $" + total);
        System.out.println("Student List:");
        students.forEach(System.out::println);
    }

}
