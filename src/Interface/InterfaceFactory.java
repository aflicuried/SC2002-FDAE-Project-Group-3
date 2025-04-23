package Interface;

import Entity.Applicant;
import Entity.HDBOfficer;
import Entity.HDBManager;
import Entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * Factory class to create the appropriate user interface (UI) instance
 * based on the role of the provided {@link User}.
 *
 * <p>This factory uses a role-based map to instantiate and return the correct
 * implementation of {@link BaseInterface} (e.g., {@link ApplicantInterface}, 
 * {@link OfficerInterface}, {@link ManagerInterface}).</p>
 */
public class InterfaceFactory { //to create "new Interface(currentUser)"
    private static final Map<User.UserRole, Function<User, BaseInterface>> creators = new HashMap<>();

    static {
        creators.put(User.UserRole.APPLICANT, user -> new ApplicantInterface((Applicant) user));
        creators.put(User.UserRole.OFFICER, user -> new OfficerInterface((HDBOfficer) user));
        creators.put(User.UserRole.MANAGER, user -> new ManagerInterface((HDBManager) user));
    }


    /**
     * Returns the appropriate interface implementation based on the user's role.
     *
     * @param user the currently logged-in user
     * @return the interface implementation specific to the user's role
     * @throws IllegalArgumentException if the user is null or has an unsupported role
     */
    public static BaseInterface getInterface(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }

        Function<User, BaseInterface> creator = creators.get(user.getRole());
        if (creator == null) {
            throw new IllegalArgumentException("No interface defined for role: " + user.getRole());
        }

        return creator.apply(user);
    }
}
