package main.java.dao;

import java.sql.*;

public class DBInit {
    private static final String URL = "jdbc:sqlite:appointment.db";

    public static void initializeDatabase() {
        String createDoctorsTable = "CREATE TABLE IF NOT EXISTS doctors (" +
                "username VARCHAR(50) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "password VARCHAR(100) NOT NULL," +
                "specialty VARCHAR(50) NOT NULL," +
                "phone VARCHAR(20)," +
                "email VARCHAR(100)," +
                "address VARCHAR(200))";

        String createPatientsTable = "CREATE TABLE IF NOT EXISTS patients (" +
                "username VARCHAR(50) PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "password VARCHAR(100) NOT NULL," +
                "phone VARCHAR(20)," +
                "email VARCHAR(100)," +
                "address VARCHAR(200))";

        String createAppointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "doctor_username VARCHAR(50) NOT NULL," +
                "patient_username VARCHAR(50) NOT NULL," +
                "datetime VARCHAR(50) NOT NULL," +
                "status VARCHAR(20) NOT NULL," +
                "FOREIGN KEY (doctor_username) REFERENCES doctors(username)," +
                "FOREIGN KEY (patient_username) REFERENCES patients(username))";

        String createAvailabilityTable = "CREATE TABLE IF NOT EXISTS availability (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "doctor_username VARCHAR(50) NOT NULL," +
                "slot VARCHAR(100) NOT NULL," +
                "FOREIGN KEY (doctor_username) REFERENCES doctors(username))";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createDoctorsTable);
            stmt.execute(createPatientsTable);
            stmt.execute(createAppointmentsTable);
            stmt.execute(createAvailabilityTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}