import java.util.ArrayList;
import java.util.Scanner;

/**
 * TODO shift all dataManager calls to handler
 */
public class StudentInterface extends UserInterface{

    private StudentHandler studHandler;

    public StudentInterface (User currentUser, Scanner sc) {
        super(sc);
        studHandler = new StudentHandler((Student)currentUser);
    }

    /**
     * Student UI is displayed here
     */
    public void start() {
        int choice;

        do
        {
            System.out.println("Welcome, Student " + studHandler.currentStudent.getStudentName()
                                + ", " + studHandler.currentStudent.getMatricNum() + "!");
            System.out.println("Choose an action: ");
            System.out.println("1. Add Course");
            System.out.println("2. Drop Course");
            System.out.println("3. Check Registered Courses");
            System.out.println("4. Check Vacancies");
            System.out.println("5. Change Index");
            System.out.println("6. Swap Index");
            System.out.println("7. Back to main menu");
            choice = sc.nextInt();
            String courseCodeInput;
            Course courseSelected;

            switch (choice) {
                case 1:
                    System.out.println("You have selected to add Course\n" +
                                       "-------------------------------\n" +
                                       "Enter Course Code (e.g. CZ2002):");
                    courseCodeInput = sc.nextLine();
                    courseSelected = studHandler.cdm.getCourse(courseCodeInput);

                    if (courseSelected != null) {
                        String cCode = courseSelected.getCourseCode();
                        String cName = courseSelected.getCourseName();
                        ArrayList<Index> indexList = courseSelected.getIndexes();

                        System.out.println("You have selected: " + cName + ", " + cCode + ".\n" +
                                           "Indexes available:" +
                                           "Index Number | Remaining Vacancies" +
                                           "----------------------------------");

                        for (Index index : indexList)
                            System.out.println(index);

                        System.out.println("Enter the index you would like to enroll in:" +
                                           "You will be added to wait-list if you choose an index with no vacancies.");
                        Index index = courseSelected.searchIndex(sc.nextInt());

                        if (index != null) {
                            if (indexList.contains(index)) {
                                if (!index.isAtMaxCapacity()) {
                                    boolean success = studHandler.addCourse(courseSelected, index);
                                    if (success)
                                        System.out.println("You have successfully registered for " + index.getIndexNum() + "!\n");
                                    else
                                        System.out.println("There is a clash, you cannot be registered for" +
                                                index.getIndexNum() + "!");
                                }
                                else {
                                    System.out.println("Adding to wait-list...");
                                    studHandler.updateWaitList(index);
                                    System.out.println("You have been added to wait-list for Index " + index.getIndexNum());
                                }
                                System.out.println("Returning to main menu...");
                            }
                        else
                            System.out.println("Index does not exist!");
                        }
                    }
                    else
                        System.out.println("Course does not exist!");

                    break;

                case 2:
                    System.out.println("You have selected to drop Course");
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    courseCodeInput = sc.nextLine();
                    courseSelected = studHandler.cdm.getCourse(courseCodeInput);
                    if(courseSelected==null)
                        System.out.println("Course does not exist!");

                    else if (!studHandler.currentStudent.getCoursesRegistered().containsKey(courseSelected))
                        System.out.println("You are not enrolled in this course!");

                    else {
                        String cCode = courseSelected.getCourseCode();
                        String cName = courseSelected.getCourseName();
                        Index cIndex = this.studHandler.currentStudent.retrieveIndex(courseSelected);
                        System.out.println("You have selected to drop : ");
                        System.out.println("Course Code: " + cCode);
                        System.out.println("Course Name: " + cName);
                        System.out.println("Index Number: " + cIndex.getIndexNum());

                        studHandler.dropCourse(courseSelected, cIndex);
                        System.out.println("You have successfully dropped " + cIndex + "!");
                    }
                    break;

                case 3:
                    System.out.println("Here are your currently registered courses:");
                    System.out.println(studHandler.getRegisteredCourses());
                    break;

                case 4:
                    System.out.println("Enter course code: ");
                    String courseInput = sc.next();
                    System.out.println("Here are the vacancies for the indexes in this course:");
                    System.out.println(studHandler.getIndexVacancies(courseInput));
                    break;

                case 5:
                    System.out.println("You have selected to change index ");
                    System.out.println("-----------");
                    System.out.println("Enter Course Code: ");
                    courseCodeInput = sc.nextLine();
                    courseSelected = studHandler.cdm.getCourse(courseCodeInput);
                    studHandler.changeIndex(courseSelected);
                    break;

                case 6:
                    //TODO: Swap courses
                    break;

                case 7:
                    System.out.println("Saving Data...");
                    studHandler.close();
                    System.out.println("Thank you for using MyStars!");
                    System.out.println("Goodbye!");
                    break;

                default:
                    break;
            }
        }
        while(choice!=7);
    }
}
