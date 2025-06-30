package com.hexaware.sis.entity;

import java.util.Date;

public class Payment {
    private int paymentId;
    private int studentId;
    private int courseId;
    private double paymentAmount;
    private Date paymentDate;

    public Payment() {}

    public Payment(int studentId, int courseId, double paymentAmount, Date paymentDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public Payment(int paymentId, int studentId, int courseId, double paymentAmount, Date paymentDate) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
