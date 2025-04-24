package Service;

import Entity.*;

import java.util.List;


/**
 * Interface for services related to an HDB applicant.
 * Provides methods for project applications, enquiries, and application management.
 */
public interface IApplicantService {
    /**
     * Checks if the applicant has already applied for a project.
     *
     * @return true if the applicant has a project, false otherwise
     */
    boolean haveProject();


    /**
     * Retrieves a list of currently visible and eligible projects for the applicant.
     *
     * @return a list of visible and eligible Project objects
     */
    List<Project> getVisibleProjects();


    /**
     * Determines if the applicant is eligible to apply for the specified project.
     *
     * @param project the project to check eligibility for
     * @return true if eligible, false otherwise
     */
    boolean isEligibleForProject(Project project);


    /**
     * Checks if the applicant is single.
     *
     * @return true if the applicant is single, false otherwise
     */
    boolean isSingle();


    /**
     * Creates a new application for the specified project and flat type.
     *
     * @param project the project to apply for
     * @param flatType the type of flat (1 for 2-Room, 2 for 3-Room)
     * @return the created Application object
     */
    Application newApplication(Project project, int flatType);


    /**
     * Submits the given application and updates the applicant's status.
     *
     * @param application the Application to be submitted
     */
    void sendApplication(Application application);


    /**
     * Retrieves the project the applicant has applied for.
     *
     * @return the Project the applicant is currently assigned to, or null if none
     */
    Project getProject();


    /**
     * Checks if the applicant is eligible to book a flat.
     * Eligibility requires a successful application.
     *
     * @return true if eligible to book a flat, false otherwise
     */
    boolean isAvailableToBookFlat();


    /**
     * Submits a withdrawal request for the applicant's current application.
     */
    void withdrawalApplication();


    /**
     * Submits an enquiry about a specific project.
     *
     * @param q the enquiry message
     * @param project the project the enquiry is related to
     */
    void submitEnquiry(String q, Project project);


    /**
     * Retrieves all enquiries submitted by the applicant.
     *
     * @return a list of Enquiry objects
     */
    List<Enquiry> getEnquiries();


    /**
     * Retrieves a specific enquiry by its ID.
     *
     * @param enquiryId the ID of the enquiry
     * @return the Enquiry object if found, or null if not found
     */
    Enquiry getEnquiry(int enquiryId);


    /**
     * Updates the message content of an existing enquiry.
     *
     * @param q the new enquiry message
     * @param enquiry the Enquiry object to update
     */
    void editEnquiry(String q, Enquiry enquiry);


    /**
     * Deletes the specified enquiry.
     *
     * @param enquiry the Enquiry object to be deleted
     */
    void deleteEnquiry(Enquiry enquiry);
}
