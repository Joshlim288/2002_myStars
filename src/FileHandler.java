import java.util.ArrayList;

/**
 * Feels too much like functional programming but we can discuss later
 */
public class FileHandler {
    private static ArrayList<Course> courseList;
    private static ArrayList<Student> studentList;
    private static ArrayList<Admin> adminList;

    /**
     * Read in the lists
     */
    public static void initialize(){

    }

    /**
     * Save edited lists back to file
     */
    public static void close(){

    }


    public static Student getStudent(String matricNum) {
        return null;
    }

    public static boolean removeStudent(String matricNum) {
        return false;
    }

    public static boolean addStudent(Student newStud) {
        return false;
    }

    public static ArrayList<Student> getStudentList(){
        return null;
    }



    public static Admin getAdmin(String idkwhattoget) {
        return null;
    }

    public static boolean removeAdmin(String idkwhattoget) {
        return false;
    }

    public static boolean addAdmin(Admin newAdmin) {
        return false;
    }

    public static ArrayList<Admin> getAdminList(){
        return null;
    }



    public static Course getCourse(String courseCode) {
        return null;
    }

    public static boolean removeCourse(String courseCode) {
        return false;
    }

    public static boolean addCourse(Course newCourse) {
        return false;
    }

    public static ArrayList<Course> getCourseList(){
        return null;
    }
}
