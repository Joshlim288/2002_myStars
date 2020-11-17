/**
 * UserValidator extends the Validator class and adds additional validation methods specialized for handling users
 * in the system.
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-11-16
 */
public class UserValidator extends Validator {

    /**
     * Validates a userID
     * userID may only contain letters and digits
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
     * validates an email
     * Must end with @e.ntu.edu.sg
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
     * Validates a student's matriculation number. Assumes format as a string of 9 characters where:
     * <ul>
     *     <li>First and last characters are any letters from A-Z (capital).</li>
     *     <li>Middle 7 characters can be any combination of digits from 0-9.</li>
     * </ul>
     * @param matricNum String to match pattern against
     * @return true if input matches, false otherwise
     */
    public boolean validateMatricNum(String matricNum) {
        if (matricNum.matches("^[A-Z][0-9]{7}[A-Z]$")) {
            return true;
        }
        System.out.println("ERROR: Invalid matriculation number format.");
        return false;
    }

    /**
     * Validates a Gender
     * May only be MALE, FEMALE or OTHER
     * @param gender String to match pattern against
     * @return true if input matches, false otherwise
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

    /**
     * Validates an admin's staff number. Assumes format of staffNum as a string of 10 characters where:
     * <ul>
     *     <li>ADMIN must be the first 5 characters.</li>
     *     <li>The last character is any letter from A-Z (capital).</li>
     *     <li>The remaining 4 characters are any combination of digits from 0-9.</li>
     * </ul>
     * @param staffNum String to match pattern against
     * @return true if input matches, false otherwise
     */
    public boolean validateStaffNum(String staffNum) {
        if (staffNum.matches("ADMIN[0-9]{4}[A-Z]")) {
            return true;
        }
        System.out.println("ERROR: Invalid staff number format.");
        return false;
    }
}
