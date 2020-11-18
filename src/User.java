import java.io.Serializable;

/**
 * Definition of enumeration called typeOfGender, with 3 elements, referred to as:
 * <ul>
 *     <li>typeOfGender.MALE</li>
 *     <li>typeOfGender.FEMALE</li>
 *     <li>typeOfGender.OTHER</li>
 * </ul>
 */
enum typeOfGender {
    MALE, FEMALE, OTHER
}

/**
 * User represents a user in the STARS system.<br>
 * It is inherited by Student and Admin.
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.4
 * @since 1.1
 */
public abstract class User implements Serializable {

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
     * @param password This user's password (hashed).
     * @param domain This user's domain
     * @param name This user's name
     * @param email This user's email
     */
    public User(String userID, String password, String domain, String name, String email) {
        this.userID = userID;
        this.hashedPassword = hash(password);
        this.domain = domain;
        this.name = name;
        this.email = email;
    }

    /**
     * @return userID of the User as a String
     */
    public String getUserID() {
        return userID;
    }

    /**
     * @return hashed password of the User as a String
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @return Domain that the User belongs to (e.g. Student, Admin)
     */
    public String getDomain() {return domain;}

    /**
     * @param userID String to change the User's userID to
     */
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

    /**
     * @return email currently linked to the User
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param newEmail String to change the User's email to
     */
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    /**
     * @return Name of the User as a String
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name String to change the User's name to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Validates a login for a User object
     * @param checkID userID to check if match
     * @param checkpw unhashed password to check if match
     * @return <code>true</code> if userID and password match, false if userID does not match
     * @throws AccessDeniedException thrown when the userID matches, but the password entered is wrong
     */
    public boolean validateLogin(String checkID, String checkpw) throws AccessDeniedException{
        if(checkID.equals(this.userID)) {
            if (BCrypt.checkpw(checkpw, hashedPassword)) {
                return true;
            } else {
                throw new AccessDeniedException("Invalid password");
            }
        }
        return false;
    }

    /**
     * Compares this User with another User based on userID
     * @param o The other User to compare with this User
     * @return <code>true</code> if the other student has the same matriculation number as this student
     */
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
