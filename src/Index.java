import java.io.Serializable;
import java.util.ArrayList;

/**
 * Index represents the actual schedules for each course.
 * There can be multiple indexes per course, where the timings of the lessons may differ (except for LEC).
 * Contains an ArrayList of Lesson.
 * Contains two ArrayList of Student, one for students registered, the other for students in waitlist.
 * @author Josh, Jun Wei, Chong Shen Rui, Joshua, Daryl
 * @version 1.2
 * @since 2020-10-18
 */
public class Index implements Serializable {

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
     * Is a calculated field, and thus does not have a setter
     * Will be automatically updated when the relevant methods are called
     */
    private int currentVacancy;

    /**
     * Boolean variable showing if index is at max capacity
     * Is a calculated field, and thus does not have a setter
     * Will be automatically updated whent he relevant methods are called
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
    private ArrayList<String> waitlist;

    /**
     * Contains all students currently registered in this index
     * Each student can only be registered in one index for each course.
     */
    private ArrayList<String> enrolledStudents;

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

    /**
     * @return Index Number of the Index object
     */
    public String getIndexNum() {
        return indexNum;
    }

    /**
     * @param indexNum Index Number to change to
     */
    public void setIndexNum(String indexNum) {
        this.indexNum = indexNum;
    }

    /**
     * @return Number of vacancies the Course has in total as an int
     */
    public int getIndexVacancy() {
        return indexVacancy;
    }

    /**
     * Current vacancy of the Course is automatically updated
     * @param newVacancy New number of vacancies the Course should have in total
     */
    public void setIndexVacancy(int newVacancy) {
        this.currentVacancy += newVacancy - indexVacancy;
        this.indexVacancy = newVacancy;
        this.atMaxCapacity = (currentVacancy == 0);
    }

    /**
     * @return Current number of Vacancies in the Index as an int
     */
    public int getCurrentVacancy() {
        return currentVacancy;
    }

    /**
     * @return true if Index is at max capcity, false otherwise
     */
    public boolean isAtMaxCapacity() {
        return atMaxCapacity;
    }

    /**
     * @return Group name of the Index
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group that the Group name of the Index is to be changed to
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return Lessons as an ArrayList of Lesson objects that the index currently has
     */
    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    /**
     * Creates and adds a new Lesson object into the list of Lessons the index has
     * @param type The Type of the new Lessons
     * @param day Day the new Lesson is conducted on
     * @param startTime Start time for the new Lesson
     * @param endTime End time for the new Lesson
     * @param venue Venue the new Lesson is conducted at
     * @param teachingWeeks Teaching weeks that the Lesson is conducted on
     */
    public void addLesson(String type, String day, String startTime, String endTime,
                          String venue, ArrayList<Integer> teachingWeeks) {
        Lesson newLesson = new Lesson(type, day, startTime, endTime, venue, teachingWeeks);
        lessons.add(newLesson);
    }

    /**
     * @return Waitlist as an ArrayList of Matriculation Numbers (Strings)
     */
    public ArrayList<String> getWaitlist() {
        return waitlist;
    }

    /**
     * Method to add a student to current waitlist
     * Student added to the back of the queue.
     * @param matricNum Matriculation number of the Student to be added
     */
    public void addToWaitlist(String matricNum){
        waitlist.add(matricNum);
    }

    /**
     * Method to remove a student from current waitlist.
     * Student at front of queue removed
     * Exception handling if waitlist is already empty
     */
    public String removeFromWaitlist(){
        try{
            return waitlist.remove(0);
        }
        catch(IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * Removes a Specified Student from the waitlist
     * @param matricNum Matriculation Number of Student to be removed from the waitlist
     * @return Matriculation number of Student removed if successful, null otherwise
     */
    public String removeFromWaitlist(String matricNum){
        if (waitlist.remove(matricNum))
            return matricNum;
        return null;
    }

    /**
     * @return EnrolledStudents as an ArrayList of Matriculation Numbers (Strings)
     */
    public ArrayList<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    /**
     * @param matricNum Matriculation number of the Student to be added to enrolledStudents
     */
    public void addToEnrolledStudents(String matricNum){
        enrolledStudents.add(matricNum);
        currentVacancy--;
        if (currentVacancy == 0)
            this.atMaxCapacity = true;
    }

    /**
     *
     * @param matricNum Matriculation number of the Student to be removed from enrolledStudents
     */
    public void removeFromEnrolledStudents(String matricNum){
            enrolledStudents.remove(matricNum);
            this.currentVacancy++;
            this.atMaxCapacity = false;
    }

    /**
     * @return Index details as a String, suitable for printing
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Index " + indexNum + "  |  " + "Group " + group + "\n" +
                "Remaining Vacancies: " + currentVacancy + "/" + indexVacancy + "\n");
        stringBuilder.append("Number of students currently in wait list: " + waitlist.size() + "\n");
        return stringBuilder.toString();
    }

    /**
     * Checks if two Index objects refer to the same Index
     * Compares only the staffNum, which uniquely identifies an Index
     * @param o Object to compare this to
     * @return false if the object is not an instance of Index, or does not have the same indexNum
     */
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
