package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.sis.entity.Course;
import com.hexaware.sis.util.DBConnUtil;

public class CourseDAOImpl implements ICourseDAO {

    @Override
    public List<String> getCourseDisplayList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT courseName, courseCode FROM Course";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("courseName") + " (" + rs.getString("courseCode") + ")");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving courses: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int getCourseIdByInput(String courseInput) {
        String sql = "SELECT courseId FROM Course WHERE courseCode = ? OR courseName = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseInput);
            ps.setString(2, courseInput);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("courseId") : -1;
            }
        } catch (SQLException e) {
            System.out.println("Error finding courseId: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean assignTeacherToCourse(int courseId, int teacherId, Connection conn) throws SQLException {
        String sql = "UPDATE Course SET teacherId = ? WHERE courseId = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ps.setInt(2, courseId);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    @Override
    public List<String> getCoursesByTeacher(int teacherId) throws Exception {
        List<String> courses = new ArrayList<>();
        String sql = "SELECT courseName, courseCode FROM Course WHERE teacherId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    courses.add(rs.getString("courseName") + " (" + rs.getString("courseCode") + ")");
                }
            }
        }
        return courses;
    }

    @Override
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT courseId, courseName, courseCode FROM Course";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                    rs.getInt("courseId"),
                    rs.getString("courseName"),
                    rs.getString("courseCode")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all courses: " + e.getMessage());
        }
        return list;
    }
}