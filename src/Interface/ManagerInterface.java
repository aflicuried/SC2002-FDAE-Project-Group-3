package Interface;

import Entity.*;
import Service.*;
import View.*;

import java.util.List;

public class ManagerInterface extends BaseInterface {

    private final IManagerService managerService;
    private final IProjectService projectService;

    public ManagerInterface(User currentUser) {
        super(currentUser);//in this case, for currentUser: reference - User; object - roles
        this.managerService = new ManagerService((HDBManager) currentUser);
        this.projectService = new ProjectService();
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
            System.out.println("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    ProjectView.displayProjectList(projectService.findAllProjects(), currentUser);
                    System.out.println("1 - Create A New Project");
                    System.out.println("2 - Edit or Delete A Project");
                    System.out.println("3 - Toggle visibility of a Project");
                    System.out.println("4 - Filter projects you created");
                    System.out.println("5 - Back");
                    System.out.println("Enter your choice: ");

                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.println("Enter information.");
                            System.out.println("Project Name: ");
                            String projectName = sc.next();
                            System.out.println("Neighbourhood: ");
                            String neighbourhood = sc.next();
                            System.out.println("# of Units of 2-Room Flat Type:");
                            int UnitsOf2Room = sc.nextInt();
                            sc.nextLine();
                            System.out.println("2-room Flat Price:");
                            int PriceOf2Room = sc.nextInt();
                            sc.nextLine();
                            System.out.println("# of Units of 3-Room Flat Type:");
                            int UnitsOf3Room = sc.nextInt();
                            sc.nextLine();
                            System.out.println("3-room Flat Price:");
                            int PriceOf3Room = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Application Opening Date:");
                            String openingDate = sc.nextLine();
                            System.out.println("Application Closing Date:");
                            String closingDate = sc.nextLine();
                            System.out.println("Available HDB Officer Slots:"); //need 'confirm details' and EH.
                            int officerSlots = sc.nextInt();
                            sc.nextLine();

                            try {
                                Project project = managerService.createProject(projectName,
                                        neighbourhood, UnitsOf2Room, PriceOf2Room,
                                        UnitsOf3Room, PriceOf3Room, openingDate, closingDate,
                                        officerSlots, currentUser);
                                System.out.println("Confirm Details:");
                                ProjectView.displayProject(project, currentUser);

                                System.out.println("1 - Create Project");
                                System.out.println("2 - Back");
                                System.out.println("Enter your choice: ");

                                choice = sc.nextInt();
                                sc.nextLine();

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

                            choice = sc.nextInt();
                            sc.nextLine();

                            switch (choice) {
                                case 1:
                                    System.out.println("Which attribute do you want to edit? Enter your choice: ");
                                    System.out.println("1 - name\n2 - neighbourhood\n" +
                                            "3 - 2-Room available units\n4 - 2-Room flat price\n" +
                                            "5 - 3-Room available units\n6 - 3-Room flat price\n" +
                                            "7 - Application Opening Date\n8 - Application Closing Date\n" +
                                            "9 - officer slots");

                                    choice = sc.nextInt();
                                    sc.nextLine();

                                    try {
                                        managerService.editProject(choice, project2);
                                        System.out.println("Project updated successfully. Here are the details:");
                                        ProjectView.displayProject(project2, currentUser);
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

                        case 3:
                            System.out.println("Enter the project name you want to toggle: ");
                            String name3 = sc.nextLine();
                            Project project3 = projectService.findProjectByName(name3);
                            if (project3 != null) {
                                managerService.shiftVisibility(project3);
                                System.out.println("Project visibility shifted successfully.");
                            }
                            else
                                System.out.println("Project not found.");
                            break;

                        case 4:
                            List<Project> projects = projectService.findByManager(currentUser.getName());
                            if (projects.isEmpty()) {
                                System.out.println("Project not found.");
                                break;
                            }
                            System.out.println("Here are the projects you created:");
                            ProjectView.displayProjectList(projects, currentUser);
                            break;

                        case 5:
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
                    System.out.println("Enter the registration ID you want to approve or reject: ");
                    int registrationId = sc.nextInt();
                    sc.nextLine();
                    Registration registration = managerService.findById(registrationId);
                    if (registration == null) {
                        System.out.println("Invalid registration ID.");
                        break;
                    }
                    boolean canApprove = registration.getProject().getOfficerSlots() > 0;
                    if (canApprove) {
                        System.out.println("1 - Approve");
                        System.out.println("2 - Reject");
                    }
                    else {
                        System.out.println("1 - Reject (No officer slots available)");
                    }
                    System.out.println("Enter your choice: ");
                    int action = sc.nextInt();
                    sc.nextLine();

                    try {
                        if (canApprove && action == 1) {
                            managerService.approveRegistration(registration);
                            System.out.println("Registration approved successfully.");
                        }
                        else if (action == 1 || action == 2) {
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

                case 3:
                    List<Application> applications = managerService.getApplications();
                    if (applications.isEmpty()) {
                        System.out.println("No pending or approved applications found.");
                        break;
                    }
                    System.out.println("Below are applications.");
                    ApplicationView.displayApplications(applications);
                    System.out.println("1 - approve or reject application\n2 - approve or reject withdrawal");

                    choice = sc.nextInt();
                    sc.nextLine();

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
                            choice = sc.nextInt();
                            sc.nextLine();

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
                            choice = sc.nextInt();
                            sc.nextLine();

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
                    }

                case 4:
                    List<Applicant> applicants = managerService.getApplicantsForReport();
                    if(applicants.isEmpty()) {
                        System.out.println("No approved applicants found.");
                        break;
                    }
                    System.out.println("Below is a report of the list of applicants.");
                    UserView.displayApplicants(applicants);//NEED FILTER!
                    break;

                case 5:
                    List<Enquiry> enquiries = managerService.getEnquiries();
                    if (enquiries.isEmpty()) {
                        System.out.println("No enquiries found.");
                        break;
                    }
                    System.out.println("Here are the enquiries: ");
                    EnquiryView.displayEnquiries(enquiries);
                    System.out.println("1 - Reply\n2 - Back\nEnter your choice: ");
                    choice = sc.nextInt();
                    sc.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.println("Enter the Enquiry ID you want to reply: ");
                            int enquiryId = sc.nextInt();
                            sc.nextLine();
                            if(!managerService.checkAuthForEnquiry(enquiryId)){
                                System.out.println("You are not authorized to reply this enquiry.");
                                break;
                            }
                            System.out.println("Enter your reply.");
                            String reply = sc.nextLine();
                            managerService.replyEnquiry(enquiryId, reply);
                            System.out.println("Reply successfully.");
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;

                case 6: // change password
                    AuthService authService = new AuthService();
                    System.out.println("Enter your old password: ");
                    String oldPassword = sc.nextLine();
                    if (authService.checkUser(currentUser.getNric(), oldPassword)) {
                        System.out.println("Enter your new password: ");
                        String newPassword = sc.nextLine();
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
