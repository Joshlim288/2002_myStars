
import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;

public class AdminHandler{
    Scanner sc = new Scanner(System.in);
    Admin currentAdmin;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }
    /**
     * Edits the course details
     * @param  courseCode, courseName,courseType, academicUnits,school, indexes
     * Retrieve the course based on coursecode. Then edits and saves the new details
     */
    public void editCourse(String courseCode, String courseName,typeOfCourse courseType, int academicUnits, String school, ArrayList<Index> indexes){
        Course course = FileHandler.getCourse(courseCode);
        course.setCourseName(courseName);
        course.setCourseType(courseType);
        course.setAcademicUnits(academicUnits);
        course.setSchool(school);
        course.setIndexes(indexes);

    }

    public void addCourse(String courseCode, String courseName,typeOfCourse courseType, int academicUnits, String school, ArrayList<Index> indexes){
        Course newcourse = new Course(courseCode, courseName,courseType, academicUnits,school, indexes);
        FileHandler.addCourse(newcourse);
    }

    /**
     * Check the number of available slot for an index number
     * @param  course, indexnum
     * @return int number of vacancys
     */
    public int checkSlot(String course, int indexnum){
        Index index = FileHandler.getCourse(course).searchIndex(indexnum);
        return index.getCurrentVacancy();
    }

    /**
     * Prints student list in the index specified by the admin
     * @param indexnum
     */
    public ArrayList<Student> printStudentListbyIndex(String coursecode, int indexnum){         /*getStudentList()*/
        ArrayList<Course> courselist = FileHandler.getCourseList();
        ArrayList<Student> studentlistbyindex = new ArrayList<>();
        for (Course coursei : courselist){
            if (coursei.getCourseCode()==coursecode) {
                studentlistbyindex.addAll(coursei.searchIndex(indexnum).getEnrolledStudents());
                break;
            }
        }
        return studentlistbyindex;
        }

    /**
     * Prints student list in the index specified by the admin
     * @param coursecode
     * @returns array list of students
     */
    public ArrayList<Student> printStudentListbyCourse(String coursecode){         /*getStudentList()*/
        ArrayList<Index> indexlist = FileHandler.getCourse(coursecode).getIndexes();
        ArrayList<Student> studentlistbycourse = new ArrayList<>();
        for (Index indexi: indexlist){
            studentlistbycourse.addAll(indexi.getEnrolledStudents());
        }
        return studentlistbycourse;

    }

    public void editAccessPeriod(String matricNum, LocalDateTime start, LocalDateTime end){
        Student student = FileHandler.getStudent(matricNum);
        student.setAccessTime(start, end);

    }

    public void addStudent(Student student){
        FileHandler.getStudentList().add(student);

    }
    /**
     * Check if the matric number is available.
     * @param matric
     * @returns false if matric number already exist
     */
    public boolean checkmatricexist(String matric){
        for(int i =0;i<FileHandler.getStudentList().size();i++)
        {
            if(FileHandler.getStudentList().get(i).getMatricNum().equals(matric))
            {
                System.out.println("Matriculation number already exists!");
                return true;
            }
        }
        return false;
    }

    public typeOfCourse choosecoursetype(int useropt){
        switch (useropt){
            case 1:
                return typeOfCourse.CORE;
            case 2:
                return typeOfCourse.MPE;
            case 3:
                return typeOfCourse.GER;
            case 4:
                return typeOfCourse.UE;
            default:
                return typeOfCourse.CORE;
        }
    }
}
