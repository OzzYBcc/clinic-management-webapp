package main.java.dao;

import main.java.basicClasses.Admin;
import main.java.basicClasses.Doctor;
import main.java.basicClasses.Patient;
import main.java.util.passwordUtil;

public class DemoUsersInit {
    public static void main(String[] args) {
        // Δημιουργία Admin
        String adminSalt = passwordUtil.generateSalt();
        String adminHashed = passwordUtil.hashPassword("adminpass", adminSalt);
        Admin admin = new Admin("admin_user", adminHashed, "Bob", "Adminson", 40, adminSalt, "00000000000"); // Προσθήκη AMKA
        UserDAO.addUser(admin, "admin", "00000000000"); // Προσθήκη AMKA

        // Δημιουργία Doctor
        String docSalt = passwordUtil.generateSalt();
        String docHashed = passwordUtil.hashPassword("docpass", docSalt);
        Doctor doctor = new Doctor("doc1", docHashed, "Emily", "Jones", 38, "Neurology", docSalt, "11111111111"); // Προσθήκη AMKA
        UserDAO.addUser(doctor, "doctor", "11111111111"); // Προσθήκη AMKA

        // Δημιουργία Patient
        String patSalt = passwordUtil.generateSalt();
        String patHashed = passwordUtil.hashPassword("patientpass", patSalt);
        Patient patient = new Patient("patient1", patHashed, "John", "Doe", 30, "12345678901", patSalt);
        UserDAO.addUser(patient, "patient", "12345678901"); // Προσθήκη AMKA

        System.out.println("Demo users added successfully.");
    }
}