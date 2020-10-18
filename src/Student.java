import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Student represents a student enrolled in the university. It inherits from User.
 * A student is enrolled in one or more courses through indexes.
 * @author Toh Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */

public class Student extends User {

    /**
     * This student's name.
     */
    private String studentName;

    /**
     * This student's matriculation number. It is unique.
     */
    private String matricNum;

    /**
     * This student's authorized access time to STARS. accessTime is an array of LocalDateTime of size 2.
     * <br>accessTime[0] refers to the start datetime, and access[1] refers to the end datetime.
     */
    private LocalDateTime[] accessTime;

    /**
     * This student's AU balance.
     */
    private int AUBalance;

    /**
     * This student's registered courses for the semester.
     */
    private ArrayList<Course> coursesRegistered;

    /**
     * This student's major.
     */
    private String major;

    /**
     * Constructor for <code>Student</code>.<br>
     * <code>accessTime</code> >and <code>coursesRegistered</code> cannot be passed into the constructor and must instead be manually set.
     * @param userID This student's user ID.
     * @param hashedPassword This student's password (hashed).
     * @param studentName This student's name.
     * @param matricNum This student's matriculation number. It is unique.
     * @param AUBalance This student's AU balance.
     * @param major This student's major.
     */
    public Student(String userID, String hashedPassword, String studentName, String matricNum, int AUBalance, String major) {
        super(userID, hashedPassword);
        this.studentName = studentName;
        this.matricNum = matricNum;
        this.AUBalance = AUBalance;
        this.major = major;
        this.accessTime = new LocalDateTime[2];
        this.coursesRegistered = new ArrayList<Course>();
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getMatricNum() {
        return matricNum;
    }

    public void setMatricNum(String matricNum) {
        this.matricNum = matricNum;
    }

    public int getAUBalance() {
        return AUBalance;
    }

    public void setAUBalance(int AUBalance) {
        this.AUBalance = AUBalance;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public LocalDateTime[] getAccessTime() {
        return accessTime;
    }

    public ArrayList<Course> getCoursesRegistered() {
        return coursesRegistered;
    }

    /**
     * Adds a course into this student's registered courses for the semester.
     * @param course The course to be added.
     */
    public void addCourse(Course course) {
        coursesRegistered.add(course);
    }

    /**
     * Displays the courses currently registered by this student.
     */
    public void printCoursesRegistered() {
        if (!coursesRegistered.isEmpty()) {
            // print course
        }
    }

    public void setAccessTime(LocalDateTime start, LocalDateTime end) {
        accessTime[0] = start;
        accessTime[1] = end;
    }

    /**
     * Removes a course from this student's registered courses.
     * @param course The course to be removed.
     * @return <code>true</code> if the course is removed.
     */
    public boolean removeCourse(Course course) {
        return coursesRegistered.remove(course);
    }

    /**
     * Compares this student with another student based on matriculation number.
     * @param o The other student to compare with this student.
     * @return true if the other student has the same matriculation number as this student.
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return false;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student other = (Student) o;
        return other.getMatricNum() == this.matricNum;
    }

}
