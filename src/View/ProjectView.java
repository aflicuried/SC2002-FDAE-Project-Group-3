package View;

import Entity.*;

import java.util.List;

public class ProjectView {
    public static void displayProject(Project project, User currentUser) {
        if (currentUser.getRole() == User.UserRole.OFFICER) {
            HDBOfficer hdbOfficer = (HDBOfficer) currentUser;
            if (hdbOfficer.getProjectHandling() != null) {
                if (hdbOfficer.getProjectHandling().getName().equals(project.getName())) {
                    return;
                }
            }
        }
        System.out.println("Project name: " + project.getName());
        System.out.println("Neighbourhood: " + project.getNeighbourhood());
        System.out.println(" - 2-Room: " + project.get2RoomUnits() + "   Price: " + project.get2RoomPrice());
        if (currentUser.getMaritalStatus().equals("MARRIED")) {
            System.out.println(" - 3-Room: " + project.get3RoomUnits() + "   Price: " + project.get3RoomPrice());
        }
        System.out.println("Opening Date: " + project.getOpeningDate());
        System.out.println("Closing Date: " + project.getClosingDate());
        System.out.println("Manager: " + project.getManager().getName() + "\n");
    }

    public static void displayManagedProject(Project project) {
        System.out.println("Project name: " + project.getName());
        System.out.println("Neighbourhood: " + project.getNeighbourhood());
        System.out.println(" - 2-Room: " + project.get2RoomUnits() + "   Price: " + project.get2RoomPrice());
        System.out.println(" - 3-Room: " + project.get3RoomUnits() + "   Price: " + project.get3RoomPrice());
        System.out.println("Opening Date: " + project.getOpeningDate());
        System.out.println("Closing Date: " + project.getClosingDate());
        System.out.println("Manager: " + project.getManager().getName());

        System.out.println("Officer Slot: " + project.getOfficerSlots() + "\n");
    }

    public static void displayManagedList(List<Project> projects) {
        projects.forEach(p -> displayManagedProject(p));
    }
    public static void displayProjectList(List<Project> projects, User currentUser) {
        projects.forEach(p -> displayProject(p, currentUser));
    }
}
