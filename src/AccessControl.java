import java.util.ArrayList;

/**
 * Utility class for handling security matters
 * @author Josh
 * @version 1.0
 * @since 2020-10-24
 */
public class AccessControl {

    /**
     * Used to validate user credentials. Currently uses the jBCrypt library
     * @param userId
     * userId to be searched for
     * @param password
     * unhashed password entered by user
     * @return
     * a User object that corresponds to the parameters given if a matching account is found,
     * null object otherwise
     */
    public static User validate(String userId, String password){
        ArrayList<User> userList = FileHandler.getUserList();
        Object test = new ArrayList<User>();
        if (test instanceof ArrayList<?>)
        userList = (ArrayList<User>) test;
        for (User account: userList) {
            if (account.getUserID().equals(userId) && BCrypt.checkpw(password, account.getHashedPassword())) {
                return account;
            }
        }
        return null;
    }

    /**
     * Used to hash a String. Currently uses the jBCrypt library
     * @param toHash
     * String to be hashed
     * @return
     * Hashed string
     */
    public static String hash(String toHash) {
        return BCrypt.hashpw(toHash, BCrypt.gensalt());
    }
}
