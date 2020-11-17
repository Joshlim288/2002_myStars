import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Control class to display to the Student what functions are available to them
 * Gets input for performing these functions and passes them to the handler to be executed
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.15
 * @since 1.1
 */
public class StudentInterface extends UserInterface {

    private final StudentHandler studHandler;

    public StudentInterface(User currentUser, Scanner sc) {
        super(sc);
        String matricNum = ((Student)currentUser).getMatricNum();
        studHandler = new StudentHandler(matricNum);
        System.out.println("\nWelcome, " + currentUser.getName() + "!");
    }

    /**
     * Student UI is displayed here
     */
    public void start() {
        int choice;
        boolean exitFlag = false;
        do {
            System.out.println("--------------------------------------------------------------");
            System.out.println("| What would you like to do today?                           |");
            System.out.println("--------------------------------------------------------------");
            System.out.println("| 1. Add New Course                                          |");
            System.out.println("| 2. Drop Registered Course / Waitlisted Course              |");
            System.out.println("| 3. Check Currently Registered Courses                      |");
            System.out.println("| 4. Check Vacancies for A Course                            |");
            System.out.println("| 5. Print Overview of Course Database                       |");
            System.out.println("| 6. Change Index of Registered Course                       |");
            System.out.println("| 7. Swap Index of Registered Course with Another Student    |");
            System.out.println("| 8. Log Out of MyStars                                      |");
            System.out.println("| (Enter ~ at any time to return back to previous menu)      |");
            System.out.println("--------------------------------------------------------------");

            try {
                System.out.print("Please enter your choice: ");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
            } catch (EscapeException e) {
                logout();
                return;
            }
            switch (choice) {
                case (1) -> addCourse();
                case (2) -> dropCourse();
                case (3) -> getRegisteredCourses();
                case (4) -> checkIndexVacancies();
                case (5) -> printOverview();
                case (6) -> changeIndex();
                case (7) -> swapIndex();
                case (8) -> exitFlag = logout();
                default -> System.out.println("ERROR: Invalid menu option selected.");
            }
        } while (!exitFlag);
    }

    private void addCourse() {
        Course courseSelected;
        Index indexSelected;
        boolean validCourse, validIndex;

        try {
            System.out.println(studHandler.getCourseOverview(1));
            do {
                System.out.print("Enter course to add (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse) {
                    if (studHandler.willGoOverMaxAU(courseSelected)) {
                        System.out.println("Cannot register for course, will exceed maximum AUs!\n");
                        validCourse = false;
                    }
                    else if (studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are already enrolled in this course!\n");
                        validCourse = false;
                    }
                    else if (studHandler.hasExamClash(courseSelected))
                        validCourse = false;
                }
            } while (!validCourse);

            showIndexesInCourse(courseSelected);
            do {
                System.out.println("Enter the index you would like to enroll in.\n" +
                        "You will be added to wait-list if you choose an index with no vacancies:");
                indexSelected = studHandler.retrieveIndex(courseSelected, getInput(typeOfInput.INDEX_NUM));
                validIndex = studHandler.checkValidIndex(indexSelected, studHandler.currentStudent, null);
            } while (!validIndex);

            System.out.println("\nYou have selected to add : \n" +
                               courseSelected.getCourseCode() + " " + courseSelected.getCourseName() + "\n" +
                               "Index Number: " + indexSelected.getIndexNum());

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, null, true);
            printStatusOfAddCourse(status, indexSelected);

            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void dropCourse() {
        Course courseSelected;
        boolean validCourse;
        boolean waitlistedCourse = false;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();
            do{
                System.out.print("Enter course to drop (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse)
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected) &&
                        !studHandler.checkIfWaitListed(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are not enrolled in this course!\n");
                        validCourse = false;
                    }
            } while (!validCourse);

            Index indexToDrop = studHandler.getIndexRegistered(studHandler.currentStudent, courseSelected);
            String index;

            if (indexToDrop == null) {
                waitlistedCourse = true;
                index = studHandler.currentStudent.retrieveIndexFromWaitList(courseSelected.getCourseCode());
            }
            else
                index = studHandler.currentStudent.retrieveIndex(courseSelected.getCourseCode());

            System.out.println("\nEnter \"Y\" to confirm that you would like to drop this index: \n" +
                               courseSelected.getCourseCode() + ", " + courseSelected.getCourseName() +
                               ", Index Number: " + index);

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                studHandler.dropCourse(studHandler.currentStudent, courseSelected, index, waitlistedCourse);
                System.out.println("Dropping index " + index + ", please wait a moment...");
                if(!waitlistedCourse)
                    studHandler.refreshWaitList(courseSelected, indexToDrop);
                System.out.println("Successfully dropped index " + index + "!");
            }
            else System.out.println("Index not dropped. Returning to main menu.");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getRegisteredCourses(){
        showRegisteredCourses();
        waitForEnterInput();
    }

    private void checkIndexVacancies(){
        try {
            Course courseSelected;
            boolean validCourse;

            System.out.println(studHandler.getCourseOverview(1));
            do {
                System.out.print("Enter course code to check vacancies (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
            } while (!validCourse);

            showIndexesInCourse(courseSelected);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printOverview(){
        try{
            int choice;
            do {
                System.out.println("Choose overview :");
                System.out.println("1) Print all Courses\n" +
                        "2) Print all Courses + Indexes\n" +
                        "3) Print all Courses + Indexes + Lessons");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
            } while (choice > 3 || choice == 0);
            System.out.println(studHandler.getCourseOverview(choice));
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeIndex() {
        Course courseSelected;
        Index indexSelected;
        boolean validCourse, validIndex;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();
            do {
                System.out.print("Choose course for changing of index (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse)
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are not enrolled in this course!\n" +
                                            "Please re-enter course again.\n");
                        validCourse = false;
                    }
            }while (!validCourse);

            Index indexToDrop = studHandler.getIndexRegistered(studHandler.currentStudent, courseSelected);
            showIndexesInCourse(courseSelected);

            do {
                System.out.println("Enter the index you would like to swap to.\n" +
                            "You will be added to wait-list if you choose an index with no vacancies:");
                indexSelected = studHandler.retrieveIndex(courseSelected, getInput(typeOfInput.INDEX_NUM));
                validIndex = studHandler.checkValidIndex(indexSelected, studHandler.currentStudent, indexToDrop);

                if(indexSelected.equals(indexToDrop)) {
                    System.out.println("You have selected the index you are currently in!");
                    System.out.println("Please choose a different index.\n");
                    validIndex = false;
                }

            } while (!validIndex);


            System.out.println("\nIndex change details for " + courseSelected.getCourseCode()+ ", " + courseSelected.getCourseName());
            System.out.println("Current index before swap: " + indexToDrop.getIndexNum());
            System.out.println("Updated index after swap: " + indexSelected.getIndexNum());
            System.out.println("Confirm to proceed with the change? Press 'Y/y' to continue and any other key otherwise.");

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, indexToDrop, true);
                printStatusOfAddCourse(status, indexSelected);
                System.out.println("Changing index, please wait a moment...");
                studHandler.refreshWaitList(courseSelected, indexToDrop);
                System.out.println("Index successfully changed!");
            } else
                System.out.println("Index not changed.\n" + "Exiting to main menu...");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void swapIndex() {
        Course courseSelected;
        boolean validCourse, validOtherStudent = false;
        Student otherStudent;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();
            do {
                System.out.print("Choose course for swapping of index (e.g. CZ2002):");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse)
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are not enrolled in this course!\n" +
                                           "Please re-enter course again.\n");
                        validCourse = false;
                    }
            }while (!validCourse);

            Index indexToSwapOut = studHandler.getIndexRegistered(studHandler.currentStudent, courseSelected);

            System.out.println("\nEnter the particulars of the student to swap with:");
            otherStudent = studHandler.retrieveOtherStudent(sc);
            if (otherStudent != null)
                validOtherStudent = studHandler.checkIfRegistered(otherStudent, courseSelected);
            if (!validOtherStudent) {
                System.out.println("\nReturning to main menu!");
                waitForEnterInput();
                return;
            }

            Index indexToSwapIn = studHandler.getIndexRegistered(otherStudent, courseSelected);
            if (indexToSwapIn.equals(indexToSwapOut)){
                System.out.println("\nERROR! Both of you are registered for the same index. Swap not performed.");
                System.out.println("Returning to main menu!");
                waitForEnterInput();
                return;
            }

            boolean currentStudentClash = studHandler.hasTimetableClash(indexToSwapIn, studHandler.currentStudent, indexToSwapOut);
            boolean otherStudentClash = studHandler.hasTimetableClash(indexToSwapOut, otherStudent, indexToSwapIn);
            if (currentStudentClash || otherStudentClash) {
                System.out.println("\nERROR! Swap not performed due to clashes in timetable for one or more students.");
                System.out.println("Returning to main menu!");
                waitForEnterInput();
                return;
            }

            System.out.println("\nSwap details for " + courseSelected.getCourseCode()+ ", " + courseSelected.getCourseName());
            System.out.println(studHandler.currentStudent.getName() + "'s current index before swap: " + indexToSwapOut.getIndexNum());
            System.out.println(otherStudent.getName() + "'s current index before swap: " + indexToSwapIn.getIndexNum());
            System.out.println("Confirm to proceed with the swap? Press 'Y/y' to continue and any other key otherwise.");

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                int status1 = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexToSwapIn, indexToSwapOut, false);
                int status2 = studHandler.addCourse(otherStudent, courseSelected, indexToSwapOut, indexToSwapIn, false);
                System.out.println("\nFor " + studHandler.currentStudent.getName() + " :");
                printStatusOfAddCourse(status1, indexToSwapIn);
                System.out.println("For " + otherStudent.getName() + " :");
                printStatusOfAddCourse(status2, indexToSwapOut);
                System.out.println("Updating database, please wait...");
                //For current student
                studHandler.emailStudent(studHandler.currentStudent, otherStudent, courseSelected, indexToSwapOut, indexToSwapIn);
                //For other student
                studHandler.emailStudent(otherStudent, studHandler.currentStudent, courseSelected, indexToSwapIn, indexToSwapOut);
                System.out.println("Database updated!\n");
            } else
                System.out.println("Swap not performed.\n" + "Exiting to main menu...");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean noRegisteredCourses(){
        HashMap<String, String> coursesRegistered = studHandler.currentStudent.getCoursesRegistered();
        if (coursesRegistered.isEmpty()) {
            System.out.println("ERROR! No currently registered courses.");
            waitForEnterInput();
            return true;
        }
        return false;
    }

    //Used in adding of course, changing index and swapping index
    private void showIndexesInCourse(Course courseSelected){

        ArrayList<Index> indexList = courseSelected.getIndexes();

        System.out.println("\nList of Indexes in " +
                courseSelected.getCourseName() + " - " +
                courseSelected.getCourseCode() + ":\n" +
                "Index Number | Remaining Vacancies\n" +
                "----------------------------------");

        for (Index index : indexList)
            System.out.println(index);
    }

    private void showRegisteredCourses(){
        System.out.println("\nThese are your currently registered courses and indexes:");
        System.out.println(studHandler.getRegisteredCourses());
    }

    //Default when an integer representing clash is returned
    private void printStatusOfAddCourse(int status, Index indexSelected){
        switch (status) {
            case (1) -> System.out.println("Registered for Index " + indexSelected.getIndexNum() + " successfully!\n");
            case (2) -> System.out.println("Added to wait-list for Index " + indexSelected.getIndexNum() + " successfully!\n");
        }
    }

    private boolean logout(){
        if (exit()) {
            System.out.println("\nSaving Data...");
            studHandler.close();
            System.out.println("Thank you for using MyStars!");
            System.out.println("Goodbye!");
            return true;
        } else {
            return false;
        }
    }
}
