package View;

import Entity.Application;

import java.util.List;

/**
 * Provides static methods to display and generate various views and reports 
 * related to {@link Application} entities.
 */
public class ApplicationView {

    /**
     * Displays the details of a single application in a formatted manner.
     *
     * @param application the {@link Application} object to be displayed
     */
    public static void displayApplication(Application application) {
        System.out.println("Applicant NRIC: " + application.getUser().getNric());
        System.out.println("Applicant Name: " + application.getUser().getName());
        System.out.println("Project: " + application.getProject().getName());
        System.out.println("Neighbourhood: " + application.getProject().getNeighbourhood());
        System.out.println("Status: " + application.getStatus());
        System.out.println("Flat Type: " + application.getFlatType());
        System.out.println("Withdrawal: " + application.isWithdrawal());
    }

    /**
     * Displays a list of applications by invoking {@link #displayApplication(Application)}
     * for each application in the list.
     *
     * @param applications a list of {@link Application} objects to be displayed
     */
    public static void displayApplications(List<Application> applications) {
        applications.forEach(ApplicationView::displayApplication);
    }

    /**
     * Generates and displays a receipt for a successfully booked application.
     * The receipt includes user and project information if the status is BOOKED.
     *
     * @param application the {@link Application} object for which the receipt is to be generated
     */
    public static void generateReceipt(Application application) {
        if (application.getStatus() == Application.ApplicationStatus.BOOKED) {
            System.out.println("Below is your receipt.");
            System.out.println("Applicant Name: " + application.getUser().getName());
            System.out.println("NRIC: " + application.getUser().getNric());
            System.out.println("Age: " + application.getUser().getAge());
            System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
            System.out.println("Flat Type: " + application.getFlatType());
            ProjectView.displayManagedProject(application.getProject());
        }
    }
}
