import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Index represents the actual schedules for each course.
 * There can be multiple indexes per course, where the timings of the lessons may differ (except for LEC).
 * Contains an ArrayList of Lesson.
 * Contains two ArrayList of Student, one for students registered, the other for students in waitlist.
 * @author Chong Shen Rui
 * @version 1.2
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
     */
    public Index(int indexNum, int indexVacancy) {
        this.indexNum = indexNum;
        this.indexVacancy = indexVacancy;
        this.currentVacancy = 0;
        this.atMaxCapacity = false;
        this.lessons = new ArrayList<>();
        this.waitlist = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
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

    // TODO: Check if necessary to have set method for lessons as a whole
    public void addLesson(typeOfLesson type, String group, dayOfWeek day, LocalDateTime startTime, LocalDateTime endTime,
                          String venue, ArrayList<Integer> teachingWeeks) {
        Lesson newLesson = new Lesson(type, group, day, startTime, endTime, venue, teachingWeeks);
        lessons.add(newLesson);
    }

    public ArrayList<Student> getWaitlist() {
        return waitlist;
    }

    // TODO: Check if necessary to have set method for entire waitlist
    public void setWaitlist(ArrayList<Student> waitlist) {
        this.waitlist = waitlist;
    }

    public ArrayList<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    // TODO: Check if necessary to have set method for entire enrolledStudents
    public void setEnrolledStudents(ArrayList<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    /**
     * Method to add a student to current waitlist
     * Student added to the back of the queue.
     * @param waitlist The waitlist to add to.
     * @param student The student to add to waitlist.
     */
    public void addToWaitlist(ArrayList<Student> waitlist, Student student){
        waitlist.add(student);
    }

    /**
     * Method to remove a student from current waitlist.
     * Student at front of queue removed
     * Exception handling if waitlist is already empty
     * @param waitlist The waitlist to remove from.
     */
    //TODO: Improve if possible (first draft)
    public Student removeFromWaitlist(ArrayList<Student> waitlist){
        try{
            return waitlist.remove(0);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Waitlist is empty!");
            return null;
        }
    }

    /**
     * Method to add a student from EnrolledStudents ArrayList
     * @param enrolledStudents The ArrayList to add the student into.
     * @param student The student to be added.
     */
    // TODO: Exception handling if at max capacity
    public void addToEnrolledStudents(ArrayList<Student> enrolledStudents, Student student){
        enrolledStudents.add(student);
        this.currentVacancy--;
    }

    /**
     * Method to remove a student from EnrolledStudents ArrayList
     * @param enrolledStudents The ArrayList to remove the student from.
     * @param student The student to be removed.
     */
    // TODO: Exception handling if at zero capacity
    public void removeFromEnrolledStudents(ArrayList<Student> enrolledStudents, Student student){
            enrolledStudents.remove(student);
            this.currentVacancy++;
    }
}
