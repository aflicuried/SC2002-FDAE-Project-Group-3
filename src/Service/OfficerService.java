package Service;

import Database.ApplicationDatabase;
import Database.EnquiryDatabase;
import Database.ProjectDatabase;
import Database.RegistrationDatabase;
import Entity.*;

import java.util.List;

public class OfficerService extends ApplicantService implements IOfficerService{
    //database
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private RegistrationDatabase registrationDatabase = RegistrationDatabase.getInstance();
    private HDBOfficer officer;

    //constructor
    public OfficerService(HDBOfficer officer) {
        super(officer);
        this.officer = officer;
    }

    //methods
    public void submitRegistration(String projectName) {
        Project project = projectDatabase.findProjectByName(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }
        if (officer.getProjectHandling() != null && officer.getProjectHandling().equals(project)) {
            throw new IllegalArgumentException("You are already managing this project.");
        }
        Registration registration = new Registration(officer, project, Registration.Status.PENDING);
        registrationDatabase.addRegistration(registration);
    }

    public List<Registration> getRegistrations() {
        return registrationDatabase.findByOfficerNric(officer.getNric());
    }

    public Project getManagedProject() {
        return officer.getProjectHandling();
    }

    @Override
    public Application newApplication(Project project, int flatType) {
        if (project.equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer cannot apply for their own managed project.");
        }
        return super.newApplication(project, flatType);
    }

    @Override
    public void sendApplication(Application application) {
        if (application.getProject().equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer cannot apply for their own managed project.");
        }
        super.sendApplication(application);
    }

    public void bookApplication(String applicantNric, Project project) {
        if (!project.equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer can only manage their own project.");
        }
        Application application = applicationDatabase.findByApplicantNric(applicantNric);
        if (application.getProject().equals(project)
                && application.getStatus() == Application.ApplicationStatus.SUCCESSFUL) {
            application.setStatus(Application.ApplicationStatus.BOOKED);
        }//need updates: more than 1 application?
        throw new IllegalArgumentException("No successful application found for this applicant.");
    }

    public Project getProjectHandling() {
        return officer.getProjectHandling();//better to get from officer, or projectDatabase?
    }

    @Override
    public List<Enquiry> getEnquiriesForReply() {
        return enquiryDatabase.findByProject(officer.getProjectHandling());
    }

    public boolean checkAuthority(int id) {
        return enquiryDatabase.findById(id).getProject()
                .getManager().getName().equals(officer.getName());
    }
    public void replyEnquiry(int id, String reply) {
        Enquiry enquiry = enquiryDatabase.findById(id);
        enquiry.setResponse(reply);
    }

    public boolean isAvailableToBookFlat() {
        Application application = applicationDatabase.findByApplicantNric(officer.getNric());
        if (application != null && application.getProject().equals(officer.getProjectHandling())) {
            return false;
        }
        return super.isAvailableToBookFlat();
    }//need updates: more than 1 application?
}