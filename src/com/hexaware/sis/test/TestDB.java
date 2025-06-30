package com.hexaware.sis.test;

import com.hexaware.sis.util.DBConnUtil;
import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection conn = DBConnUtil.getConnection("db.properties");
        if (conn != null) {
            System.out.println("Database connection successful!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}

