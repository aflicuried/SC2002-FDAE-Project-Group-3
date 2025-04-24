package Service;

import Entity.*;

import java.time.LocalDate;
import java.util.List;


/**
 * Interface for services related to an HDB Manager.
 * Provides functionalities for managing projects, registrations, applications,
 * enquiries, and generating reports.
 */
public interface IManagerService {

    /**
     * Checks if there is a conflict with existing projects during the specified date range.
     *
     * @param openingDate the proposed opening date of the project
     * @param closingDate the proposed closing date of the project
     * @return true if a conflict exists, false otherwise
     */
    boolean hasProjectConflict(LocalDate openingDate, LocalDate closingDate);


    /**
     * Creates a new HDB project with the provided details.
     *
     * @param projectName the name of the project
     * @param neighbourhood the neighbourhood where the project is located
     * @param room2 the number of 2-room flats
     * @param price2 the price of 2-room flats
     * @param room3 the number of 3-room flats
     * @param price3 the price of 3-room flats
     * @param openingDate the opening date for the project
     * @param closingDate the closing date for the project
     * @param slots the total number of available slots
     * @param user the manager creating the project
     * @return the created Project object
     */
    Project createProject(String projectName, String neighbourhood, int room2, int price2,
                          int room3, int price3, LocalDate openingDate, LocalDate closingDate, int slots, User user);



    /**
     * Checks whether the current manager has authorization over the specified project.
     *
     * @param project the project to check authorization for
     * @return true if the manager is authorized, false otherwise
     */
    boolean checkAuthForProject(Project project);


    /**
     * Edits the specified project based on a provided option.
     *
     * @param choice the attribute to edit (e.g., name, dates, prices)
     * @param project the project to be edited
     */
    void editProject(int choice, Project project);


    /**
     * Toggles the visibility status of the specified project.
     *
     * @param project the project to update
     */
    void shiftVisibility(Project project);


    /**
     * Finds a registration by its ID.
     *
     * @param id the ID of the registration
     * @return the Registration object if found, otherwise null
     */
    Registration findById(int id);

    
    /**
     * Retrieves all project registrations for the manager.
     *
     * @return a list of Registration objects
     */
    List<Registration> getRegistrations();


    /**
     * Approves the specified project registration.
     *
     * @param registration the Registration object to approve
     */
    void approveRegistration(Registration registration);


    /**
     * Rejects the specified project registration.
     *
     * @param registration the Registration object to reject
     */
    void rejectRegistration(Registration registration);


    /**
     * Retrieves all submitted applications for the manager's project.
     *
     * @return a list of Application objects
     */
    List<Application> getApplications();


    /**
     * Finds an application based on the applicant's NRIC.
     *
     * @param nric the NRIC of the applicant
     * @return the Application object if found, otherwise null
     */
    Application findApplicationByNric(String nric);


    /**
     * Approves the specified application.
     *
     * @param application the Application object to approve
     */
    void approveApplication(Application application);


    /**
     * Rejects the specified application.
     *
     * @param application the Application object to reject
     */
    void rejectApplication(Application application);


    /**
     * Retrieves all applications that have requested withdrawals.
     *
     * @return a list of Application objects marked for withdrawal
     */
    List<Application> getWithdrawals();


    /**
     * Approves the withdrawal request for the specified application.
     *
     * @param application the Application object requesting withdrawal
     */
    void approveWithdrawal(Application application);


    /**
     * Rejects the withdrawal request for the specified application.
     *
     * @param application the Application object requesting withdrawal
     */
    void rejectWithdrawal(Application application);

    
    //List<Applicant> getApplicantsForReport();
    /**
     * Retrieves a list of applicants for report generation based on a specific filter.
     *
     * @param filterType the type of filter (e.g., age, status)
     * @param filterValue the value to filter by
     * @return a list of Applicant objects matching the criteria
     */
    List<Applicant> getApplicantsForReport(String filterType, String filterValue);


    /**
     * Retrieves all enquiries submitted for the manager's project.
     *
     * @return a list of Enquiry objects
     */
    List<Enquiry> getEnquiries();


    /**
     * Checks if the manager is authorized to reply to the specified enquiry.
     *
     * @param id the ID of the enquiry
     * @return true if authorized, false otherwise
     */
    boolean checkAuthForEnquiry(int id);


    /**
     * Submits a reply to a specific enquiry.
     *
     * @param Id the ID of the enquiry
     * @param reply the response message
     */
    void replyEnquiry(int Id, String reply);

}
