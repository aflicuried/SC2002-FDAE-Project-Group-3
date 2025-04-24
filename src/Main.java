import Database.*;
import Interface.BaseInterface;
import Interface.InterfaceFactory;
import Entity.User;
import Service.AuthService;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The entry point of the BTO Management System application.
 * Handles user login, role-based interface delegation, and persistent data loading/saving.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Main method that launches the application and handles user interaction.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Initialize singleton database instances
        UserDatabase userDatabase = UserDatabase.getInstance();
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
        RegistrationDatabase registrationDatabase = RegistrationDatabase.getInstance();
        ApplicationDatabase applicationDatabase = ApplicationDatabase.getInstance();
        EnquiryDatabase enquiryDatabase = EnquiryDatabase.getInstance();

        // Load data from persistent storage
        try {
            userDatabase.loadData();
            projectDatabase.loadData();
            registrationDatabase.loadData();
            applicationDatabase.loadData();
            enquiryDatabase.loadData();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }

        // Main menu loop
        while (true) {
            System.out.println("Welcome to BTO Management System!");
            System.out.println("1 - Login");
            System.out.println("2 - Exit");

            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> {
                    AuthService authService = new AuthService();

                    // Prompt for NRIC and validate format
                    System.out.print("Enter your NRIC number: ");
                    String inputNRIC = scanner.nextLine();
                    while (!authService.isValidNRIC(inputNRIC)) {
                        System.out.println("Invalid NRIC format, try again");
                        System.out.print("Enter your NRIC number: ");
                        inputNRIC = scanner.nextLine();
                    }

                    // Prompt for password
                    System.out.print("Enter your password: ");
                    String inputPassword = scanner.nextLine();

                    try {
                        // Authenticate and retrieve the current user
                        User currentUser = authService.authenticate(inputNRIC, inputPassword);

                        // Delegate control to role-specific user interface
                        BaseInterface baseInterface = InterfaceFactory.getInterface(currentUser);
                        baseInterface.start();

                        // Save all data changes
                        saveData();

                    } catch (AuthenticationException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    return; // Exit application
                }
                default -> System.out.println("Invalid choice");
            }
        }
    }

    
    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = scanner.nextInt();
                scanner.nextLine(); // consume newline
                return input;
            } catch (InputMismatchException e) {
                scanner.nextLine(); // consume invalid input
                System.out.println("Please enter a valid number.");
            }
        }
    }

    
    private static void saveData() {
        try {
            UserDatabase.getInstance().saveData();
            ProjectDatabase.getInstance().saveData();
            RegistrationDatabase.getInstance().saveData();
            ApplicationDatabase.getInstance().saveData();
            EnquiryDatabase.getInstance().saveData();
            System.out.println("All data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
