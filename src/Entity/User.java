package Entity;

/**
 * Represents a generic user in the system. This is an abstract base class that
 * defines common attributes and methods for all user types including Applicant,
 * HDBOfficer, and HDBManager.
 */
public abstract class User {

    /**
     * Enum representing the roles a user can have in the system.
     */
    public enum UserRole {
        APPLICANT, OFFICER, MANAGER
    }

    private String name;
    private String nric; // e.g. S1234567A
    private String password; // default = "password"
    private int age;
    private String maritalStatus; // "Single" or "Married"

    /**
     * Constructs a new User instance.
     *
     * @param name          the name of the user
     * @param nric          the NRIC of the user
     * @param age           the age of the user
     * @param maritalStatus the marital status ("Single" or "Married")
     * @param password      the password for the user account
     */
    public User(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user
     */
    public abstract UserRole getRole();

    /**
     * Gets the name of the user.
     *
     * @return the user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the NRIC of the user.
     *
     * @return the user's NRIC
     */
    public String getNric() {
        return this.nric;
    }

    /**
     * Gets the age of the user.
     *
     * @return the user's age
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Gets the marital status of the user.
     *
     * @return the user's marital status
     */
    public String getMaritalStatus() {
        return this.maritalStatus;
    }

    /**
     * Gets the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets a new password for the user.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
