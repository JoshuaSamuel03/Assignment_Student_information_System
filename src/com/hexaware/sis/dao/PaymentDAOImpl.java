package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.sis.entity.Payment;
import com.hexaware.sis.util.DBConnUtil;

public class PaymentDAOImpl implements IPaymentDAO {

    @Override
    public String addPayment(Payment payment, Connection conn) {
        String sql = "INSERT INTO Payment (studentId, courseId, paymentAmount, paymentDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, payment.getStudentId());
            ps.setInt(2, payment.getCourseId());
            ps.setDouble(3, payment.getPaymentAmount());
            ps.setDate(4, new Date(payment.getPaymentDate().getTime()));
            int rows = ps.executeUpdate();
            return rows > 0 ? "Payment added successfully." : "Failed to add payment.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public List<Payment> getPaymentsByStudentId(int studentId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payment WHERE studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment p = new Payment(
                        rs.getInt("paymentId"),
                        rs.getInt("studentId"),
                        rs.getInt("courseId"),
                        rs.getDouble("paymentAmount"),
                        rs.getDate("paymentDate")
                    );
                    payments.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Error retrieving payments: " + e.getMessage());
        }
        return payments;
    }

    @Override
    public List<String> generatePaymentReport(int studentId) {
        List<String> report = new ArrayList<>();
        String sql = "SELECT p.paymentId, p.paymentAmount, p.paymentDate, c.courseName " +
                     "FROM Payment p JOIN Course c ON p.courseId = c.courseId WHERE p.studentId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    report.add("Payment ID: " + rs.getInt("paymentId") +
                               " | Amount: $" + rs.getDouble("paymentAmount") +
                               " | Date: " + rs.getDate("paymentDate") +
                               " | Course: " + rs.getString("courseName"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error generating payment report: " + e.getMessage());
        }
        return report;
    }

    @Override
    public double getTotalPaymentsForCourse(int courseId) {
        double total = 0;
        String sql = "SELECT SUM(paymentAmount) AS total FROM Payment WHERE courseId = ?";
        try (Connection conn = DBConnUtil.getConnection("db.properties");
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            System.out.println("Error calculating total payments: " + e.getMessage());
        }
        return total;
    }
}