package main.java.dao;

import main.java.basicClasses.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'doctor'";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                doctors.add(new Doctor(
                        rs.getString("username"),
                        rs.getString("hashed_password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("age"),
                        rs.getString("specialty"),
                        rs.getString("salt"),
                        rs.getString("amka") // Προσθήκη AMKA
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public static boolean addDoctor(Doctor doctor) {
        return UserDAO.addUser(doctor, "doctor", doctor.getAmka()); // Προσθήκη AMKA
    }

    public static boolean deleteDoctor(String username) {
        return UserDAO.deleteUser(username);
    }
}