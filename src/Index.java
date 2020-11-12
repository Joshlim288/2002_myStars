import java.io.Serializable;
import java.time.LocalTime;
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

public class Index implements Serializable {

    private static final long serialVersionUID = 7335698930935614740L;

    /**
     * Uniquely identifies each index within a course.
     */
    private String indexNum;

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
     * The corresponding group for this index (e.g. SS10)
     */
    private String group;

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
    public Index(String indexNum, int indexVacancy, String group) {
        this.indexNum = indexNum;
        this.indexVacancy = indexVacancy;
        this.currentVacancy = this.indexVacancy;
        this.atMaxCapacity = false;
        this.group = group;
        this.lessons = new ArrayList<>();
        this.waitlist = new ArrayList<>();
        this.enrolledStudents = new ArrayList<>();
    }

    public String getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(String indexNum) {
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

    private void setAtMaxCapacity(boolean atMaxCapacity) {
        this.atMaxCapacity = atMaxCapacity;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    // TODO: Check if necessary to have set method for lessons as a whole
    public void addLesson(String type, String day, String startTime, String endTime,
                          String venue, ArrayList<Integer> teachingWeeks) {
        Lesson newLesson = new Lesson(type, day, startTime, endTime, venue, teachingWeeks);
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
    // TODO: Exception handling if at max capacity (added?)
    public void addToEnrolledStudents(ArrayList<Student> enrolledStudents, Student student){
        enrolledStudents.add(student);
        currentVacancy--;
        if (currentVacancy == 0)
            setAtMaxCapacity(true);
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
            setAtMaxCapacity(false);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Index " + indexNum + "  | " + "Group " + group + "\n" +
                "Vacancies: " + currentVacancy + "/" + indexVacancy + "\n");
        stringBuilder.append("Number of students currently in wait list: " + waitlist.size() + "\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Index)) {
            return false;
        }
        Index other = (Index) o;
        return other.getIndexNum().equals(indexNum);
    }


}
