package main.java.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:/Users/aristomenisg/IdeaProjects/Assignment2/clinicDB.db";
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver not found.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}