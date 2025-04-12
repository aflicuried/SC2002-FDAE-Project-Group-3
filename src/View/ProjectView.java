package View;

import Entity.*;

import java.util.List;

public class ProjectView {
    public static void displayProject(Project project, User currentUser) {
        System.out.println("Project name: " + project.getName());
        System.out.println("Neighbourhood: " + project.getNeighbourhood());
        System.out.println(" - 2-Room: " + project.get2RoomUnits() + "\nPrice: " + project.get2RoomPrice());
        if (currentUser.getRole() == User.UserRole.APPLICANT) {
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

}
