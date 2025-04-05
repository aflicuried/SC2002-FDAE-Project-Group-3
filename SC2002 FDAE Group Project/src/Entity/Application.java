package Entity;

public class Application {

    public enum ApplicationStatus {
        PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED
    }
    public enum FlatType {
        TWO_ROOM, THREE_ROOM
    }

    private User user;
    private Project project;
    private ApplicationStatus status;
    private FlatType flatType;
    private boolean withdrawal = false;

    public Application(User user, Project project, ApplicationStatus status, FlatType flatType) {
        this.user = user;
        this.project = project;
        this.status = status;
        this.flatType = flatType;
    }

    /*public void updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    public void bookFlat(FlatType newFlatType) {
        this.flatType = newFlatType;
        this.updateStatus(ApplicationStatus.BOOKED);
    }*/

    //getters
    public User getUser() {
        return user;
    }
    public Project getProject() {
        return project;
    }
    public ApplicationStatus getStatus() {
        return status;
    }
    public FlatType getFlatType() {
        return flatType;
    }
    public boolean isWithdrawal() {
        return withdrawal;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}
