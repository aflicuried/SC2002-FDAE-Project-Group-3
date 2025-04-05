package Entity;

import Repository.UserRepository.UserRole;

public class HDBManager extends User {

    private Project ProjectInCharge;

    public UserRole getRole() {
        return UserRole.MANAGER;
    }

    public HDBManager(String name, String nric, int age, String maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.ProjectInCharge = null;
    }

    public Project getProjectInCharge() {
        return ProjectInCharge;
    }
}