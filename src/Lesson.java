import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Definition of enumeration called dayOfWeek, with 7 elements, referred to as:
 * dayOfWeek.MON, dayOfWeek.TUES, dayOfWeek.WED, dayOfWeek.THURS, dayOfWeek.FRI, dayOfWeek.SAT, dayOfWeek.SUN
 */
enum dayOfWeek{
    MON, TUE, WED, THU, FRI, SAT, SUN
}

/**
 * Definition of enumeration called typeOfLesson, with 6 elements, referred to as:
 * typeOfLesson.LEC, typeOfLesson.TUT, typeOfLesson.LAB, typeOfLesson.DES, typeOfLesson.PRJ, typeOfLesson.SEM
 */
enum typeOfLesson{
    LEC, TUT, LAB, DES, PRJ, SEM
}

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
public class Lesson implements Serializable {

    /**
     * This lesson's type (e.g. LEC, TUT, LAB)
     * Represented with <code>typeOfLesson</code> enumeration.
     */
    private typeOfLesson lessonType;

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
     * @param day Day on which lesson is held. Represented with <code>dayofWeek</code> enumeration.
     * @param startTime This lesson's start time.
     * @param endTime This lesson's end time.
     * @param venue This lesson's venue.
     * @param teachingWeeks Array of integers representing teaching weeks (Wk 1 - 13).
     */
    public Lesson(String lessonType, String day, LocalTime startTime, LocalTime endTime,
                  String venue, ArrayList<Integer> teachingWeeks) {
        this.lessonType = typeOfLesson.valueOf(lessonType);
        this.day = dayOfWeek.valueOf(day);
        this.startTime = startTime; // kind of spaghetti here
        this.endTime = endTime;
        this.venue = venue;
        this.teachingWeeks = teachingWeeks;
    }

    public typeOfLesson getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = typeOfLesson.valueOf(lessonType);
    }

    public dayOfWeek getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = dayOfWeek.valueOf(day);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public void setTeachingWeeks(ArrayList<Integer> teachingWeeks) {
                this.teachingWeeks = teachingWeeks;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lesson Type: " + lessonType + " | ");
        stringBuilder.append("Venue: " + venue + " | ");
        stringBuilder.append(day + ", " + startTime + " - " + endTime + "\n");
        stringBuilder.append("Teaching Weeks: ");
        teachingWeeks.forEach((week) -> stringBuilder.append(week + ", "));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
