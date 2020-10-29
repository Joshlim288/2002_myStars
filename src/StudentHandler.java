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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentHandler {
    Student currentStudent;

    public StudentHandler(Student currentStudent) {
        this.currentStudent = currentStudent;
    }


    /**
     * Method to add a course for current student.
     * Retrieves indexes under the course and prompts for student's choice
     * Includes checking of index capacity and clashes with student's current timetable
     * Adds student to waitlist if chosen index is full
     * Exception handling if waitlist is already empty
     * @param course Course to be added
     */
    //TODO: Improve input validation
    //TODO: If all indexes full, skip index selection and jump to asking if waitlist desired
    //TODO: checkTimetableClash()

    public boolean getResponse(char input)
    {
        while(true)
        {
            if (input == 'y' || input == 'Y')
                return true;
            else if (input == 'n' || input == 'N')
                return false;
        }
    }

    public void askForWaitList(Course course, Index index,boolean ans)
    {
        if(ans==true)
        {
            System.out.println("You have been added to waitlist for Index "+ index);
            index.addToWaitlist(index.getWaitlist(), this.currentStudent);
            // there should be more stuffs happening when added to wait list
        }
        else if(ans==false)
            System.out.println("Returning to main menu..");
        else
            System.out.println("You have entered an invalid choice. Returning to main menu..");
    }

    public void addCourse(Course course, Index index)
    {
            index.addToEnrolledStudents(index.getEnrolledStudents(), this.currentStudent);
            currentStudent.setCurrentAUs(currentStudent.getCurrentAUs()+course.getAcademicUnits());
    }


    public void dropCourse(Course course,Index cIndex)
    {
        //Remove student from list of enrolled students in index
        cIndex.removeFromEnrolledStudents(cIndex.getEnrolledStudents(), this.currentStudent);

        //Remove course from student's registered courses
        currentStudent.removeCourse(course);
        System.out.println("You have successfully dropped "+cIndex+"!");

        //If there are students in waitlist, register them for the index
        if(!cIndex.getWaitlist().isEmpty()) {
            cIndex.removeFromWaitlist(cIndex.getWaitlist());
            //TODO: Notify the student who has been added to index from waitlist
        }
    }

    public void checkRegistered()
    {
        HashMap<Course, Index> coursesRegistered = currentStudent.getCoursesRegistered();

        //Iterate through coursesRegistered HashMap and print out information
        //TODO: Initial implementation placed here. Consider implementing this method in Student class instead.
        //TODO: Decide on how much information to show when printing out courses and index
        coursesRegistered.forEach((course, index) -> System.out.println(course.getCourseName() + " " +
                course.getCourseName() + ": Index" + index.getIndexNum()));
    }

    public int checkVacancies(String course, int index)
    {
        return FileHandler.getCourse(course).searchIndex(index).getCurrentVacancy();

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

        Scanner sc = new Scanner(System.in);

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

            System.out.println("Please enter the student's matriculation number whom you would like to swap with:");


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
