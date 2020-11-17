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
 * @author Josh, Jun Wei, Chong Shen Rui , Joshua, Daryl
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
    public Lesson(String lessonType, String day, String startTime, String endTime,
                  String venue, ArrayList<Integer> teachingWeeks) {
        this.lessonType = typeOfLesson.valueOf(lessonType);
        this.day = dayOfWeek.valueOf(day);
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);
        this.venue = venue;
        this.teachingWeeks = teachingWeeks;
    }

    /**
     * @return Type of the Lesson, refer to the above enum for possible values
     */
    public typeOfLesson getLessonType() {
        return lessonType;
    }

    /**
     * @param lessonType String equivalent of the new lessonType
     */
    public void setLessonType(String lessonType) {
        this.lessonType = typeOfLesson.valueOf(lessonType);
    }

    /**
     * @return Day that the Lesson is conducted on, refer to above Enum for possible values
     */
    public dayOfWeek getDay() {
        return day;
    }

    /**
     * @param day String equivalent for the dat that the Lesson is conducted on
     */
    public void setDay(String day) {
        this.day = dayOfWeek.valueOf(day);
    }

    /**
     * @return Start time of the Lesson as LocalTime
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime Start time of the Lesson to change to
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * @return End time of the Lesson as LocalTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime End time of the Lesson to change to
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * @return Venue that the Lesson is conducted at as a string
     */
    public String getVenue() {
        return venue;
    }

    /**
     * @param venue new venue that the Lesson is to be conducted at
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }

    /**
     * @return teaching weeks as an ArrayList of Integer objects, representing the teaching weeks that the Lesson
     * is conducted on
     */
    public ArrayList<Integer> getTeachingWeeks() {
        return teachingWeeks;
    }

    /**
     * @param teachingWeeks ArrayList of Integers that represents the teaching weeks the Lesson is conducted on
     */
    public void setTeachingWeeks(ArrayList<Integer> teachingWeeks) {
                this.teachingWeeks = teachingWeeks;
    }

    /**
     * @return Lesson details as a String, suitable for printing
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Lesson Type: " + lessonType + " | ");
        stringBuilder.append("Venue: " + venue + " | ");
        stringBuilder.append(day + ", " + startTime + " - " + endTime + "\n");
        stringBuilder.append("Teaching Weeks: ");
        for (int i = 0; i < teachingWeeks.size(); i++)
            if (i < teachingWeeks.size() - 1)
                stringBuilder.append(teachingWeeks.get(i) + ", ");
            else
                stringBuilder.append(teachingWeeks.get(i));
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}
