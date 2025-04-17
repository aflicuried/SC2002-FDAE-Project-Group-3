package View;

import Entity.Applicant;
import Entity.Registration;
import Entity.User;

import java.util.List;

public class UserView {
    public static void displayApplicants(Applicant applicant) {
        System.out.println();
        System.out.println("Name: " + applicant.getName());
        System.out.println("Flat Type: " + applicant.getApplication().getFlatType());
        System.out.println("Project Name: " + applicant.getApplication().getProject().getName());
        System.out.println("Age: " + applicant.getAge());
        System.out.println("Marital Status: " + applicant.getApplication().getStatus());
    }
    public static void displayApplicants(List<Applicant> applicants) {
        applicants.forEach(a -> displayApplicants(a));
    }
}
