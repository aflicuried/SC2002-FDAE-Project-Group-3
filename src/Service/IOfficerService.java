package Service;

import Entity.Application;
import Entity.Enquiry;
import Entity.Project;
import Entity.Registration;

import java.util.List;


/**
 * Interface for services related to an HDB Officer.
 * Extends IApplicantService, adding functionalities for project registration,
 * booking applications, and handling enquiries on behalf of a project.
 */
public interface IOfficerService extends IApplicantService {


    /**
     * Retrieves the project currently being handled by the officer.
     *
     * @return the Project object the officer is assigned to
     */
    Project getProjectHandling();


    /**
     * Retrieves a list of projects available for officer registration.
     *
     * @return a list of available Project objects
     */
    List<Project> getProjectsForRegi();


    /**
     * Submits a registration request for the officer to handle the specified project.
     *
     * @param projectName the name of the project to register for
     */
    void submitRegistration(String projectName);


    /**
     * Retrieves the list of project registration submissions by the officer.
     *
     * @return a list of Registration objects submitted by the officer
     */
    List<Registration> getRegistrations();


    /**
     * Books a flat for the specified applicant under the given project.
     *
     * @param applicantNric the NRIC of the applicant
     * @param project the project where the flat is to be booked
     */
    void bookApplication(String applicantNric, Project project);


    /**
     * Retrieves enquiries related to the officer's assigned project that require replies.
     *
     * @return a list of Enquiry objects awaiting responses
     */
    List<Enquiry> getEnquiriesForReply();


    /**
     * Sends a reply to a specific enquiry.
     *
     * @param id the ID of the enquiry
     * @param reply the reply message content
     */    
    void replyEnquiry(int id, String reply) ;
}
