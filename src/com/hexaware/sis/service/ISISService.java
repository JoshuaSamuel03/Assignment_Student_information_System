package com.hexaware.sis.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.hexaware.sis.entity.Student;

public interface ISISService {
    String enrollStudentInCourse(int studentId, int courseId, Date enrollmentDate) throws Exception;
    List<String> getAllCoursesForDisplay();
    String enrollStudentByCourseSelection(String fname, String lname, Date dob, String email, String phone, String courseInput) throws Exception;    
    String recordStudentPayment(int studentId, double amount, Date paymentDate, String courseInput);
    String assignTeacherToCourse(String firstName, String lastName, String email, String courseCode);
    List<String> getEnrollmentsByStudent(int studentId) throws Exception;
    List<String> getCoursesByTeacher(int teacherId) throws Exception;
    List<String> getCoursesByStudent(int studentId);
    List<String> getUnpaidEnrolledCourses(int studentId);
    List<String> generateEnrollmentReport() throws Exception;
    List<String> generatePaymentReport(int studentId) throws Exception;
    List<String> getEnrollmentReportForCourse(String courseInput) throws Exception;
    int getEnrollmentCount(int courseId) throws Exception;
    double getTotalPaymentsForCourse(int courseId) throws Exception;
    List<String> getStudentsEnrolledInCourse(int courseId) throws Exception;
    List<Student> getStudentsByCourseId(int courseId);
    int getCourseIdByInput(String input);

}
