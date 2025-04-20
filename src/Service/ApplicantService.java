package Service;

import Database.ApplicationDatabase;
import Database.EnquiryDatabase;
import Database.ProjectDatabase;
import Database.UserDatabase;
import Entity.*;
import util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static Entity.Application.ApplicationStatus;

public class ApplicantService implements IApplicantService {
    //database
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private Applicant applicant;

    //constructor
    public ApplicantService(Applicant applicant) {
        this.applicant = applicant;
    }

    //methods
    public boolean haveProject() {
        return applicant.getProject() != null;
    }

    //
    /*public List<Project> getVisibleProjects() {
        return projectDatabase.findProjects().stream()
                .filter(Project::isVisible)
                .filter(this::isEligibleForProject)
                .collect(Collectors.toList());
    }*/
    public List<Project> getVisibleProjects() {
        LocalDate today = DateUtil.getCurrentDate();

        return projectDatabase.findProjects().stream()
                .filter(Project::isVisible)
                .filter(p -> !today.isBefore(p.getOpeningDate()) && !today.isAfter(p.getClosingDate()))
                .filter(this::isEligibleForProject)
                .collect(Collectors.toList());
    }

    public boolean isEligibleForProject(Project project) {
        if (!project.isVisible())
            return false;
        if (applicant.isSingle() && applicant.getAge() >= 35) {
            return true;
        }
        else if (!applicant.isSingle() && applicant.getAge() >= 21) {
            return true;
        }
        return false;
    }

    //
    public boolean isSingle() {
        return this.applicant.isSingle();
    }

    //
    public Application newApplication(Project project, int flatType) {
        Application application;
        if (flatType == 1) {
            application = new Application(applicant, project, ApplicationStatus.PENDING, Application.FlatType.TWO_ROOM);
        }
        else if (flatType == 2) {
            application = new Application(applicant, project, ApplicationStatus.PENDING, Application.FlatType.THREE_ROOM);
        }
        else
            return null;
        return application;
    }
    public void sendApplication(Application application) {
        applicationDatabase.addApplication(application);
        applicant.setProject(application.getProject());
        applicant.setApplication(application);
    }

    //
    public Project getProject() {
        return applicant.getProject();
    }

    //
    public boolean isAvailableToBookFlat() {
        Application application = applicationDatabase.findByApplicantNric(applicant.getNric());
        if (application == null) {
            throw (new IllegalArgumentException("Application not found."));
        }
        if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            return true;
        }
        return false;
    }

    //
    public void withdrawalApplication() {
        Application application = applicationDatabase.findByApplicantNric(applicant.getNric());
        if(application == null) {
            throw (new IllegalArgumentException("Application not found."));
        }
        application.setWithdrawal(true);
    }

    //
    public void submitEnquiry(String q, Project project) {
        Enquiry enquiry = new Enquiry(q, applicant, project);
        enquiryDatabase.addEnquiry(enquiry);
    }

    //
    public List<Enquiry> getEnquiries() {
        return enquiryDatabase.findByName(applicant.getName());
    }
    public Enquiry getEnquiry(int enquiryId) {
        return enquiryDatabase.findById(enquiryId);
    }
    public void editEnquiry(String q, Enquiry enquiry) {
        enquiry.setMessage(q);
    }
    public void deleteEnquiry(Enquiry enquiry) {
        enquiryDatabase.removeEnquiry(enquiry);
    }
}