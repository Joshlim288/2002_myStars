import java.util.ArrayList;
import java.util.Scanner;

/**
 * TODO Repeated code to be put in their own methods
 */
public class StudentInterface extends UserInterface {

    private StudentHandler studHandler;

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
            System.out.println("| 2. Drop Registered Course                                  |");
            System.out.println("| 3. Check Currently Registered Courses                      |");
            System.out.println("| 4. Check Vacancies of Course                               |");
            System.out.println("| 5. Change Index of Registered Course                       |");
            System.out.println("| 6. Swap Index of Registered Course with Another Student    |");
            System.out.println("| 7. Log Out of MyStars                                      |");
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
                case (5) -> changeIndex();
                case (6) -> swapIndex();
                case (7) -> exitFlag = logout();
            }
        } while (!exitFlag);
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

    //Default when an integer representing clash is returned
    private void printStatusOfAddCourse(int status, Index indexSelected){
        switch (status) {
            case (1) -> System.out.println("Added to wait-list for Index " + indexSelected.getIndexNum() + " successfully!");
            case (2) -> System.out.println("Registered for Index " + indexSelected.getIndexNum() + " successfully!");
        }
    }

    private void checkRegisteredCourses(){
        System.out.println("\nHere are your currently registered courses:");
        System.out.println(studHandler.getRegisteredCourses());
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
                if (validCourse)
                    if (studHandler.willGoOverMaxAU(courseSelected)) {
                        System.out.println("Cannot register for course, will exceed maximum AUs!\n");
                        validCourse = false;
                    }
                    if (studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are already enrolled in this course!");
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

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, null);
            printStatusOfAddCourse(status, indexSelected);

            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void dropCourse() {
        Course courseSelected;
        boolean validCourse;

        try {
            checkRegisteredCourses();
            do{
                System.out.print("Enter course to drop (e.g. CZ2002):");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected))
                    validCourse = false;
            } while (!validCourse);

            String index = studHandler.currentStudent.retrieveIndex(courseSelected.getCourseCode());
            System.out.println("Enter \"Y\" to confirm that you would like to drop this index: \n" +
                               courseSelected.getCourseCode() + " " + courseSelected.getCourseName() +
                               ", Index Number: " + index);

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                studHandler.dropCourse(studHandler.currentStudent, courseSelected, index);
                System.out.println("Successfully dropped " + index + "!");
            }
            else System.out.println("Index not dropped.");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void getRegisteredCourses(){
        checkRegisteredCourses();
        waitForEnterInput();
    }

    private void checkIndexVacancies(){
        try {
            String courseCode;
            Course courseSelected;
            boolean validCourse;

            do {
                System.out.println(studHandler.getCourseOverview(1));
                System.out.print("Enter course code to check (e.g. CZ2002): ");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
            } while (!validCourse);

            showIndexesInCourse(courseSelected);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeIndex() {
        Course courseSelected;
        Index indexSelected;
        boolean validCourse, validIndex;

        try {
            checkRegisteredCourses();
            do {
                System.out.print("Choose course for changing of index (e.g. CZ2002):");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse)
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are not enrolled in this course!");
                        validCourse = false;
                    }
            }while (!validCourse);

            //TODO: Got to be a better way to retrieve index to drop but I'm tired so this will do for now HAHA
            String temp = studHandler.currentStudent.retrieveIndex(courseSelected.getCourseCode());
            Index indexToDrop = studHandler.retrieveIndex(courseSelected, temp);
            showIndexesInCourse(courseSelected);

            do {
                System.out.println("Enter the index you would like to enroll in.\n" +
                            "You will be added to wait-list if you choose an index with no vacancies:");
                indexSelected = studHandler.retrieveIndex(courseSelected, getInput(typeOfInput.INDEX_NUM));
                validIndex = studHandler.checkValidIndex(indexSelected, studHandler.currentStudent, indexToDrop);
                if (studHandler.hasClash(indexSelected, studHandler.currentStudent, indexToDrop))
                    validIndex = false;
            } while (!validIndex);

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, indexToDrop);
            printStatusOfAddCourse(status, indexSelected);
            studHandler.refreshWaitList(courseSelected, indexToDrop);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void swapIndex() {
        Course courseSelected;
        boolean validCourse, validUser;
        //TODO: Shift student to here
        Student otherStudent;

        try {
            checkRegisteredCourses();
            do {
                System.out.print("Choose course for swapping of index (e.g. CZ2002):");
                courseSelected = studHandler.retrieveCourse(getInput(typeOfInput.COURSE_CODE));
                validCourse = studHandler.checkValidCourse(courseSelected);
                if (validCourse)
                    if (!studHandler.checkIfRegistered(studHandler.currentStudent, courseSelected)) {
                        System.out.println("You are not enrolled in this course!");
                        validCourse = false;
                    }
            }while (!validCourse);

            //TODO: Got to be a better way to retrieve index to drop but I'm tired so this will do for now HAHA
            String temp1 = studHandler.currentStudent.retrieveIndex(courseSelected.getCourseCode());
            Index indexToSwapOut = studHandler.retrieveIndex(courseSelected, temp1);

            //TODO: Someone please try to check this part I'm too tired to test the login
            do {
                System.out.println("Enter the particulars of the student to swap with:");
                    try {
                        studHandler.retrieveOtherStudent(sc);
                    } catch (AccessDeniedException e){
                        System.out.println(e.getMessage());
                    }
                    validUser = studHandler.checkIfRegistered(studHandler.otherStudent, courseSelected);
            } while (!validUser);

            //TODO: Got to be a better way to retrieve index to drop but I'm tired so this will do for now HAHA
            String temp2 = studHandler.otherStudent.retrieveIndex(courseSelected.getCourseCode());
            Index indexToSwapIn = studHandler.retrieveIndex(courseSelected, temp2);

            //TODO: Include these in the loop
            boolean currentStudentClash = studHandler.hasClash(indexToSwapIn, studHandler.currentStudent, indexToSwapOut);
            boolean otherStudentClash = studHandler.hasClash(indexToSwapOut, studHandler.otherStudent, indexToSwapIn);
            if (currentStudentClash || otherStudentClash)
                return;

            System.out.println("\nSummary of Indexes after swap performed:\n" +
                    courseSelected.getCourseName());

            System.out.println(studHandler.currentStudent.getName() + " | " + studHandler.otherStudent.getName() + "\n" +
                    indexToSwapIn.getIndexNum() + " | " + indexToSwapOut.getIndexNum() + "\n" +
                    "Confirm to proceed with the swap? Press 'Y/y' to continue and any other key otherwise.");

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                int status1 = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexToSwapIn, indexToSwapOut);
                int status2 = studHandler.addCourse(studHandler.otherStudent, courseSelected, indexToSwapOut, indexToSwapIn);
                System.out.println("For current student: ");
                printStatusOfAddCourse(status1, indexToSwapIn);
                System.out.println("\n For other student");
                printStatusOfAddCourse(status2, indexToSwapOut);
                studHandler.updateOtherStudent(courseSelected, indexToSwapIn, indexToSwapOut);
            } else
                System.out.println("Swap not performed.\n" + "Exiting to main menu...");
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
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
