import java.util.ArrayList;
import java.util.Scanner;

/**
 * TODO shift all dataManager calls to handler
 */
public class StudentInterface extends UserInterface {

    private StudentHandler studHandler;

    public StudentInterface(User currentUser, Scanner sc) {
        super(sc);
        studHandler = new StudentHandler((Student) currentUser);
    }

    /**
     * Student UI is displayed here
     */
    public void start() {
        int choice;

        do {
            System.out.println("Welcome, Student " + studHandler.currentStudent.getName()
                    + ", " + studHandler.currentStudent.getMatricNum() + "!");
            System.out.println("Choose an action: ");
            System.out.println("1. Add Course");
            System.out.println("2. Drop Course");
            System.out.println("3. Check Registered Courses");
            System.out.println("4. Check Vacancies");
            System.out.println("5. Change Index");
            System.out.println("6. Swap Index");
            System.out.println("7. Back to main menu");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case (1) -> addCourse();
                case (2) -> dropCourse();
                case (3) -> checkRegisteredCourses();
                case (4) -> checkVacancies();
                case (5) -> changeIndex();
                case (6) -> swapIndex();
                case (7) -> logout();
            }
        } while (choice != 7);
    }

    private ArrayList<Index> displayIndexesFromCourseInput(Course courseSelected){

        ArrayList<Index> indexList = courseSelected.getIndexes();

        System.out.println("You have selected: " + courseSelected.getCourseName() + ", " + courseSelected.getCourseCode() + ".\n" +
                "Indexes available:" +
                "Index Number | Remaining Vacancies" +
                "----------------------------------");

        for (Index index : indexList)
            System.out.println(index);

        return indexList;
    }

    private void addCourse() {

        System.out.println("You have selected to add Course\n" +
                "Enter Course Code (e.g. CZ2002):\n" +
                "Press ~ to return to main menu.");

        String courseCodeInput = getString("[A-Z]{2}[0-9]{4}");
        Course courseSelected = studHandler.cdm.getCourse(courseCodeInput);

        if (courseSelected != null) {

            // Takes in course selected and output the current indexes and vacancies
            ArrayList<Index> indexList = displayIndexesFromCourseInput(courseSelected);

            System.out.println("Enter the index you would like to enroll in:" +
                    "You will be added to wait-list if you choose an index with no vacancies.");

            //TODO: change index to string?
            Index index = courseSelected.getIndex(sc.nextLine());

            if (index != null) {
                if (indexList.contains(index)) {
                    if (!index.isAtMaxCapacity()) {
                        boolean success = studHandler.addCourse(courseSelected, index);
                        if (success)
                            System.out.println("You have successfully registered for " + index.getIndexNum() + "!\n");
                        else
                            System.out.println("There is a clash, you cannot be registered for" +
                                    index.getIndexNum() + "!");
                    } else {
                        System.out.println("Adding to wait-list...");
                        studHandler.updateWaitList(index);
                        System.out.println("You have been added to wait-list for Index " + index.getIndexNum());
                    }
                    System.out.println("Returning to main menu...");
                } else
                    System.out.println("Index does not exist!");
            }
        } else
            System.out.println("Course does not exist!");
    };

    private void dropCourse() {
        System.out.println("You have selected to drop Course\n" +
                "Enter Course Code (e.g. CZ2002):\n" +
                "Press ~ to return to main menu.");
        Course courseSelected = studHandler.cdm.getCourse(sc.nextLine());
        if (courseSelected == null)
            System.out.println("Course does not exist!");

        else if (!studHandler.currentStudent.getCoursesRegistered().containsKey(courseSelected))
            System.out.println("You are not enrolled in this course!");

        else {
            Index cIndex = this.studHandler.currentStudent.retrieveIndex(courseSelected);
            System.out.println("You have selected to drop : \n" +
                               "Course Code: " + courseSelected.getCourseCode() + "\n" +
                               "Course Name: " + courseSelected.getCourseName() + "\n" +
                               "Index Number: " + cIndex.getIndexNum());

            studHandler.dropCourse(courseSelected, cIndex);
            System.out.println("You have successfully dropped " + cIndex + "!");
        }
    };

    private void checkRegisteredCourses(){
        System.out.println("Here are your currently registered courses:");
        System.out.println(studHandler.getRegisteredCourses());
    };

    private void checkVacancies(){
        System.out.println("Enter course code: ");
        String courseInput = sc.next();
        System.out.println("Here are the vacancies for the indexes in this course:");
        System.out.println(studHandler.getIndexVacancies(courseInput));
    };

    private void changeIndex(){
        System.out.println("You have selected to add Course\n" +
                "Enter Course Code (e.g. CZ2002):\n" +
                "Press ~ to return to main menu.");

        String courseCodeInput = getString("[A-Z]{2}[0-9]{4}");
        Course courseSelected = studHandler.cdm.getCourse(courseCodeInput);

        if (courseSelected != null) {

            // Takes in course selected and output the current indexes and vacancies
            ArrayList<Index> indexList = displayIndexesFromCourseInput(courseSelected);
            Index cIndex = this.studHandler.currentStudent.retrieveIndex(courseSelected);

            System.out.println("Enter the index you would like to enroll in:" +
                    "You will be added to wait-list if you choose an index with no vacancies.");

            //TODO: change index to string?
            Index index = courseSelected.getIndex(sc.nextLine());

            if (index != null) {
                if (indexList.contains(index)) {
                    if (!index.isAtMaxCapacity()) {
                        boolean success = studHandler.addCourse(courseSelected, index);

                        if (success) {
                            studHandler.dropCourse(courseSelected, cIndex);
                            System.out.println("You have successfully dropped " + cIndex + "!");
                            System.out.println("You have successfully registered for " + index.getIndexNum() + "!\n");
                        }
                        else
                            System.out.println("There is a clash, you cannot be registered for" +
                                    index.getIndexNum() + "!");
                    } else {
                        System.out.println("No");
                    }
                    System.out.println("Returning to main menu...");
                } else
                    System.out.println("Index does not exist!");
            }
        } else
            System.out.println("Course does not exist!");
    };

    private void swapIndex(){
        //TODO: Swap courses

        String courseCodeInput;
        Course courseSelected;
        boolean validUser = false;

        System.out.println("You have selected to swap Indexes");
        System.out.println("-----------");
        System.out.println("Enter Course Code: ");
        courseCodeInput = sc.nextLine();
        courseSelected = studHandler.cdm.getCourse(courseCodeInput);
        if (courseSelected == null)
            System.out.println("Course does not exist!");
        else {
            String cCode = courseSelected.getCourseCode();
            String cName = courseSelected.getCourseName();
            Index cIndex = this.studHandler.currentStudent.retrieveIndex(courseSelected);

            System.out.println("You have selected to swap the following : ");
            System.out.println("Course Code: " + cCode);
            System.out.println("Course Name: " + cName);
            System.out.println("Current index Number: " + cIndex.getIndexNum() + "\n");

            System.out.println("Enter the student's particulars:");

            User targetUser;
            // Retrieve and validate other student's details
            targetUser = MyStars.login(sc);

            Student targetStudent = studHandler.sdm.getStudent(((Student)targetUser).getMatricNum());

            studHandler.currentStudent.removeCourse();
            studHandler.currentStudent.addCourse();
            targetStudent.removeCourse();
            targetStudent.addCourse();

            System.out.println("Swap completed.");
            }
    };

    private void logout(){
        System.out.println("Saving Data...");
        studHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    };
}
