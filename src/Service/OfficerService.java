package Service;

import Database.ApplicationDatabase;
import Database.EnquiryDatabase;
import Database.ProjectDatabase;
import Database.RegistrationDatabase;
import Entity.*;

import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public List<Project> getVisibleProjects() {
        if (officer.getProjectHandling() != null){
            String name = officer.getProjectHandling().getName();
            return projectDatabase.findProjects().stream()
                    .filter(Project::isVisible)
                    .filter(this::isEligibleForProject)
                    .filter(p -> !p.getName().equals(name))
                    .collect(Collectors.toList());
        }
        return projectDatabase.findProjects().stream()
                .filter(Project::isVisible)
                .filter(this::isEligibleForProject)
                .collect(Collectors.toList());
    }

    public List<Project> getProjectsForRegi() {
        if (officer.getApplication() == null){
            return projectDatabase.findProjects();
        }
        return projectDatabase.findProjects()
                .stream().filter(p -> !p.getName().equals(officer.getApplication().getProject().getName()))
                .collect(Collectors.toList());
    }

    public void submitRegistration(String projectName) {
        Project project = projectDatabase.findProjectByName(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }
        if (officer.getProjectHandling() != null && officer.getProjectHandling().equals(project)) {
            throw new IllegalArgumentException("You are already managing this project.");
        }
        if (!registrationDatabase.findByOfficerNric(officer.getNric()).isEmpty()) { // actually there are some problems.
            throw new IllegalArgumentException("You have already sent registrations."); // if >1 registrations?
        }
        Registration registration = new Registration(officer, project, Registration.Status.PENDING);
        registrationDatabase.addRegistration(registration);
    }

    public List<Registration> getRegistrations() {
        return registrationDatabase.findByOfficerNric(officer.getNric());
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
            throw new IllegalArgumentException("Officer can only manage applications under their own project.");
        }
        Application application = applicationDatabase.findByApplicantNric(applicantNric);
        if (application == null) {
            throw new IllegalArgumentException("Applicant or application not found.");
        }
        if (application.getProject().equals(project)
                && application.getStatus() == Application.ApplicationStatus.SUCCESSFUL) {
            application.setStatus(Application.ApplicationStatus.BOOKED);
            return;
        }
        throw new IllegalArgumentException("No successful application found for this applicant.");
    }

    public Project getProjectHandling() {
        return officer.getProjectHandling();//better to get from officer, or projectDatabase?
    }

    @Override
    public List<Enquiry> getEnquiriesForReply() {
        Project project = officer.getProjectHandling();
        if (project == null) {
            throw new IllegalArgumentException("You are not handling any project.");
        }
        return enquiryDatabase.findByProject(officer.getProjectHandling());
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