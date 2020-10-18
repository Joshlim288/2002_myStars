import java.util.ArrayList;

/**
 * Utility class for controlling access and handling security
 *
 * We might want to change this to a utility class as well, perhaps serving all security functions
 * Like adding/storing passwords, in addition to current functionality
 */
public class AccessControl {

    public static User validate(String userId, String password, String domain){
        ArrayList<User> userList;
        if (domain.equals("Student")) userList = new ArrayList<>(FileHandler.getStudentList());
        else if (domain.equals("Admin")) userList = new ArrayList<>(FileHandler.getAdminList());
        else return null;

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
