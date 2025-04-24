package Entity;

/**
 * Represents a registration made by an HDB officer for a specific project.
 * A registration has a status that can be pending, approved, or rejected.
 */
public class Registration {

    /**
     * Enum representing the registration status.
     */
    public enum Status {
        PENDING, APPROVED, REJECTED
    }

    private int id;
    private HDBOfficer officer;
    private Project project;
    private Status status;

    /**
     * Constructs a new Registration instance.
     *
     * @param officer the officer who registered
     * @param project the project registered for
     * @param status  the status of the registration
     */
    public Registration(HDBOfficer officer, Project project, Status status) {
        this.officer = officer;
        this.project = project;
        this.status = status;
    }

    /**
     * Gets the unique ID of the registration.
     *
     * @return the registration ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the officer associated with the registration.
     *
     * @return the HDB officer
     */
    public HDBOfficer getOfficer() {
        return officer;
    }

    /**
     * Gets the project associated with the registration.
     *
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the current status of the registration.
     *
     * @return the registration status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the registration ID.
     *
     * @param id the new ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the officer for the registration.
     *
     * @param officer the HDB officer
     */
    public void setOfficer(HDBOfficer officer) {
        this.officer = officer;
    }

    /**
     * Sets the project for the registration.
     *
     * @param project the project
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Sets the registration status.
     *
     * @param status the new status
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
