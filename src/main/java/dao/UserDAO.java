package main.java.dao;

import main.java.basicClasses.*;
import java.sql.*;

public class UserDAO {

    public static User findByUsernameAndType(String username, String userType) {
        String query = "SELECT * FROM users WHERE username = ? AND role = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, userType);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String hashedPassword = rs.getString("hashed_password");
                String salt = rs.getString("salt");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                int age = rs.getInt("age");
                String amka = rs.getString("amka");

                switch (userType) {
                    case "patient":
                        return new Patient(username, hashedPassword, name, surname, age, amka, salt);
                    case "doctor":
                        String specialty = rs.getString("specialty");
                        return new Doctor(username, hashedPassword, name, surname, age, specialty, salt, amka);
                    case "admin":
                        return new Admin(username, hashedPassword, name, surname, age, salt, amka);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addUser(User user, String role, String amka) {
        String query = "INSERT INTO users (username, hashed_password, salt, name, surname, age, role, specialty, amka) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getHashedPassword());
            stmt.setString(3, user.getSalt());
            stmt.setString(4, user.getName());
            stmt.setString(5, user.getSurname());
            stmt.setInt(6, user.getAge());
            stmt.setString(7, role);

            if ("doctor".equals(role)) {
                stmt.setString(8, user instanceof Doctor ? ((Doctor) user).getSpecialty() : null);
                stmt.setString(9, amka);
            } else {
                stmt.setNull(8, Types.VARCHAR); // Specialty null για μη-ιατρούς
                stmt.setString(9, amka);
            }

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ? AND role = 'doctor'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}