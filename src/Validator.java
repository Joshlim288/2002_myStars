import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Validator contains methods to validate inputs where format can be generalized, such as name, time and datetime.<p>
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.9
 * @since 1.7
 */
public class Validator {

    /**
     * Validates a name, which may only contain letters and spaces
     * @param name String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateName(String name) {
        if (name.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Only letters and spaces are allowed.");
        return false;
    }

    /**
     * Validates input to be an integer.
     * @param integer String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateInt(String integer) {
        if (integer.matches("^[0-9]+$")) {
            return true;
        }
        System.out.println("ERROR: Input is not a number.");
        return false;
    }

    /**
     * Validates a time, which must be of the format HH:mm
     * @param time String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateTime(String time) {
        if (time.matches("[0-9]{2}:[0-9]{2}")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalTime.parse(time, formatter);
                return true;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Time is invalid.");
                return false;
            }
        }
        System.out.println("ERROR: Time must be in the format HH:MM");
        return false;
    }

    /**
     * validates a dateTime, which must be of format yyyy-MM-dd HH:mm
     * @param dateTime String to match against the pattern
     * @return true if input matches, false otherwise
     */
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

    /**
     * Validates a dateTime period, which ensures that a start dateTime is after an end dateTime
     * @param start start dateTime to check against
     * @param end end dateTime to check against
     * @return true if start dateTime is after end dateTime, false otherwise
     */
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

    /**
     * Validates a time period, which ensures that a start time is after an end time
     * @param start start time to check against
     * @param end end time to check against
     * @return true if start time is after end time, false otherwise
     */
    public boolean validateTimePeriod(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);
        if (startTime.compareTo(endTime) < 0) {
            return true;
        }
        System.out.println("ERROR: End time cannot be before or same as start time.");
        return false;
    }

    /**
     * Validates day. Accepted day inputs are the first three characters of days of the week, capitalized
     * <br>Day inputs are checked against dayOfWeek defined under Lesson.
     * @param day String to match against the pattern
     * @return true if input matches, false otherwise
     * @see dayOfWeek
     */
    public boolean validateDay(String day) {
        try {
            dayOfWeek.valueOf(day);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Day can only be MON / TUE / WED / THU / FRI / SAT / SUN");
            return false;
        }
    }
}
