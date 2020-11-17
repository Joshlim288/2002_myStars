import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Definition of enumeration called typeOfCourse, with 4 elements, referred to as:
 * typeOfCourse.CORE, typeOfCourse.MPE, typeOfCourse.GER, typeOfCourse.UE
 */
enum typeOfCourse{
    CORE, MPE, GER, UE
}

/**
 * Course represents a course taught in the university (e.g. CZ2002 - OODP).
 * It consists of multiple indexes which have lessons of different timings.
 * The database of courses is maintained and obtained through <code>FileHandler</code>.
 * Enumeration for type of courses also included here.
 *
 * To see the format of all the data fields, refer to CourseVaildator
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.7
 * @since 1.0
 */
public class Course implements Serializable {

    /**
     * Uniquely identifies the course.
     */
    private String courseCode;

    /**
     * This course's name.
     */
    private String courseName;

    /**
     * This course's type (e.g. CORE/MPE/UE)
     * Represented with <code>typeOfCourse</code> enumeration.
     */
    private typeOfCourse courseType;

    /**
     * AUs allocated for this course.
     */
    private int academicUnits;

    /**
     * The school that teaches this course.
     */
    private String school;

    /**
     * Indexes under this course.
     * Stored as an ArrayList of indexes to allow for flexibility of modification.
     */
    private ArrayList<Index> indexes;

    /**
     * Datetime for the final exam for this Course
     * Contains 2 elements:
     * <code>LocalDateTime[0]</code> is the start datetime
     * <code>LocalDateTime[1]</code> is the end datetime
     */
    private LocalDateTime[] examDateTime;

    /**
     * Constructor for <code>Course</code>.
     * @param courseCode Unique identifier for this course.
     * @param courseName This course's name.
     * @param courseType This course's type. Represented with <code>typeOfCourse</code> enumeration.
     * @param academicUnits This course's allocated academic units (AUs).
     * @param school The school that teaches this course.
     */
    public Course(String courseCode, String courseName, String courseType, int academicUnits, String school
                    , String examStart, String examEnd) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseType = typeOfCourse.valueOf(courseType);
        this.academicUnits = academicUnits;
        this.school = school;
        this.indexes = new ArrayList<>();
        this.examDateTime = new LocalDateTime[2];
        setExamDateTime(examStart, examEnd);
    }

    /**
     * @return ExamDateTime array that contains 2 elements:
     * index 0 contains start datetime
     * index 1 contains end datetime
     */
    public LocalDateTime[] getExamDateTime(){return examDateTime;}

    /**
     * Sets exam datetime for a Course
     * check CourseVaildator for the format
     * @param examStart as a String representing the datetime for the start of the exam
     *                  if null is passed in, exams will be removed
     * @param examEnd as a String representing the datetime for the end of the exam
     */
    public void setExamDateTime(String examStart, String examEnd) {
        // remove exams for the Course if null is passed in
        if (examStart == null) {
            examDateTime[0] = null;
            examDateTime[1] = null;
        }
        // edit exams for the Course
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            examDateTime[0] = LocalDateTime.parse(examStart, formatter);
            examDateTime[1] = LocalDateTime.parse(examEnd, formatter);
        }
    }

    /**
     * @return Course code for the Course
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @param courseCode Course code to change to, check CourseVaildator for the format
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * @return Name of the Course
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * @param courseName Name to change to
     */
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    /**
     * @return Type of Course, refer to above enum for possible values
     */
    public typeOfCourse getCourseType() {
        return courseType;
    }

    /**
     * @param courseType to change to, check CourseVaildator for the format
     */
    public void setCourseType(String courseType) {
        this.courseType = typeOfCourse.valueOf(courseType);
    }

    /**
     * @return Academic Units the Course is worth
     */
    public int getAcademicUnits() {
        return academicUnits;
    }

    /**
     * @param academicUnits to change to
     */
    public void setAcademicUnits(int academicUnits) {
        this.academicUnits = academicUnits;
    }

    /**
     * @return School as a String, represents which School the Course belongs to
     */
    public String getSchool() {
        return school;
    }

    /**
     * @param school to change to
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * @return Indexes as an ArrayList of Index objects that belong to the Course
     */
    public ArrayList<Index> getIndexes() {
        return indexes;
    }

    /**
     * @param indexes to change to, replaces the entire list
     */
    public void setIndexes(ArrayList<Index> indexes) {
        this.indexes = indexes;
    }

    /**
     * Creates and adds a new Index to the Course
     * @param indexNum Index number of the new Index to be created
     * @param indexVacancy Number of vacancies for Students that this Index has
     * @param group Used for more intuitive identification of an Index
     */
    public void addIndex(String indexNum, int indexVacancy, String group) {
        Index newIndex = new Index(indexNum, indexVacancy, group);
        indexes.add(newIndex);
    }

    /**
     * Retrieves an Index from the list of Indexes in this Course
     * @param indexCode index code to search for
     * @return Index object with a code that matches
     */
    public Index getIndex(String indexCode) {
        for (Index index : indexes)
            if (index.getIndexNum().equals(indexCode))
                return index;
        return null;
    }

    /**
     * Checks if two Course objects refer to the same admin
     * Compares only the courseCode, which uniquely identifies a Course
     * @param o Object to compare this to
     * @return false if the object is not an instance of Course, or does not have the same staffNum
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        Course other = (Course) o;
        return other.getCourseCode().equals(courseCode);
    }

    /**
     * @return Course details as a String, suitable for printing
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(courseName + ", " + courseCode + "\n");
        stringBuilder.append("Course Type: " + courseType + "\n");
        stringBuilder.append("Academic Units: " + academicUnits + "\n");
        stringBuilder.append("School: " + school + "\n");
        if (examDateTime[0] != null)
            stringBuilder.append("Exam Date: " + examDateTime[0].format(formatter) + " to " +
                    examDateTime[1].format(formatter) + "\n");
        else
            stringBuilder.append("No examinations for this course.\n");
        return stringBuilder.toString();
    }
}

