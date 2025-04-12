package Entity;

public class Registration {
    public enum Status {
        PENDING, APPROVED, REJECTED
    }
    private int id;
    private HDBOfficer officer;
    private Project project;
    private Status status;

    public Registration(HDBOfficer officer, Project project, Status status) {
        this.officer = officer;
        this.project = project;
        this.status = status;
    }

    //getters
    public int getId() {
        return id;
    }
    public HDBOfficer getOfficer() {
        return officer;
    }
    public Project getProject() {
        return project;
    }
    public Status getStatus() {
        return status;
    }

    //setters
    public void setId(int id) {
        this.id = id;
    }
    public void setOfficer(HDBOfficer officer) {
        this.officer = officer;
    }
    public void setProject(Project project) {
        this.project = project;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}