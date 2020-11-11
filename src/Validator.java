import java.time.LocalDateTime;
import java.time.LocalTime;
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
                LocalTime.parse(time, formatter);
                return true;
            } catch (DateTimeParseException e) {
                System.out.println(e);
                System.out.println("ERROR: Time is invalid.");
                return false;
            }
        }
        System.out.println("ERROR: Time must be in the format HH:MM");
        return false;
    }

    public boolean validateDateTime(String dateTime) {
        if (dateTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            try {
                LocalDateTime.parse(dateTime, formatter);
                return true;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Date is invalid.");
                return false;
            }
        }
        System.out.println("ERROR: Date must be in the format YYYY-MM-DD HH:mm");
        return false;
    }

    public boolean validateDateTimePeriod(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
        if (startDateTime.compareTo(endDateTime) < 0) {
            return true;
        }
        System.out.println("ERROR: End datetime cannot be before or same as start datetime.");
        return false;
    }

    public boolean validateTimePeriod(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(start);
        if (startTime.compareTo(endTime) < 0) {
            return true;
        }
        System.out.println("ERROR: End time cannot be before or same as start time.");
        return false;
    }

    //TODO: let students set their passwords upon first login
    public boolean validatePassword(String password) {
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,15}$")) {
            return true;
        }
        System.out.println("ERROR: Password must contain at least \n" +
                            "- an uppercase letter\n" +
                            "- a lowercase letter\n" +
                            "- a digit\n" +
                            "- a symbol\n" +
                            "and must be at least 8 characters long");
        return true;
    }
}
