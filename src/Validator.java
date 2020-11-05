import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

    public boolean validateName(String name) {
        if (name.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Only alphabets and spaces are allowed.");
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

    public boolean validateDate(String date) {
        if (date.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                LocalDateTime.parse(date, formatter);
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
