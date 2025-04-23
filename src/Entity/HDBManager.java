package Entity;

/**
 * Represents an HDB Manager user in the system.
 * A manager is responsible for overseeing a specific housing project.
 */
public class HDBManager extends User {

    private Project ProjectInCharge;

    /**
     * Returns the role of the user, which is MANAGER.
     *
     * @return the user role MANAGER
     */
    public UserRole getRole() {
        return UserRole.MANAGER;
    }

    /**
     * Constructs an HDBManager with the specified personal and login details.
     *
     * @param name           the name of the manager
     * @param nric           the NRIC of the manager
     * @param age            the age of the manager
     * @param maritalStatus  the marital status of the manager
     * @param password       the password of the manager
     */
    public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.ProjectInCharge = null;
    }

    /**
     * Gets the project that the manager is in charge of.
     *
     * @return the project in charge, or null if not assigned
     */
    public Project getProjectInCharge() {
        return ProjectInCharge;
    }
}
