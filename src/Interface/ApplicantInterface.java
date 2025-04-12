package Interface;
import Entity.*;
import Service.*;
import View.ApplicationView;
import View.ProjectView;
import View.EnquiryView;

import java.util.List;
import java.util.Scanner;

public class ApplicantInterface extends BaseInterface {
    //set final to maintain this thread
    private final IApplicantService applicantService;
    private final IProjectService projectService;

    public ApplicantInterface(User currentUser) {
        super(currentUser);    //in this case, for currentUser: reference - User; object - roles
        this.applicantService = new ApplicantService((Applicant) currentUser);
        this.projectService = new ProjectService();
    }

    public void start() {
        while(true) {
            System.out.println("Applicant Menu: ");
            System.out.println("1. View or Apply Projects");
            System.out.println("2. View My Project");
            System.out.println("3. Book a Flat");
            System.out.println("4. Request Withdrawal for Application");
            System.out.println("5. Submit an Enquiry");
            System.out.println("6. Edit Enquiries");
            System.out.println("7. Change password");
            System.out.println("8. Log Out");
            System.out.println("Enter your choice: ");

            switch (sc.nextInt()) {

                case 1:
                    List<Project> projects = applicantService.getVisibleProjects(); //handle eligible projects
                    ProjectView.displayProjectList(projects, currentUser); // whether to display 2-room or 3-room in every project
                    System.out.println("1. Apply A Project");
                    System.out.println("2. Back");
                    System.out.println("Enter your choice: ");

                    switch (sc.nextInt()) {
                        case 1:
                            if (applicantService.haveProject()) {
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
                                if (applicantService.isSingle())
                                    System.out.println("2. 3-Room");
                                int flatType = sc.nextInt();
                                Application application = applicantService.newApplication(project, flatType);

                                System.out.println("Confirm application details: ");
                                ApplicationView.displayApplication(application);
                                System.out.println("1. Send Application");
                                System.out.println("2. Back");
                                System.out.println("Enter your choice: ");
                                if (sc.nextInt() == 1) {
                                    applicantService.sendApplication(application);
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
                    if (applicantService.haveProject()) {
                        System.out.println("Here is your project: ");
                        Project project = applicantService.getProject();
                        ProjectView.displayProject(project, currentUser);// down-casting!
                    }
                    else
                        System.out.println("You have not applied for a project.");
                    break;

                case 3:
                    if (applicantService.isAvailableToBookFlat()) {
                        System.out.println("You can book a flat now.");
                    }
                    else
                        System.out.println("Application must be SUCCESSFUL to book.");
                    break;

                case 4: // withdraw application
                    try {
                        applicantService.withdrawalApplication();
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.println("Enter the project name you want to enquire about: ");
                    String name = sc.nextLine();
                    Project enqProject = projectService.findProjectByName(name);
                    if (enqProject == null){
                        System.out.println("Project not found");
                        break;
                    }
                    if(!applicantService.isEligibleForProject(enqProject)) {
                        System.out.println("You are not allowed to enquire about this project.");
                        break;
                    }
                    System.out.println("Enter your enquiry: ");
                    String query = sc.nextLine();
                    applicantService.submitEnquiry(query, enqProject);
                    System.out.println("Enquiry submitted successfully.");
                    break;

                case 6: // edit enquiry
                    List<Enquiry> enquiries = applicantService.getEnquiries();
                    if (enquiries.isEmpty()) {
                        System.out.println("No enquiries found.");
                        break;
                    }
                    System.out.println("Here are your enquiries: ");
                    EnquiryView.displayEnquiries(enquiries);
                    System.out.println("Enter enquiry ID to edit: ");
                    int enquiryId = sc.nextInt();
                    sc.nextLine();
                    Enquiry enquiry = applicantService.getEnquiry(enquiryId);
                    if (enquiry != null) {
                        System.out.println("Invalid enquiry ID.");
                        break;
                    }
                    System.out.println("Enter your new enquiry: ");
                    String newEnquiry = sc.nextLine();
                    applicantService.editEnquiry(newEnquiry, enquiry);
                    System.out.println("Enquiry edited successfully.");
                    break;

                case 7: // change password
                    AuthService authService = new AuthService();
                    System.out.println("Enter your old password: ");
                    String oldPassword = sc.next();
                    if (authService.checkUser(currentUser.getNric(), oldPassword)){
                        System.out.println("Enter your new password: ");
                        String newPassword = sc.next();
                        if (newPassword != null && !newPassword.trim().isEmpty()) {
                            authService.changePassword(currentUser, newPassword);
                            System.out.println("Password changed successfully.");
                        }
                        else
                            System.out.println("Invalid password.");
                    }
                    else
                        System.out.println("Incorrect old password.");
                    break;

                case 8: // log out
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}