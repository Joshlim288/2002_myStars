import java.time.LocalDateTime;

/**
 * Lesson represents the actual classes held for an index of a course.
 * These classes encompasses lectures, tutorials, seminars, etc.
 * These classes can have different venues, timings, and so on.
 * ArrayList of lessons is a variable for <code>Index</code>
 * @author Shen Rui
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
    private LocalDateTime startTime;

    /**
     * This lesson's end time.
     * Implemented using LocalDateTime class.
     */
    private LocalDateTime endTime;

    /**
     * This lesson's venue.
     */
    private String venue;

    /**
     * This lesson's teaching weeks represented by array of integers representing week number.
     * (e.g. 1,3,5,7,9,11,13)
     */
    private int[] teachingWeeks;

    /**
     * Constructor for <code>Lesson</code>.<br>
     * @param lessonType Type of lesson. Represented with <code>typeOfLesson</code> enumeration.
     * @param group //Might move to Index
     * @param day Day on which lesson is held. Represented with <code>dayofWeek</code> enumeration.
     * @param startTime This lesson's start time.
     * @param endTime This lesson's end time.
     * @param venue This lesson's venue.
     * @param teachingWeeks Array of integers representing teaching weeks (Wk 1 - 14).
     */
    public Lesson(typeOfLesson lessonType, String group, dayOfWeek day, LocalDateTime startTime, LocalDateTime endTime,
                  String venue, int[] teachingWeeks) {
        this.lessonType = lessonType;
        this.group = group;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.teachingWeeks = teachingWeeks;
    }

    public typeOfLesson getLessonType() {
        return lessonType;
    }

    public void setLessonType(typeOfLesson lessonType) {
        this.lessonType = lessonType;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public dayOfWeek getDay() {
        return day;
    }

    public void setDay(dayOfWeek day) {
        this.day = day;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int[] getTeachingWeeks() {
        return teachingWeeks;
    }

    public void setTeachingWeeks(int[] teachingWeeks) {
        this.teachingWeeks = teachingWeeks;
    }

}
