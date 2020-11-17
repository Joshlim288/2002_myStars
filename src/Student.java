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
     * Key is the registered course's code, and value is the index number registered by this student.
     */
    private HashMap<String, String> coursesRegistered;

    /**
     * This student's list of courses waiting to be registered, stored as a HashMap.
     * Key is the course code to be registered, and value is the index number registered by this student.
     */
    private HashMap<String, String> waitList;

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

    /**
     * @return Matriculation number of the Student
     */
    public String getMatricNum() {
        return matricNum;
    }

    /**
     * @param matricNum Matriculation number to change to
     */
    public void setMatricNum(String matricNum) {
        this.matricNum = matricNum;
        System.out.println("SUCCESS: New matriculation number saved.");
    }

    /**
     * @return Gender of the Student
     */
    public typeOfGender getGender() {
        return gender;
    }

    /**
     * @param gender Gender to change the Student to
     */
    public void setGender(String gender) {
        this.gender = typeOfGender.valueOf(gender);
        System.out.println("SUCCESS: New gender type saved.");
    }

    /**
     * @return Nationality of the Student as a String
     */
    public String getNationality() {
        return nationality;
    }

    /**
     * @param nationality Nationality to change the Student to
     */
    public void setNationality(String nationality) {
        this.nationality = nationality;
        System.out.println("SUCCESS: New nationality saved.");
    }

    /**
     * @return Maximum Academic Units the Student is allowed to take
     */
    public int getMaxAUs() {
        return maxAUs;
    }

    /**
     * @param maxAUs Maximum Academic Units to allow the Student to take
     */
    public void setMaxAUs(int maxAUs) {
        this.maxAUs = maxAUs;
    }

    /**
     * @return Number of Academic Units that the Student is currently taking
     */
    public int getCurrentAUs() { return currentAUs; }

    /**
     * @return Major that the Student is taking
     */
    public String getMajor() { return major; }

    /**
     * @param major Major to change the Student to
     */
    public void setMajor(String major) {
        this.major = major;
    }

    /**
     * @return Array of 2 LocalDateTime objects that indicate when the Student is allowed to access the system
     * <ul>
     *     <li>LocalDateTime[0]: the start time as LocalDateTime for accessing STARS for this student</li>
     *     <li>LocalDateTime[1]: the end time as LocalDateTime for accessing STARS for this student</li>
     * </ul>
     */
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

    public HashMap<String, String> getCoursesRegistered() {
        return coursesRegistered;
    }

    /**
     * Retrieves an index registered by this student.
     * @param course The course code of the index to be retrieved
     * @return <code>Index</code> object if the corresponding course exists; null otherwise.
     */
    public String retrieveIndex(String course){
        return coursesRegistered.get(course);
    }

    /**
     * Adds a course into this student's registered courses for the semester.
     * @param course The object of the course to be added.
     * @param index The object of the index of the course to be added.
     */
    public void addCourse(String course, String index, int AUs) {
        coursesRegistered.put(course, index);
        currentAUs += AUs;
    }

    /**
     * Removes a course from this student's registered courses.
     * @param course The object of the course to be removed.
     * @return <code>true</code> if the course code is removed.
     */
    public boolean removeCourse(String course, int AUs) {
        String removedIndex = coursesRegistered.remove(course);
        if (removedIndex == null)
            return false;
        currentAUs -= AUs;
        return true;
    }

    /**
     * @return waitList of the Student, as a HashMap with key CourseCode, and value indexNum
     */
    public HashMap<String, String> getWaitList() {
        return waitList;
    }

    /**
     * Adds a course to wait list.
     * @param course the code of the course to be added
     * @param index the number of the chosen index to be added
     */
    public void addCourseToWaitList(String course, String index) {
        waitList.put(course, index);
    }

    /**
     * Removes a course from wait list
     * @param course the code of the course to be removed
     * @return true if the removal is successful
     */
    public boolean removeCourseFromWaitList(String course) {
        return waitList.remove(course) != null;
    }

    /**
     * Retrieves an index registered by this student, from this student's wait list.
     * @param course The course code of the index to be retrieved
     * @return <code>Index</code> object if the corresponding course exists; null otherwise.
     */
    public String retrieveIndexFromWaitList(String course) {
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

    /**
     * Overriden login validation from the User parent class
     * Uses the implementation of User's validate login, but also checks for whether the Student is allowed to log in
     * at the current time
     * @param checkID userID to check against this Student's userID
     * @param checkpw unhashed password to check against this Student's userID
     * @return true if login is allowed, false if userID does not match
     * @throws AccessDeniedException thrown when userID and password matches with this object, but it is currently
     * outside of the allocated access period
     */
    @Override
    public boolean validateLogin(String checkID, String checkpw) throws AccessDeniedException {
        if (super.validateLogin(checkID, checkpw)) {
            if (LocalDateTime.now().isBefore(this.accessTime[0]) || LocalDateTime.now().isAfter(this.accessTime[1])) {
                String startTime = this.accessTime[0].toString().replace("T", " ");
                String endTime = this.accessTime[1].toString().replace("T", " ");
                throw new AccessDeniedException("Access Denied: Outside of allocated time period\n" +
                        "Time period allocated is from " + startTime + " to " + endTime);
            }
            return true;
        }
        return false;
    }

    /**
     * @return Student details as a String, suitable for printing
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.getName() + ", " + matricNum + "\n");
        stringBuilder.append(nationality + ", " + gender + "\n");
        stringBuilder.append("Major: " + major + "\n");
        stringBuilder.append("AUs registered: " + currentAUs + "\n");
        stringBuilder.append("Courses registered: \n");
        for (String course : coursesRegistered.keySet()) {
            stringBuilder.append("- " + course + " (" + coursesRegistered.get(course) + ")\n");
        }
        return stringBuilder.toString();
    }
}
