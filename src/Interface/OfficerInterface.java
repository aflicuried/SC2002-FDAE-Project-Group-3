package Interface;
import Entity.*;
import Service.*;
import View.ApplicationView;
import View.EnquiryView;
import View.ProjectView;
import View.RegistrationView;

import java.util.List;


/**
 * OfficerInterface provides the command-line interface for users who are HDB officers.
 * Officers can apply for projects, manage enquiries, book flats, handle applications,
 * and view or register for projects. It supports both applicant and officer roles.
 */
public class OfficerInterface extends BaseInterface {

    private final IOfficerService officerService;
    private final IProjectService projectService;


    /**
     * Constructs an OfficerInterface with the current logged-in officer.
     *
     * @param currentUser the currently authenticated user, expected to be an instance of HDBOfficer
     */
    public OfficerInterface(User currentUser) {
        super(currentUser);//in this case, for currentUser: reference - User; object - Applicant/Officer/...
        this.officerService = new OfficerService((HDBOfficer) currentUser);
        this.projectService = new ProjectService();
        super.setProjectService(this.projectService);
    }

    //for case 1
    /**
     * Displays the list of available visible projects, allows the officer to filter them,
     * apply for one, or manage project filters. Ensures an officer can only apply once.
     */
    public void viewOrApplyProjects() {
        List<Project> projects = officerService.getVisibleProjects();

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
                if (officerService.haveProject()) {
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
                    if (!officerService.isSingle())
                        System.out.println("2 - 3-Room");
                    int flatType = readIntInput("Enter your choice: ");
                    Application application = officerService.newApplication(project, flatType);

                    System.out.println("Confirm application details: ");
                    ApplicationView.displayApplication(application);
                    System.out.println("1 - Send Application");
                    System.out.println("2 - Back");
                    choice = readIntInput("Enter your choice: ");

                    if (choice == 1) {
                        officerService.sendApplication(application);
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

    //for case 2
    /**
     * Displays the project that the officer has applied for, if any.
     */
    public void viewMyProject() {
        if (officerService.haveProject()) {
            System.out.println("Here is your project: ");
            Project project = officerService.getProject();
            ProjectView.displayProject(project, currentUser);
        } else
            System.out.println("You have not applied for a project.");
    }

    
    //for case 3
    /**
     * Allows the officer to book a flat if their application has been approved.
     */
    public void bookAFlat() {
        try {
            if (officerService.isAvailableToBookFlat()) {
                System.out.println("You can book a flat now.");
            } else
                System.out.println("Application must be SUCCESSFUL to book.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    
    //for case 4
    /**
     * Allows the officer to request withdrawal of their submitted application.
     */
    public void withdrawApplication() {
        try {
            officerService.withdrawalApplication();
            System.out.println("Submitted withdrawal successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Allows the officer to submit an enquiry for a specific project, if eligible.
     * Validates that the project exists and is visible to the officer.
     */
    public void submitAnEnquiry() {
        try {
            List<Project> projects = officerService.getVisibleProjects();
            if (projects.isEmpty())
                throw new IllegalArgumentException("Project not found.");

            ProjectView.displayProjectList(projects, currentUser);
            System.out.println("Enter the project name you want to enquire about: ");
            String enqName = sc.nextLine();
            Project enqProject = projectService.findProjectByName(enqName);
            if (enqProject == null)
                throw new IllegalArgumentException("Project not found.");
            if (!officerService.isEligibleForProject(enqProject))
                throw new IllegalArgumentException("You are not allowed to enquire about this project.");

            System.out.println("Enter your enquiry: ");
            String query = sc.nextLine();
            officerService.submitEnquiry(query, enqProject);
            System.out.println("Enquiry submitted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Displays the officer's past enquiries and allows editing or deletion if the enquiry 
     * has not been responded to yet.
     */
    public void editOrDeleteEnquiry() {
        try {
            List<Enquiry> enquiries = officerService.getEnquiries();
            if (enquiries.isEmpty()) {
                throw new IllegalArgumentException("No enquiries found.");
            }
            System.out.println("Here are your enquiries: ");
            EnquiryView.displayEnquiries(enquiries);
            System.out.println("Enter enquiry ID to edit: ");
            int enquiryId = sc.nextInt();
            sc.nextLine();

            Enquiry enquiry = officerService.getEnquiry(enquiryId);
            if (enquiry == null) {
                throw new IllegalArgumentException("Invalid enquiry ID.");
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
                    officerService.editEnquiry(newQuery, enquiry);
                    System.out.println("Enquiry edited successfully.");
                }
                case 2 -> {
                    officerService.deleteEnquiry(enquiry);
                    System.out.println("Enquiry deleted successfully.");
                }
                case 3 -> {}
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Allows the officer to register for a project they are eligible to manage.
     * Validates project eligibility before submitting the registration.
     */
    public void registerForAProject() {
        try {
            List<Project> projects2 = officerService.getProjectsForRegi();
            if (projects2.isEmpty()) {
                throw new IllegalArgumentException("No eligible projects found for registration");
            }
            System.out.println("Here are the projects you can register: ");
            ProjectView.displayManagedList(projects2);

            System.out.println("Enter the project name to register for: ");
            String regProjectName = sc.nextLine();

            // get the project
            Project selectedProject = projectService.findProjectByName(regProjectName);
            if (selectedProject == null) {
                throw new IllegalArgumentException("Project not found.");
            }

            // check if officer can register
            if (!projects2.contains(selectedProject)) {
                throw new IllegalArgumentException("You are not eligible to register for this project.");
            }

            // register
            officerService.submitRegistration(regProjectName);
            System.out.println("Registration submitted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Displays the officer's submitted project registrations, if any.
     */
    public void viewMyRegistration() {
        try {
            List<Registration> registrations = officerService.getRegistrations();
            if (registrations.isEmpty())
                throw (new IllegalArgumentException("Registrations not found."));
            System.out.println("Your registrations: ");
            RegistrationView.displayRegistrations(registrations);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Displays the project that the officer is currently managing.
     */
    public void viewMyManagingProject() {
        try {
            Project managedProject = officerService.getProjectHandling();
            if (managedProject == null)
                throw new IllegalArgumentException("You are not managing any project.");
            System.out.println("Your managed project: ");
            ProjectView.displayManagedProject(managedProject);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Allows the officer to update an approved applicant's application status to "BOOKED"
     * for the project they are managing.
     */
    public void setApplicationsToBooked() {
        try {
            Project managedProject = officerService.getProjectHandling();
            if (managedProject == null) {
                throw new IllegalArgumentException("You are not managing any project.");
            }
            System.out.println("Enter applicant NRIC: ");
            String nric = sc.next();
            sc.nextLine();

            officerService.bookApplication(nric, managedProject);
            System.out.println("Application set to BOOKED successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Displays enquiries directed at the officer's managed project and allows the officer
     * to respond to them.
     */
    public void replyToEnquiries() {
        try {
            List<Enquiry> projectEnquiries = officerService.getEnquiriesForReply();
            if (projectEnquiries.isEmpty())
                throw new IllegalArgumentException("No enquiries found.");

            System.out.println("Enquiries for your project: ");
            EnquiryView.displayEnquiries(projectEnquiries);
            System.out.println("1 - Reply\n2 - Back");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> {
                    int replyId = readIntInput("Enter enquiry ID to reply: ");
                    System.out.println("Enter your reply:");
                    String reply = sc.nextLine();
                    officerService.replyEnquiry(replyId, reply);
                    System.out.println("Reply successfully.");
                }
                case 2 -> {}
                default -> System.out.println("Invalid choice");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Entry point for the OfficerInterface menu. Displays role-based options (applicant/officer)
     * and routes user actions accordingly.
     */
    public void start() {
        while(true) {
            System.out.println("\nWelcome, " + currentUser.getName() + "!");
            System.out.println("Officer Menu: ");
            System.out.println("Act as an applicant:");
            System.out.println("1 - View or Apply Projects");
            System.out.println("2 - View My Applied Project");
            System.out.println("3 - Book a Flat (Applicant)");
            System.out.println("4 - Request Withdrawal for Application");
            System.out.println("5 - Submit an Enquiry");
            System.out.println("6 - Edit or Delete Enquiries");

            System.out.println("Act as an officer:");
            System.out.println("7 - Register for a Project");
            System.out.println("8 - View My Registration");
            System.out.println("9 - View My Managing Project");
            System.out.println("10 - Manage Applications (Set to BOOKED)");
            System.out.println("11 - Reply to Enquiries");
            System.out.println("12 - Change Password");
            System.out.println("13 - Log Out");
            choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> viewOrApplyProjects();
                case 2 -> viewMyProject();
                case 3 -> bookAFlat();
                case 4 -> withdrawApplication();
                case 5 -> submitAnEnquiry();
                case 6 -> editOrDeleteEnquiry();

                case 7 -> registerForAProject();
                case 8 -> viewMyRegistration();
                case 9 -> viewMyManagingProject();
                case 10 -> setApplicationsToBooked();
                case 11 -> replyToEnquiries();
                case 12 -> { if (changePassword()) { return; } }
                case 13 -> { return; }
                default -> System.out.println("Invalid choice");
            }
        }
    }
}
