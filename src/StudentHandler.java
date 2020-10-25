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
        if(course==null)
        {
            System.out.println("Course does not exist!");
        }

        else
        {
            Scanner sc = new Scanner(System.in);

            String cCode = course.getCourseCode();
            String cName = course.getCourseName();

            System.out.println("You have selected to add : ");
            System.out.println("Course Code: " + cCode);
            System.out.println("Course Name: " + cName);
            System.out.println("Course Index available: ");
            // this might supposed to be a for loop to print each indexes
            System.out.println(course.getIndexes());

            System.out.println("Select an Index: ");
            int input = sc.nextInt();

            // CHECK FOR TIMETABLE CLASHES //

            System.out.println("Checking index for vacancy...");
            Index index = course.searchIndex(input);
            if (course.getIndexes().contains(index))
            {
                if (checkVacancies(index) > 0)
                {
                    System.out.println("Index available ");
                    index.addToEnrolledStudents(index.getEnrolledStudents(), this.currentStudent);
                    System.out.println("You have successfully registered for "+input+"!");
                    // currentStudent.setCurrentAU(currentStudent.setCurrentAU()+course.getAcademicUnits());
                    // increase AU
                }
                else
                {
                    System.out.println("No vacancy for selected index");
                    Scanner scanner = new Scanner(System.in);
                    char ch = scanner. next(). charAt(0);
                    System.out.println("Do you still want to register index? (y/n) You will be added to a waiting list if yes");
                    if(ch=='y')
                    {
                        System.out.println("You have been added to waiting list for "+ index);
                        index.addToWaitlist(index.getWaitlist(), this.currentStudent);
                        // there should be more stuffs happening when added to wait list
                    }
                    else if(ch=='n')
                    {
                        System.out.println("Returning to main menu..");
                    }
                    else
                    {
                        System.out.println("You have entered an invalid choice. Returning to main menu..");
                    }
                }
            }
            else
            {
                System.out.println("Index does not exist!");
            }
        }
    }

    public void dropCourse(Course course)
    {
        if(course==null)
        {
            System.out.println("Course does not exist!");

        }
//        else if()
//        {
//            System.out.println("You are not enrolled in this course!");
//        }
        else
        {
            String cCode = course.getCourseCode();
            String cName = course.getCourseName();
            Index cIndex= this.currentStudent.retrieveIndex(cCode);
            System.out.println("You have selected to drop : ");
            System.out.println("Course Code: " + cCode);
            System.out.println("Course Name: " + cName);
            System.out.println("Index Number: " + cIndex.getIndexNum());

            cIndex.removeFromEnrolledStudents(cIndex.getEnrolledStudents(), this.currentStudent);
            System.out.println("You have successfully dropped "+cIndex+"!");
            // UPDATE WAITING LIST
            // maybe create new method to update waiting list //
        }
    }

    public void checkRegistered()
    {

    }

    public int checkVacancies(Index index)
    {
        return index.getCurrentVacancy();
    }

    public void changeIndex(Course course)
    {
        if(course==null)
        {
            System.out.println("Course does not exist!");
        }
//        else if()
//        {
//            System.out.println("You are not enrolled in this course!");
//        }
        else
        {
            Scanner sc = new Scanner(System.in);

            String cCode = course.getCourseCode();
            String cName = course.getCourseName();
            Index cIndex= this.currentStudent.retrieveIndex(cCode);
            System.out.println("You have selected to swap index for the following : ");
            System.out.println("Course Code: " + cCode);
            System.out.println("Course Name: " + cName);
            System.out.println("Current index Number: " + cIndex.getIndexNum()+"\n");

            System.out.println("Course Index available: ");
            // this might supposed to be a for loop to print each indexes
            System.out.println(course.getIndexes());

            System.out.println("Select an Index to swap: ");
            int input = sc.nextInt();

            // CHECK FOR TIMETABLE CLASHES //

            System.out.println("Checking index for vacancy...");
            Index nIndex = course.searchIndex(input);
            if (course.getIndexes().contains(nIndex))
            {
                if (checkVacancies(nIndex) > 0)
                {
                    // There might be more things required to be changed when changing index
                    System.out.println("Index available ");

                    nIndex.addToEnrolledStudents(nIndex.getEnrolledStudents(), this.currentStudent);
                    System.out.println("You have successfully registered for "+input+"!");

                    cIndex.removeFromEnrolledStudents(cIndex.getEnrolledStudents(), this.currentStudent);
                    System.out.println("You have successfully dropped "+input+"!");
                }
                else
                {
                    System.out.println("No vacancy for selected index");
                    Scanner scanner = new Scanner(System.in);
                    char ch = scanner. next(). charAt(0);
                    System.out.println("Do you still want to swap index? (y/n) You will be added to a waiting list if yes");
                    if(ch=='y')
                    {
                        System.out.println("You have been added to waiting list for "+ nIndex);
                        nIndex.addToWaitlist(nIndex.getWaitlist(), this.currentStudent);
                        // there should be more stuffs happening when added to wait list
                    }
                    else if(ch=='n')
                    {
                        System.out.println("Returning to main menu..");
                    }
                    else
                    {
                        System.out.println("You have entered an invalid choice. Returning to main menu..");
                    }
                }
            }
            else
            {
                System.out.println("Index does not exist!");
            }

        }
    }

    public void swapIndex(Course course)
    {
        if(course==null)
        {
            System.out.println("Course does not exist!");

        }
//        else if()
//        {
//            System.out.println("You are not enrolled in this course!");
//        }
        else
        {
            String cCode = course.getCourseCode();
            String cName = course.getCourseName();
            Index cIndex= this.currentStudent.retrieveIndex(cCode);
            System.out.println("You have selected to swap : ");
            System.out.println("Course Code: " + cCode);
            System.out.println("Course Name: " + cName);
            System.out.println("Index Number: " + cIndex.getIndexNum());

            /*
            Request for student2's particulars i.e ( full name / matric no. / password )

            Check if student2's info is correct and exist in database

            Check if both students have indicated " willing to swap "  for the indexes

            Check for TT clashes

            if both is true
                update student 1 index
                update student 2 index

            else
                output error msg
             */
        }

    }
}
