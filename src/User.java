/**
 * User represents a user in the STARS system.<br>
 * It is inherited by Student and Admin.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
public abstract class User {

    /** This user's user ID. */
    private String userID;

    /** This user's password (hashed). */
    private String hashedPassword;

    /** The domain this user belongs to */
    private String domain;

    /** This user's name */
    private String name;

    /** The email of the user */
    private String email;
    /**
     * Constructor for User, using userID and hashedPassword.
     * @param userID This user's user ID.
     * @param hashedPassword This user's password (hashed).
     */
    public User(String userID, String hashedPassword, String domain, String name, String email) {
        this.userID = userID;
        this.hashedPassword = hashedPassword;
        this.domain = domain;
        this.name = name;
        this.email = email;
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

    /**
     * Used to hash a String. Currently uses the jBCrypt library
     * @param toHash String to be hashed
     * @return Hashed string
     */
    private String hash(String toHash) {
        return BCrypt.hashpw(toHash, BCrypt.gensalt());
    }

    /**
     * Set password for a user
     * Will automatically hash the given password, so password is not stored as raw text
     * @param password String to change the user's password to
     */
    public void setPassword(String password) {
        this.hashedPassword = hash(password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean validateLogin(String checkID, String checkpw) throws AccessDeniedException{
        return checkID.equals(this.userID) && BCrypt.checkpw(checkpw, hashedPassword);
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
