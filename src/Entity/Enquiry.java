package Entity;

/**
 * Represents an enquiry made by a user regarding a project.
 * Contains the user's message and the response (if any) from an officer or manager.
 */
public class Enquiry {
    private int id;
    private User user;
    private Project project;
    private String message;
    private String response;

    /**
     * Constructs a new Enquiry.
     *
     * @param message the enquiry message
     * @param user the user who submitted the enquiry
     * @param project the project the enquiry is related to
     */
    public Enquiry(String message, User user, Project project) {
        this.user = user;
        this.project = project;
        this.message = message;
        this.response = null;
    }

    /**
     * Sets the response to the enquiry.
     *
     * @param response the reply message
     */
    public void reply(String response) {
        this.response = response;
    }

    /**
     * Returns the enquiry ID.
     *
     * @return the ID of the enquiry
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the user who submitted the enquiry.
     *
     * @return the user associated with the enquiry
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the project related to the enquiry.
     *
     * @return the project associated with the enquiry
     */
    public Project getProject() {
        return project;
    }

    /**
     * Returns the enquiry message.
     *
     * @return the enquiry content
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the response to the enquiry, if any.
     *
     * @return the response content
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the ID of the enquiry.
     *
     * @param id the enquiry ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Updates the enquiry message.
     *
     * @param message the new enquiry message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Updates the response to the enquiry.
     *
     * @param response the new response message
     */
    public void setResponse(String response) {
        this.response = response;
    }
}
