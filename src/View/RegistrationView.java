package View;

import Entity.Registration;

import java.util.List;

/**
 * Provides static methods to display information related to {@link Registration} entities,
 * which represent HDB officer registrations for project assignments.
 */
public class RegistrationView {

    /**
     * Displays the details of a single registration, including the registration ID,
     * officer's NRIC and name, associated project, and registration status.
     *
     * @param registration the registration to display
     */
    public static void displayRegistration(Registration registration) {
        System.out.println("Registration ID: " + registration.getId());
        System.out.println("Officer NRIC: " + registration.getOfficer().getNric());
        System.out.println("Officer Name: " + registration.getOfficer().getName());
        System.out.println("Project: " + registration.getProject().getName());
        System.out.println("Status: " + registration.getStatus());
    }

    /**
     * Displays a list of registrations by invoking {@link #displayRegistration(Registration)} 
     * on each registration in the list.
     *
     * @param registrations the list of registrations to display
     */
    public static void displayRegistrations(List<Registration> registrations) {
        registrations.forEach(RegistrationView::displayRegistration);
    }
}
