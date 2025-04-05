package Entity;

public class Enquiry {
    private int id = 10000;
    private User user;
    private Project project;
    private String message;
    private String response;
    private boolean isReponse = false;

    public Enquiry(User user, Project project, String message) {
        this.id = id;
        this.user = user;
        this.project = project;
        this.message = message;
        this.response = null;
        id++;

    }

    public void reply(String response) {
        this.response = response;
        this.isReponse = true;
    }

    //getters and setters
    public String getMessage() {
        return message;
    }
    public boolean getIsResponse() {
        return isReponse;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
