package Service;

import Entity.*;
import Repository.IProjectRepository;
import Repository.IUserRepository;
import Repository.ProjectRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerService {
    private HDBManager manager;
    private final IUserRepository userRepo;
    private final IProjectRepository projectRepo;

    public ManagerService(IUserRepository userRepo, IProjectRepository projectRepo) {
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
    }

    //
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

    public void editProject(){}

    public void deleteProject(){}

    public Project getProjectInCharge() {
        return manager.getProjectInCharge();
    }

    public void shiftVisibility(Project project) {
        project.shiftVisibility();
    }

    public void viewOfficerReg(){}

    public void handleRegistration(){}
}
