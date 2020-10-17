import java.io.Serializable;

/**
 * User represents a user in the STARS system.<br>
 * It is inherited by Student and Admin.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */

public abstract class User implements Serializable {

    /**
     * This user's user ID.
     */
    private String userID;

    /**
     * This user's password (hashed).
     */
    private transient String hashedPassword;

    /**
     * Constructor for User, using userID and hashedPassword.
     * @param userID This user's user ID.
     * @param hashedPassword This user's password (hashed).
     */
    public User(String userID, String hashedPassword) {
        this.userID = userID;
        this.hashedPassword = hashedPassword;
    }

    public String getUserID() {
        return userID;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
}
