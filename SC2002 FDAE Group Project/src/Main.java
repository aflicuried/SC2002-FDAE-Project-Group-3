import Controller.ApplicantController;
import Controller.ManagerController;
import Controller.OfficerController;
import Entity.Applicant;
import Entity.HDBManager;
import Entity.HDBOfficer;
import Entity.User;
import Repository.IProjectRepository;
import Repository.IUserRepository;
import Repository.ProjectRepository;
import Repository.UserRepository;
import Service.*;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        IUserRepository userRepo = new UserRepository();
        IProjectRepository projectRepo = new ProjectRepository((UserRepository) userRepo);//downcasting

        //load data
        try {
            userRepo.loadData();
            projectRepo.loadData();

        } catch (IOException e) {
            System.out.println("Error loading data" + e.getMessage());
        }

        //start app
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to BTO Management System!");//also can read by console
        System.out.print("Enter your NRIC number: ");
        String inputNRIC = scanner.nextLine();
        System.out.print("Enter your password: ");
        String inputPassword = scanner.nextLine();

        AuthService authService = new AuthService(userRepo);
        try{
            User currentUser = authService.authenticate(inputNRIC, inputPassword);//reference - User; object - Applicant/Officer/...
            ProjectService projectService = new ProjectService(userRepo, projectRepo);
            if (currentUser instanceof HDBOfficer){
                OfficerService officerService = new OfficerService(userRepo, projectRepo);
                OfficerController officerController = new OfficerController(officerService, projectService, (HDBOfficer)currentUser);
                officerController.start();
            }
            else if (currentUser instanceof Applicant) {
                ApplicantService applicantService = new ApplicantService(userRepo, projectRepo);
                ApplicantController applicantController = new ApplicantController(applicantService, projectService, (Applicant)currentUser); //downcasting, after authentication
                applicantController.start();
            }
            else if (currentUser instanceof HDBManager){
                ManagerService managerService = new ManagerService(userRepo, projectRepo);
                ManagerController managerController = new ManagerController(managerService, projectService, (HDBManager)currentUser);
                managerController.start();
            }
        } catch (AuthenticationException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}