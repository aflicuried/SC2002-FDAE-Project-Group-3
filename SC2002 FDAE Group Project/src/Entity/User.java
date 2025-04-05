package Entity;

import Repository.UserRepository.UserRole;

public abstract class User {
    private String name;
    private String nric; //e.g. S1234567A
    private String password; //default="password"
    private int age;
    private String maritalStatus; //"Single" or "Married"

    public abstract UserRole getRole();

    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    //getters
    public String getName() { return this.name; }
    public String getNric() { return this.nric; }
    public int getAge() { return this.age; }
    public String getMaritalStatus() { return this.maritalStatus; }
    public String getPassword() { return this.password; }

    /*public String toString() {
        return "NRIC: " + nric + "\nAge: " + age + "\nMaritalStatus: " + maritalStatus;
    }*/
}