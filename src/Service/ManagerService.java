package Service;

import Database.*;
import Entity.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManagerService implements IManagerService {
    //database
    private UserDatabase userDatabase = UserDatabase.getInstance();
    private ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
    private EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();
    private ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
    private RegistrationDatabase registrationDatabase = RegistrationDatabase.getInstance();
    private HDBManager manager;

    //constructor
    public ManagerService(HDBManager manager) {
        this.manager = manager;
    }

    //methods
    public Project createProject(String projectName, String neighbourhood, int room2, int price2,
                              int room3, int price3, String opening, String closing, int slots, User user) {
        FlatType flatType1 = new FlatType("2-Room", room2, price2);
        FlatType flatType2 = new FlatType("3-Room", room3, price3);
        List<FlatType> flatTypes = new ArrayList<FlatType>(Arrays.asList(flatType1, flatType2)); //can?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        LocalDate openingDate = LocalDate.parse(opening, formatter);
        LocalDate closingDate = LocalDate.parse(closing, formatter);

        Project project = new Project(projectName, neighbourhood, flatTypes, openingDate, closingDate, (HDBManager) user, slots, new ArrayList<>());
        return project;
    }

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
                project.set2RoomUnits(availableUnits);
            }
            case 4 -> {
                System.out.println("Enter new price: ");
                int price = sc.nextInt();
                project.set2RoomPrice(price);
            }
            case 5 -> {
                System.out.println("Enter new available units: ");
                int availableUnits = sc.nextInt();
                project.set3RoomUnits(availableUnits);
            }
            case 6 -> {
                System.out.println("Enter new price: ");
                int price = sc.nextInt();
                project.set3RoomPrice(price);
            }
            case 7 -> {
                System.out.println("Enter new opening date (format: yyyy/M/d): ");
                String opening = sc.nextLine();
                LocalDate openingDate = LocalDate.parse(opening, formatter);
                project.setOpeningDate(openingDate);
            }
            case 8 -> {
                System.out.println("Enter new closing date (format: yyyy/M/d): ");
                String closing = sc.nextLine();
                LocalDate closingDate = LocalDate.parse(closing, formatter);
                project.setClosingDate(closingDate);
            }
            case 9 -> {
                System.out.println("Enter new slots: ");
                int slots = sc.nextInt();
                project.setOfficerSlots(slots);
            }
            default -> System.out.println("Invalid choice");
        }
    }

    public void shiftVisibility(Project project) {
        project.setVisibility(!project.isVisible());
    }

    //
    public Registration findById(int id) {
        return registrationDatabase.findById(id);
    }
    public List<Registration> getRegistrations() {
        return registrationDatabase.findAll().stream()
                .filter(r -> r.getStatus() == Registration.Status.PENDING || r.getStatus() == Registration.Status.APPROVED)
                .filter(r -> r.getProject().getManager().getNric().equals(manager.getNric()))
                .toList();
    }
    public void approveRegistration(int registrationId) {
        Registration registration = registrationDatabase.findById(registrationId);
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
    public void rejectRegistration(int registrationId) {
        Registration registration = registrationDatabase.findById(registrationId);
        if (registration == null) {
            throw new IllegalArgumentException("Registration not found.");
        }
        if (!registration.getProject().getManager().getNric().equals(manager.getNric())) {
            throw new IllegalArgumentException("Not authorized to manage this registration.");
        }
        registration.setStatus(Registration.Status.REJECTED);
    }

    //
    public List<Application> getApplications() {
        return applicationDatabase.findApplications();
    }
    public Application findApplicationByNric(String nric) {
        return applicationDatabase.findByApplicantNric(nric);
    }
    public void approveApplication(Application application) {
        application.setStatus(Application.ApplicationStatus.SUCCESSFUL);
    }
    public void rejectApplication(Application application) {
        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }
    public List<Application> getWithdrawals() {
        return applicationDatabase.findByWithdrawal(true);
    }
    public void approveWithdrawal(Application application) {
        application.setStatus(Application.ApplicationStatus.UNSUCCESSFUL);
    }
    public void rejectWithdrawal(Application application) {
        application.setWithdrawal(false);
    }

    //
    public List<Enquiry> getEnquiries() {
        return enquiryDatabase.findAll();
    }
    public boolean checkAuthority(int id) {
        return enquiryDatabase.findById(id).getProject()
                .getManager().getName().equals(manager.getName());
    }
    public void replyEnquiry(int id, String reply) {
        Enquiry enquiry = enquiryDatabase.findById(id);
        enquiry.setResponse(reply);
    }
}
