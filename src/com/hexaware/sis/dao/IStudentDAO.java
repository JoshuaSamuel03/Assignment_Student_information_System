package com.hexaware.sis.dao;

import java.sql.Date;

import com.hexaware.sis.entity.Student;

public interface IStudentDAO {

    boolean exists(int studentId) throws Exception;

    int insertStudent(String fname, String lname, Date dob, String email, String phoneNumber) throws Exception;

    Student getStudentById(int studentId) throws Exception;
}
