package Interface;
import Entity.*;
import Service.*;
import View.ApplicationView;
import View.EnquiryView;
import View.ProjectView;
import View.RegistrationView;

import java.util.List;

public class OfficerInterface extends BaseInterface {

    private final IOfficerService officerService;
    private final IProjectService projectService;

    public OfficerInterface(User currentUser) {
        super(currentUser);//in this case, for currentUser: reference - User; object - Applicant/Officer/...
        this.officerService = new OfficerService((HDBOfficer) currentUser);
        this.projectService = new ProjectService();
    }

    public void start() {
        while(true) {
            System.out.println("Officer Menu: ");
            System.out.println("1. View or Apply Projects");
            System.out.println("2. View My Project");
            System.out.println("3. Book a Flat (Applicant)");
            System.out.println("4. Request Withdrawal for Application");
            System.out.println("5. Submit an Enquiry");
            System.out.println("6. Edit Enquiries");

            System.out.println("7. Register for a Project");
            System.out.println("8. View My Registration");
            System.out.println("9. View My Project");

            System.out.println("10. Manage Applications (Set to BOOKED)");
            System.out.println("11. Reply to Enquiries");
            System.out.println("12. Change Password");
            System.out.println("13. Log Out");
            System.out.println("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    List<Project> projects = officerService.getVisibleProjects();
                    ProjectView.displayProjectList(projects, currentUser);
                    System.out.println("1. Apply for a Project");
                    System.out.println("2. Back");
                    System.out.println("Enter your choice: ");
                    switch (sc.nextInt()) {
                        case 1:
                            if (officerService.haveProject()) {
                                System.out.println("You have already applied for a project.");
                                break;
                            }
                            System.out.println("Enter project name: ");
                            sc.nextLine();
                            String name = sc.nextLine();
                            Project project = projectService.findProjectByName(name);
                            if (project == null) {
                                System.out.println("Project not found.");
                                break;
                            }

                            try {
                                System.out.println("Select flat type: ");
                                System.out.println("1. 2-Room");
                                if (officerService.isSingle()) {
                                    System.out.println("2. 3-Room");
                                }
                                int flatType = sc.nextInt();
                                Application application = officerService.newApplication(project, flatType);

                                System.out.println("Confirm application details: ");
                                ApplicationView.displayApplication(application);
                                System.out.println("1. Send Application");
                                System.out.println("2. Back");
                                System.out.println("Enter your choice: ");
                                if (sc.nextInt() == 1) {
                                    officerService.sendApplication(application);
                                    System.out.println("Application submitted successfully.");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                    break;

                case 2: // view my project
                    if (officerService.haveProject()) {
                        System.out.println("Here is your project: ");
                        Project project = officerService.getProject();
                        ProjectView.displayProject(project, currentUser);
                    }
                    else
                        System.out.println("You have not applied for a project.");
                    break;

                case 3: // book a flat for myself
                    if (officerService.isAvailableToBookFlat()) {
                        System.out.println("You can book a flat now.");
                    }
                    else
                        System.out.println("Application must be SUCCESSFUL to book.");
                    break;

                case 4: // withdraw application
                    try {
                        officerService.withdrawalApplication();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5: // submit enquiry
                    System.out.println("Enter the project name to enquire about: ");
                    String enqName = sc.nextLine();
                    Project enqProject = projectService.findProjectByName(enqName);
                    if (enqProject == null) {
                        System.out.println("Project not found.");
                        break;
                    }
                    if (!officerService.isEligibleForProject(enqProject)) {
                        System.out.println("You are not eligible to enquire about this project.");
                        break;
                    }
                    System.out.println("Enter your enquiry: ");
                    String query = sc.nextLine();
                    officerService.submitEnquiry(query, enqProject);
                    System.out.println("Enquiry submitted successfully.");
                    break;

                case 6: // edit enquiry
                    List<Enquiry> enquiries = officerService.getEnquiries();
                    if (enquiries.isEmpty()) {
                        System.out.println("No enquiries found.");
                        break;
                    }
                    System.out.println("Here are your enquiries: ");
                    EnquiryView.displayEnquiries(enquiries);
                    System.out.println("Enter enquiry ID to edit: ");
                    int enquiryId = sc.nextInt();
                    sc.nextLine();
                    Enquiry enquiry = officerService.getEnquiry(enquiryId);
                    if (enquiry == null) {
                        System.out.println("Invalid enquiry ID.");
                        break;
                    }
                    System.out.println("Enter new enquiry message: ");
                    String newQuery = sc.nextLine();
                    officerService.editEnquiry(newQuery, enquiry);
                    System.out.println("Enquiry edited successfully.");
                    break;

                case 7:
                    System.out.println("Enter the project name to register for: ");
                    String regProjectName = sc.nextLine();
                    try {
                        officerService.submitRegistration(regProjectName);
                        System.out.println("Registration submitted successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 8:
                    List<Registration> registrations = officerService.getRegistrations();
                    if (registrations.isEmpty()) {
                        System.out.println("No registrations found.");
                        break;
                    }
                    System.out.println("Your registrations: ");
                    RegistrationView.displayRegistrations(registrations);
                    break;

                case 9:
                    Project managedProject1 = officerService.getManagedProject();
                    if (managedProject1 == null) {
                        System.out.println("You are not managing any project.");
                        break;
                    }
                    System.out.println("Your managed project: ");
                    ProjectView.displayProject(managedProject1, currentUser);
                    break;

                case 10: // manage application
                    System.out.println("Enter applicant NRIC: ");
                    String nric = sc.nextLine();
                    Project managedProject = officerService.getProjectHandling();
                    if (managedProject == null) {
                        System.out.println("You are not managing any project.");
                        break;
                    }
                    try {
                        officerService.bookApplication(nric, managedProject);
                        System.out.println("Application set to BOOKED successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 11: // reply to enquiries
                    Project handlingProject = officerService.getProjectHandling();
                    if (handlingProject == null) {
                        System.out.println("You are not managing any project.");
                        break;
                    }
                    List<Enquiry> projectEnquiries = officerService.getEnquiriesForReply();
                    if (projectEnquiries.isEmpty()) {
                        System.out.println("No enquiries for your project.");
                        break;
                    }
                    System.out.println("Enquiries for your project: ");
                    EnquiryView.displayEnquiries(projectEnquiries);
                    System.out.println("Enter enquiry ID to reply: ");
                    int replyId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter your response: ");
                    String response = sc.nextLine();
                    try {
                        officerService.replyEnquiry(replyId, response);
                        System.out.println("Response submitted successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 12: // change password
                    AuthService authService = new AuthService();
                    System.out.println("Enter your old password: ");
                    String oldPassword = sc.nextLine();
                    if (authService.checkUser(currentUser.getNric(), oldPassword)) {
                        System.out.println("Enter your new password: ");
                        String newPassword = sc.nextLine();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            authService.changePassword(currentUser, newPassword);
                            System.out.println("Password changed successfully.");
                        }
                        else
                            System.out.println("Invalid password.");
                    }
                    else {
                        System.out.println("Incorrect old password.");
                    }
                    break;

                case 13: // log out
                    return;

                default:
                    System.out.println("Invalid choice");
            }

        }

    }
}
