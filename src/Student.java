import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * Definition of enumeration called typeOfGender, with 3 elements, referred to as:
 * typeOfGender.MALE, typeOfGender.FEMALE, typeOfGender.OTHER
 */
enum typeOfGender {
    MALE, FEMALE, OTHER
}

/**
 * Student represents a student enrolled in the university. It inherits from User.
 * A student is enrolled in one or more courses through indexes.
 * @author Toh Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
public class Student extends User {

    /** This student's matriculation number. It is unique. */
    private String matricNum;

    /** This student's gender (MALE / FEMALE / OTHER). */
    private typeOfGender gender;

    /** This student's nationality. */
    private String nationality;

    /**
     * This student's authorized access time to STARS. accessTime is an array of LocalDateTime of size 2.
     * <br>accessTime[0] refers to the start datetime, and access[1] refers to the end datetime.
     */
    private LocalDateTime[] accessTime;

    /** This student's max AUs possible for the semester. */
    private int maxAUs;

    /** This student's current AUs registered for the semester. */
    private int currentAUs;

    /** This student's major. */
    private String major;

    /**
     * This student's registered courses and the corresponding index for the semester, stored as a HashMap.
     * Key is the registered course, and value is the index registered by this student.
     */
    private HashMap<Course, Index> coursesRegistered;

    /**
     * This student's list of courses waiting to be registered, stored as a HashMap.
     * Key is the course to be registered, and value is the index registered by this student.
     */
    private HashMap<Course, Index> waitList;

    /**
     * Constructor for <code>Student</code>.<br>
     * <code>accessTime</code> and <code>coursesRegistered</code> cannot be passed into the constructor and must instead be manually set.
     * @param password This student's password (in plain text).
     * @param studentName This student's name.
     * @param matricNum This student's matriculation number. It is unique.
     * @param email This student's email.
     * @param gender This student's gender (MALE / FEMALE / OTHERS) as string.
     * @param nationality This student's nationality
     * @param maxAUs This student's AU balance.
     * @param major This student's major.
     */
    public Student(String userID, String password, String studentName, String matricNum, String email,
                   String gender, String nationality, String major, int maxAUs, String startAccessTime,
                   String endAccessTime) {
        super(userID, password, "Student", studentName, email);
        this.matricNum = matricNum;
        this.gender = typeOfGender.valueOf(gender);
        this.nationality = nationality;
        this.currentAUs = 0;
        this.maxAUs = maxAUs;
        this.major = major;
        this.accessTime = new LocalDateTime[2];
        setAccessTime(startAccessTime, endAccessTime);
        this.coursesRegistered = new HashMap<>();
        this.waitList = new HashMap<>();
    }

    public String getMatricNum() {
        return matricNum;
    }

    public void setMatricNum(String matricNum) {
        this.matricNum = matricNum;
        System.out.println("SUCCESS: New matriculation number saved.");
    }

    public typeOfGender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = typeOfGender.valueOf(gender);
        System.out.println("SUCCESS: New gender type saved.");
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
        System.out.println("SUCCESS: New nationality saved.");
    }

    public int getMaxAUs() {
        return maxAUs;
    }

    public void setMaxAUs(int maxAUs) {
        this.maxAUs = maxAUs;
    }

    public int getCurrentAUs() { return currentAUs; }

    /**
     * Updates this student's current AUs, based on courses this student has registered.<br>
     * This method is called whenever a modification to this student's courses registered is performed.
     */
    private void updateCurrentAUs() {
        currentAUs = 0;
        for (Course course : coursesRegistered.keySet()) {
            currentAUs += course.getAcademicUnits();
        }
    }

    public String getMajor() { return major; }

    public void setMajor(String major) {
        this.major = major;
    }

    public LocalDateTime[] getAccessTime() {
        return accessTime;
    }

    /**
     * Sets the access time for this student.
     * @param start the starting time as LocalDateTime for accessing STARS for this student
     * @param end the ending time as LocalDateTime for accessing STARS for this student
     */
    public void setAccessTime(String start, String end) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        accessTime[0] = LocalDateTime.parse(start, formatter);
        accessTime[1] = LocalDateTime.parse(end, formatter);
    }

    public HashMap<Course, Index> getCoursesRegistered() {
        return coursesRegistered;
    }

    /**
     * Retrieves an index registered by this student.
     * @param course The course object of the index to be retrieved
     * @return <code>Index</code> object if the corresponding course exists; null otherwise.
     */
    public Index retrieveIndex(Course course){
        return coursesRegistered.get(course);
    }

    /**
     * Adds a course into this student's registered courses for the semester.
     * @param course The object of the course to be added.
     * @param index The object of the index of the course to be added.
     */
    public void addCourse(Course course, Index index) {
        coursesRegistered.put(course, index);
        updateCurrentAUs();
    }

    /**
     * Removes a course from this student's registered courses.
     * @param course The object of the course to be removed.
     * @return <code>true</code> if the course code is removed.
     */
    public boolean removeCourse(Course course) {
        Index removedIndex = coursesRegistered.remove(course);
        if (removedIndex == null)
            return false;
        updateCurrentAUs();
        return true;
    }

    public HashMap<Course, Index> getWaitList() {
        return waitList;
    }

    /**
     * Adds a course to wait list.
     * @param course the object of the course to be added
     * @param index the object of the chosen index to be added
     */
    public void addCourseToWaitList(Course course, Index index) {
        waitList.put(course, index);
    }

    /**
     * Removes a course from wait list
     * @param course the object of the course to be removed
     * @return true if the removal is successful
     */
    public boolean removeCourseFromWaitList(Course course) {
        return waitList.remove(course) != null;
    }

    /**
     * Retrieves an index registered by this student, from this student's wait list.
     * @param course The course object of the index to be retrieved
     * @return <code>Index</code> object if the corresponding course exists; null otherwise.
     */
    public Index retrieveIndexFromWaitList(Course course) {
        return waitList.get(course);
    }

    /**
     * Compares this student with another student based on matriculation number.
     * @param o The other student to compare with this student.
     * @return <code>true</code> if the other student has the same matriculation number as this student.
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
        return other.getMatricNum().equals(this.matricNum);
    }

    @Override
    public boolean validateLogin(String checkID, String checkpw) throws AccessDeniedException {
        if (super.validateLogin(checkID, checkpw)) {
            if (LocalDateTime.now().isBefore(this.accessTime[0]) || LocalDateTime.now().isAfter(this.accessTime[1]))
                throw new AccessDeniedException("Access Denied: Outside of allocated time period\n" +
                        "Time period allocated is from " + this.accessTime[0] + " to " + this.accessTime[1]);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getName() + ", " + matricNum + "\n");
        stringBuilder.append(nationality + ", " + gender + "\n");
        stringBuilder.append("Major: " + major + "\n");
        stringBuilder.append("AUs registered: " + currentAUs + "\n");
        stringBuilder.append("Courses registered: \n");
        for (Course course : coursesRegistered.keySet()) {
            stringBuilder.append("- " + course.getCourseCode() + " (" + coursesRegistered.get(course).getIndexNum() + ")\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Displays course code, course name, course type, number of AUs, index number, and for each lesson in that index,
     * the lesson information.
     * KIV if to be placed here or handler
     */
    public void printRegisteredCoursesInfo() {
        System.out.println("Courses Registered For " + super.getName());
        for (Course course : coursesRegistered.keySet()) {
            System.out.println(course.getCourseCode() + ": " + course.getCourseName());
            System.out.println(course.getCourseType() + "\tAUs: " + course.getAcademicUnits());
            Index index = coursesRegistered.get(course);
            System.out.println("Index: " + index.getIndexNum());
            for (Lesson lesson : index.getLessons()) {
                System.out.println(lesson);
            }
        }
    }
}
