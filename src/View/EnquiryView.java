package View;

import Entity.Enquiry;

import java.util.List;

public class EnquiryView {
    public static void displayEnquiry(Enquiry enquiry) {
        System.out.println("Enquiry ID: " + enquiry.getId());
        System.out.println("Name: " + enquiry.getUser().getName());
        System.out.println("Project: " + enquiry.getProject().getName());
        System.out.println("Message: " + enquiry.getMessage());
        System.out.println("Response: " + enquiry.getResponse());
    }

    public static void displayEnquiries(List<Enquiry> enquiries) {
        enquiries.forEach(e -> displayEnquiry(e));
    }
}
