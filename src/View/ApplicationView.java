package View;

import Entity.Application;
import Entity.Project;

import java.util.List;

/**
 * Provides static methods to display and generate various views and reports 
 * related to {@link Application} entities.
 */
public class ApplicationView {

    /**
     * Displays the details of a single application.
     *
     * @param application the application to display
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
     * on each application in the list.
     *
     * @param applications the list of applications to display
     */
    public static void displayApplications(List<Application> applications) {
        applications.forEach(a -> displayApplication(a));
    }

    /**
     * Generates and displays a summary of application details if the application status is BOOKED.
     *
     * @param application the application for which to generate the summary
     */
    public static void generate(Application application) {
        if (application.getStatus() == Application.ApplicationStatus.BOOKED) {
            System.out.println("Applicant Name: " + application.getUser().getName());
            System.out.println("Flat Type: " + application.getFlatType());
            System.out.println("Project Name: " + application.getProject().getName());
            System.out.println("Applicant Age: " + application.getUser().getAge());
            System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
        }
    }

    /**
     * Generates a summary report for a list of applications, displaying details 
     * only for applications with status BOOKED.
     *
     * @param applications the list of applications to generate the report for
     */
    public static void generateReport(List<Application> applications) {
        applications.forEach(a -> generate(a));
    }

    /**
     * Generates and displays a receipt for an application if its status is BOOKED.
     * Includes personal and project information.
     *
     * @param application the application for which to generate the receipt
     */
    public static void generateReceipt(Application application) {
        if (application.getStatus() == Application.ApplicationStatus.BOOKED) {
            System.out.println("Applicant Name: " + application.getUser().getName());
            System.out.println("NRIC: " + application.getUser().getNric());
            System.out.println("Age: " + application.getUser().getAge());
            System.out.println("Marital Status: " + application.getUser().getMaritalStatus());
            System.out.println("Flat Type: " + application.getFlatType());
            ProjectView.displayManagedProject(application.getProject());
        }
    }
}
