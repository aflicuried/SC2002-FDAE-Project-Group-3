package View;

import Entity.Applicant;
import Entity.Registration;
import Entity.User;

import java.util.List;

/**
 * Provides static methods to display information related to {@link Applicant} entities,
 * particularly their associated application and personal details.
 */
public class UserView {

    /**
     * Displays the details of a single applicant, including name, selected flat type,
     * project name, age, and application status.
     *
     * @param applicant the applicant whose information is to be displayed
     */
    public static void displayApplicants(Applicant applicant) {
        System.out.println();
        System.out.println("Name: " + applicant.getName());
        System.out.println("Flat Type: " + applicant.getApplication().getFlatType());
        System.out.println("Project Name: " + applicant.getApplication().getProject().getName());
        System.out.println("Age: " + applicant.getAge());
        System.out.println("Marital Status: " + applicant.getApplication().getStatus());
    }

    /**
     * Displays a list of applicants by invoking {@link #displayApplicants(Applicant)} 
     * on each applicant in the list.
     *
     * @param applicants the list of applicants to display
     */
    public static void displayApplicants(List<Applicant> applicants) {
        applicants.forEach(a -> displayApplicants(a));
    }
}
