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


/**
 * ApplicantService provides services related to HDB project applications for an applicant.
 * It handles application submission, project eligibility checks, enquiries, and withdrawal requests.
 */
public class ApplicantService implements IApplicantService {
    //database
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private Applicant applicant;

    //constructor
    /**
     * Constructs an ApplicantService for the specified applicant.
     *
     * @param applicant the applicant using the service
     */
    public ApplicantService(Applicant applicant) {
        this.applicant = applicant;
    }

    //methods
    /**
     * Checks whether the applicant has already applied for a project.
     *
     * @return true if the applicant has a project, false otherwise
     */
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
    /**
     * Retrieves a list of visible projects that are currently open and for which
     * the applicant is eligible based on age and marital status.
     *
     * @return list of eligible visible projects
     */
    public List<Project> getVisibleProjects() {
        LocalDate today = DateUtil.getCurrentDate();

        return projectDatabase.findProjects().stream()
                .filter(Project::isVisible)
                .filter(p -> !today.isBefore(p.getOpeningDate()) && !today.isAfter(p.getClosingDate()))
                .filter(this::isEligibleForProject)
                .collect(Collectors.toList());
    }

    /**
     * Checks if the applicant is eligible for the specified project.
     * Single applicants must be at least 35 years old, and non-single applicants must be at least 21.
     *
     * @param project the project to check eligibility for
     * @return true if eligible, false otherwise
     */
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

    /**
     * Checks if the applicant is single.
     *
     * @return true if the applicant is single, false otherwise
     */
    public boolean isSingle() {
        return this.applicant.isSingle();
    }

    /**
     * Creates a new application for a specific project and flat type.
     *
     * @param project the project to apply for
     * @param flatType 1 for 2-room, 2 for 3-room flat type
     * @return the created Application object
     */
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


    /**
     * Submits the given application to the application database and updates the applicant's record.
     *
     * @param application the application to be submitted
     */
    public void sendApplication(Application application) {
        applicationDatabase.addApplication(application);
        applicant.setProject(application.getProject());
        applicant.setApplication(application);
    }

    /**
     * Retrieves the project that the applicant has applied for.
     *
     * @return the applied project, or null if none
     */
    public Project getProject() {
        return applicant.getProject();
    }

    /**
     * Checks if the applicant's application is successful and allows flat booking.
     *
     * @return true if the application status is SUCCESSFUL, false otherwise
     * @throws IllegalArgumentException if the application is not found
     */
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

    /**
     * Marks the applicant's current application as withdrawn.
     *
     * @throws IllegalArgumentException if the application is not found
     */
    public void withdrawalApplication() {
        Application application = applicationDatabase.findByApplicantNric(applicant.getNric());
        if(application == null) {
            throw (new IllegalArgumentException("Application not found."));
        }
        application.setWithdrawal(true);
    }


    /**
     * Submits an enquiry for the specified project.
     *
     * @param q the enquiry message
     * @param project the project related to the enquiry
     */
    public void submitEnquiry(String q, Project project) {
        Enquiry enquiry = new Enquiry(q, applicant, project);
        enquiryDatabase.addEnquiry(enquiry);
    }

    /**
     * Retrieves all enquiries submitted by the applicant.
     *
     * @return list of the applicant's enquiries
     */
    public List<Enquiry> getEnquiries() {
        return enquiryDatabase.findByName(applicant.getName());
    }


    /**
     * Retrieves an enquiry by its ID.
     *
     * @param enquiryId the ID of the enquiry
     * @return the Enquiry object, or null if not found
     */
    public Enquiry getEnquiry(int enquiryId) {
        return enquiryDatabase.findById(enquiryId);
    }


    /**
     * Updates the message of an existing enquiry.
     *
     * @param q the new enquiry message
     * @param enquiry the enquiry to be edited
     */
    public void editEnquiry(String q, Enquiry enquiry) {
        enquiry.setMessage(q);
    }

    /**
     * Deletes an existing enquiry from the database.
     *
     * @param enquiry the enquiry to be deleted
     */
    public void deleteEnquiry(Enquiry enquiry) {
        enquiryDatabase.removeEnquiry(enquiry);
    }
}
