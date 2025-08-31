package main.java.basicClasses;

public class Admin extends User {
    public Admin(String username, String password, String name, String surname, int age, String salt) {
        super(username, password, name, surname, age, salt);
    }

    public Admin(String username, String password, String name, String surname, int age, String salt, String amka) {
        super(username, password, name, surname, age, salt, amka);
    }

    public boolean addDoctor(String username, String password, String name, String surname, int age, String specialty, String salt, String amka) {
        Doctor newDoctor = new Doctor(username, password, name, surname, age, specialty, salt, amka);
        boolean success = main.java.dao.UserDAO.addUser(newDoctor, "doctor", amka);
        if (success) {
            System.out.println("Doctor " + name + " has been added to the system.");
        } else {
            System.out.println("Failed to add Doctor " + name + ".");
        }
        return success;
    }

    public boolean addUser(String username, String password, String name, String surname, int age, String role, String salt, String amka) {
        User newUser = new User(username, password, name, surname, age, salt, amka);
        boolean success = main.java.dao.UserDAO.addUser(newUser, role, amka);
        if (success) {
            System.out.println("User " + name + " (role: " + role + ") has been added to the system.");
        } else {
            System.out.println("Failed to add User " + name + ".");
        }
        return success;
    }

    public boolean removeDoctor(String doctorUsername) {
        boolean success = main.java.dao.UserDAO.deleteUser(doctorUsername);
        if (success) {
            System.out.println("Doctor " + doctorUsername + " has been removed from the system.");
        } else {
            System.out.println("Failed to remove Doctor " + doctorUsername + ".");
        }
        return success;
    }

    public void viewDoctorsList() {
        System.out.println("Displaying list of all registered doctors.");
    }
}