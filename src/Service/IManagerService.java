package Service;

import Entity.*;

import java.util.List;

public interface IManagerService {
    Project createProject(String projectName, String neighbourhood, int room2, int price2,
                          int room3, int price3, String opening, String closing, int slots, User user);
    void editProject(int choice, Project project);
    void shiftVisibility(Project project);

    Registration findById(int id);
    List<Registration> getRegistrations();
    void approveRegistration(int registrationId);
    void rejectRegistration(int registrationId);

    List<Application> getApplications();
    Application findApplicationByNric(String nric);
    void approveApplication(Application application);
    void rejectApplication(Application application);
    List<Application> getWithdrawals();
    void approveWithdrawal(Application application);
    void rejectWithdrawal(Application application);

    List<Enquiry> getEnquiries();
    boolean checkAuthority(int id);
    void replyEnquiry(int Id, String reply);

}
