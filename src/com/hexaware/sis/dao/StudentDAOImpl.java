package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hexaware.sis.entity.Student;
import com.hexaware.sis.util.DBConnUtil;

public class StudentDAOImpl implements IStudentDAO {

    @Override
    public boolean exists(int studentId) throws Exception {
        String sql = "SELECT 1 FROM Student WHERE studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int insertStudent(String fname, String lname, Date dob, String email, String phoneNumber) throws Exception {
        String sql = "INSERT INTO Student (firstName, lastName, dateOfBirth, email, phoneNumber) VALUES (?, ?, ?, ?, ?)";
        String getIdSQL = "SELECT LAST_INSERT_ID() AS id";

        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fname);
            ps.setString(2, lname);
            ps.setDate(3, dob);
            ps.setString(4, email);
            ps.setString(5, phoneNumber);
            ps.executeUpdate();

            try (PreparedStatement ps2 = conn.prepareStatement(getIdSQL);
                 ResultSet rs = ps2.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        }
        throw new Exception("Failed to insert student.");
    }

    @Override
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM Student WHERE studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("studentId"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getDate("dateOfBirth"),
                        rs.getString("email"),
                        rs.getString("phoneNumber")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching student: " + e.getMessage());
        }
        return null;
    }
}