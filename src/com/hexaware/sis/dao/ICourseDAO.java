package com.hexaware.sis.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ICourseDAO {
    List<String> getCourseDisplayList();

    int getCourseIdByInput(String input);

    boolean assignTeacherToCourse(int courseId, int teacherId, Connection conn) throws SQLException;

    List<String> getCoursesByTeacher(int teacherId) throws Exception;

    List<com.hexaware.sis.entity.Course> getAllCourses();
}
