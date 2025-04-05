package Entity;

import Repository.UserRepository.UserRole;

public class Applicant extends User {

    private Project project;
    private Application application;
    private String flatType;

    public UserRole getRole() {
        return UserRole.APPLICANT;
    }

    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.project = null;
        this.application = null;
        this.flatType = null;
    }

    public boolean isSingle() { return super.getMaritalStatus().equals("Single"); }
    public Project getProject() {
        return project;
    }
    public Application getApplication() {
        return this.application;
    }
    public void setFlatType(int flatType) {
        this.flatType = flatType + "-Room";
    }
    public void setProject(Project project) {
        this.project = project;
    }
}

