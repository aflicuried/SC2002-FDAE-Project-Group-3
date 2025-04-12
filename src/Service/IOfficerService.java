package Service;

import Entity.Application;
import Entity.Enquiry;
import Entity.Project;
import Entity.Registration;

import java.util.List;

public interface IOfficerService extends IApplicantService {
    Project getProjectHandling();

    void submitRegistration(String projectName);
    List<Registration> getRegistrations();
    Project getManagedProject();

    void bookApplication(String applicantNric, Project project);
    List<Enquiry> getEnquiriesForReply();
    void replyEnquiry(int enquiryId, String response);
}