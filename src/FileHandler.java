import java.io.*;
import java.util.ArrayList;
//Feels too much like functional programming but we can discuss later

//TODO: Course methods, password handlers, decide if entities need Serializable
/**
 * FileHandler simulates a database by reading in all data stored.<br>
 * Objects can call FileHandler methods to retrieve their desired data.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
public class FileHandler {
    /**
     * ArrayList holding all course objects.
     */
    private static ArrayList<Course> courseList;

    /**
     * ArrayList holding all student objects.
     */
    private static ArrayList<Student> studentList;

    /**
     * ArrayList holding all admin objects.
     */
    private static ArrayList<Admin> adminList;

    /**
     * Load all saved objects
     */
    public static void initialize(){
        System.out.println("Loading data...");
        loadCourses();
        loadStudents();
        loadAdmins();

        if (courseList == null || studentList == null || adminList == null) {
            System.out.println("Error(s) occured during initialization.");
        } else {
            System.out.println("All data loaded successfully.");
        }
    }

    private static void loadCourses() {
        try {
            FileInputStream fileIn = new FileInputStream("data/courseData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            courseList = (ArrayList<Course>) in.readObject();
            if (courseList.isEmpty()) {
                System.out.println("...course data loaded but is empty");
            } else {
                System.out.println("...course data loaded");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadStudents() {
        try {
            FileInputStream fileIn = new FileInputStream("data/studentData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            studentList = (ArrayList<Student>) in.readObject();
            if (studentList.isEmpty()) {
                System.out.println("...student data loaded but is empty");
            } else {
                System.out.println("...student data loaded");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadAdmins() {
        try {
            FileInputStream fileIn = new FileInputStream("data/adminData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            adminList = (ArrayList<Admin>) in.readObject();
            if (adminList.isEmpty()) {
                System.out.println("...admin data loaded but is empty");
            } else {
                System.out.println("...admin data loaded");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save edited lists back to file
     */
    public static void close(){ // can consider setting all lists to null
        System.out.println("Saving data...");
        saveCourses();
        saveStudents();
        saveAdmins();
    }

    private static void saveCourses() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/courseData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(courseList);
            out.close();
            fileOut.close();
            System.out.println("... course data saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveStudents() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/studentData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(studentList);
            out.close();
            fileOut.close();
            System.out.println("... student data saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveAdmins() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/adminData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(adminList);
            out.close();
            fileOut.close();
            System.out.println("... admin data saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a student object from studentList based on matriculation number.
     * @param matricNum The matriculation number of the student we are retrieving.
     * @return the student if the matriculation number exists; null otherwise
     */
    public static Student getStudent(String matricNum) {
        for (Student student : studentList) {
            if (student.getMatricNum().equals(matricNum)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Removes a student from studentList based on matriculation number.
     * @param matricNum The matriculation number of the student we are removing.
     * @return true if removal is successful
     */
    public static boolean removeStudent(String matricNum) {
        Student studentToRemove = getStudent(matricNum);
        return studentList.remove(studentToRemove); // if studentToRemove is null, will return false
    }

    /**
     * Adds a new student into studentList.
     * @param newStud The student object to be added.
     * @return true if addition is successful.
     */
    public static boolean addStudent(Student newStud) {
        // check if student with identical matriculation number already exists in the list
        if (studentList.contains(newStud)) { // checks using equals method in Student
            System.out.println("The student already exists in the database!");
            return false;
        }
        studentList.add(newStud);
        return true;
    }

    public static ArrayList<Student> getStudentList(){
        return studentList;
    }

    /**
     * Retrieves an admin object from adminList based on staff number.
     * @param staffNum The staff number of the admin we are retrieving.
     * @return the admin if the staff number exists; null otherwise
     */
    public static Admin getAdmin(String staffNum) {
        for (Admin admin : adminList) {
            if (admin.getStaffNum().equals(staffNum)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Removes an admin from adminList based on staff number.
     * @param staffNum The staff number of the admin we are removing.
     * @return true if removal is successful
     */
    public static boolean removeAdmin(String staffNum) {
        Admin adminToRemove = getAdmin(staffNum);
        return adminList.remove(adminToRemove); // if adminToRemove is null, will return false
    }

    /**
     * Adds a new student into studentList.
     * @param newAdmin The student object to be added.
     * @return true if addition is successful.
     */
    public static boolean addAdmin(Admin newAdmin) {
        // check if admin with identical staff number already exists in the list
        if (adminList.contains(newAdmin)) { // checks using equals method in Admin
            System.out.println("The admin already exists in the database!");
            return false;
        }
        adminList.add(newAdmin);
        return true;
    }

    public static ArrayList<Admin> getAdminList(){
        return adminList;
    }

    // TODO
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
