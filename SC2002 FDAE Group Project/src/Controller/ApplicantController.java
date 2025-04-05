package Controller;
import Entity.Applicant;
import Entity.Application;
import Entity.Enquiry;
import Entity.Project;
import Service.ApplicantService;
import Service.ProjectService;
import View.ProjectView;

import java.util.List;

public class ApplicantController extends BaseController{
    //set final to maintain this thread
    private final ApplicantService applicantService;
    private final ProjectService projectService;//Main里还要修改！

    public ApplicantController(ApplicantService applicantService, ProjectService projectService, Applicant user) {
        super(user);//in this case, for currentUser: reference - User; object - Applicant/Officer/...
        this.applicantService = applicantService;
        this.projectService = projectService;
    }

    public void start() {
        while(true) {
            System.out.println("Menu: ");
            System.out.println("1. View or Apply Projects");
            System.out.println("2. View My Project");
            System.out.println("3. Book a Flat");
            System.out.println("4. Request Withdrawal For Application");
            System.out.println("5. Submit A Enquiry");
            System.out.println("6. Edit Enquiries");
            System.out.println("7. Log out");
            System.out.println("Enter your choice: ");

            switch (sc.nextInt()) {

                case 1:
                    List<Project> projects = applicantService.getVisibleProjects((Applicant) currentUser); //handle eligible projects
                    ProjectView.displayProjectList(projects, currentUser); // whether to display 2-room or 3-room in every project
                    System.out.println("1. Apply Projects");
                    System.out.println("2. Back");
                    System.out.println("Enter your choice: ");

                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("Enter project name: ");
                            String name = sc.next();//need also enter room type!!!
                            Project project = applicantService.findProjectByName(name);//need exception handling!

                            System.out.println("Select flat type: ");
                            System.out.println("1. 2-Room");
                            if (applicantService.isSingle((Applicant) currentUser))
                                System.out.println("2. 3-Room");
                            int flatType = sc.nextInt();
                            Application application = applicantService.newApplication(currentUser, project, flatType);//need EH!

                            System.out.println("Confirm application details: ");
                            ProjectView.viewApplication(application);
                            System.out.println("1. Send Application");
                            System.out.println("2. Back");
                            System.out.println("Enter your choice: ");
                            switch (sc.nextInt()) {
                                case 1:
                                    applicantService.sendApplication((Applicant) currentUser, application);
                                    break;
                                case 2:
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                            }
                            break;
                        case 2:
                            break;
                        default:
                            System.out.println("Invalid choice");
                    }
                    break;

                case 2:
                    Project project = applicantService.getProject((Applicant) currentUser);
                    ProjectView.displayProject(project, currentUser);// down-casting!
                    break;//but also need exception handling! //need 'Back' button or not???

                case 3:
                    boolean isAvailable = applicantService.isAvailableToBookFlat((Applicant) currentUser);
                    if (isAvailable) {
                        System.out.println("You can book a flat now.");//to be completed in officerService
                    }
                    else
                        System.out.println("Application must be SUCCESSFUL to book.");
                    break;

                case 4:
                    applicantService.withdrawalApplication((Applicant) currentUser);
                    System.out.println("Application has been successfully withdrawn.");
                    break;

                case 5:
                    System.out.println("Enter the project name you want to enquire about: ");
                    String name = sc.next();
                    project = applicantService.findProjectByName(name);//need exception handling!
                    if (project == null){
                        System.out.println("Project not found");
                        break;
                    }
                    else{
                        System.out.println("Enter your enquiry: ");
                        String q = sc.nextLine();
                        applicantService.submitEnquiry(q, currentUser, project);
                        System.out.println("Enquiry submitted successfully.");
                    }
                    break;

                case 6:
                    //projectService.getEnquiry();
                    System.out.println("Here are your enquiries: ");

                    //ProjectView.displayMyEnquiry(Enquiry);
                    System.out.println("Which enquiry do you want to edit?");
                    System.out.println("Enter the enquiry id: ");
                    int enquiryId = sc.nextInt();
                    //projectService.getEnquiry(enquiryId);
                    System.out.println("Enter your new enquiry: ");
                    String newEnquiry = sc.nextLine();
                    //applicantService.editEnquiry((Applicant) currentUser, newEnquiry);
                    System.out.println("Enquiry edited successfully.");
                    break;

                case 7:
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}