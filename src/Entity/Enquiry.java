package Entity;

public class Enquiry {
    private int id;
    private User user;
    private Project project;
    private String message;
    private String response;

    public Enquiry(String message, User user, Project project) {
        this.user = user;
        this.project = project;
        this.message = message;
        this.response = null;
    }

    public void reply(String response) {
        this.response = response;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Project getProject() {
        return project;
    }
    public String getMessage() {
        return message;
    }
    public String getResponse() {
        return response;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public void setResponse(String response) {
        this.response = response;
    }
}
