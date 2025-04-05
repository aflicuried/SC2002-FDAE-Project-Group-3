package Controller;

import Entity.*;
import Service.ManagerService;
import Service.ProjectService;
import View.ProjectView;

public class ManagerController extends BaseController{
    private final ManagerService managerService;
    private final ProjectService projectService;

    public ManagerController(ManagerService managerService, ProjectService projectService, HDBManager user) {
        super(user);//in this case, for currentUser: reference - User; object - Applicant/Officer/...
        this.managerService = managerService;
        this.projectService = projectService;
    }

    public void start() {
        while(true) {
            System.out.println("Menu: ");
            System.out.println("1. Manage Projects");//1 create: enter info and create; 2 edit or delete: enter name and manage 3 toggle visibility; 4 filter projects they created
            System.out.println("2. Manage Registrations");//1 approve (update officer slots) or reject (only in-charge);
            System.out.println("3. Manage Applications");//1 approve or reject applicant's application (approval is limited to the supply of flats) and 2 withdrawal
            System.out.println("4. Generate reports"); //1 applicants - flatType, project name, age, marital status
            System.out.println("5. Manage Enquiries"); //1 reply to enquiries he/she is handling
            System.out.println("6. Log out");
            System.out.println("Enter your choice: ");

            switch (sc.nextInt()) {
                case 1:
                    ProjectView.displayProjectList(projectService.getProjects(new ProjectService.ProjectQuery()), currentUser);// can delete 'currentUser' in the future
                    System.out.println("1 - Create A New Project");
                    System.out.println("2 - Edit or Delete A Project");
                    System.out.println("3 - Toggle visibility of a Project");
                    System.out.println("4 - Filter projects you created");
                    System.out.println("5 - Back");
                    System.out.println("Enter your choice: ");

                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("Enter information.");
                            System.out.println("Project Name: ");
                            String projectName = sc.next();
                            System.out.println("Neighbourhood: ");
                            String neighbourhood = sc.next();
                            System.out.println("# of Units of 2-Room Flat Type:");
                            int UnitsOf2Room = sc.nextInt();
                            System.out.println("2-room Flat Price:");
                            int PriceOf2Room = sc.nextInt();
                            System.out.println("# of Units of 3-Room Flat Type:");
                            int UnitsOf3Room = sc.nextInt();
                            System.out.println("3-room Flat Price:");
                            int PriceOf3Room = sc.nextInt();
                            System.out.println("Application Opening Date:");
                            String openingDate = sc.next();
                            System.out.println("Application Closing Date:");
                            String closingDate = sc.next();
                            System.out.println("Available HDB Officer Slots:"); //need 'confirm details' and EH.
                            int officerSlots = sc.nextInt();

                            Project project = managerService.createProject(projectName, neighbourhood, UnitsOf2Room, PriceOf2Room,
                                    UnitsOf3Room, PriceOf3Room, openingDate, closingDate, officerSlots, currentUser);
                            projectService.addProject(project);
                            System.out.println("Project created successfully.");
                            break;

                        case 2:
                            System.out.println("Enter project name: ");
                            String name2 = sc.next();
                            Project project2 = projectService.getProjects(
                                    new ProjectService.ProjectQuery().withName(name2)).stream().findFirst().orElse(null);
                            if (project2 != null) {
                                System.out.println("Edit or Delete:\n1 - Edit\n2 - Delete");
                                switch (sc.nextInt()) {
                                    case 1:
                                        System.out.println("Enter the attribute you want to edit: ");
                                        String attribute = sc.next();
                                        //if (attribute.equals() || ...)
                                        System.out.println("Enter the new information: ");
                                        String newInformation = sc.next();
                                        //managerService.update();
                                        System.out.println("Project updated successfully. Here are the details:");
                                        //ProjectView.displayProject(projectService.getProjects(new ProjectService.ProjectQuery()), currentUser);
                                    case 2:
                                        //projectService.deleteProject(); 对于projects本身的增删改查筛选都在projectService里？
                                        System.out.println("Project deleted successfully. Here are new project lists:");
                                        //ProjectView.displayProjects(projectService.getProjects(new ProjectService.ProjectQuery()), currentUser);
                                        break;
                                }
                            }
                            else
                                System.out.println("Project not found.");
                            break;

                        case 3:
                            System.out.println("Enter the project name you want to toggle: ");
                            String name3 = sc.next();
                            Project project3 = projectService.getProjects(
                                    new ProjectService.ProjectQuery().withName(name3)).stream().findFirst().orElse(null);
                            if (project3 != null) {
                                managerService.shiftVisibility(project3);
                                System.out.println("Project shifted successfully.");
                            }
                            else
                                System.out.println("Project not found.");

                        case 4:
                            System.out.println("Here are the projects you created:");
                            //ProjectView.displayProjectList(projectService.getProjects(new ProjectService.ProjectQuery()), currentUser);
                            break;

                        case 5:
                            break;
                    }

                case 2:
                    System.out.println("Below are registrations.");
                    //ManagerService.getRegistrations();
                    //ProjectView.displayRegistrations();
                    System.out.println("Enter the registration name you want to approve or reject: ");
                    String registrationName = sc.next();
                    System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
                    switch (sc.nextInt()) {
                        case 1:
                            //managerService.setApprove();
                            System.out.println("Below are updated registrations.");
                            //ProjectView.displayRegistrations();
                            break;
                        case 2:
                            //managerService.setReject();
                            System.out.println("Below are updated registrations.");
                            //ProjectView.displayRegistrations();
                            break;

                    }
                    break;

                case 3:
                    System.out.println("1 - approve or reject application\n2 - approve or reject withdrawal");
                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("Below are applications.");
                            //ProjectView.displayProjectList(projectService.getProjects(new ProjectService.ProjectQuery()), currentUser);
                            System.out.println("Enter the user name you want to approve or reject: ");
                            String userName = sc.next();
                            //Application application = ProjectService(...)
                            System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
                            switch (sc.nextInt()) {
                                case 1:
                                    //manageService.approveApplication();
                                    System.out.println("Approve successfully.");//need details?
                                    break;
                                case 2:
                                    //managerService.rejectApplication();
                                    System.out.println("Reject successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }

                        case 2:
                            System.out.println("Below are attempted withdrawals.");
                            //ProjectView.displayProject();
                            System.out.println("Enter the user name you want to approve or reject: ");
                            String userName2 = sc.next();
                            //Application application = ProjectService(...)
                            System.out.println("Approve or Reject:\n1 - Approve\n2 - Reject");
                            switch (sc.nextInt()) {
                                case 1:
                                    //manageService.approveWithdrawal();
                                    System.out.println("Approve withdrawal successfully.");//need details?
                                    break;
                                case 2:
                                    //managerService.rejectWithdrawal();
                                    System.out.println("Reject withdrawal successfully.");
                                    break;
                                default:
                                    System.out.println("Invalid choice.");
                            }
                            break;
                    }

                case 4:

                    //ProjectView.generateReport(); attribute!!!
                    break;

                case 5:
                    //ProjectView.displayEnquiries(); //can only display in-charge project enquiries or all enquiries?
                    System.out.println("1 - Reply\n2 - Back\nEnter your choice: ");
                    switch (sc.nextInt()) {
                        case 1:
                            System.out.println("Enter the Enquiry id you want to reply: ");
                            String enquiryId = sc.next();
                            System.out.println("Enter your reply.");
                            String reply = sc.next();
                            //managerService.replyEnquiry(enquiryId, reply, currentUser);
                            break;
                        case 2:
                            break;
                    }
                    break;

                case 6:
                    return;
            }
        }
    }
}
