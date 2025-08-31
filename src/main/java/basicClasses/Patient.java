package main.java.basicClasses;

public class Patient extends User {
    private final String amka;

    public Patient(String username, String password, String name, String surname, int age, String amka, String salt) {
        super(username, password, name, surname, age, salt);
        this.amka = amka;
    }

    public String getAmka() {
        return amka;
    }

    public void registration() {
        System.out.println("Patient " + getName() + " registered successfully.");
    }

    public void searchAvailableAppointment(String doctorName) {
        System.out.println("Searching for available appointments with Dr. " + doctorName);
    }

    public void searchSpecialistAppointment(String specialty) {
        System.out.println("Searching for available appointments with a specialist in " + specialty);
    }

    public void viewScheduledAppointments() {
        System.out.println("Displaying scheduled appointments for " + getName());
    }

    public void viewAppointmentHistory() {
        System.out.println("Displaying appointment history for " + getName());
    }
}