package com.hexaware.sis.dao;

import java.sql.Connection;
import java.util.List;

import com.hexaware.sis.entity.Payment;

public interface IPaymentDAO {

    String addPayment(Payment payment, Connection conn) throws Exception;

    List<Payment> getPaymentsByStudentId(int studentId);

    List<String> generatePaymentReport(int studentId) throws Exception;

    double getTotalPaymentsForCourse(int courseId) throws Exception;
}
