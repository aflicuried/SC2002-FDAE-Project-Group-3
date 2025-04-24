package View;

import Entity.Application;

import java.util.List;

public class ApplicationView {
    public static void displayApplication(Application application) {
        System.out.println("Applicant NRIC: " + application.getUser().getNric());
        System.out.println("Applicant Name: " + application.getUser().getName());
        System.out.println("Project: " + application.getProject().getName());
        System.out.println("Neighbourhood: " + application.getProject().getNeighbourhood());
        System.out.println("Status: " + application.getStatus());
        System.out.println("Flat Type: " + application.getFlatType());
        System.out.println("Withdrawal: " + application.isWithdrawal());
    }

    public static void displayApplications(List<Application> applications) {
        applications.forEach(ApplicationView::displayApplication);
    }

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