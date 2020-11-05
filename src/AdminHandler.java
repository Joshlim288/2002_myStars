
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Creates objects and handles logic for Admin actions
 */
public class AdminHandler{
    Scanner sc = new Scanner(System.in);
    Admin currentAdmin;
    CourseDataManager cdm;
    StudentDataManager sdm;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
        this.cdm = new CourseDataManager();
        this.sdm = new StudentDataManager();
        cdm.load();
        sdm.load();
    }
    /**
     * Edits the course details
     * @param  courseCode, courseName,courseType, academicUnits,school, indexes
     * Retrieve the course based on coursecode. Then edits and saves the new details
     */
    public void editCourse(String courseCode, String courseName,String courseType, int academicUnits, String school, ArrayList<Index> indexes){
        Course course = cdm.getCourse(courseCode);
        course.setCourseName(courseName);
        course.setCourseType(courseType);
        course.setAcademicUnits(academicUnits);
        course.setSchool(school);
        course.setIndexes(indexes);

    }

    public boolean addCourse(String courseCode, String courseName,String courseType, int academicUnits, String school){
        try {
            Course newCourse = new Course(courseCode, courseName,courseType, academicUnits,school);
            return (cdm.addCourse(newCourse));
        } catch (ObjectCreationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addIndex(String courseCode, int indexNum, int indexVacancies) {
        try {
            cdm.getCourse(courseCode).addIndex(indexNum, indexVacancies);
            return true;
        } catch (ObjectCreationException e) {
            cdm.removeCourse(courseCode);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addLesson(String courseCode, int indexNum, String lessonType, String group, String day,
                          LocalTime startTime, LocalTime endTime, String venue, ArrayList<Integer>teachingWeeks) {
        try {
            cdm.getCourse(courseCode).getIndex(indexNum).addLesson(lessonType, group, day, startTime, endTime, venue, teachingWeeks);
            return true;
        } catch (ObjectCreationException e) {
            cdm.removeCourse(courseCode);
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Check the number of available slot for an index number
     * @param  course, indexnum
     * @return int number of vacancys
     */
    public int checkSlot(String course, int indexnum){
        Index index = cdm.getCourse(course).getIndex(indexnum);
        if (index == null) {
            System.out.println("Index not found");
            return -1;
        }
        return index.getCurrentVacancy();
    }

    /**
     * Prints student list in the index specified by the admin
     * @param indexNum
     */
    public ArrayList<Student> getStudentListByIndex(String courseCode, int indexNum){         /*getStudentList()*/
        try {
            Course foundCourse = cdm.getCourse(courseCode);
            Index foundIndex = foundCourse.getIndex(indexNum);
            return foundIndex.getEnrolledStudents();
        } catch (NullPointerException e) {
            System.out.println("Could not find course or index");
            return null;
        }
    }

    /**
     * Prints student list in the index specified by the admin
     * @param courseCode
     * @returns array list of students
     */
    public ArrayList<Student> getStudentListByCourse(String courseCode){         /*getStudentList()*/
        try {
            ArrayList<Index> courseIndexes = cdm.getCourse(courseCode).getIndexes();
            ArrayList<Student> courseStudents = new ArrayList<>();
            for (Index idx : courseIndexes) {
                courseStudents.addAll(idx.getEnrolledStudents());
            }
            return courseStudents;
        } catch (NullPointerException e) {
            System.out.println("Could not find course");
            return null;
        }

    }

    public LocalDateTime[] getAccessPeriod(String matricNum){
        return sdm.getStudent(matricNum).getAccessTime();
    }

    public boolean editAccessPeriod(String matricNum, String start, String end){
        // check start time < end time
        sdm.getStudent(matricNum).setAccessTime(start, end);
        return true;
    }

    public boolean addStudent(String userid, String password, String studentName, String studentMatric, String email,
                           String gender, String nationality, String major, int maxAUs){
        try {
            Student newStudent = new Student(userid, password, studentName, studentMatric, email,
                    gender, nationality, major, maxAUs);
            sdm.getStudentList().add(newStudent);
            System.out.println("Student "+studentName+" has been added successfully!");
            return true;
        } catch (ObjectCreationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void close() {
        cdm.save();
        sdm.save();
    }
}
