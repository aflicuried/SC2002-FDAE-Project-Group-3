package Entity;

/**
 * Represents an HDB applicant in the system.
 * Extends the User class with specific fields related to application and project participation.
 */
public class Applicant extends User {

    private Project project;

    private Application application;

    private String flatType;

    /**
     * Constructs an Applicant with the specified details.
     *
     * @param name           the applicant's name
     * @param nric           the applicant's NRIC
     * @param age            the applicant's age
     * @param maritalStatus  the applicant's marital status
     * @param password       the applicant's account password
     */
    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.project = null;
        this.application = null;
        this.flatType = null;
    }

    /**
     * Gets the role of this user.
     *
     * @return the user role as APPLICANT
     */
    @Override
    public UserRole getRole() {
        return UserRole.APPLICANT;
    }

    /**
     * Returns true if the applicant is single.
     *
     * @return true if marital status is "SINGLE", false otherwise
     */
    public boolean isSingle() {
        return super.getMaritalStatus().equals("SINGLE");
    }

    /**
     * Gets the project the applicant has applied to.
     *
     * @return the project associated with this applicant
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project the applicant is applying to.
     *
     * @param project the project to associate with this applicant
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the application submitted by the applicant.
     *
     * @return the application object
     */
    public Application getApplication() {
        return this.application;
    }

    /**
     * Sets the application for this applicant.
     *
     * @param application the application object
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * Sets the flat type the applicant is interested in.
     * Automatically appends "-Room" to the type.
     *
     * @param flatType the flat type, e.g., "2" for "2-Room"
     */
    public void setFlatType(String flatType) {
        this.flatType = flatType + "-Room";
    }
}
