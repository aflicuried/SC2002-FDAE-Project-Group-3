package Service;

import Entity.User;
import Repository.IUserRepository;
import javax.naming.AuthenticationException;

public class AuthService {

    private IUserRepository userRepo;  //non-static dependency
    //transfer by constructor
    public AuthService(IUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User authenticate(String nric, String password) throws AuthenticationException {
        User user = userRepo.findByNric(nric); //upcasting, before authentication
        if (user == null || !user.getPassword().equals(password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        return user;
    }

    /*public boolean changePassword(String newPassword) {
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            this.password = newPassword;
            return true;
        }
        return false;
    }*/
}
