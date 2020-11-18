/**
 * StudentValidator extends the UserValidator class and adds additional validation methods specialized for course-related
 * data in the system.<p>
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.12
 * @since 1.10
 */

public class StudentValidator extends UserValidator {

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


}
