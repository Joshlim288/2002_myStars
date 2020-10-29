import java.util.ArrayList;
import java.util.Scanner;

public class StudentInterface implements UserInterface{
    private StudentHandler studHandler;
    private Scanner sc;

    public StudentInterface (User currentUser, Scanner sc)
    {
        studHandler = new StudentHandler((Student)currentUser);
        this.sc = sc;
    }

    /**
     * Student UI is displayed here
     */
    public void start()
    {
        int choice;

        do
        {
            System.out.println("Choose an action: ");
            choice = sc.nextInt();
            String cc;
            Course course;
            System.out.println("1. Add Course");
            System.out.println("2. Drop Course");
            System.out.println("3. Check Registered Courses");
            System.out.println("4. Check Vacancies");
            System.out.println("5. Change Index");
            System.out.println("6. Swap Index");
            System.out.println("7. Back to main menu");

            switch (choice)
            {
                case 1:
                    System.out.println("You have selected to add Course");
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    cc = sc.nextLine();
                    course=FileHandler.getCourse(cc);
                    if(course==null)
                        System.out.println("Course does not exist!");
                    else
                    {
                        String cCode = course.getCourseCode();
                        String cName = course.getCourseName();

                        System.out.println("You have selected to add : ");
                        System.out.println("Course Code: " + cCode);
                        System.out.println("Course Name: " + cName);
                        System.out.println("Course Index available: ");

                        ArrayList<Index> indexes = course.getIndexes();
                        System.out.println("Index : Remaining Vacancies" +
                                "----------------");

                        for (Index index: indexes)
                            System.out.println("Index " + index.getIndexNum() + ": " + index.getCurrentVacancy());

                        System.out.println("Enter the index you would like to enroll in: ");
                        Index index = course.searchIndex(sc.nextInt());
                        //TODO: checkTimetableClash() not yet implemented.
                        //currentStudent.checkTimetableClash(Index index);
                        if(index==null)
                            System.out.println("Index does not exist!");
                        else {
                            if (indexes.contains(index))
                            {
                                if (!index.isAtMaxCapacity())
                                {
                                    studHandler.addCourse(course, index);
                                    System.out.println("You have successfully registered for " + index.getIndexNum() + "!");
                                }
                                else
                                    {
                                    char ch = sc.next().charAt(0);
                                    System.out.println("You have selected an index with no more vacancy.");
                                    System.out.println("Do you want to be added to waitlist? (Y/N)");
                                    boolean ans=studHandler.getResponse(ch);
                                    if (ans==true) {
                                        studHandler.askForWaitList(course, index, ans);
                                        System.out.println("You have been added to waitlist for Index " + index);
                                    }
                                    else if (ans==false)
                                        System.out.println("Returning to main menu..");
                                }
                            }
                            else
                                System.out.println("You did not choose a valid index!");
                        }
                    }
                    break;

                case 2:
                    System.out.println("You have selected to drop Course");
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    cc = sc.nextLine();
                    course=FileHandler.getCourse(cc);
                    if(course==null)
                        System.out.println("Course does not exist!");

                    else if (!studHandler.currentStudent.getCoursesRegistered().containsKey(course))
                        System.out.println("You are not enrolled in this course!");

                    else {
                        String cCode = course.getCourseCode();
                        String cName = course.getCourseName();
                        Index cIndex = this.studHandler.currentStudent.retrieveIndex(cCode);
                        System.out.println("You have selected to drop : ");
                        System.out.println("Course Code: " + cCode);
                        System.out.println("Course Name: " + cName);
                        System.out.println("Index Number: " + cIndex.getIndexNum());

                        studHandler.dropCourse(course, cIndex);
                        System.out.println("You have successfully dropped " + cIndex + "!");
                    }
                    break;

                case 3:
                    System.out.println("Enter Length:");

                    break;

                case 4:
                    System.out.println("Enter course code: ");
                    String course1 = sc.next();
                    System.out.println("Enter index number: ");
                    int indexno = sc.nextInt();
                    System.out.println(course1+" " + indexno + " has "+ studHandler.checkVacancies(course1,indexno)+ " vacancies");
                    break;

                case 5:
                    System.out.println("You have selected to change index ");
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    cc = sc.nextLine();
                    course=FileHandler.getCourse(cc);
                    studHandler.changeIndex(course);
                    break;

                case 6:
                    System.out.println("Enter Length:");

                    break;

                case 7:
                    System.out.println("Enter Length:");

                    break;

                default:

                    break;
            }
        }
        while(choice!=7);

    }

}
