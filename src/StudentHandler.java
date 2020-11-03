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
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class StudentHandler {
    Student currentStudent;

    public StudentHandler(Student currentStudent) {
        this.currentStudent = currentStudent;
    }

    //TODO: Improve input validation
    //TODO: If all indexes full, skip index selection and jump to asking if waitlist desired

    public boolean getResponse(char input) {
        boolean answer;
        while(true) {
            if (input == 'y' || input == 'Y'){
                answer = true;
                break;
            }
            else if (input == 'n' || input == 'N') {
                answer = false;
                break;
            }
        }
        return answer;
    }

    public void askForWaitList(Course course, Index index,boolean ans)
    {
        if(ans==true)
        {
            index.addToWaitlist(index.getWaitlist(), this.currentStudent);
            // there should be more stuffs happening when added to wait list
        }
    }

    //Return true if there is clash and false otherwise
    //TODO: First draft, to improve
    private boolean hasClash(Index indexToAdd, HashMap<Course,Index> coursesRegistered){
        //Retrieve lessons to be added for new index
        ArrayList<Lesson> lessonsToCheck = indexToAdd.getLessons();

        // Initialise list of existing lessons to check against
        // Iterate through coursesRegistered to add lessons for each index
        ArrayList<Lesson> timetable = new ArrayList<Lesson>();
        coursesRegistered.forEach((course, index) -> timetable.addAll(index.getLessons()));

        // For all lessons in timetable, check against each lesson to be added for clashes
        for (Lesson existingLesson : timetable){
            for (Lesson newLesson : lessonsToCheck) {
                LocalTime startTime = newLesson.getStartTime();
                LocalTime endTime = newLesson.getEndTime();

                if (startTime.isBefore(existingLesson.getEndTime()) &&
                        startTime.isAfter(existingLesson.getStartTime()))
                    return true;

                if (endTime.isBefore(existingLesson.getEndTime()) &&
                        endTime.isAfter(existingLesson.getStartTime()))
                    return true;
            }
        }
        return false;
    }

    // Returns true if addCourse() was successful
    // Calls the private hasClash() method
    // Returns false if there was a clash in timetable
    public boolean addCourse(Course course, Index index)
    {
            if(!hasClash(index, currentStudent.getCoursesRegistered())) {
                index.addToEnrolledStudents(index.getEnrolledStudents(), this.currentStudent);
                //TODO: Fix updating current AUs
                //currentStudent.setCurrentAUs(currentStudent.getCurrentAUs() + course.getAcademicUnits());
                return true;
            }
            else
                return false;
    }

    public void dropCourse(Course course,Index cIndex)
    {
            //Remove student from list of enrolled students in index
            cIndex.removeFromEnrolledStudents(cIndex.getEnrolledStudents(), this.currentStudent);

            //Remove course from student's registered courses
            currentStudent.removeCourse(course);

            //If there are students in waitlist, register them for the index
            if(!cIndex.getWaitlist().isEmpty()) {
                cIndex.removeFromWaitlist(cIndex.getWaitlist());
                //TODO: Notify the student who has been added to index from waitlist
            }
    }

    public String getRegisteredCourses()
    {
        //Get list of courses student is registered in.
        HashMap<Course, Index> coursesRegistered = currentStudent.getCoursesRegistered();

        //Use StringBuilder to create required output and return to StudentInterface
        StringBuilder stringBuilder = new StringBuilder();
        coursesRegistered.forEach((course, index) -> stringBuilder.append(course.getCourseName() + " " +
                course.getCourseName() + ": Index" + index.getIndexNum()));
        return stringBuilder.toString();
    }


    public String getIndexVacancies(String course){

        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Index> indexes= FileHandler.getCourse(course).getIndexes();
        indexes.forEach((index) -> stringBuilder.append("Index " + index.getIndexNum() + ": " +
                        index.getCurrentVacancy() + "/" + index.getIndexVacancy()));
        return stringBuilder.toString();
    }

    //Retired for now unless method is needed in the future
    //public int checkVacancies(String course, int index)
    //{
        //return FileHandler.getCourse(course).searchIndex(index).getCurrentVacancy();

    //}

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
            Index cIndex= this.currentStudent.retrieveIndex(course);
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
                if (!cIndex.isAtMaxCapacity())
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
            Index cIndex= this.currentStudent.retrieveIndex(course);
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
