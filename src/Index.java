import java.util.ArrayList;

/**
 * Index represents the actual schedules for each course.
 * There can be multiple indexes per course, where the timings of the lessons may differ (except for LEC).
 * Contains ArrayList of Lesson.
 * Contains two ArrayList of Student, one for students registered, the other for students in waitlist.
 * @author Shen Rui
 * @version 1.1
 * @since 2020-10-18
 */

public class Index {

    /**
     * Uniquely identifies each index within a course.
     */
    private int indexNum;

    /**
     * Maximum vacancy for this index.
     */
    private int indexVacancy;

    /**
     * Number of vacancies remaining for the index.
     * Once vacancies reaches 0, index is full and can no longer accept more students
     */
    private int currentVacancy;

    /**
     * Boolean variable showing if index is at max capacity
     */
    private boolean atMaxCapacity;

    /**
     * Contains all lessons conducted under this index.
     * Each lesson can be mapped to only one index.
     */
    private ArrayList<Lesson> lessons;

    /**
     * Contains all students in waiting list for this index.
     * Each student can only be on the waitlist on one index per course.
     */
    private ArrayList<Student> waitlist;

    /**
     * Contains all students currently registered in this index
     * Each student can only be registered in one index for each course.
     */
    private ArrayList<Student> enrolledStudents;

    /**
     * Constructor for <code>Index</code>.
     * Only administrators should be able to initialise an index.
     * @param indexNum Unique identifier for this index.
     * @param indexVacancy Maximum vacancies for this index.
     * @param currentVacancy Current remaining vacancies for this index.
     * @param atMaxCapacity Boolean variable, 1 when at max capacity and 0 if not.
     * @param lessons Lessons under this index.
     * @param waitlist Students in queue registered for this index.
     */
    public Index(int indexNum, int indexVacancy, int currentVacancy, boolean atMaxCapacity,ArrayList<Lesson> lessons,
                 ArrayList<Student> waitlist, ArrayList<Student> enrolledStudents) {
        this.indexNum = indexNum;
        this.indexVacancy = indexVacancy;
        this.currentVacancy = currentVacancy;
        this.atMaxCapacity = atMaxCapacity;
        this.lessons = lessons;
        this.waitlist = waitlist;
        this.enrolledStudents = enrolledStudents;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public int getIndexVacancy() {
        return indexVacancy;
    }

    public void setIndexVacancy(int indexVacancy) {
        this.indexVacancy = indexVacancy;
    }

    public int getCurrentVacancy() {
        return currentVacancy;
    }

    public void setCurrentVacancy(int currentVacancy) {
        this.currentVacancy = currentVacancy;
    }

    public boolean isAtMaxCapacity() {
        return atMaxCapacity;
    }

    public void setAtMaxCapacity(boolean atMaxCapacity) {
        this.atMaxCapacity = atMaxCapacity;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public ArrayList<Student> getWaitlist() {
        return waitlist;
    }

    // TODO, change to editing or removing waitlist rather than setting whole list
    public void setWaitlist(ArrayList<Student> waitlist) {
        this.waitlist = waitlist;
    }

    public ArrayList<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    // TODO, change to editing or removing enrolled students rather than setting whole list
    public void setEnrolledStudents(ArrayList<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }
}
