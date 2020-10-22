/**
 * User represents a user in the STARS system.<br>
 * It is inherited by Student and Admin.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
//TODO: add validations and error messages
public abstract class User {

    /**
     * This user's user ID.
     */
    private String userID;

    /**
     * This user's password (hashed).
     */
    private String hashedPassword;

    /**
     * The domain this user belongs to
     */
    private String domain;
    /**
     * Constructor for User, using userID and hashedPassword.
     * @param userID This user's user ID.
     * @param hashedPassword This user's password (hashed).
     */
    public User(String userID, String hashedPassword, String domain) {
        this.userID = userID;
        this.hashedPassword = hashedPassword;
        this.domain = domain;
    }

    public String getUserID() {
        return userID;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getDomain() {return domain;}

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return other.getUserID().equals(userID);
    }
}
