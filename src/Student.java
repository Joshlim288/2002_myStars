import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Student represents a student enrolled in the university. It inherits from User.
 * A student is enrolled in one or more courses through indexes.
 * @author Toh Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
//TODO: add validations and error messages

/**
 * Definition of enumeration called typeOfGender, with 3 elements, referred to as:
 * typeOfGender.MALE, typeOfGender.FEMALE, typeOfGender.OTHER
 */
enum typeOfGender {
    MALE, FEMALE, OTHER
}

public class Student extends User {

    /** This student's name. */
    private String studentName;

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

    /**
     * This student's registered courses and the corresponding index for the semester, stored as a HashMap.
     * Key is the registered course, and value is the index registered by this student.
     */
    private HashMap<Course, Index> coursesRegistered;

    /** This student's major. */
    private String major;

    /**
     * Constructor for <code>Student</code>.<br>
     * <code>accessTime</code> and <code>coursesRegistered</code> cannot be passed into the constructor and must instead be manually set.
     * @param userID This student's user ID.
     * @param hashedPassword This student's password (hashed).
     * @param studentName This student's name.
     * @param matricNum This student's matriculation number. It is unique.
     * @param gender This student's gender (MALE / FEMALE / OTHERS).
     * @param nationality This student's nationality
     * @param maxAUs This student's AU balance.
     * @param major This student's major.
     */
    public Student(String userID, String hashedPassword, String studentName, String matricNum,
                   typeOfGender gender, String nationality, int maxAUs, String major) {
        super(userID, hashedPassword, "Student");
        this.studentName = studentName;
        this.matricNum = matricNum;
        this.gender = gender;
        this.nationality = nationality;
        this.currentAUs = 0;
        this.maxAUs = maxAUs;
        this.major = major;
        this.accessTime = new LocalDateTime[2];
        this.coursesRegistered = new HashMap<Course, Index>();
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        if (validateStudentName(studentName))
            this.studentName = studentName;
    }

    public String getMatricNum() {
        return matricNum;
    }

    public void setMatricNum(String matricNum) {
        if (validateMatricNum(matricNum))
            this.matricNum = matricNum;
    }

    public typeOfGender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (validateGender(gender))
            switch (gender) {
                case ("MALE") -> this.gender = typeOfGender.MALE;
                case ("FEMALE") -> this.gender = typeOfGender.FEMALE;
                case ("OTHER") -> this.gender = typeOfGender.OTHER;
            }
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        if (validateNationality(nationality))
            this.nationality = nationality;
    }

    public int getMaxAUs() {
        return maxAUs;
    }

    public void setMaxAUs(int maxAUs) {
        if (validateMaxAUs(maxAUs))
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
        if (validateMajor(major))
            this.major = major;
    }

    public LocalDateTime[] getAccessTime() {
        return accessTime;
    }

    /**
     * Sets the access time for this student.
     * @param start the starting time as String for accessing STARS for this student
     * @param end the ending time as String for accessing STARS for this student
     */
    public void setAccessTime(String start, String end) {
        LocalDateTime startTime = validateAndConvertTime(start);
        LocalDateTime endTime = validateAndConvertTime(end);
        if (startTime != null && endTime != null) {
            accessTime[0] = startTime;
            accessTime[1] = endTime;
        }
    }

    /** Retrieves the course codes of the courses registered by this student, in the form of an ArrayList */
    public ArrayList<Course> getCoursesRegistered() {
        return new ArrayList(coursesRegistered.keySet());
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
        return other.getMatricNum() == this.matricNum;
    }

    @Override
    public boolean validate(String checkID, String checkpw) throws AccessDeniedException {
        if(LocalDateTime.now().isBefore(this.accessTime[0]) || LocalDateTime.now().isAfter(this.accessTime[1]))
            throw new AccessDeniedException("Access Denied: Outside of allocated time period\n"+
                    "Time period allocated is from "+ this.accessTime[0] + " to " + this.accessTime[1]);
        return super.validate(checkID, checkpw);
    }

    private boolean validateStudentName(String studentName) {
        if (studentName.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Name can only contain alphabets and spaces.");
        return false;
    }

    /**
     * Validates matriculation number. Assumes format as:
     * <ul>
     *     <li>Total length of matriculation number is 9.</li>
     *     <li>First and last characters are any letters from A-Z (capital).</li>
     *     <li>Middle 7 characters can be any combination of digits from 0-9.</li>
     * </ul>
     * @param matricNum
     * @return
     */
    private boolean validateMatricNum(String matricNum) {
        if (matricNum.matches("^[A-Z][0-9]{7}[A-Z]$")) {
            return true;
        }
        System.out.println("ERROR: Invalid matriculation number format.");
        return false;
    }

    private boolean validateGender(String gender) {
        for (typeOfGender genderType : typeOfGender.values()) {
            if (genderType.name().equals(gender)) {
                return true;
            }
        }
        System.out.println("ERROR: Gender can only be MALE / FEMALE / OTHER.");
        return false;
    }

    private boolean validateNationality(String nationality) {
        if (nationality.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Nationality can only contain alphabets and spaces.");
        return false;
    }

    /**
     * Validates a string representing a datetime is in the correct format (YYYY-MM-DD HH:MM:SS, 24H clock)
     * @param dateTime the string representing a datetime
     * @return the datetime as a LocalDateTime object, null if the string is invalid
     */
    private LocalDateTime validateAndConvertTime(String dateTime) {
        if (dateTime.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}")) { // check format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try {
                LocalDateTime validDateTime = LocalDateTime.parse(dateTime, formatter);
                return validDateTime;
            } catch (DateTimeParseException e) {
                System.out.println("ERROR: Please enter a valid date and time.");
                return null;
            }
        } else {
            System.out.println("ERROR: Datetime must be in the format YYYY-MM-DD HH:MM:SS (24H Time)");
            return null;
        }
    }

    /**
     * Assumes new maxAUs are already in int type.
     * @param maxAUs
     * @return
     */
    private boolean validateMaxAUs(int maxAUs) {
        if (maxAUs >= currentAUs) {
            return true;
        }
        System.out.println("ERRORS: Max AUs cannot be fewer than current AUs.");
        return false;
    }

    private boolean validateMajor(String major) {
        if (major.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Major can only contain alphabets and spaces.");
        return false;
    }
}
