package main.java.basicClasses;

public class Doctor extends User {
    private String specialty;

    public Doctor(String username, String password, String name, String surname, int age, String specialty, String salt) {
        super(username, password, name, surname, age, salt);
        this.specialty = specialty;
    }

    public Doctor(String username, String password, String name, String surname, int age, String specialty, String salt, String amka) {
        super(username, password, name, surname, age, salt, amka);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void registerAvailability(String month) {
        System.out.println("Doctor " + getName() + " has registered availability for " + month);
    }

    public void viewAppointmentsSchedule() {
        System.out.println("Displaying appointment schedule for Dr. " + getName());
    }
}