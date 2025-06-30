package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.hexaware.sis.entity.Teacher;
import com.hexaware.sis.util.DBConnUtil;

public class TeacherDAOImpl implements ITeacherDAO {

    @Override
    public boolean exists(int teacherId) throws Exception {
        String sql = "SELECT 1 FROM Teacher WHERE teacherId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public int addTeacherAndReturnId(Teacher teacher, Connection conn) throws SQLException {
        String sql = "INSERT INTO Teacher (firstName, lastName, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getLastName());
            ps.setString(3, teacher.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }
}