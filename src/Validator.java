import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator contains methods to validate inputs where format can be generalized, such as name, time and datetime.
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 */
public class Validator {

    public boolean validateName(String name) {
        if (name.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Only letters and spaces are allowed.");
        return false;
    }

    public boolean validateInt(String integer) {
        if (integer.matches("^[0-9]+$")) {
            return true;
        }
        System.out.println("ERROR: Input is not a number.");
        return false;
    }

    public boolean validateTime(String time) {
        if (time.matches("[0-9]{2}:[0-9]{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalDateTime.parse(time, formatter);
                return true;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Time is invalid.");
                return false;
            }
        }
        System.out.println("ERROR: Time must be in the format HH:MM");
        return false;
    }

    public boolean validateDateTime(String dateTime) {
        if (dateTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDateTime.parse(dateTime, formatter);
                return true;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Date is invalid.");
                return false;
            }
        }
        System.out.println("ERROR: Date must be in the format YYYY-MM-DD");
        return false;
    }
}
