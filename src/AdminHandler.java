
import java.time.LocalDateTime;
import java.util.Scanner;
public class AdminHandler implements AdminInterface{
    Scanner sc = new Scanner(System.in);
    Admin currentAdmin;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public void editCourse(Course course, String school, String indexnum){

    }

    public void addCourse(Course newcoursecode){

        FileHandler.addcourse(newcoursecode);

    }
        else{
        system.out.println("Error, null course code input.");
    }

    public int checkSlot(Index indexnum){
        return indexnum.getCurrentVacancy();
    }

    /**
     * Prints student list
     * @param byIndex
     * Sort student list by index if byIndex is True, by course if False
     */
    public void printStudentList(boolean byIndex){

    }

    public void editAccessPeriod(String matricNum, LocalDateTime start, LocalDateTime end){
        Student student = FileHandler.getStudent(matricNum);
        student.setAccessTime(start, end);
        FileHandler.saveStudents();
    }

    public void addStudent(Student student){
        FileHandler.getStudentList().add(student);
        FileHandler.saveStudents();
        FileHandler.saveUsers();






    }
}}
