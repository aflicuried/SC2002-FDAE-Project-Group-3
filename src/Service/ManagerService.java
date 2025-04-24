package Service;

import Database.*;
import Entity.*;
import util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Service class for handling all manager-related operations such as creating and editing projects,
 * managing officer registrations, applications, withdrawals, and responding to enquiries.
 */
public class ManagerService implements IManagerService {
    // database instances
    private UserDatabase userDatabase = UserDatabase.getInstance();
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private RegistrationDatabase registrationDatabase = RegistrationDatabase.getInstance();

    private HDBManager manager;

    /**
     * Constructs a new ManagerService for the specified manager.
     *
     * @param manager the HDB manager associated with this service
     */
    public ManagerService(HDBManager manager) {
        this.manager = manager;
    }

    /**
     * Creates a new HDB project with the specified details.
     *
     * @return the created Project object
     */
    public Project createProject(String projectName, String neighbourhood, int room2, int price2,
                                 int room3, int price3, LocalDate openingDate, LocalDate closingDate, int slots, User user) {
        FlatType flatType1 = new FlatType("2-Room", room2, price2);
        FlatType flatType2 = new FlatType("3-Room", room3, price3);
        List<FlatType> flatTypes = new ArrayList<>(Arrays.asList(flatType1, flatType2));
        return new Project(projectName, neighbourhood, flatTypes, openingDate, closingDate, (HDBManager) user, slots, new ArrayList<>());
    }

    /**
     * Checks whether the specified project dates conflict with any of the manager's existing projects.
     *
     * @param openingDate the proposed opening date
     * @param closingDate the proposed closing date
     * @return true if there's a conflict; false otherwise
     */
    public boolean hasProjectConflict(LocalDate openingDate, LocalDate closingDate) {
        List<Project> managedProjects = projectDatabase.findByManager(manager.getName());
        return managedProjects.stream().anyMatch(p -> DateUtil.hasDateOverlap(openingDate, closingDate, p.getOpeningDate(), p.getClosingDate()));
    }

    /**
     * Checks if the manager is authorized to manage the specified project.
     *
     * @param project the project to check
     * @return true if authorized; false otherwise
     */
    public boolean checkAuthForProject(Project project) {
        return project.getManager().getName().equals(manager.getName());
    }

    /**
     * Edits a project based on user input and a specified field to modify.
     *
     * @param choice  the field number to modify
     * @param project the project to edit
     */
    public void editProject(int choice, Project project) {
        Scanner sc = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        switch (choice) {
            case 1 -> {
                System.out.println("Enter new name: ");
                String name = sc.nextLine();
                project.setName(name);
            }
            case 2 -> {
                System.out.println("Enter new neighbourhood: ");
                String neighbourhood = sc.nextLine();
                project.setNeighbourhood(neighbourhood);
            }
            case 3 -> {
                System.out.println("Enter new available units: ");
                int availableUnits = sc.nextInt();
                sc.nextLine();
                project.set2RoomUnits(availableUnits);
            }
            case 4 -> {
                System.out.println("Enter new price: ");
                int price = sc.nextInt();
                sc.nextLine();
                project.set2RoomPrice(price);
            }
            case 5 -> {
                System.out.println("Enter new available units: ");
                int availableUnits = sc.nextInt();
                sc.nextLine();
                project.set3RoomUnits(availableUnits);
            }
            case 6 -> {
                System.out.println("Enter new price: ");
                int price = sc.nextInt();
                sc.nextLine();
                project.set3RoomPrice(price);
            }
            case 7 -> {
                System.out.println("Enter new opening date (format: yyyy/M/d, e.g. 2000/1/30): ");
                String opening = sc.nextLine();
                LocalDate openingDate = LocalDate.parse(opening, formatter);
                project.setOpeningDate(openingDate);
            }
            case 8 -> {
                System.out.println("Enter new closing date (format: yyyy/M/d, e.g. 2000/1/30): ");
                String closing = sc.nextLine();
                LocalDate closingDate = LocalDate.parse(closing, formatter);
                project.setClosingDate(closingDate);
            }
            case 9 -> {
                System.out.println("Enter new slots: ");
                int slots = sc.nextInt();
                sc.nextLine();
                project.setOfficerSlots(slots);
            }
            default -> System.out.println("Invalid choice");
        }
    }

    /**
     * Toggles the visibility of a project.
     *
     * @param project the project whose visibility to change
     */
    public void shiftVisibility(Project project) {
        project.setVisibility(!project.isVisible());
    }

    /**
     * Finds a registration by its ID.
     *
     * @param id the registration ID
     * @return the matching Registration or null
     */
    public Registration findById(int id) {
        return registrationDatabase.findById(id);
    }

    /**
     * Retrieves all pending or approved officer registrations for the manager's projects.
     *
     * @return list of relevant Registrations
     */
    public List<Registration> getRegistrations() {
        return registrationDatabase.findAll().stream()
                .filter(r -> r.getStatus() == Registration.Status.PENDING || r.getStatus() == Registration.Status.APPROVED)
                .filter(r -> r.getProject().getManager().getNric().equals(manager.getNric()))
                .toList();
    }

    /**
     * Approves a pending officer registration.
     *
     * @param registration the registration to approve
     */
    public void approveRegistration(Registration registration) {
        // Logic to check validity and update registration/project/officer data
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found.");
        }
        if (!registration.getProject().getManager().getNric().equals(manager.getNric())) {
            throw new IllegalArgumentException("Not authorized to manage this registration.");
        }
        if (registration.getStatus() != Registration.Status.PENDING) {
            throw new IllegalArgumentException("Only PENDING registrations can be approved.");
        }
        Project project = registration.getProject();
        if (project.getOfficerSlots() <= 0) {
            throw new IllegalArgumentException("No officer slots available.");
        }
        registration.setStatus(Registration.Status.APPROVED);
        HDBOfficer officer = registration.getOfficer();
        officer.setProjectHandling(project);
        project.getOfficers().add(officer);
        project.setOfficerSlots(project.getOfficerSlots() - 1);
    }

    /**
     * Rejects an officer registration.
     *
     * @param registration the registration to reject
     */
    public void rejectRegistration(Registration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found.");
        }
        if (!registration.getProject().getManager().getNric().equals(manager.getNric())) {
            throw new IllegalArgumentException("Not authorized to manage this registration.");
        }
        registration.setStatus(Registration.Status.REJECTED);
    }

    /**
     * Retrieves all applications for projects managed by this manager.
     *
     * @return list of applications
     */
    public List<Application> getApplications() {
        return applicationDatabase.findApplications().stream()
                .filter(a -> a.getProject().getManager().getName().equals(manager.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Finds an application by the applicant's NRIC.
     *
     * @param nric the NRIC to search
     * @return the matching application or null
     */
    public Application findApplicationByNric(String nric) {
        return applicationDatabase.findByApplicantNric(nric);
    }

    /**
     * Approves an application by setting its status to SUCCESSFUL.
     *
     * @param application the application to approve
     */
    public void approveApplication(Application application) {
        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
    }

    /**
     * Rejects an application by setting its status to UNSUCCESSFUL.
     *
     * @param application the application to reject
     */
    public void rejectApplication(Application application) {
        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Retrieves all withdrawal requests.
     *
     * @return list of withdrawn applications
     */
    public List<Application> getWithdrawals() {
        return applicationDatabase.findByWithdrawal(true);
    }

    /**
     * Approves a withdrawal request by marking the application as unsuccessful.
     *
     * @param application the application to update
     */
    public void approveWithdrawal(Application application) {
        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }

    /**
     * Rejects a withdrawal request and cancels the withdrawal flag.
     *
     * @param application the application to update
     */
    public void rejectWithdrawal(Application application) {
        application.setWithdrawal(false);
    }

    /**
     * Retrieves a list of successful or booked applicants based on a filter.
     *
     * @param filterType  the type of filter (e.g., "flatType", "projectName", "age", "maritalStatus")
     * @param filterValue the value for the filter
     * @return filtered list of applicants
     */
    public List<Applicant> getApplicantsForReport(String filterType, String filterValue) {
        List<Applicant> applicants = userDatabase.findApplicants().stream()
                .filter(a -> a.getApplication() != null)
                .filter(a -> a.getApplication().getStatus().equals(Application.ApplicationStatus.SUCCESSFUL)
                        || a.getApplication().getStatus().equals(Application.ApplicationStatus.BOOKED))
                .collect(Collectors.toList());

        if (filterType == null) {
            return applicants;
        }
        // Filtering logic already well-explained
        return switch (filterType) {
            case "flatType" -> applicants.stream().filter(a -> a.getApplication().getFlatType().toString().equals(filterValue)).collect(Collectors.toList());
            case "projectName" -> applicants.stream().filter(a -> a.getApplication().getProject().getName().equals(filterValue)).collect(Collectors.toList());
            case "age" -> {
                int age = Integer.parseInt(filterValue);
                yield applicants.stream().filter(a -> a.getAge() == age).collect(Collectors.toList());
            }
            case "maritalStatus" -> applicants.stream().filter(a -> a.getMaritalStatus().equals(filterValue)).collect(Collectors.toList());
            default -> applicants;
        };
    }

    /**
     * Retrieves all enquiries in the system.
     *
     * @return list of enquiries
     */
    public List<Enquiry> getEnquiries() {
        return enquiryDatabase.findAll();
    }

    /**
     * Checks if the manager is authorized to reply to the given enquiry.
     *
     * @param id the enquiry ID
     * @return true if the manager can reply; false otherwise
     */
    public boolean checkAuthForEnquiry(int id) {
        return enquiryDatabase.findById(id).getProject().getManager().getName().equals(manager.getName());
    }

    /**
     * Replies to a specific enquiry by setting the response.
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
}
