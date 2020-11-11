import java.util.ArrayList;

/**
 * Utility class for handling security matters
 * @author Josh
 * @version 1.0
 * @since 2020-10-24
 */
public class AccessControl {
    private static UserDataManager udm;
    public static void initialize() {
        udm = new UserDataManager();
        udm.load();
    }
    /**
     * Used to validate user credentials.
     * Retrieves the list of users for the system, and checks if the given credentials match any of them.
     * Our implementation assumes userID is unique among all users, regardless of domain
     * @param userId userId to be searched for
     * @param password unhashed password entered by user
     * @return a User object if a matching account is found, null if not
     */
    public static User validate(String userId, String password) throws AccessDeniedException{
        ArrayList<User> userList = udm.getUserList();
        for (User account : userList) {
            if (account.validateLogin(userId, password)) {
                System.out.println("Login successful!");
                return account;
            }
        }
        throw new AccessDeniedException("User id not found");
    }
}
