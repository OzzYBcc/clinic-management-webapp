package main.java.basicClasses;

public class Appointment {
    private Patient patient;
    private Doctor doctor;
    private String dateTime;
    private String status;
    private int id;

    public Appointment(Patient patient, Doctor doctor, String dateTime, String status) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = status;
    }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}