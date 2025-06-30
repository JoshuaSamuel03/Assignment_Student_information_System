package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.hexaware.sis.entity.Teacher;

public interface ITeacherDAO {

    boolean exists(int teacherId) throws Exception;

    int addTeacherAndReturnId(Teacher teacher, Connection conn) throws SQLException;
}
