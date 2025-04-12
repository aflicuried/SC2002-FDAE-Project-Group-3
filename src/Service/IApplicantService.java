package Service;

import Entity.*;

import java.util.List;

public interface IApplicantService {
    boolean haveProject();
    List<Project> getVisibleProjects();
    boolean isEligibleForProject(Project project);
    boolean isSingle();
    Application newApplication(Project project, int flatType);
    void sendApplication(Application application);
    Project getProject();

    boolean isAvailableToBookFlat();
    void withdrawalApplication();

    void submitEnquiry(String q, Project project);
    List<Enquiry> getEnquiries();
    Enquiry getEnquiry(int enquiryId);
    void editEnquiry(String q, Enquiry enquiry);
}
