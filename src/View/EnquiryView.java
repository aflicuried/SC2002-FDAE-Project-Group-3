package View;

import Entity.Enquiry;

import java.util.List;

/**
 * Provides static methods to display details related to {@link Enquiry} entities.
 */
public class EnquiryView {

    /**
     * Displays the details of a single enquiry, including the ID, user's name, 
     * associated project, message, and response.
     *
     * @param enquiry the enquiry to display
     */
    public static void displayEnquiry(Enquiry enquiry) {
        System.out.println("Enquiry ID: " + enquiry.getId());
        System.out.println("Name: " + enquiry.getUser().getName());
        System.out.println("Project: " + enquiry.getProject().getName());
        System.out.println("Message: " + enquiry.getMessage());
        System.out.println("Response: " + enquiry.getResponse());
    }

    /**
     * Displays a list of enquiries by invoking {@link #displayEnquiry(Enquiry)} 
     * on each enquiry in the list.
     *
     * @param enquiries the list of enquiries to display
     */
    public static void displayEnquiries(List<Enquiry> enquiries) {
        enquiries.forEach(e -> displayEnquiry(e));
    }
}
