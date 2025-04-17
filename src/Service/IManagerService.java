package Service;

import Entity.*;

import java.util.List;

public interface IManagerService {
    Project createProject(String projectName, String neighbourhood, int room2, int price2,
                          int room3, int price3, String opening, String closing, int slots, User user);
    boolean checkAuthForProject(Project project);
    void editProject(int choice, Project project);
    void shiftVisibility(Project project);

    Registration findById(int id);
    List<Registration> getRegistrations();
    void approveRegistration(Registration registration);
    void rejectRegistration(Registration registration);

    List<Application> getApplications();
    Application findApplicationByNric(String nric);
    void approveApplication(Application application);
    void rejectApplication(Application application);
    List<Application> getWithdrawals();
    void approveWithdrawal(Application application);
    void rejectWithdrawal(Application application);

    List<Applicant> getApplicantsForReport();

    List<Enquiry> getEnquiries();
    boolean checkAuthForEnquiry(int id);
    void replyEnquiry(int Id, String reply);

}
