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
     * The main method that initializes databases, handles login flow, and dispatches
     * users to their respective role-based interfaces.
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

        // Load data from CSV or storage files
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
                        // Authenticate and retrieve current user
                        User currentUser = authService.authenticate(inputNRIC, inputPassword);

                        // Delegate to appropriate interface based on user role
                        BaseInterface baseInterface = InterfaceFactory.getInterface(currentUser);
                        baseInterface.start();

                        // Save data changes
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

    /**
     * Prompts the user for an integer input, validating and re-prompting until a valid number is entered.
     *
     * @param prompt the message to display before input
     * @return a valid integer input from the user
     */
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

    /**
     * Saves data from all databases back to their respective files.
     * Handles any IO exceptions that may occur during saving.
     */
    private static void saveData() {
        try {
            // Save all database changes to their respective files
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
