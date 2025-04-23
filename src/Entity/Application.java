package Entity;

/**
 * Represents an application submitted by a user for an HDB project.
 * Includes details such as the applicant, project, application status, flat type, and withdrawal flag.
 */
public class Application {

    /**
     * Enum representing the status of an application.
     */
    public enum ApplicationStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED
    }

    /**
     * Enum representing the type of flat applied for.
     */
    public enum FlatType {
        TWO_ROOM, THREE_ROOM
    }

    private User user;

    private Project project;

    private ApplicationStatus status;

    private FlatType flatType;

    private boolean withdrawal = false;

    /**
     * Constructs a new Application instance.
     *
     * @param user      the user who submitted the application
     * @param project   the project being applied to
     * @param status    the current status of the application
     * @param flatType  the flat type applied for
     */
    public Application(User user, Project project, ApplicationStatus status, FlatType flatType) {
        this.user = user;
        this.project = project;
        this.status = status;
        this.flatType = flatType;
    }

    /**
     * Gets the user associated with the application.
     *
     * @return the user who submitted the application
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the project associated with the application.
     *
     * @return the project being applied to
     */
    public Project getProject() {
        return project;
    }

    /**
     * Gets the current status of the application.
     *
     * @return the application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Gets the flat type the user applied for.
     *
     * @return the flat type
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Returns true if the application has been withdrawn.
     *
     * @return true if withdrawn, false otherwise
     */
    public boolean isWithdrawal() {
        return withdrawal;
    }

    /**
     * Updates the status of the application.
     *
     * @param status the new application status
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    /**
     * Sets the withdrawal flag of the application.
     *
     * @param withdrawal true if the application is withdrawn, false otherwise
     */
    public void setWithdrawal(boolean withdrawal) {
        this.withdrawal = withdrawal;
    }
}
