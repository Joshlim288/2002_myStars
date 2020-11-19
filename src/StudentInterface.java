import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Control class to display to the Student what functions are available to them.<p>
 * Gets input for performing these functions and passes them to the handler to be executed
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.15
 * @since 1.1
 */
public class StudentInterface extends UserInterface {

    private final StudentHandler studHandler;

    /**
     * Constructor for the Student Interface.
     * Called from {@link UserInterfaceCreator}.
     * @param currentUser User which has logged in
     * @param sc Scanner to be used for getting input
     */
    public StudentInterface(User currentUser, Scanner sc) {
        super(sc);
        String matricNum = ((Student)currentUser).getMatricNum();
        studHandler = new StudentHandler(matricNum);
        System.out.println("\nWelcome, " + currentUser.getName() + "!");
    }

    /**
     * Menu for Students is displayed here
     */
    public void start() {
        int choice;
        boolean exitFlag = false;
        do {
            System.out.println("--------------------------------------------------------------");
            System.out.println("| What would you like to do today?                           |");
            System.out.println("--------------------------------------------------------------");
            System.out.println("| 1. Add New Course                                          |");
            System.out.println("| 2. Drop Registered Course / Wait-listed Course             |");
            System.out.println("| 3. Check Currently Registered Courses                      |");
            System.out.println("| 4. Check Vacancies for a Course                            |");
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
                case (1) -> addCourseOption();
                case (2) -> dropCourseOption();
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

    /**
     * Gets input to register a course for the current student.<p>
     * Asks for input repeatedly if course/index input given is invalid.<p>
     * Relays the user's input to studentHandler to add student to selected index and outputs the outcome.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void addCourseOption() {
        Course courseSelected;
        Index indexSelected;
        boolean validCourse, validIndex;

        try {
            System.out.println(studHandler.getCourseOverview(1));
            do {
                //Get valid course to add
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
                //Get valid index to add
                System.out.println("Enter the index you would like to enroll in.\n" +
                                   "You will be added to wait-list if an index with no vacancies is chosen:");
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

    /**
     * Gets input to drop a course from the current student's registered/wait-listed courses.<p>
     * Asks for input repeatedly if course/index input given is invalid.<p>
     * Relays the user's input to studentHandler to drop student from selected index and outputs the outcome.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void dropCourseOption() {
        Course courseSelected;
        String indexCode;
        boolean validCourse, waitListed = false;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();

            //Get valid course to drop
            do{
                System.out.print("Enter course to drop (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse){
                    waitListed = studHandler.checkIfWaitListed(studHandler.currentStudent, courseSelected);
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected) && !waitListed) {
                        System.out.println("You are not enrolled in this course/wait-listed for this course!\n");
                        validCourse = false;
                    }
                }
            } while (!validCourse);

            if (waitListed)
                indexCode = studHandler.currentStudent.retrieveIndexFromWaitList(courseSelected.getCourseCode());
            else
                indexCode = studHandler.currentStudent.retrieveIndexFromRegistered(courseSelected.getCourseCode());

            Index indexToDrop = studHandler.retrieveIndex(courseSelected, indexCode);
            System.out.println("\nEnter \"Y\" to confirm that you would like to drop this index: \n" +
                               courseSelected.getCourseCode() + ", " + courseSelected.getCourseName() +
                               ", Index Number: " + indexCode);

            //Get confirmation to drop index
            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                studHandler.dropCourse(studHandler.currentStudent, courseSelected, indexCode, waitListed);
                System.out.println("Dropping index " + indexCode + ", please wait a moment...");
                //Trigger waitlist update for index only if student was actually registered and not waitlisted
                if(!waitListed) studHandler.refreshWaitList(courseSelected, indexToDrop);
                System.out.println("Successfully dropped index " + indexCode + "!");
            }
            else System.out.println("Index not dropped. Returning to main menu.");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Outputs the information of the student's registered and wait-listed courses/indexes.<p>
     * Retrieves the relevant data needed for the output from studentHandler.
     */
    private void getRegisteredCourses(){
        showRegisteredCourses();
        System.out.println("Total AUs registered: " + studHandler.currentStudent.getCurrentAUs() + "\n");
        waitForEnterInput();
    }

    /**
     * Outputs the chosen index's remaining vacancies in the course database.<p>
     * Asks for input repeatedly if index input given is invalid.<p>
     * Relays the user's input to studentHandler to retrieve the remaining vacancy and outputs the outcome.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void checkIndexVacancies(){
        Course courseSelected;
        boolean validCourse;

        try {
            System.out.println(studHandler.getCourseOverview(1));

            //Get valid course to check vacancies
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

    /**
     * Allows the user to prints an overview of the current course database (courses, indexes, lessons).<p>
     * Asks for input of choice of granularity of data to be retrieved.<p>
     * Relays the user's input to studentHandler to retrieve the required information from database.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void printOverview(){
        int choice;

        try{
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

    /**
     * Gets input to change a currently registered index of the student.<p>
     * Asks for input repeatedly if course/index input is invalid.<p>
     * Relays the user's input to studentHandler to execute the change of index and outputs the outcome.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void changeIndex() {
        Course courseSelected;
        Index indexSelected;
        boolean validCourse, validIndex;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();

            //Get valid index for changing of index
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

            //Get valid index to change to
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

            //Get confirmation to change index
            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, indexToDrop, true);
                printStatusOfAddCourse(status, indexSelected);
                System.out.println("Changing index, please wait a moment...");
                studHandler.refreshWaitList(courseSelected, indexToDrop); //Trigger waitlist update for original index
                System.out.println("Index successfully changed!");
            } else
                System.out.println("Index not changed.\n" + "Exiting to main menu...");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets input to swap a currently registered index of the student with another student.<p>
     * Asks for input repeatedly if course/user input is invalid.<p>
     * Relays the user's input to studentHandler to execute the swap of index and outputs the outcome.<p>
     * Escape exception is caught to allow user to return to main menu at any time during method's execution.
     */
    private void swapIndex() {
        Course courseSelected;
        boolean validCourse, validOtherStudent = false;
        Student otherStudent;

        if(noRegisteredCourses())
            return;

        try {
            showRegisteredCourses();
            do {
                //Get valid index to swap
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

            //Get the other student to swap indexes with
            System.out.println("\nEnter the particulars of the student to swap with:");
            otherStudent = studHandler.retrieveOtherStudent(sc);
            if (otherStudent != null)
                validOtherStudent = studHandler.checkIfRegistered(otherStudent, courseSelected);
            if (!validOtherStudent) {
                System.out.println("\nReturning to main menu!");
                waitForEnterInput();
                return;
            }

            //Check if both students are registered in the same index
            Index indexToSwapIn = studHandler.getIndexRegistered(otherStudent, courseSelected);
            if (indexToSwapIn.equals(indexToSwapOut)){
                System.out.println("\nERROR! Both of you are registered for the same index. Swap not performed.");
                System.out.println("Returning to main menu!");
                waitForEnterInput();
                return;
            }

            //Check for clashes of timetable for both students
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
                System.out.println("\nFor " + studHandler.currentStudent.getName() + ":");
                printStatusOfAddCourse(status1, indexToSwapIn);
                System.out.println("For " + otherStudent.getName() + ":");
                printStatusOfAddCourse(status2, indexToSwapOut);
                System.out.println("Updating database, please wait...");
                //Email for notifying current student
                studHandler.emailStudent(studHandler.currentStudent, otherStudent, courseSelected, indexToSwapOut, indexToSwapIn);
                //Email for notifying other student
                studHandler.emailStudent(otherStudent, studHandler.currentStudent, courseSelected, indexToSwapIn, indexToSwapOut);
                System.out.println("Database updated!\n");
            } else
                System.out.println("Swap not performed.\n" + "Exiting to main menu...");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks if the student has any currently registered or wait-listed courses.
     * @return <code>true</code> if no currently registered/wait-listed courses and <code>false</code> otherwise.
     * Used in {@link StudentInterface#dropCourseOption()}, {@link StudentInterface#changeIndex()} and {@link StudentInterface#swapIndex()}.
     */
    private boolean noRegisteredCourses(){
        HashMap<String, String> coursesRegistered = studHandler.currentStudent.getCoursesRegistered();
        HashMap<String, String> coursesWaitListed = studHandler.currentStudent.getWaitList();
        if (coursesRegistered.isEmpty() && coursesWaitListed.isEmpty()) {
            System.out.println("ERROR! No currently registered or waitlisted courses.");
            waitForEnterInput();
            return true;
        }
        return false;
    }

    /**
     * Prints indexes in the course selected and number of remaining vacancies.<p>
     * Used in {@link StudentInterface#addCourseOption()}, {@link StudentInterface#checkIndexVacancies()}
     * and {@link StudentInterface#changeIndex()}.
     * @param courseSelected The course to check in the database.
     *
     */
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

    /**
     * Prints courses and indexes currently registered or wait-listed by the student.<p>
     * Used in {@link StudentInterface#dropCourseOption()}, {@link StudentInterface#getRegisteredCourses()},
     * {@link StudentInterface#changeIndex()} and {@link StudentInterface#swapIndex()}.
     */
    private void showRegisteredCourses(){
        System.out.println("\nThese are your currently registered/wait-listed courses and indexes:");
        System.out.println(studHandler.getRegisteredCourses());
    }

    /**
     * Prints status after adding an index for the student.<p>
     * Status depends on whether student is registered for the index or added to wait-list.
     * Used in {@link StudentInterface#addCourseOption()}, {@link StudentInterface#changeIndex()}
     * and {@link StudentInterface#swapIndex()}.
     * @param indexSelected Index object to retrieve index number from.
     * @param status Status of adding a course to the current Student's timetable.
     * @see StudentHandler#addCourse(Student, Course, Index, Index, boolean)
     */
    private void printStatusOfAddCourse(int status, Index indexSelected){
        switch (status) {
            case (1) -> System.out.println("Registered for Index " + indexSelected.getIndexNum() + " successfully!\n");
            case (2) -> System.out.println("Added to wait-list for Index " + indexSelected.getIndexNum() + " successfully!\n");
        }
    }

    /**
     * Initiates logout process, confirms if user wants to logout.<p>
     * Saves data back to file by closing the handler.
     * @return true if user wishes to logout, false otherwise.
     */
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
