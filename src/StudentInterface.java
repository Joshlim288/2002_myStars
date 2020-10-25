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
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    cc = sc.nextLine();
                    course=FileHandler.getCourse(cc);
                    studHandler.addCourse(course);
                    break;

                case 2:
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    cc = sc.nextLine();
                    course=FileHandler.getCourse(cc);
                    studHandler.dropCourse(course);
                    break;

                case 3:
                    System.out.println("Enter Length:");

                    break;

                case 4:
                    System.out.println("Enter Length:");

                    break;

                case 5:
                    System.out.println("Enter Length:");

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
