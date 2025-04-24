package Service;

import Database.ApplicationDatabase;
import Database.EnquiryDatabase;
import Database.ProjectDatabase;
import Database.RegistrationDatabase;
import Entity.*;
import View.ApplicationView;
import util.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class providing functionalities for HDB officers, including project registrations,
 * application handling, enquiry responses, and project filtering.
 */
public class OfficerService extends ApplicantService implements IOfficerService {

    // Databases
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private RegistrationDatabase registrationDatabase = RegistrationDatabase.getInstance();

    private HDBOfficer officer;

    /**
     * Constructs a new OfficerService for the specified officer.
     *
     * @param officer the HDB officer using this service
     */
    public OfficerService(HDBOfficer officer) {
        super(officer);
        this.officer = officer;
    }

    /**
     * Retrieves a list of visible projects the officer is eligible for,
     * excluding the one they are currently handling.
     *
     * @return a list of eligible visible projects
     */
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

    /**
     * Retrieves a list of eligible projects for officer registration.
     *
     * @return list of projects the officer can register for
     */
    public List<Project> getProjectsForRegi() {
        LocalDate today = DateUtil.getCurrentDate();

        List<Project> eligibleProjects = projectDatabase.findProjects().stream()
                .filter(p -> !today.isBefore(p.getOpeningDate()) && !today.isAfter(p.getClosingDate()))
                .collect(Collectors.toList());

        // Remove project already applied for
        if (officer.getApplication() != null) {
            Project appliedProject = officer.getApplication().getProject();
            eligibleProjects = eligibleProjects.stream()
                    .filter(p -> !p.getName().equals(appliedProject.getName()))
                    .collect(Collectors.toList());
        }

        // Remove project being handled
        if (officer.getProjectHandling() != null) {
            Project handlingProject = officer.getProjectHandling();
            eligibleProjects = eligibleProjects.stream()
                    .filter(p -> !DateUtil.hasDateOverlap(
                            p.getOpeningDate(), p.getClosingDate(),
                            handlingProject.getOpeningDate(), handlingProject.getClosingDate()))
                    .collect(Collectors.toList());
        }

        // Remove projects with pending/approved registrations
        List<Registration> registrations = registrationDatabase.findByOfficerNric(officer.getNric());
        for (Registration reg : registrations) {
            if (reg.getStatus() != Registration.Status.REJECTED) {
                Project regProject = reg.getProject();
                eligibleProjects = eligibleProjects.stream()
                        .filter(p -> !DateUtil.hasDateOverlap(
                                p.getOpeningDate(), p.getClosingDate(),
                                regProject.getOpeningDate(), regProject.getClosingDate()))
                        .collect(Collectors.toList());
            }
        }

        return eligibleProjects;
    }

    /**
     * Submits a registration request for the officer to manage a specific project.
     *
     * @param projectName the name of the project to register for
     */
    public void submitRegistration(String projectName) {
        Project project = projectDatabase.findProjectByName(projectName);
        if (project == null) {
            throw new IllegalArgumentException("Project not found.");
        }

        // Check for date conflict with project officer is managing
        if (officer.getProjectHandling() != null) {
            Project handlingProject = officer.getProjectHandling();
            if (DateUtil.hasDateOverlap(
                    project.getOpeningDate(), project.getClosingDate(),
                    handlingProject.getOpeningDate(), handlingProject.getClosingDate())) {
                throw new IllegalArgumentException("You are already managing a project during this period.");
            }
        }

        // Check existing registrations for conflicts
        List<Registration> registrations = registrationDatabase.findByOfficerNric(officer.getNric());
        for (Registration reg : registrations) {
            Project regProject = reg.getProject();
            if (reg.getStatus() != Registration.Status.REJECTED &&
                    DateUtil.hasDateOverlap(
                            project.getOpeningDate(), project.getClosingDate(),
                            regProject.getOpeningDate(), regProject.getClosingDate())) {
                throw new IllegalArgumentException("You already have a pending or approved registration during this period.");
            }
        }

        Registration registration = new Registration(officer, project, Registration.Status.PENDING);
        registrationDatabase.addRegistration(registration);
    }

    /**
     * Retrieves all registrations submitted by the officer.
     *
     * @return list of the officer's registrations
     */
    public List<Registration> getRegistrations() {
        return registrationDatabase.findByOfficerNric(officer.getNric());
    }

    /**
     * Submits a new application for a project, ensuring the officer is not applying to their own project.
     *
     * @param project  the project to apply to
     * @param flatType the index of the flat type
     * @return the created Application object
     */
    @Override
    public Application newApplication(Project project, int flatType) {
        if (project.equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer cannot apply for their own managed project.");
        }
        return super.newApplication(project, flatType);
    }

    /**
     * Sends an application for processing, with validation that officer cannot apply to their own project.
     *
     * @param application the application to send
     */
    @Override
    public void sendApplication(Application application) {
        if (application.getProject().equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer cannot apply for their own managed project.");
        }
        super.sendApplication(application);
    }

    /**
     * Books a flat for an applicant in a project handled by the officer.
     *
     * @param applicantNric the NRIC of the applicant
     * @param project       the project the applicant applied to
     */
    public void bookApplication(String applicantNric, Project project) {
        if (!project.equals(officer.getProjectHandling())) {
            throw new IllegalArgumentException("Officer can only manage applications under their own project.");
        }
        Application application = applicationDatabase.findByApplicantNric(applicantNric);
        if (application == null) {
            throw new IllegalArgumentException("Applicant or application not found.");
        }
        if (application.getProject().equals(project)
                && application.getStatus() != Application.ApplicationStatus.SUCCESSFUL) {
            throw new IllegalArgumentException("No successful application found for this applicant.");
        }

        application.setStatus(Application.ApplicationStatus.BOOKED);
        Project bookedProject = application.getProject();
        if (application.getFlatType() == Application.FlatType.TWO_ROOM) {
            bookedProject.set2RoomUnits(bookedProject.get2RoomUnits() - 1);
        } else if (application.getFlatType() == Application.FlatType.THREE_ROOM) {
            bookedProject.set3RoomUnits(bookedProject.get3RoomUnits() - 1);
        }
        ApplicationView.generateReceipt(application);
    }

    /**
     * Retrieves the project currently being handled by the officer.
     *
     * @return the officer's project
     */
    public Project getProjectHandling() {
        return officer.getProjectHandling(); // Or use projectDatabase to fetch by ID
    }

    /**
     * Retrieves all enquiries directed to the project the officer is handling.
     *
     * @return list of enquiries for the officer to reply to
     */
    @Override
    public List<Enquiry> getEnquiriesForReply() {
        Project project = officer.getProjectHandling();
        if (project == null) {
            throw new IllegalArgumentException("You are not handling any project.");
        }
        return enquiryDatabase.findByProject(project);
    }

    /**
     * Responds to a specific enquiry.
     *
     * @param id    the ID of the enquiry
     * @param reply the reply message
     */
    public void replyEnquiry(int id, String reply) {
        Enquiry enquiry = enquiryDatabase.findById(id);
        if (enquiry.getResponse() != null) {
            throw new IllegalArgumentException("This enquiry has already been replied.");
        }
        enquiry.setResponse(reply);
    }

    /**
     * Checks if the officer is eligible to book a flat, ensuring they haven't applied to their own project.
     *
     * @return true if booking is allowed, false otherwise
     */
    public boolean isAvailableToBookFlat() {
        Application application = applicationDatabase.findByApplicantNric(officer.getNric());
        if (application != null && application.getProject().equals(officer.getProjectHandling())) {
            return false;
        }
        return super.isAvailableToBookFlat();
    }
}
