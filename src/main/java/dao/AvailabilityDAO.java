package main.java.dao;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailabilityDAO {
    public static boolean addAvailability(String doctorUsername, String slot) {
        String query = "INSERT INTO availability (doctor_username, slot) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, doctorUsername);
            stmt.setString(2, slot);
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Attempted to add availability for " + doctorUsername + " at " + slot + ": " + (rowsAffected == 1 ? "Success" : "Failed"));
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error adding availability: " + e.getMessage());
            return false;
        }
    }

    public static List<String> getAvailableSlots(String specialty) {
        List<String> slots = new ArrayList<>();
        String query = "SELECT slot FROM availability";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            System.out.println("Querying all available slots.");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            while (rs.next()) {
                String slotRange = rs.getString("slot"); // π.χ. "2025-06-13 09:00 to 2025-06-13 18:00"
                String[] parts = slotRange.split(" to ");
                if (parts.length == 2) {
                    String startStr = parts[0].trim();
                    String endStr = parts[1].trim();

                    // Ανάλυση ημερομηνίας
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(sdf.parse(startStr));
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(sdf.parse(endStr));

                    // Δημιουργία slots μίας ώρας
                    while (!startCal.after(endCal)) {
                        String slotStart = sdf.format(startCal.getTime());
                        Calendar nextHour = (Calendar) startCal.clone();
                        nextHour.add(Calendar.HOUR_OF_DAY, 1);
                        if (nextHour.after(endCal)) break;
                        String slotEnd = sdf.format(nextHour.getTime());
                        slots.add(slotStart);
                        startCal.add(Calendar.HOUR_OF_DAY, 1);
                    }
                }
            }
            System.out.println("Found " + slots.size() + " available slots.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error querying slots: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return slots;
    }

    //μέθοδος για να πάρουμε slots με doctor_username και specialty από τον πίνακα users
    public static Map<String, Map<String, String>> getAvailableSlotsWithDetails() {
        Map<String, Map<String, String>> slotDetailsMap = new HashMap<>();
        String query = "SELECT a.doctor_username, a.slot, u.specialty FROM availability a LEFT JOIN users u ON a.doctor_username = u.username";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            while (rs.next()) {
                String doctorUsername = rs.getString("doctor_username");
                String slotRange = rs.getString("slot"); // π.χ. "2025-06-13 09:00 to 2025-06-13 18:00"
                String specialty = rs.getString("specialty") != null ? rs.getString("specialty") : "Unknown";

                String[] parts = slotRange.split(" to ");
                if (parts.length == 2) {
                    String startStr = parts[0].trim();
                    String endStr = parts[1].trim();

                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(sdf.parse(startStr));
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(sdf.parse(endStr));

                    while (!startCal.after(endCal)) {
                        String slotStart = sdf.format(startCal.getTime());
                        Calendar nextHour = (Calendar) startCal.clone();
                        nextHour.add(Calendar.HOUR_OF_DAY, 1);
                        if (nextHour.after(endCal)) break;
                        Map<String, String> details = new HashMap<>();
                        details.put("doctorUsername", doctorUsername);
                        details.put("specialty", specialty);
                        slotDetailsMap.put(slotStart, details);
                        startCal.add(Calendar.HOUR_OF_DAY, 1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error querying slots: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error parsing date: " + e.getMessage());
        }
        return slotDetailsMap;
    }
}