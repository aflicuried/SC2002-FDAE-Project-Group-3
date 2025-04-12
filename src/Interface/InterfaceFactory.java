package Interface;

import Entity.Applicant;
import Entity.HDBOfficer;
import Entity.HDBManager;
import Entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class InterfaceFactory { //to create "new Interface(currentUser)"
    private static final Map<User.UserRole, Function<User, BaseInterface>> creators = new HashMap<>();

    static {
        creators.put(User.UserRole.APPLICANT, user -> new ApplicantInterface((Applicant) user));
        creators.put(User.UserRole.OFFICER, user -> new OfficerInterface((HDBOfficer) user));
        creators.put(User.UserRole.MANAGER, user -> new ManagerInterface((HDBManager) user));
    }

    public static BaseInterface getInterface(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Function<User, BaseInterface> creator = creators.get(user.getRole());
        if (creator == null) {
            throw new IllegalArgumentException("No interface defined for role: " + user.getRole());
        }

        return creator.apply(user);
    }
}