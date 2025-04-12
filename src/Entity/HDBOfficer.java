package Entity;

public class HDBOfficer extends Applicant {

    private Project projectHandling;

    public UserRole getRole() {
        return UserRole.OFFICER;
    }

    public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus,  password);
        this.projectHandling = null;
    }

    //getters
    public Project getProjectHandling() {
        return projectHandling;
    }

    //setters
    public void setProjectHandling(Project projectHandling) {
        this.projectHandling = projectHandling;
    }
}