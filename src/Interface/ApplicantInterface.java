package Interface;
import Entity.*;
import Service.*;
import View.ApplicationView;
import View.ProjectView;
import View.EnquiryView;

import java.util.List;
/**
 * This class provides the interface for Applicants to interact with the HDB application system.
 * It allows applicants to view and apply for projects, manage their enquiries,
 * book flats, and manage their application status.
 */
public class ApplicantInterface extends BaseInterface {
    private final IApplicantService applicantService;
    private final IProjectService projectService;

    /**
     * Constructs an ApplicantInterface with the given user context.
     * @param currentUser The currently logged-in applicant.
     */
    public ApplicantInterface(User currentUser) {
        super(currentUser);
        this.applicantService = new ApplicantService((Applicant) currentUser);
        this.projectService = new ProjectService();
        super.setProjectService(this.projectService);
    }

    /**
     * Allows the applicant to view available HDB projects and apply for one if eligible.
     * Applicant can also manage project filters from this menu.
     */
    public void viewOrApplyProjects() {
        List<Project> projects = applicantService.getVisibleProjects();

        //handle eligible projects
        System.out.println(filterSettings.getFilterSummary());
        projects = applyProjectFilters(projects);
        if (projects.isEmpty()) {
            System.out.println("Project not found.");
        }

        ProjectView.displayProjectList(projects, currentUser);
        System.out.println("1 - Apply for a Project");
        System.out.println("2 - Manage Filters");
        System.out.println("3 - Back");
        choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1 -> {
                if (applicantService.haveProject()) {
                    System.out.println("You have already applied for a project.");
                    break;
                }
                System.out.println("Enter project name: ");
                String name = sc.nextLine();
                Project project = projectService.findProjectByName(name);
                if (project == null) {
                    System.out.println("Project not found.");
                    break;
                }

                try {
                    System.out.println("Select flat type: ");
                    System.out.println("1 - 2-Room");
                    if (!applicantService.isSingle())
                        System.out.println("2 - 3-Room");
                    int flatType = readIntInput("Enter your choice: ");

                    Application application = applicantService.newApplication(project, flatType);
                    if (application == null) {
                        throw (new IllegalArgumentException("Invalid Application."));
                    }
                    System.out.println("Confirm application details: ");
                    ApplicationView.displayApplication(application);
                    System.out.println("1 - Send Application");
                    System.out.println("2 - Back");
                    choice = readIntInput("Enter your choice: ");

                    if (choice == 1) {
                        applicantService.sendApplication(application);
                        System.out.println("Application submitted successfully.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            case 2 -> manageFilters();
            case 3 -> {}
            default -> System.out.println("Invalid choice");
        }
    }

    /**
     * Displays the current project that the applicant has applied to, if any.
     */
    public void viewMyProjects() {
        if (applicantService.haveProject()) {
            System.out.println("Here is your project: ");
            Project project = applicantService.getProject();
            ProjectView.displayProject(project, currentUser);// down-casting!
        }
        else
            System.out.println("You have not applied for a project.");
    }

    /**
     * Allows the applicant to book a flat if their application is approved.
     */
    public void bookAFlat() {
        try {
            if (applicantService.isAvailableToBookFlat()) {
                System.out.println("You can book a flat now.");
            } else
                System.out.println("Application must be SUCCESSFUL to book.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to withdraw their application from a project.
     */
    public void withdrawApplication() {
        try {
            applicantService.withdrawalApplication();
            System.out.println("Submitted withdrawal successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to submit an enquiry about a visible and eligible project.
     */
    public void submitAnEnquiry() {
        try {
            List <Project> projects = applicantService.getVisibleProjects();
            if (projects.isEmpty())
                throw (new IllegalArgumentException("Project not found."));

            ProjectView.displayProjectList(projects, currentUser);
            System.out.println("Enter the project name you want to enquire about: ");
            String name = sc.nextLine();
            Project enqProject = projectService.findProjectByName(name);
            if (enqProject == null)
                throw (new IllegalArgumentException("Project not found."));
            if (!applicantService.isEligibleForProject(enqProject))
                throw (new IllegalArgumentException("You are not allowed to enquire about this project."));

            System.out.println("Enter your enquiry: ");
            String query = sc.nextLine();
            applicantService.submitEnquiry(query, enqProject);
            System.out.println("Enquiry submitted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the applicant to edit or delete previously submitted enquiries, provided
     * they have not yet been responded to by officers.
     */
    public void editOrDeleteEnquiry() {
        try {
            List<Enquiry> enquiries = applicantService.getEnquiries();
            if (enquiries.isEmpty()) {
                throw (new IllegalArgumentException("No enquiries found."));
            }
            System.out.println("Here are your enquiries: ");
            EnquiryView.displayEnquiries(enquiries);
            System.out.println("Enter enquiry ID to edit or delete: ");
            int enquiryId = sc.nextInt();
            sc.nextLine();

            Enquiry enquiry = applicantService.getEnquiry(enquiryId);
            if (enquiry == null) {
                throw (new IllegalArgumentException("Invalid enquiry ID."));
            }
            if (enquiry.getResponse() != null) {
                throw new IllegalArgumentException("This enquiry has already been replied.");
            }
            System.out.println("1 - Edit\n2 - Delete\n3 - Back");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter new enquiry message: ");
                    String newQuery = sc.nextLine();
                    applicantService.editEnquiry(newQuery, enquiry);
                    System.out.println("Enquiry edited successfully.");
                }
                case 2 -> {
                    applicantService.deleteEnquiry(enquiry);
                    System.out.println("Enquiry deleted successfully.");
                }
                case 3 -> {}
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Starts the main interface loop for the applicant, presenting all available options.
     * This method will keep running until the applicant chooses to log out.
     */
    public void start() {
        while(true) {
            System.out.println("Welcome, " + currentUser.getName() + "!");
            System.out.println("Applicant Menu: ");
            System.out.println("1 - View or Apply Projects");
            System.out.println("2 - View My Project");
            System.out.println("3 - Book a Flat");
            System.out.println("4 - Request Withdrawal for Application");
            System.out.println("5 - Submit an Enquiry");
            System.out.println("6 - Edit or Delete Enquiries");
            System.out.println("7 - Change password");
            System.out.println("8 - Log Out");

            choice = readIntInput("Enter your choice: ");

            switch (choice) {

                case 1 -> viewOrApplyProjects();
                case 2 -> viewMyProjects();
                case 3 -> bookAFlat();
                case 4 -> withdrawApplication();
                case 5 -> submitAnEnquiry();
                case 6 -> editOrDeleteEnquiry();
                case 7 -> { if (changePassword()) { return;} }
                case 8 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
