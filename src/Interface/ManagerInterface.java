package Interface;

import Entity.*;
import Service.*;
import View.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ManagerInterface extends BaseInterface {

    private final IManagerService managerService;
    private final IProjectService projectService;

    public ManagerInterface(User currentUser) {
        super(currentUser);//in this case, for currentUser: reference - User; object - roles
        this.managerService = new ManagerService((HDBManager) currentUser);
        this.projectService = new ProjectService();
        super.setProjectService(this.projectService);
    }

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
                case 1:
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
                        case 1:
                            System.out.println("Enter information.");
                            System.out.println("Project Name: ");
                            String projectName = sc.next();
                            System.out.println("Neighbourhood: ");
                            String neighbourhood = sc.next();
                            int UnitsOf2Room = readIntInput("# of Units of 2-Room Flat Type:");
                            int PriceOf2Room = readIntInput("2-room Flat Price:");
                            int UnitsOf3Room = readIntInput("# of Units of 3-Room Flat Type:");
                            int PriceOf3Room = readIntInput("3-room Flat Price:");
                            System.out.println("Application Opening Date (format: yyyy/M/d, e.g. 2000/1/30):");
                            String openingDateStr = sc.nextLine();
                            System.out.println("Application Closing Date (format: yyyy/M/d, e.g. 2000/1/30):");
                            String closingDateStr = sc.nextLine();
                            System.out.println("Available HDB Officer Slots:"); //need 'confirm details' and EH.
                            int officerSlots = sc.nextInt();
                            sc.nextLine();

                            try {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
                                LocalDate openingDate = LocalDate.parse(openingDateStr, formatter);
                                LocalDate closingDate = LocalDate.parse(closingDateStr, formatter);

                                if (managerService.hasProjectConflict(openingDate, closingDate)) {
                                    throw (new Exception("You are already managing a project during this period."));
                                }

                                Project project = managerService.createProject(projectName,
                                        neighbourhood, UnitsOf2Room, PriceOf2Room,
                                        UnitsOf3Room, PriceOf3Room, openingDateStr, closingDateStr,
                                        officerSlots, currentUser);
                                System.out.println("Confirm Details:");
                                ProjectView.displayManagedProject(project);

                                System.out.println("1 - Create Project");
                                System.out.println("2 - Back");
                                choice = readIntInput("Enter your choice: ");


                                switch (choice) {
                                    case 1:
                                        projectService.addProject(project);
                                        System.out.println("Project created successfully.");
                                        break;
                                    case 2:
                                        break;
                                    default:
                                        System.out.println("Invalid choice.");
                                }

                            } catch (Exception e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;

                        case 2:
                            System.out.println("Enter project name: ");
                            String name2 = sc.nextLine();
                            Project project2 = projectService.findProjectByName(name2);
                            if (project2 == null) {
                                System.out.println("Project not found.");
                                break;
                            }
                            if (!managerService.checkAuthForProject(project2)) {
                                System.out.println("You are not authorized to edit this project.");
                                break;
                            }
                            System.out.println("Edit or Delete:\n1 - Edit\n2 - Delete");
                            choice = readIntInput("Enter your choice: ");

                            switch (choice) {
                                case 1:
                                    System.out.println("Which attribute do you want to edit? Enter your choice: ");
                                    System.out.println("1 - name\n2 - neighbourhood\n" +
                                            "3 - 2-Room available units\n4 - 2-Room flat price\n" +
                                            "5 - 3-Room available units\n6 - 3-Room flat price\n" +
                                            "7 - Application Opening Date\n8 - Application Closing Date\n" +
                                            "9 - officer slots");
                                    choice = readIntInput("Enter your choice: ");

                                    try {
                                        managerService.editProject(choice, project2);
                                        System.out.println("Project updated successfully. Here are the details:");
                                        ProjectView.displayManagedProject(project2);
                                    } catch (Exception e) {
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                    break;
                                case 2:
                                    projectService.deleteProject(project2);
                                    System.out.println("Project deleted successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;

                        case 3:
                            System.out.println("Enter the project name you want to toggle: ");
                            String name3 = sc.nextLine();
                            Project project3 = projectService.findProjectByName(name3);
                            if (project3 == null) {
                                System.out.println("Project not found.");
                                break;
                            }
                            if (!managerService.checkAuthForProject(project3)) {
                                System.out.println("You are not authorized to edit this project.");
                                break;
                            }
                            managerService.shiftVisibility(project3);
                            System.out.println("Project visibility shifted successfully.");
                            break;

                        case 4:
                            List<Project> projects2 = projectService.findByManager(currentUser.getName());
                            if (projects2.isEmpty()) {
                                System.out.println("Project not found.");
                                break;
                            }
                            System.out.println("Here are the projects you created:");
                            ProjectView.displayProjectList(projects2, currentUser);
                            break;

                        case 5:
                            manageFilters();
                            break;

                        case 6:
                            break;

                        default:
                            System.out.println("Invalid choice");
                    }
                    break;

                case 2:
                    List<Registration> registrations = managerService.getRegistrations();
                    if (registrations.isEmpty()) {
                        System.out.println("No pending or approved registrations found.");
                        break;
                    }
                    System.out.println("Below are registrations.");
                    RegistrationView.displayRegistrations(registrations);
                    System.out.println("1 - Approve or reject registration");
                    System.out.println("2 - Back");
                    choice = readIntInput("Enter your choice: ");

                    switch (choice) {
                        case 1:
                            System.out.println("Enter the registration ID you want to approve or reject: ");
                            int registrationId = sc.nextInt();
                            sc.nextLine();

                            try {
                                Registration registration = managerService.findById(registrationId);
                                if (registration == null)
                                    throw (new IllegalArgumentException("Registration not found."));
                                if (registration.getStatus().equals(Registration.Status.APPROVED)){
                                    throw (new IllegalArgumentException("Registration is already approved."));
                                }

                                boolean canApprove = registration.getProject().getOfficerSlots() > 0;
                                if (canApprove) {
                                    System.out.println("1 - Approve");
                                    System.out.println("2 - Reject");
                                }
                                else {
                                    System.out.println("1 - Reject (No officer slots available)");
                                }
                                int choice = readIntInput("Enter your choice: ");


                                if (canApprove && choice == 1) {
                                    managerService.approveRegistration(registration);
                                    System.out.println("Registration approved successfully.");
                                }
                                else if (choice == 1 || choice == 2) {
                                    managerService.rejectRegistration(registration);
                                    System.out.println("Registration rejected successfully.");
                                }
                                else {
                                    System.out.println("Invalid choice.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;


                case 3:
                    List<Application> applications = managerService.getApplications();
                    if (applications.isEmpty()) {
                        System.out.println("No pending or approved applications found.");
                        break;
                    }
                    System.out.println("Below are applications.");
                    ApplicationView.displayApplications(applications);
                    System.out.println("1 - approve or reject application\n2 - approve or reject withdrawal");
                    choice = readIntInput("Enter your choice: ");

                    switch (choice) {
                        case 1:
                            System.out.println("Enter the user NRIC you want to approve or reject: ");
                            String NRIC = sc.nextLine();
                            Application application = managerService.findApplicationByNric(NRIC);
                            if (application == null) {
                                System.out.println("Application not found.");
                                break;
                            }
                            if (application.getStatus() != Application.ApplicationStatus.PENDING){
                                System.out.println("You are not allowed to approve or reject this application.");
                                break;
                            }
                            System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
                            choice = readIntInput("Enter your choice: ");

                            switch (choice) {
                                case 1:
                                    managerService.approveApplication(application);
                                    System.out.println("Approve successfully.");
                                    break;
                                case 2:
                                    managerService.rejectApplication(application);
                                    System.out.println("Reject successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;

                        case 2:
                            System.out.println("Enter the user NRIC you want to approve or reject: ");
                            String NRIC2 = sc.nextLine();
                            Application application2 = managerService.findApplicationByNric(NRIC2);
                            if (application2 == null) {
                                System.out.println("Application for withdrawal not found.");
                                break;
                            }
                            if (!application2.isWithdrawal()){
                                System.out.println("You are not allowed to approve this application for withdrawal.");
                                break;
                            }
                            System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
                            choice = readIntInput("Enter your choice: ");

                            switch (choice) {
                                case 1:
                                    managerService.approveWithdrawal(application2);
                                    System.out.println("Approve withdrawal successfully.");//need details?
                                    break;
                                case 2:
                                    managerService.rejectWithdrawal(application2);
                                    System.out.println("Reject withdrawal successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;

                case 4:
                    System.out.println("Generate reports based on:");
                    System.out.println("1 - All applicants");
                    System.out.println("2 - By flat type");
                    System.out.println("3 - By project");
                    System.out.println("4 - By age");
                    System.out.println("5 - By marital status");
                    choice = readIntInput("Enter your choice: ");

                    List<Applicant> applicants = new ArrayList<>();

                    switch (choice) {
                        case 1:
                            applicants = managerService.getApplicantsForReport(null, null);
                            break;
                        case 2:
                            System.out.println("Enter flat type (TWO_ROOM/THREE_ROOM): ");
                            String flatType = sc.nextLine();
                            applicants = managerService.getApplicantsForReport("flatType", flatType);
                            break;
                        case 3:
                            System.out.println("Enter project name: ");
                            String projectName = sc.nextLine();
                            applicants = managerService.getApplicantsForReport("projectName", projectName);
                            break;
                        case 4:
                            System.out.println("Enter age: ");
                            String age = sc.nextLine();
                            applicants = managerService.getApplicantsForReport("age", age);
                            break;
                        case 5:
                            System.out.println("Enter marital status (SINGLE/MARRIED): ");
                            String status = sc.nextLine();
                            applicants = managerService.getApplicantsForReport("maritalStatus", status);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }

                    if(applicants.isEmpty()) {
                        System.out.println("No approved applicants found with these criteria.");
                        break;
                    }
                    System.out.println("Below is a report of the filtered list of applicants.");
                    UserView.displayApplicants(applicants);
                    break;


                case 5:
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
                            case 1:
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
                                break;
                            case 2:
                                break;
                            default:
                                System.out.println("Invalid choice.");
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 6: // change password
                    AuthService authService = new AuthService();
                    System.out.println("Enter your old password: ");
                    String oldPassword = sc.next();
                    sc.nextLine();
                    if (authService.checkUser(currentUser.getNric(), oldPassword)) {
                        System.out.println("Enter your new password: ");
                        String newPassword = sc.next();
                        sc.nextLine();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            authService.changePassword(currentUser, newPassword);
                            System.out.println("Password changed successfully.");
                            return;
                        }
                        else
                            System.out.println("Invalid password.");
                    }
                    else {
                        System.out.println("Incorrect old password.");
                    }
                    break;

                case 7:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
