import java.util.ArrayList;

/**
 * Utility class for handling login into the program
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.5
 * @since 1.4
 */
public class AccessControl {
    private static UserDataManager udm;

    /**
     * Instantiates a new UserDataManager, to handle User data within data/users
     * User data is not meant to be edited from the UserDataManager,
     * it is an aggregation of data from all subclasses of User, thus this dataManager does not need to be closed
     *
     * May be called multiple times in an execution, to update the list of users if they have changed, once a user
     * logs out
     */
    public static void initialize() {
        udm = new UserDataManager();
        udm.load();
    }

    /**
     * Used to validate user credentials.
     * Retrieves the list of users for the system, and checks if the given credentials match any of them.
     * Our implementation requires userID to be unique among all users, regardless of domain
     * @param userId userId to be searched for
     * @param password unhashed password entered by user
     * @return User object if a matching account is found, null if not
     * @throws AccessDeniedException if userId cannot be found in the system
     */
    public static User validate(String userId, String password) throws AccessDeniedException{
        ArrayList<User> userList = udm.getUserList();
        for (User account : userList) {
            if (account.validateLogin(userId, password)) {
                System.out.println("Login successful!");
                return account;
            }
        }
        throw new AccessDeniedException("User ID not found!\n");
    }
}
