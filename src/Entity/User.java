package Entity;

public abstract class User {

    public enum UserRole { APPLICANT, OFFICER, MANAGER };

    private String name;
    private String nric; //e.g. S1234567A
    private String password; //default="password"
    private int age;
    private String maritalStatus; //"Single" or "Married"



    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    //getters
    public abstract UserRole getRole();
    public String getName() { return this.name; }
    public String getNric() { return this.nric; }
    public int getAge() { return this.age; }
    public String getMaritalStatus() { return this.maritalStatus; }
    public String getPassword() { return this.password; }

    public void setPassword(String password) { this.password = password; }
}