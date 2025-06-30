package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hexaware.sis.entity.Course;
import com.hexaware.sis.entity.Enrollment;
import com.hexaware.sis.entity.Student;
import com.hexaware.sis.util.DBConnUtil;

public class EnrollmentDAOImpl implements IEnrollmentDAO {

    @Override
    public String enrollStudentInCourse(int studentId, int courseId, Date enrollmentDate) throws Exception {
        String sql = "INSERT INTO Enrollment (studentId, courseId, enrollmentDate) VALUES (?, ?, ?)";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, courseId);
            ps.setDate(3, enrollmentDate);
            int rows = ps.executeUpdate();
            return rows > 0 ? "✅ Enrollment successful." : "Enrollment failed.";
        }
    }

    @Override
    public String enrollStudent(Enrollment enrollment, Connection conn) throws SQLException {
        String sql = "INSERT INTO Enrollment (studentId, courseId, enrollmentDate) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollment.getStudentId());
            ps.setInt(2, enrollment.getCourseId());
            ps.setDate(3, new java.sql.Date(enrollment.getEnrollmentDate().getTime()));
            int rows = ps.executeUpdate();
            return rows > 0 ? "✅ Enrolled successfully." : "Enrollment failed.";
        }
    }

    @Override
    public List<Course> getCoursesByStudentId(int studentId) {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.courseId, c.courseName, c.courseCode FROM Course c JOIN Enrollment e ON c.courseId = e.courseId WHERE e.studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(new Course(rs.getInt("courseId"), rs.getString("courseName"), rs.getString("courseCode")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
        return courses;
    }

    @Override
    public List<String> getEnrollmentsByStudent(int studentId) {
        List<String> enrollments = new ArrayList<>();
        String sql = "SELECT c.courseName, e.enrollmentDate FROM Enrollment e JOIN Course c ON e.courseId = c.courseId WHERE e.studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enrollments.add("Course: " + rs.getString("courseName") + ", Date: " + rs.getDate("enrollmentDate"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    @Override
    public List<Student> getStudentsByCourseId(int courseId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.studentId, s.firstName, s.lastName, s.dateOfBirth, s.email, s.phoneNumber " +
                     "FROM Student s JOIN Enrollment e ON s.studentId = e.studentId WHERE e.courseId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    students.add(new Student(
                        rs.getInt("studentId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getDate("dateOfBirth"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting students: " + e.getMessage());
        }
        return students;
    }

    @Override
    public int getEnrollmentCount(int courseId) throws Exception {
        String sql = "SELECT COUNT(*) FROM Enrollment WHERE courseId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    @Override
    public List<String> generateEnrollmentReport() throws Exception {
        List<String> report = new ArrayList<>();
        String sql = "SELECT s.firstName, s.lastName, c.courseName, e.enrollmentDate " +
                     "FROM Enrollment e JOIN Student s ON e.studentId = s.studentId " +
                     "JOIN Course c ON e.courseId = c.courseId ORDER BY e.enrollmentDate DESC";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                report.add(rs.getString("firstName") + " " + rs.getString("lastName") +
                           " | " + rs.getString("courseName") + " | " + rs.getDate("enrollmentDate"));
            }
        }
        return report;
    }

    @Override
    public List<String> getEnrollmentReportForCourse(String courseInput) throws Exception {
        List<String> report = new ArrayList<>();
        String sql = "SELECT s.firstName, s.lastName, e.enrollmentDate " +
                     "FROM Enrollment e JOIN Student s ON e.studentId = s.studentId " +
                     "JOIN Course c ON e.courseId = c.courseId " +
                     "WHERE c.courseName = ? OR c.courseCode = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseInput);
            ps.setString(2, courseInput);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.add(rs.getString("firstName") + " " + rs.getString("lastName") +
                               " | Enrolled on: " + rs.getDate("enrollmentDate"));
                }
            }
        }
        return report;
    }

    @Override
    public List<Map<String, String>> runDynamicQuery(String table, String columns, String whereClause, String orderBy) throws Exception {
        List<Map<String, String>> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT " + columns + " FROM " + table);
        if (whereClause != null && !whereClause.isBlank()) {
            sql.append(" WHERE ").append(whereClause);
        }
        if (orderBy != null && !orderBy.isBlank()) {
            sql.append(" ORDER BY ").append(orderBy);
        }

        try (Connection conn = DBConnUtil.getConnection("db.properties");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    row.put(meta.getColumnLabel(i), rs.getString(i));
                }
                results.add(row);
            }
        }

        return results;
    }
}