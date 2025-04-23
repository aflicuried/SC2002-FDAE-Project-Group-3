package Service;

import Database.*;
import Entity.*;
import util.DateUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
                              int room3, int price3, LocalDate openingDate, LocalDate closingDate, int slots, User user) {
        FlatType flatType1 = new FlatType("2-Room", room2, price2);
        FlatType flatType2 = new FlatType("3-Room", room3, price3);
        List<FlatType> flatTypes = new ArrayList<FlatType>(Arrays.asList(flatType1, flatType2)); //can?
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");

        Project project = new Project(projectName, neighbourhood, flatTypes, openingDate, closingDate, (HDBManager) user, slots, new ArrayList<>());
        return project;
    }
    public boolean hasProjectConflict(LocalDate openingDate, LocalDate closingDate) {
        List<Project> managedProjects = projectDatabase.findByManager(manager.getName());

        return managedProjects.stream()
                .anyMatch(p -> DateUtil.hasDateOverlap(
                        openingDate, closingDate,
                        p.getOpeningDate(), p.getClosingDate()));
    }

    public boolean checkAuthForProject(Project project) {
        if(project.getManager().getName().equals(manager.getName()))
            return true;
        return false;
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
    public void approveRegistration(Registration registration) {
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
    public void rejectRegistration(Registration registration) {
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
        return applicationDatabase.findApplications()
                .stream().filter(a -> a.getProject().getManager().getName()
                        .equals(manager.getName()))
                .collect(Collectors.toList());
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
    public List<Applicant> getApplicantsForReport(String filterType, String filterValue) {
        List<Applicant> applicants = userDatabase.findApplicants()
                .stream()
                .filter(a -> a.getApplication() != null)
                .filter(a -> a.getApplication().getStatus().equals(Application.ApplicationStatus.SUCCESSFUL) ||
                        a.getApplication().getStatus().equals(Application.ApplicationStatus.BOOKED))
                .collect(Collectors.toList());

        if (filterType != null && filterValue != null) {
            switch (filterType) {
                case "flatType":
                    return applicants.stream()
                            .filter(a -> a.getApplication().getFlatType().toString().equals(filterValue))
                            .collect(Collectors.toList());
                case "projectName":
                    return applicants.stream()
                            .filter(a -> a.getApplication().getProject().getName().equals(filterValue))
                            .collect(Collectors.toList());
                case "age":
                    int age = Integer.parseInt(filterValue);
                    return applicants.stream()
                            .filter(a -> a.getAge() == age)
                            .collect(Collectors.toList());
                case "maritalStatus":
                    return applicants.stream()
                            .filter(a -> a.getMaritalStatus().equals(filterValue))
                            .collect(Collectors.toList());
                default:
                    return applicants;
            }
        }

        return applicants;
    }

    //
    public List<Enquiry> getEnquiries() {
        return enquiryDatabase.findAll();
    }
    public boolean checkAuthForEnquiry(int id) {
        return enquiryDatabase.findById(id).getProject()
                .getManager().getName().equals(manager.getName());
    }
    public void replyEnquiry(int id, String reply) {
        Enquiry enquiry = enquiryDatabase.findById(id);
        if (enquiry.getResponse() != null) {
            throw new IllegalArgumentException("This enquiry has already been replied.");
        }
        enquiry.setResponse(reply);
    }
}
