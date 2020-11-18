/**
 * UserValidator extends the Validator class and adds additional validation methods specialized for handling users
 * in the system.
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.8
 * @since 1.7
 */
public class UserValidator extends Validator {

    /**
     * Validates a userID. userID may only contain letters and digits
     * @param userID String to match pattern against
     * @return true if input matches, false otherwise
     */
    public boolean validateUserID(String userID) {
        if (userID.matches("^[A-Za-z0-9]+$"))
            return true;
        System.out.println("ERROR: User ID can only contain letters and digits.");
        return false;
    }

    /**
     * validates an email, which must end with @e.ntu.edu.sg
     * @param email String to match pattern against
     * @return true if input matches, false otherwise
     */
    public boolean validateEmail(String email) {
        if (email.matches("^[\\w-\\.]+@e.ntu.edu.sg")) { //old: ^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$
            return true;
        }
        System.out.println("ERROR: Wrong email format.");
        return false;
    }

    /**
     * Validates a Gender. Gender may only be MALE, FEMALE or OTHER.
     * Gender is checked against typeOfGender defined under User.
     * @param gender String to match pattern against
     * @return true if input matches, false otherwise
     * @see typeOfGender
     */
    public boolean validateGender(String gender) {
        try {
            typeOfGender.valueOf(gender);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Gender can only be MALE / FEMALE / OTHER.");
            return false;
        }
    }
}
