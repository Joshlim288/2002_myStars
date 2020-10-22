import java.util.ArrayList;

/**
 * Utility class for controlling access and handling security
 *
 * We might want to change this to a utility class as well, perhaps serving all security functions
 * Like adding/storing passwords, in addition to current functionality
 */
public class AccessControl {

    /**
     * Used to validate user credentials
     * @param userId
     * userId to be searched for
     * @param password
     * unhashed password entered by user
     * @param domain
     * domain the user is attempting to log on to
     * @return
     * a User object that corresponds to the parameters given if a matching account is found,
     * null object otherwise
     */
    public static User validate(String userId, String password){
        ArrayList<User> userList;
        userList = FileHandler.getUserList();
        for (User account: userList) {
            if (account.getUserID().equals(userId) && BCrypt.checkpw(password, account.getHashedPassword())) {
                return account;
            }
        }
        return null;
    }

    /**
     * Method to implement the hash function for the program
     * uses the BCrypt library
     * @param toHash
     * String to be hashed
     * @return Hashed string
     */
    public static String hash(String toHash) {
        return BCrypt.hashpw(toHash, BCrypt.gensalt());
    }
}
