package View;

import Entity.*;

import java.util.List;

/**
 * Provides static methods to display information related to {@link Project} entities,
 * including views tailored to user roles such as officers and applicants.
 */
public class ProjectView {

    /**
     * Displays details of a single project, conditionally formatted based on the current user's role
     * and marital status. If the user is an {@link HDBOfficer} and is currently managing the project,
     * the project details will not be displayed.
     *
     * @param project     the project to display
     * @param currentUser the user viewing the project, used for role and marital status checks
     */
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

    /**
     * Displays the full set of project information intended for project managers or officers.
     * Includes unit and pricing details, project dates, and officer slots.
     *
     * @param project the project to display
     */
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

    /**
     * Displays a list of projects, using {@link #displayManagedProject(Project)} for each.
     * Typically used for viewing all managed projects.
     *
     * @param projects the list of projects to display
     */
    public static void displayManagedList(List<Project> projects) {
        projects.forEach(p -> displayManagedProject(p));
    }

    /**
     * Displays a list of projects to a user, filtering or formatting output based on the user’s role.
     *
     * @param projects     the list of projects to display
     * @param currentUser  the user requesting the view, used for role-specific filtering
     */
    public static void displayProjectList(List<Project> projects, User currentUser) {
        projects.forEach(p -> displayProject(p, currentUser));
    }
}
