import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Lesson represents the actual classes held for an index of a course.
 * Lessons encompasses lectures, tutorials, seminars, etc.
 * Lessons can have different venues, timings, and so on.
 * There are multiple lessons per index of a course.
 * Enumerations for day of lessons and type of lessons are also included.
 * @author Chong Shen Rui
 * @version 1.0
 * @since 2020-10-18
 */

/**
 * Definition of enumeration called dayOfWeek, with 7 elements, referred to as:
 * dayOfWeek.MON, dayOfWeek.TUES, dayOfWeek.WED, dayOfWeek.THURS, dayOfWeek.FRI, dayOfWeek.SAT, dayOfWeek.SUN
 */
enum dayOfWeek{
    MON, TUES, WED, THURS, FRI, SAT, SUN
}

/**
 * Definition of enumeration called typeOfLesson, with 6 elements, referred to as:
 * typeOfLesson.LEC, typeOfLesson.TUT, typeOfLesson.LAB, typeOfLesson.DES, typeOfLesson.PRJ, typeOfLesson.SEM
 */
enum typeOfLesson{
    LEC, TUT, LAB, DES, PRJ, SEM
}

public class Lesson {

    /**
     * This lesson's type (e.g. LEC, TUT, LAB)
     * Represented with <code>typeOfLesson</code> enumeration.
     */
    private typeOfLesson lessonType;

    //TODO: Confirm if group should be an attribute here.
    /**
     * This lesson's group.
     * Each lesson can only belong to one group.
     */
    private String group;

    /**
     * Day on which lesson is held.
     * Represented with <code>dayOfWeek</code> enumeration.
     */
    private dayOfWeek day;

    /**
     * This lesson's start time.
     * Implemented using LocalDateTime class.
     */
    private LocalTime startTime;

    /**
     * This lesson's end time.
     * Implemented using LocalDateTime class.
     */
    private LocalTime endTime;

    /**
     * This lesson's venue.
     */
    private String venue;

    /**
     * This lesson's teaching weeks represented by array of integers representing week number.
     * Valid range of teaching weeks is 1-13.
     */
    private ArrayList<Integer> teachingWeeks;

    /**
     * Constructor for <code>Lesson</code>.<br>
     * @param lessonType Type of lesson. Represented with <code>typeOfLesson</code> enumeration.
     * @param group //The group this lesson belongs to.
     * @param day Day on which lesson is held. Represented with <code>dayofWeek</code> enumeration.
     * @param startTime This lesson's start time.
     * @param endTime This lesson's end time.
     * @param venue This lesson's venue.
     * @param teachingWeeks Array of integers representing teaching weeks (Wk 1 - 13).
     */
    public Lesson(String lessonType, String group, String day, String startTime, String endTime,
                  String venue, ArrayList<Integer> teachingWeeks) throws ObjectCreationException {
        if (validateLessonType(lessonType) && validateGroup(group) && validateDay(day) &&
                validateAndConvertTime(startTime) != null && validateAndConvertTime(endTime) != null) {
            this.lessonType = typeOfLesson.valueOf(lessonType);
            this.group = group;
            this.day = dayOfWeek.valueOf(day);
            this.startTime = validateAndConvertTime(startTime); // kind of spaghetti here
            this.endTime = validateAndConvertTime(endTime);
            this.venue = venue;
            this.teachingWeeks = teachingWeeks;
        } else {
            throw new ObjectCreationException();
        }
    }

    public typeOfLesson getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        if (validateLessonType(lessonType)) {
            this.lessonType = typeOfLesson.valueOf(lessonType);
        }
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        if (validateGroup(group)) {
            this.group = group;
        }
    }

    public dayOfWeek getDay() {
        return day;
    }

    public void setDay(String day) {
        if (validateDay(day)) {
            this.day = dayOfWeek.valueOf(day);
        }
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        LocalTime time = validateAndConvertTime(startTime);
        if (time != null) {
            this.startTime = time;
        }
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        LocalTime time = validateAndConvertTime(endTime);
        if (time != null) {
            this.endTime = time;
        }
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public ArrayList<Integer> getTeachingWeeks() {
        return teachingWeeks;
    }

    /**
     * Range validation done to ensure teaching weeks fall between Week 1 -13
     */
    public void setTeachingWeeks(ArrayList<Integer> teachingWeeks) {
        for (int i = 0; i < teachingWeeks.size(); i++) {
            if (teachingWeeks.get(i) > 13 || teachingWeeks.get(i) < 1) {
                System.out.println("Invalid teaching weeks entered.\n" +
                        "Choose from Weeks 1 - 13");
                break;
            }
            else {
                this.teachingWeeks = teachingWeeks;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lesson Type: " + lessonType + "\n");
        stringBuilder.append("Venue: " + venue + "\n");
        //TODO: to format starttime and endtime, extract elements and print (LocalTime alr has its own toString)
        stringBuilder.append(day + ", " + startTime + " - " + endTime + "\n");
        stringBuilder.append("Teaching Weeks: ");
        teachingWeeks.forEach((week) -> {
            stringBuilder.append(week + ", ");
        });
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private boolean validateLessonType(String lessonType) {
        try {
            typeOfLesson.valueOf(lessonType);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Lesson type can only be LEC / TUT / LAB / DES / PRJ / SEM");
            return false;
        }
    }

    private boolean validateGroup(String group) {
        if (group.matches("^[0-9A-Z]+$")) {
            return true;
        }
        System.out.println("ERROR: Group can only contain alphabets (in uppercase) and numbers.");
        return false;
    }

    private boolean validateDay(String day) {
        try {
            dayOfWeek.valueOf(day);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Day can only be MON / TUES / WED / THURS / FRI / SAT / SUN");
            return false;
        }
    }

    private LocalTime validateAndConvertTime(String time) {
        if (time.matches("[0-9]{2}:[0-9]{2}")) { // check format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            try {
                LocalTime validDateTime = LocalTime.parse(time, formatter);
                return validDateTime;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Please enter a valid time.");
                return null;
            }
        } else {
            System.out.println("ERROR: Datetime must be in the format HH:MM (24H Time)");
            return null;
        }
    }
}
