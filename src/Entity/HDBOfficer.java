package Entity;

/**
 * Represents an HDB Officer, who is a special type of applicant responsible for handling a housing project.
 */
public class HDBOfficer extends Applicant {

    private Project projectHandling;

    /**
     * Returns the role of the user, which is OFFICER.
     *
     * @return the user role OFFICER
     */
    public UserRole getRole() {
        return UserRole.OFFICER;
    }

    /**
     * Constructs an HDBOfficer with the specified personal and login details.
     *
     * @param name           the name of the officer
     * @param nric           the NRIC of the officer
     * @param age            the age of the officer
     * @param maritalStatus  the marital status of the officer
     * @param password       the password of the officer
     */
    public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.projectHandling = null;
    }

    /**
     * Gets the project that the officer is handling.
     *
     * @return the project being handled, or null if not assigned
     */
    public Project getProjectHandling() {
        return projectHandling;
    }

    /**
     * Sets the project that the officer will handle.
     *
     * @param projectHandling the project to be handled by the officer
     */
    public void setProjectHandling(Project projectHandling) {
        this.projectHandling = projectHandling;
    }
}
