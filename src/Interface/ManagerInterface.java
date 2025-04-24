package Interface;

import Entity.*;
import Service.*;
import View.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * ManagerInterface provides the command-line interface functionalities 
 * available to HDB Managers. It enables managers to manage projects, 
 * registrations, applications, generate reports, and handle enquiries.
 */

public class ManagerInterface extends BaseInterface {

    private final IManagerService managerService;
    private final IProjectService projectService;


    /**
     * ManagerInterface provides the command-line interface functionalities 
     * available to HDB Managers. It enables managers to manage projects, 
     * registrations, applications, generate reports, and handle enquiries.
     */
    public ManagerInterface(User currentUser) {
        super(currentUser);//in this case, for currentUser: reference - User; object - roles
        this.managerService = new ManagerService((HDBManager) currentUser);
        this.projectService = new ProjectService();
        super.setProjectService(this.projectService);
    }

    //case 1
    public void manageProjects() {
        List<Project> projects = projectService.findAllProjects();
        System.out.println(filterSettings.getFilterSummary());
        projects = applyProjectFilters(projects);

        if (projects.isEmpty()) {
            System.out.println("Project not found");
        }

        ProjectView.displayManagedList(projects);
        System.out.println("1 - Create A New Project");
        System.out.println("2 - Edit or Delete A Project");
        System.out.println("3 - Toggle visibility of a Project");
        System.out.println("4 - Filter projects you created");
        System.out.println("5 - Manage Filters");
        System.out.println("6 - Back");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> createAProject();
            case 2 -> editOrDeleteProject();
            case 3 -> toggleVisibility();
            case 4 -> filterProjectsManagerCreated();
            case 5 -> manageFilters();
            case 6 -> {}
            default -> System.out.println("Invalid choice");
        }
    }

    //case 1: create project
    /**
     * Gathers user input to create a new housing project, checks for conflicts, 
     * and adds the project through the project service if confirmed.
     */
    public void createAProject() {
        try {
            System.out.println("Enter information.");
            System.out.println("Project Name: ");
            String projectName = sc.nextLine();
            System.out.println("Neighbourhood: ");
            String neighbourhood = sc.nextLine();
            int UnitsOf2Room = readIntInput("# of Units of 2-Room Flat Type:");
            int PriceOf2Room = readIntInput("2-room Flat Price:");
            int UnitsOf3Room = readIntInput("# of Units of 3-Room Flat Type:");
            int PriceOf3Room = readIntInput("3-room Flat Price:");
            LocalDate openingDate = parseDate("Application Opening Date (format: yyyy/M/d, e.g. 2000/1/30):");
            LocalDate closingDate = parseDate("Application Closing Date (format: yyyy/M/d, e.g. 2000/1/30):");
            int officerSlots = readIntInput("Available HDB Officer Slots:");
            if (managerService.hasProjectConflict(openingDate, closingDate)) {
                throw new IllegalArgumentException("You are already managing a project during this period.");
            }

            Project project = managerService.createProject(projectName,
                    neighbourhood, UnitsOf2Room, PriceOf2Room,
                    UnitsOf3Room, PriceOf3Room, openingDate, closingDate,
                    officerSlots, currentUser);
            System.out.println("Confirm Details:");
            ProjectView.displayManagedProject(project);

            System.out.println("1 - Create Project");
            System.out.println("2 - Back");
            choice = readIntInput("Enter your choice: ");
            switch (choice) {
                case 1 -> {
                    projectService.addProject(project);
                    System.out.println("Project created successfully.");
                }
                case 2 -> {}
                default -> System.out.println("Invalid choice.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    //case 1: edit or delete a project
    /**
     * Allows the manager to edit attributes of or delete an existing project 
     * after verifying authorization and project existence.
     */
    public void editOrDeleteProject() {
        System.out.println("Enter project name: ");
        String name = sc.nextLine();
        Project project = projectService.findProjectByName(name);
        if (project == null) {
            System.out.println("Project not found.");
            return;
        }
        if (!managerService.checkAuthForProject(project)) {
            System.out.println("You are not authorized to edit this project.");
            return;
        }
        System.out.println("Edit or Delete:\n1 - Edit\n2 - Delete");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                System.out.println("Which attribute do you want to edit? Enter your choice: ");
                System.out.println("1 - name\n2 - neighbourhood\n" +
                        "3 - 2-Room available units\n4 - 2-Room flat price\n" +
                        "5 - 3-Room available units\n6 - 3-Room flat price\n" +
                        "7 - Application Opening Date\n8 - Application Closing Date\n" +
                        "9 - officer slots");
                choice = readIntInput("Enter your choice: ");
                try {
                    managerService.editProject(choice, project);
                    System.out.println("Project updated successfully. Here are the details:");
                    ProjectView.displayManagedProject(project);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {
                projectService.deleteProject(project);
                System.out.println("Project deleted successfully.");
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //case 1: toggle visibility
    /**
     * Toggles the visibility (active/inactive) status of a project managed 
     * by the current user.
     */
    public void toggleVisibility() {
        System.out.println("Enter the project name you want to toggle: ");
        String name3 = sc.nextLine();
        Project project3 = projectService.findProjectByName(name3);
        if (project3 == null) {
            System.out.println("Project not found.");
            return;
        }
        if (!managerService.checkAuthForProject(project3)) {
            System.out.println("You are not authorized to edit this project.");
            return;
        }
        managerService.shiftVisibility(project3);
        System.out.println("Project visibility shifted successfully.");
    }
    //case 1: filter projects the manager created
    /**
     * Displays the list of housing projects created by the current manager.
     */
    public void filterProjectsManagerCreated() {
        List<Project> projects = projectService.findByManager(currentUser.getName());
        if (projects.isEmpty()) {
            System.out.println("Project not found.");
            return;
        }
        System.out.println("Here are the projects you created:");
        ProjectView.displayProjectList(projects, currentUser);
    }

    //case 2
    /**
     * Allows the manager to view all registrations and approve or reject 
     * individual ones, based on availability and status.
     */
    public void manageRegistrations() {
        List<Registration> registrations = managerService.getRegistrations();
        if (registrations.isEmpty()) {
            System.out.println("No pending or approved registrations found.");
            return;
        }
        System.out.println("Below are registrations.");
        RegistrationView.displayRegistrations(registrations);
        System.out.println("1 - Approve or reject registration");
        System.out.println("2 - Back");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                int registrationId = readIntInput("Enter the registration ID you want to approve or reject: ");
                try {
                    Registration registration = managerService.findById(registrationId);
                    if (registration == null)
                        throw (new Exception("Registration not found."));
                    if (registration.getStatus().equals(Registration.Status.APPROVED)) {
                        throw (new Exception("Registration is already approved."));
                    }

                    boolean canApprove = registration.getProject().getOfficerSlots() > 0;
                    if (canApprove) {
                        System.out.println("1 - Approve");
                        System.out.println("2 - Reject");
                    } else {
                        System.out.println("1 - Reject (No officer slots available)");
                    }
                    int choice = readIntInput("Enter your choice: ");


                    if (canApprove && choice == 1) {
                        managerService.approveRegistration(registration);
                        System.out.println("Registration approved successfully.");
                    } else if (choice == 1 || choice == 2) {
                        managerService.rejectRegistration(registration);
                        System.out.println("Registration rejected successfully.");
                    } else {
                        System.out.println("Invalid choice.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> {}
            default -> System.out.println("Invalid choice.");
        }
    }

    //case 3
    /**
     * Displays all pending or approved applications and allows the manager 
     * to either approve/reject applications or handle withdrawal requests.
     */
    public void manageApplications() {
        List<Application> applications = managerService.getApplications();
        if (applications.isEmpty()) {
            System.out.println("No pending or approved applications found.");
            return;
        }
        System.out.println("Below are applications.");
        ApplicationView.displayApplications(applications);
        System.out.println("1 - approve or reject application\n2 - approve or reject withdrawal");
        choice = readIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> approveOrRejectApplication();
            case 2 -> approveOrRejectWithdrawal();
            default -> System.out.println("Invalid choice.");
        }
    }
    //case 3: approve or reject application
    /**
     * Enables the manager to approve or reject a specific application 
     * based on the applicant's NRIC and application status.
     */
    public void approveOrRejectApplication() {
        System.out.println("Enter the user NRIC you want to approve or reject: ");
        String NRIC = sc.nextLine();
        Application application = managerService.findApplicationByNric(NRIC);
        if (application == null) {
            System.out.println("Application not found.");
            return;
        }
        if (application.getStatus() != Application.ApplicationStatus.PENDING){
            System.out.println("You are not allowed to approve or reject this application.");
            return;
        }
        System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                managerService.approveApplication(application);
                System.out.println("Approve successfully.");
            }
            case 2 -> {
                managerService.rejectApplication(application);
                System.out.println("Reject successfully.");
            }
            default -> System.out.println("Invalid choice.");
        }
    }
    //case 3: approve or reject withdrawal
    /**
     * Allows the manager to approve or reject an applicant's withdrawal request 
     * based on NRIC, if the application is marked as a withdrawal.
     */
    public void approveOrRejectWithdrawal() {
        System.out.println("Enter the user NRIC you want to approve or reject: ");
        String NRIC2 = sc.nextLine();
        Application application2 = managerService.findApplicationByNric(NRIC2);
        if (application2 == null) {
            System.out.println("Application for withdrawal not found.");
            return;
        }
        if (!application2.isWithdrawal()){
            System.out.println("You are not allowed to approve this application for withdrawal.");
            return;
        }
        System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                managerService.approveWithdrawal(application2);
                System.out.println("Approve withdrawal successfully.");//need details?
            }
            case 2 -> {
                managerService.rejectWithdrawal(application2);
                System.out.println("Reject withdrawal successfully.");
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    //case 4
    /**
     * Generates various reports on approved applicants filtered by 
     * flat type, project, age, or marital status.
     */
    public void generateReports() {
        System.out.println("Generate reports based on:");
        System.out.println("1 - All applicants");
        System.out.println("2 - By flat type");
        System.out.println("3 - By project");
        System.out.println("4 - By age");
        System.out.println("5 - By marital status");
        choice = readIntInput("Enter your choice: ");

        List<Applicant> applicants = new ArrayList<>();

        switch (choice) {
            case 1 -> applicants = managerService.getApplicantsForReport(null, null);
            case 2 -> {
                System.out.println("Enter flat type (TWO_ROOM/THREE_ROOM): ");
                String flatType = sc.nextLine();
                applicants = managerService.getApplicantsForReport("flatType", flatType);
            }
            case 3 -> {
                System.out.println("Enter project name: ");
                String projectName = sc.nextLine();
                applicants = managerService.getApplicantsForReport("projectName", projectName);
            }
            case 4 -> {
                System.out.println("Enter age: ");
                String age = sc.nextLine();
                applicants = managerService.getApplicantsForReport("age", age);
            }
            case 5 -> {
                System.out.println("Enter marital status (SINGLE/MARRIED): ");
                String status = sc.nextLine();
                applicants = managerService.getApplicantsForReport("maritalStatus", status);
            }
            default -> System.out.println("Invalid choice.");
        }

        if (applicants.isEmpty()) {
            System.out.println("No approved applicants found with these criteria.");
            return;
        }
        System.out.println("Below is a report of the filtered list of applicants.");
        UserView.displayApplicants(applicants);
    }


    /**
     * Displays enquiries assigned to the manager and provides functionality 
     * to reply to specific enquiries if authorized.
     */
    public void manageEnquiries() {
        try {
            List<Enquiry> enquiries = managerService.getEnquiries();
            if (enquiries.isEmpty()) {
                throw (new IllegalArgumentException("No enquiries found."));
            }
            System.out.println("Here are the enquiries: ");
            EnquiryView.displayEnquiries(enquiries);
            System.out.println("1 - Reply\n2 - Back");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the Enquiry ID you want to reply: ");
                    int enquiryId = sc.nextInt();
                    sc.nextLine();
                    if (!managerService.checkAuthForEnquiry(enquiryId)) {
                        throw (new IllegalArgumentException("You are not authorized to reply this enquiry."));
                    }
                    System.out.println("Enter your reply:");
                    String reply = sc.nextLine();
                    managerService.replyEnquiry(enquiryId, reply);
                    System.out.println("Reply successfully.");
                }
                case 2 -> {}
                default -> System.out.println("Invalid choice.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Main entry point for the manager interface menu loop. Displays the 
     * menu options and routes input to the corresponding functions.
     */
    @Override
    public void start() {
        while(true) {
            System.out.println("Welcome, " + currentUser.getName() + "!");
            System.out.println("Manager Menu: ");
            System.out.println("1 - Manage Projects");//1 create: enter info and create; 2 edit or delete: enter name and manage 3 toggle visibility; 4 filter projects they created
            System.out.println("2 - Manage Registrations");//1 approve (update officer slots) or reject (only in-charge);
            System.out.println("3 - Manage Applications");//1 approve or reject applicant's application (approval is limited to the supply of flats) and 2 withdrawal
            System.out.println("4 - Generate reports"); //1 applicants - flatType, project name, age, marital status
            System.out.println("5 - Manage Enquiries"); //1 reply to enquiries he/she is handling
            System.out.println("6 - Change password");
            System.out.println("7 - Log out");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> manageProjects();
                case 2 -> manageRegistrations();
                case 3 -> manageApplications();
                case 4 -> generateReports();
                case 5 -> manageEnquiries();
                case 6 -> { if (changePassword()) { return; } }
                case 7 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
