import Interface.BaseInterface;
import Interface.InterfaceFactory;
import Entity.User;
import Database.ProjectDatabase;
import Database.UserDatabase;
import Service.AuthService;
import View.UserView;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserDatabase userDatabase = UserDatabase.getInstance(); //Singleton pattern
        ProjectDatabase projectDatabase = ProjectDatabase.getInstance();

        //load data
        try {
            userDatabase.loadData();
            projectDatabase.loadData();

        } catch (IOException e) {
            System.out.println("Error loading data" + e.getMessage());
        }

        while(true) {
            //UserDatabase userDatabase = UserDatabase.getInstance(); //Singleton pattern
            //ProjectDatabase projectDatabase = ProjectDatabase.getInstance();
            System.out.println("Welcome to BTO Management System!");//also can read by console
            System.out.println("1 - Login");
            System.out.println("2 - Exit");
            int choice = readIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    scanner.nextLine();

                    //start app
                    AuthService authService = new AuthService();
                    System.out.print("Enter your NRIC number: ");
                    String inputNRIC = scanner.nextLine();
                    while (!authService.isValidNRIC(inputNRIC)) {
                        System.out.println("Invalid NRIC format, try again");
                        System.out.print("Enter your NRIC number: ");
                        inputNRIC = scanner.nextLine();
                    }

                    System.out.print("Enter your password: ");
                    String inputPassword = scanner.nextLine();

                    try{
                        //reference - User; object - Roles
                        User currentUser = authService.authenticate(inputNRIC, inputPassword);

                        //reference - BaseInterface; object - RoleInterfaces
                        //it has currentUser and 2 services
                        BaseInterface baseInterface = InterfaceFactory.getInterface(currentUser);
                        baseInterface.start();

                        //write data back to CSV!

                    } catch (AuthenticationException e){
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    scanner.nextLine();
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }
}