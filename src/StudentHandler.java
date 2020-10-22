/**
 * Control class for handling student matters
 * Try not to ask for input here, get input using scanner in AdminInterface
 * Then pass the input as arguments
 *
 * Return types also no need to remain void, change to your requirements
 *
 * If you need any data, you can call the static methods in FileHandler with the class name
 * e.g. FileHandler.getStudent(name)
 *
 * Try to add JavaDocs as you go
 */
import java.util.Scanner;

public class StudentHandler {
    Student currentStudent;

    public StudentHandler(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    public void addCourse(Course course)
    {

        Scanner sc = new Scanner(System.in);

        String cCode=course.getCourseCode();
        String cName=course.getCourseName();

        System.out.println("You have selected to add : ");
        System.out.println("Course Code: "+cCode);
        System.out.println("Course Name: "+cName);
        System.out.println("Course Index available: ");
        System.out.println(course.getIndexes());

        System.out.println("Select an Index: ");
        int input = sc.nextInt();

        // CHECK FOR TIMETABLE CLASHES
        System.out.println("Checking index for vacancy...");
        Index index= course.searchIndex(input);
        if(course.getIndexes().contains(index))
        {
            if(checkVacancies(index)>0)
            {
                System.out.println("Index available ");
                // add to course
                index.addToEnrolledStudents(index.getEnrolledStudents(), this.currentStudent);
                // currentStudent.setCurrentAU(currentStudent.setCurrentAU()+course.getAcademicUnits());
                // increase AU

            }
            else
            {
                System.out.println("No vacancy for selected index");
                // add to waiting list
                index.addToWaitlist(index.getWaitlist(), this.currentStudent);
            }
        }
        else
        {
            System.out.println("Index does not exist!");
        }
    }

    public void dropCourse(Course course)
    {

    }

    public void checkRegistered(){

    }

    public int checkVacancies(Index index)
    {
        return index.getCurrentVacancy();
    }

    public void changeIndex(){

    }

    public void swapIndex(){

    }
}
