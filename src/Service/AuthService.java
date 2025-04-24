package Service;

import Entity.User;
import Database.UserDatabase;
import View.UserView;

import javax.naming.AuthenticationException;
import java.util.List;


/**
 * AuthService handles user authentication and account-related operations
 * such as login validation and password changes.
 */
public class AuthService {

    private UserDatabase userDatabase = UserDatabase.getInstance();


    /**
     * Validates the format of a Singapore NRIC number.
     *
     * @param nric the NRIC string to validate
     * @return true if the NRIC format is valid, false otherwise
     */
    public boolean isValidNRIC(String nric) {
        return nric != null && nric.matches("^[ST]\\d{7}[A-Z]$");
    }


        /**
     * Finds and retrieves a user by their NRIC.
     *
     * @param nric the NRIC of the user to find
     * @return the User object if found, or null if not found
     */
    public User findByNric(String nric) {
        ;
        return userDatabase.findUsers().stream().filter(user -> nric.equals(user.getNric()))
                .findFirst().orElse(null);
    }


    /**
     * Authenticates a user using their NRIC and password.
     *
     * @param nric the NRIC of the user
     * @param password the user's password
     * @return the authenticated User object
     * @throws AuthenticationException if authentication fails due to incorrect credentials
     */
    public User authenticate(String nric, String password) throws AuthenticationException {
        User user = findByNric(nric); //upcasting, before authentication
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }


    /**
     * Checks if a user exists with the given NRIC and password.
     *
     * @param nric the NRIC of the user
     * @param password the user's password
     * @return true if the user exists and the password matches, false otherwise
     */
    public boolean checkUser(String nric, String password) {
        User user = findByNric(nric); //upcasting, before authentication
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    
    /**
     * Changes the password for the specified user.
     *
     * @param user the user whose password is to be changed
     * @param newPassword the new password to set
     */
    public void changePassword(User user, String newPassword) {
        findByNric(user.getNric()).setPassword(newPassword);
    }
}
