/**
 * AdminValidator extends the UserValidator class and adds additional validation methods specialized for admin-related
 * data in the system.
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.12
 * @since 1.10
 */

public class AdminValidator extends UserValidator {

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
