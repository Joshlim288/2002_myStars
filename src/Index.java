import java.util.ArrayList;

/**
 * Index represents the actual schedules for each course.
 * There can be multiple indexes per course, where the timings of the lessons may differ (except for LEC).
 * Contains ArrayList of Lesson and ArrayList of Student.
 * @author Shen Rui
 * @version 1.0
 * @since 2020-10-18
 */

public class Index {

    /**
     * Uniquely identifies each index within a course.
     */
    private int indexNum;

    /**
     * Number of vacancies remaining for the index.
     * Once vacancies reaches 0, index is full and can no longer accept more students
     */
    private int vacancies;

    /**
     * Contains all lessons conducted under this index.
     * Each lesson can be mapped to only one index.
     */
    private ArrayList<Lesson> lessons;

    /**
     * Contains all students subscribed to this index.
     * Each student can only be mapped to only one index per course.
     */
    private ArrayList<Student> queue;

    /**
     * Constructor for <code>Index</code>.
     * @param indexNum Unique identifier for this index.
     * @param vacancies Remaining vacancies for this index.
     * @param lessons Lessons under this index.
     * @param queue Students in queue registered for this index.
     */
    public Index(int indexNum, int vacancies, ArrayList<Lesson> lessons, ArrayList<Student> queue) {
        this.indexNum = indexNum;
        this.vacancies = vacancies;
        this.lessons = lessons;
        this.queue = queue;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }

    public int getVacancies() {
        return vacancies;
    }

    public void setVacancies(int vacancies) {
        this.vacancies = vacancies;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(ArrayList<Lesson> lessons) {
        this.lessons = lessons;
    }

    public ArrayList<Student> getQueue() {
        return queue;
    }

    public void setQueue(ArrayList<Student> queue) {
        this.queue = queue;
    }

}
