package Entity;

import Repository.UserRepository.UserRole;

public class HDBOfficer extends Applicant {

    private String regStatus; //pending, approved, rejected
    private Project projectHandling;

    public UserRole getRole() {
        return UserRole.OFFICER;
    }

    public HDBOfficer(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus,  password);
        this.regStatus = null;
        this.projectHandling = null;
    }

    //getters
    public String getRegStatus() {
        return regStatus;
    }
    public String getProjectName() {
        return projectHandling.getName();
    }

}