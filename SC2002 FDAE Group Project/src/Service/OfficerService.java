package Service;

import Entity.*;
import Repository.IProjectRepository;
import Repository.IUserRepository;

public class OfficerService {
    private HDBOfficer officer;
    private final IUserRepository userRepo;
    private final IProjectRepository projectRepo;

    public OfficerService(IUserRepository userRepo, IProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }




    public void viewHandledProject(){
        this.projectHandling.displayDetails();
    }

    public void updateForApplicant(Applicant applicant, Project project, int num, int flatType) {
        project.updateFlatNumber(num); //num, flatType在controller里会输入
        //Retrieve
        applicant.getApplication().setStatus(Application.ApplicationStatus.BOOKED);
        applicant.setFlatType(flatType);
    }

    public void generateReceipt(Applicant applicant) {
        applicant.generateReceipt();
    }
}