import java.io.*;
import java.util.ArrayList;

/**
 * DataManager for Student data
 * Handles the loading, holding, editing and storing of Admin objects from the data/users/studentData.dat
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.10
 * @since 1.9
 */
public class StudentDataManager implements DataManager{
    private ArrayList<Student> studentList;
    public StudentDataManager() {
        studentList = new ArrayList<>();
    }

    /**
     * Load studentList from data/users/studentData.dat
     */
    public void load() {
        try {
            FileInputStream fileIn = new FileInputStream("data/users/studentData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            studentList = (ArrayList<Student>) in.readObject();
            if (studentList.isEmpty()) {
                System.out.println("\n...Student data loaded but data is empty!");
            } else {
                System.out.println("\n...Student data loaded successfully!");
            }
        } catch (IOException e) {
            System.out.println("\n...Student data file not found or is empty. Initializing file...");
            studentList = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save studentList to data/users/studentData.dat
     */
    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/users/studentData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(studentList);
            out.close();
            fileOut.close();
            System.out.println("... Student Data saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a student object from studentList based on matriculation number.
     * @param matricNum The matriculation number of the student we are retrieving.
     * @return the student if the matriculation number exists; null otherwise
     */
    public Student getStudent(String matricNum) {
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
    public boolean removeStudent(String matricNum) {
        Student studentToRemove = getStudent(matricNum);
        return studentList.remove(studentToRemove); // if studentToRemove is null, will return false
    }

    /**
     * Adds a new student into studentList.
     * @param newStud The student object to be added.
     * @return true if addition is successful.
     */
    public boolean addStudent(Student newStud) {
        // check if student with identical matriculation number already exists in the list
        if (studentList.contains(newStud)) { // checks using equals method in Student
            System.out.println("The student already exists in the database!");
            return false;
        }
        studentList.add(newStud);
        return true;
    }

    /**
     * Retrieves list of all Students currently in the database
     * @return ArrayList of all Students
     */
    public ArrayList<Student> getStudentList(){
        return studentList;
    }

    /**
     * Generates an overview of all Student data
     * @return String containing overview of the data
     */
    public String generateStudentOverview(){
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Student> studentList = getStudentList();
        for (Student student: studentList)
            stringBuilder.append("\n" + student);

        return stringBuilder.toString();
    }
}
