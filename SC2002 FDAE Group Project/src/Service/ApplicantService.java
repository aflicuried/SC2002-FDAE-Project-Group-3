package Service;

import Entity.*;
import Repository.IProjectRepository;
import Repository.IUserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static Entity.Application.ApplicationStatus;

public class ApplicantService {
    //database
    private final IUserRepository userRepo;
    private final IProjectRepository projectRepo;

    public ApplicantService(IUserRepository userRepo, IProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    //getters
    public IUserRepository getUserRepo() {
        return this.userRepo;
    }
    public IProjectRepository getProjectRepo() {
        return this.projectRepo;
    }

    //methods
    public void submitEnquiry() {

    }

    //
    public List<Project> getVisibleProjects(Applicant applicant) {
        return projectRepo.findAll().stream()
                .filter(project -> project.isVisible())
                .filter(project -> isEligibleForProject(project, applicant))
                .collect(Collectors.toList());
    }
    private boolean isEligibleForProject(Project project, Applicant applicant) {
        if (!project.isVisible())
            return false;
        if (applicant.isSingle() && applicant.getAge() >= 35) {
            return true;
        }
        else if (!applicant.isSingle() && applicant.getAge() >= 21) {
            return true;
        }
        return false;
    }
    public boolean isSingle(Applicant applicant) {
        return applicant.isSingle();
    }

    //
    public Project findProjectByName(String name) {
        return projectRepo.findAll().stream()
                .filter(p -> p.getName().equals(name)).findFirst().orElse(null);
    }

    public Application newApplication(User user, Project project, int flatType) {
        Application application;
        if (flatType == 1) {
            application = new Application(user, project, ApplicationStatus.PENDING, Application.FlatType.TWO_ROOM);
        }
        else {
            application = new Application(user, project, ApplicationStatus.PENDING, Application.FlatType.THREE_ROOM);
        }
        return application;
    }
    public void sendApplication(Applicant applicant, Application application) {
        application.getProject().getApplications().add(application);
        applicant.setProject(application.getProject());
    }

    //
    public Project getProject(Applicant applicant) {
        return applicant.getProject();
    }

    //
    public Boolean isAvailableToBookFlat(Applicant applicant) {
        if (applicant.getApplication().getStatus() == ApplicationStatus.SUCCESSFUL) {
            return true;
        }
        else
            return false;
    }

    //
    public void withdrawalApplication(Applicant applicant) {
        if (applicant.getApplication().getStatus() == ApplicationStatus.SUCCESSFUL
                || applicant.getApplication().getStatus() == ApplicationStatus.BOOKED) {
            applicant.getApplication().setStatus(ApplicationStatus.UNSUCCESSFUL);//assume always successful for withdrawal
            applicant.setProject(null);
        }
        else {
            System.out.println("Withdrawal failed");
        }
    }

    //
    public void submitEnquiry(String s, User user, Project project) {
        Enquiry enquiry = new Enquiry(user, project, s);

    }
}