package com.hexaware.sis.service;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.hexaware.sis.dao.CourseDAOImpl;
import com.hexaware.sis.dao.EnrollmentDAOImpl;
import com.hexaware.sis.dao.ICourseDAO;
import com.hexaware.sis.dao.IEnrollmentDAO;
import com.hexaware.sis.dao.IPaymentDAO;
import com.hexaware.sis.dao.IStudentDAO;
import com.hexaware.sis.dao.ITeacherDAO;
import com.hexaware.sis.dao.PaymentDAOImpl;
import com.hexaware.sis.dao.StudentDAOImpl;
import com.hexaware.sis.dao.TeacherDAOImpl;
import com.hexaware.sis.entity.Course;
import com.hexaware.sis.entity.Enrollment;
import com.hexaware.sis.entity.Payment;
import com.hexaware.sis.entity.Student;
import com.hexaware.sis.entity.Teacher;
import com.hexaware.sis.util.DBConnUtil;

public class SISServiceImpl implements ISISService {

    private final IEnrollmentDAO enrollmentDAO = new EnrollmentDAOImpl();
    private final ICourseDAO courseDAO = new CourseDAOImpl();
    private final IPaymentDAO paymentDAO = new PaymentDAOImpl();
    private final IStudentDAO studentDAO = new StudentDAOImpl();
    private final ITeacherDAO teacherDAO = new TeacherDAOImpl();

    @Override
    public String enrollStudentInCourse(int studentId, int courseId, Date enrollmentDate) throws Exception {
        return enrollmentDAO.enrollStudentInCourse(studentId, courseId, enrollmentDate);
    }

    @Override
    public List<String> getAllCoursesForDisplay() {
        return courseDAO.getCourseDisplayList();
    }

    @Override
    public String enrollStudentByCourseSelection(String fname, String lname, Date dob, String email, String phone, String courseInput) throws Exception {
        int courseId = courseDAO.getCourseIdByInput(courseInput);
        if (courseId == -1) return "Course not found.";

        int studentId = studentDAO.insertStudent(fname, lname, dob, email, phone);

        try (Connection conn = DBConnUtil.getConnection("db.properties")) {
            conn.setAutoCommit(false);
            Enrollment enrollment = new Enrollment(studentId, courseId, new java.util.Date());
            String result = enrollmentDAO.enrollStudent(enrollment, conn);
            conn.commit();
            return result;
        } catch (Exception e) {
            return "Enrollment failed: " + e.getMessage();
        }
    }

    @Override
    public String assignTeacherToCourse(String firstName, String lastName, String email, String courseCode) {
        try (Connection conn = DBConnUtil.getConnection("db.properties")) {
            conn.setAutoCommit(false);

            Teacher teacher = new Teacher(firstName, lastName, email);
            int teacherId = teacherDAO.addTeacherAndReturnId(teacher, conn);
            if (teacherId == -1) throw new Exception("Teacher insert failed");

            int courseId = courseDAO.getCourseIdByInput(courseCode);
            if (courseId == -1) throw new Exception("Course not found with code: " + courseCode);

            boolean success = courseDAO.assignTeacherToCourse(courseId, teacherId, conn);
            if (!success) throw new Exception("Assignment failed");

            conn.commit();
            return "Teacher assigned to course successfully.";
        } catch (Exception e) {
            System.out.println("Error assigning teacher: " + e.getMessage());
            return "Failed to assign teacher.";
        }
    }

    @Override
    public List<String> getUnpaidEnrolledCourses(int studentId) {
        List<Course> enrolledCourses = enrollmentDAO.getCoursesByStudentId(studentId);
        List<Payment> payments = paymentDAO.getPaymentsByStudentId(studentId);

        Set<Integer> paidCourseIds = payments.stream()
                .map(Payment::getCourseId)
                .collect(Collectors.toSet());

        return enrolledCourses.stream()
                .filter(c -> !paidCourseIds.contains(c.getCourseId()))
                .map(c -> c.getCourseName() + " (" + c.getCourseCode() + ")")
                .collect(Collectors.toList());
    }

    @Override
    public String recordStudentPayment(int studentId, double paymentAmount, Date paymentDate, String courseInput) {
        try (Connection conn = DBConnUtil.getConnection("db.properties")) {
            conn.setAutoCommit(false);

            Student student = studentDAO.getStudentById(studentId);

            int courseId = courseDAO.getCourseIdByInput(courseInput);
            if (courseId == -1) return "Course not found.";

            if (student == null) {
                // Auto-enroll student in the course if student not found
                Enrollment enrollment = new Enrollment(studentId, courseId, paymentDate);
                String enrollResult = enrollmentDAO.enrollStudent(enrollment, conn);
                if (!enrollResult.toLowerCase().contains("success")) {
                    conn.rollback();
                    return "Auto-enrollment failed: " + enrollResult;
                }

                Payment payment = new Payment(studentId, courseId, paymentAmount, paymentDate);
                String paymentResult = paymentDAO.addPayment(payment, conn);
                if (!paymentResult.toLowerCase().contains("success")) {
                    conn.rollback();
                    return "Payment failed.";
                }

                conn.commit();
                return "Auto-enrolled and payment recorded successfully.";
            } else {
                List<Course> enrolledCourses = enrollmentDAO.getCoursesByStudentId(studentId);
                List<Payment> payments = paymentDAO.getPaymentsByStudentId(studentId);

                boolean isEnrolled = enrolledCourses.stream()
                    .anyMatch(c -> c.getCourseId() == courseId);

                if (!isEnrolled) {
                    Enrollment enrollment = new Enrollment(studentId, courseId, paymentDate);
                    String enrollResult = enrollmentDAO.enrollStudent(enrollment, conn);
                    if (!enrollResult.toLowerCase().contains("success")) {
                        conn.rollback();
                        return "Enrollment failed.";
                    }
                }

                Payment payment = new Payment(studentId, courseId, paymentAmount, paymentDate);
                String paymentResult = paymentDAO.addPayment(payment, conn);
                if (!paymentResult.toLowerCase().contains("success")) {
                    conn.rollback();
                    return "Payment failed.";
                }

                conn.commit();
                return "Payment recorded successfully.";
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    @Override
    public List<String> getCoursesByStudent(int studentId) {
        List<Course> courses = enrollmentDAO.getCoursesByStudentId(studentId);
        List<String> courseList = new ArrayList<>();
        for (Course c : courses) {
            courseList.add(c.getCourseName() + " (" + c.getCourseCode() + ")");
        }
        return courseList;
    }

    @Override
    public List<String> getEnrollmentsByStudent(int studentId) throws Exception {
        return enrollmentDAO.getEnrollmentsByStudent(studentId);
    }

    @Override
    public List<String> getCoursesByTeacher(int teacherId) throws Exception {
        return courseDAO.getCoursesByTeacher(teacherId);
    }

    @Override
    public List<String> generateEnrollmentReport() throws Exception {
        return enrollmentDAO.generateEnrollmentReport();
    }

    @Override
    public List<String> generatePaymentReport(int studentId) throws Exception {
        return paymentDAO.generatePaymentReport(studentId);
    }

    @Override
    public int getEnrollmentCount(int courseId) throws Exception {
        return enrollmentDAO.getEnrollmentCount(courseId);
    }

    @Override
    public double getTotalPaymentsForCourse(int courseId) throws Exception {
        return paymentDAO.getTotalPaymentsForCourse(courseId);
    }

    @Override
    public List<String> getStudentsEnrolledInCourse(int courseId) throws Exception {
        List<Student> students = enrollmentDAO.getStudentsByCourseId(courseId);
        List<String> result = new ArrayList<>();
        for (Student s : students) {
            result.add(s.getFirstName() + " " + s.getLastName() + " | " + s.getEmail());
        }
        return result;
    }

    @Override
    public List<String> getEnrollmentReportForCourse(String courseInput) throws Exception {
        return enrollmentDAO.getEnrollmentReportForCourse(courseInput);
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        return enrollmentDAO.getStudentsByCourseId(courseId);
    }
    
    @Override
    public int getCourseIdByInput(String input) {
        return courseDAO.getCourseIdByInput(input);
    }

}
