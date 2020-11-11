import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Course represents a course taught in the university (e.g. CZ2002 - OODP).
 * It consists of multiple indexes which have lessons of different timings.
 * The database of courses is maintained and obtained through <code>FileHandler</code>.
 * Enumeration for type of courses also included here.
 * @author Chong Shen Rui
 * @version 1.0
 * @since 2020-10-18
 */

/**
 * Definition of enumeration called typeOfCourse, with 4 elements, referred to as:
 * typeOfCourse.CORE, typeOfCourse.MPE, typeOfCourse.GER, typeOfCourse.UE
 */
enum typeOfCourse{
    CORE, MPE, GER, UE
}

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

    private LocalDateTime examDateTime;

    /**
     * Constructor for <code>Course</code>.
     * TODO maybe change exception throwing to static method getter for String version of enums. Can use contains()
     * @param courseCode Unique identifier for this course.
     * @param courseName This course's name.
     * @param courseType This course's type. Represented with <code>typeOfCourse</code> enumeration.
     * @param academicUnits This course's allocated academic units (AUs).
     * @param school The school that teaches this course.
     */
    public Course(String courseCode, String courseName, String courseType, int academicUnits, String school
                    , String examDateTimeInput) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.courseType = typeOfCourse.valueOf(courseType);
        this.academicUnits = academicUnits;
        this.school = school;
        this.indexes = new ArrayList<>();
        setExamDateTime(examDateTimeInput);
    }

    public LocalDateTime getExamDateTime(){return examDateTime;}

    public void setExamDateTime(String examDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        examDateTime = LocalDateTime.parse(examDateTimeString, formatter);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public typeOfCourse getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = typeOfCourse.valueOf(courseType);
    }

    public int getAcademicUnits() {
        return academicUnits;
    }

    public void setAcademicUnits(int academicUnits) {
        this.academicUnits = academicUnits;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public ArrayList<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(ArrayList<Index> indexes) {
        this.indexes = indexes;
    }

    public void addIndex(String indexNum, int indexVacancy, String group) {
        Index newIndex = new Index(indexNum, indexVacancy, group);
        indexes.add(newIndex);
    }

    public Index getIndex(String indexCode) {
        for (Index index : indexes)
            if (index.getIndexNum().equals(indexCode))
                return index;
        return null;
    }

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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(courseName + ", " + courseCode + "\n");
        stringBuilder.append("Course Type: " + courseType + "\n");
        stringBuilder.append("Academic Units: " + academicUnits + "\n");
        stringBuilder.append("School: " + school + "\n");
        return stringBuilder.toString();
    }
}

