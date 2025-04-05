package View;

import Entity.Applicant;
import Entity.Application;
import Entity.Project;
import Entity.User;
import Repository.UserRepository.UserRole;

import java.util.List;

public class ProjectView {
    public static void displayProject(Project project, User currentUser) {
        System.out.println("Project name: " + project.getName());
        System.out.println("Neighbourhood: " + project.getNeighbourhood());
        System.out.println(" - 2-Room: " + project.get2RoomUnits() + "\nPrice: " + project.get2RoomPrice());
        if (currentUser.getRole() == UserRole.APPLICANT) {
            Applicant applicant = (Applicant) currentUser;
            if (!applicant.isSingle())
                System.out.println(" - 3-Room: " + project.get3RoomUnits() + "\nPrice: " + project.get3RoomPrice());
        }
        System.out.println("Opening Date: " + project.getOpeningDate());
        System.out.println("Closing Date: " + project.getClosingDate());
        System.out.println("Manager: " + project.getManager());
    }

    public static void displayProjectList(List<Project> projects, User currentUser) {
        projects.forEach(p -> displayProject(p, currentUser));
    }

    public static void viewApplication(Application application) {
        System.out.println("Applicant: " + application.getUser().getName());
        System.out.println("Project name: " + application.getProject().getName());
        System.out.println("Neighbourhood: " + application.getProject().getNeighbourhood());
        System.out.println("Flat Type: " + application.getFlatType());
    }

    public static void displayMyEnquiry(User user) {

    }
}
