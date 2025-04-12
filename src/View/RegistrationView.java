package View;

import Entity.Registration;

import java.util.List;

public class RegistrationView {
    public static void displayRegistration(Registration registration) {
        System.out.println("Registration ID: " + registration.getId());
        System.out.println("Officer NRIC: " + registration.getOfficer().getNric());
        System.out.println("Officer Name: " + registration.getOfficer().getName());
        System.out.println("Project: " + registration.getProject().getName());
        System.out.println("Status: " + registration.getStatus());
    }

    public static void displayRegistrations(List<Registration> registrations) {
        registrations.forEach(RegistrationView::displayRegistration);
    }
}