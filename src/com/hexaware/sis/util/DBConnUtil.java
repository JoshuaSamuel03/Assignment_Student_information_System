package com.hexaware.sis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnUtil {
    public static Connection getConnection(String filename) {
        Properties props = DBPropertyUtil.loadProperties(filename);
        String url = props.getProperty("db.connection");
        String user = props.getProperty("db.username");
        String pass = props.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
