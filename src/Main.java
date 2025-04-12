import Interface.BaseInterface;
import Interface.InterfaceFactory;
import Entity.User;
import Database.ProjectDatabase;
import Database.UserDatabase;
import Service.AuthService;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.util.Scanner;

public class Main {
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

        //start app
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        System.out.println("Welcome to BTO Management System!");//also can read by console
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
    }
}