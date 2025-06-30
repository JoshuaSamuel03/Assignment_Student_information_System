package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.hexaware.sis.entity.Course;
import com.hexaware.sis.entity.Enrollment;
import com.hexaware.sis.entity.Student;

public interface IEnrollmentDAO {

    String enrollStudent(Enrollment enrollment, Connection conn) throws Exception;

    String enrollStudentInCourse(int studentId, int courseId, Date enrollmentDate) throws Exception;

    List<Course> getCoursesByStudentId(int studentId);

    List<String> getEnrollmentsByStudent(int studentId) throws Exception;

    List<String> generateEnrollmentReport() throws Exception;

    List<String> getEnrollmentReportForCourse(String courseInput) throws Exception;

    int getEnrollmentCount(int courseId) throws Exception;

    List<Student> getStudentsByCourseId(int courseId);

    List<Map<String, String>> runDynamicQuery(String table, String columns, String whereClause, String orderBy) throws Exception;
}
