package main.java.dao;

import main.java.basicClasses.Appointment;
import main.java.basicClasses.Doctor;
import main.java.basicClasses.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    public static boolean bookAppointment(String doctorUsername, String patientUsername, String datetime) {
        String query = "INSERT INTO appointments (doctor_username, patient_username, datetime, status) VALUES (?, ?, ?, 'scheduled')";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doctorUsername);
            stmt.setString(2, patientUsername);
            stmt.setString(3, datetime);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean cancelAppointment(int appointmentId) {
        String query = "DELETE FROM appointments WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean markCompleted(int appointmentId) {
        String query = "UPDATE appointments SET status = 'completed' WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Appointment> getAppointmentsByPatient(String patientUsername) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT id, doctor_username, datetime, status FROM appointments WHERE patient_username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, patientUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String doctorUsername = rs.getString("doctor_username");
                String dateTime = rs.getString("datetime");
                String status = rs.getString("status");
                Doctor doctor = new Doctor(doctorUsername, "", "", "", 0, "", "");
                Patient patient = new Patient(patientUsername, "", "", "", 0, "", "");
                Appointment appt = new Appointment(patient, doctor, dateTime, status);
                appt.setId(id);
                appointments.add(appt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static List<Appointment> getAppointmentsByDoctor(String doctorUsername) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT id, patient_username, datetime, status FROM appointments WHERE doctor_username = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doctorUsername);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String patientUsername = rs.getString("patient_username");
                String dateTime = rs.getString("datetime");
                String status = rs.getString("status");
                Doctor doctor = new Doctor(doctorUsername, "", "", "", 0, "", "");
                Patient patient = new Patient(patientUsername, "", "", "", 0, "", "");
                Appointment appt = new Appointment(patient, doctor, dateTime, status);
                appt.setId(id);
                appointments.add(appt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static void removeCompletedAppointments(String doctorUsername) {
        System.out.println("DEBUG: Entering removeCompletedAppointments for doctor: " + doctorUsername);
        String query = "DELETE FROM appointments WHERE doctor_username = ? AND status = 'completed'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            System.out.println("DEBUG: Setting doctorUsername parameter: " + doctorUsername);
            stmt.setString(1, doctorUsername);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("DEBUG: Rows affected by DELETE: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("DEBUG: SQLException occurred: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("DEBUG: Exiting removeCompletedAppointments");
    }
}