package main.java.basicClasses;

public class User {
    protected String username;
    protected String hashedPassword;
    protected String name;
    protected String surname;
    protected int age;
    protected String salt;
    protected String amka;

    public User(String username, String hashedPassword, String name, String surname, int age, String salt) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.salt = salt;
        this.amka = null; // Default
    }

    public User(String username, String hashedPassword, String name, String surname, int age, String salt, String amka) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.salt = salt;
        this.amka = amka;
    }

    // Getters
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getAge() { return age; }
    public String getSalt() { return salt; }
    public String getAmka() { return amka; } // Προσθήκη getter

    // Setters
    public void setHashedPassword(String hashedPassword) { this.hashedPassword = hashedPassword; }
    public void setSalt(String salt) { this.salt = salt; }
    public void setAmka(String amka) { this.amka = amka; } // Προσθήκη setter
}