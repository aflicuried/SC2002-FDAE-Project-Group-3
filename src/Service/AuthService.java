package Service;

import Entity.User;
import Database.UserDatabase;
import View.UserView;

import javax.naming.AuthenticationException;
import java.util.List;

public class AuthService {

    private UserDatabase userDatabase = UserDatabase.getInstance();

    public boolean isValidNRIC(String nric) {
        return nric != null && nric.matches("^[ST]\\d{7}[A-Z]$");
    }

    public User findByNric(String nric) {
        ;
        return userDatabase.findUsers().stream().filter(user -> nric.equals(user.getNric()))
                .findFirst().orElse(null);
    }

    public User authenticate(String nric, String password) throws AuthenticationException {
        User user = findByNric(nric); //upcasting, before authentication
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }

    public boolean checkUser(String nric, String password) {
        User user = findByNric(nric); //upcasting, before authentication
        if (user == null || !user.getPassword().equals(password)) {
            return false;
        }
        return true;
    }

    public void changePassword(User user, String newPassword) {
        findByNric(user.getNric()).setPassword(newPassword);
    }
}