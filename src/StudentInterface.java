import java.util.ArrayList;
import java.util.Scanner;

/**
 * TODO shift all dataManager calls to handler
 * TODO Check all loops to ensure proper handling
 */
public class StudentInterface extends UserInterface {

    private StudentHandler studHandler;

    public StudentInterface(User currentUser, Scanner sc) {
        super(sc);
        String matricNum = ((Student)currentUser).getMatricNum();
        studHandler = new StudentHandler(matricNum);
        System.out.println("\nWelcome, Student " + currentUser.getName() + "!");
    }

    /**
     * Student UI is displayed here
     */
    public void start() {
        int choice;

        do {
            System.out.println("What would you like to do today?");
            System.out.println("--------------------------------------------------------");
            System.out.println("1. Add New Course");
            System.out.println("2. Drop Registered Course");
            System.out.println("3. Check Currently Registered Courses");
            System.out.println("4. Check Vacancies of Course");
            System.out.println("5. Change Index of Registered Course");
            System.out.println("6. Swap Index of Registered Course with Another Student");
            System.out.println("7. Log Out of MyStars");
            System.out.println("(Enter ~ at any time to return back to main menu)");

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
                case (7) -> logout();
            }
        } while (choice != 7);
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

    //Used in addCourse(), dropCourse(), changeIndex() and swapIndex()
    private Course getCourseInputAndCheck(String courseCode){
        Course courseSelected = studHandler.cdm.getCourse(courseCode);

        if (courseSelected == null)
            System.out.println("Course does not exist in the database!\n" +
                               "Please re-enter course again.\n");

        return courseSelected;
    }

    //Used in addCourse(), changeIndex() and swapIndex()
    private Index getIndexInputAndCheck(Course courseSelected, String indexNum){
        Index indexSelected = courseSelected.getIndex(indexNum);

        if (indexSelected == null)
            System.out.println("Index does not exist in this course!\n" +
                               "Please re-enter index again.");

        return indexSelected;
    }

    private boolean checkForClash(Index indexSelected, Student studentToCheck, Index indexToExclude){
        String indexClashed = studHandler.hasClash(indexSelected, studentToCheck, indexToExclude);
        if (indexClashed != null) {
            System.out.println("There is a clash with Index " + indexClashed + "!");
            System.out.println("Please choose another index!");
            waitForEnterInput();
            return true;
        }
        return false;
    }

    private boolean checkIfRegistered(Student studentToCheck, String courseToCheck){
        if(!studentToCheck.getCoursesRegistered().containsKey(courseToCheck)){
            System.out.println("Student not enrolled in this course!");
            return false;
        }
        return true;
    }

    private void checkRegisteredCourses(){
        System.out.println("\nHere are your currently registered courses:");
        System.out.println(studHandler.getRegisteredCourses());
    }

    private void addCourse() {
        String courseCode, indexNum;
        Course courseSelected;
        Index indexSelected;
        boolean validCourse = false, validIndex = false;

        try {
            do {
                System.out.print("Enter course to add (e.g. CZ2002): ");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                courseSelected = getCourseInputAndCheck(courseCode);

                if (courseSelected == null) continue;
                else if (studHandler.willGoOverMaxAU(courseSelected))
                    System.out.println("Cannot register for course, will exceed maximum AUs!\n");
                else if (studHandler.studentInCourse(courseSelected))
                    System.out.println("You are already enrolled in this course!\n");
                else validCourse = true;
            } while (!validCourse);

            do {
                showIndexesInCourse(courseSelected);
                System.out.println("Enter the index you would like to enroll in.\n" +
                        "You will be added to wait-list if you choose an index with no vacancies:");
                indexNum = getInput(typeOfInput.INDEX_NUM);
                indexSelected = getIndexInputAndCheck(courseSelected, indexNum);
                if (!checkForClash(indexSelected, studHandler.currentStudent, null))
                    validIndex = true;
            } while (validIndex);

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
        String courseCode;
        Course courseSelected;
        checkRegisteredCourses();
        boolean validCourse = false;

        try {
            do{
                System.out.print("Enter course to drop (e.g. CZ2002):");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                courseSelected = getCourseInputAndCheck(courseCode);

                if (courseSelected == null) continue;
                else if(checkIfRegistered(studHandler.currentStudent, courseCode))
                    validCourse = true;

            } while (!validCourse);

            String index = studHandler.currentStudent.retrieveIndex(courseCode);
            System.out.println("Enter \"Y\" to confirm that you would like to drop this index: \n" +
                               courseCode + " " + courseSelected.getCourseName() + ", Index Number: " + index);

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

            do {
                System.out.print("Enter course code to check (e.g. CZ2002): ");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                courseSelected = getCourseInputAndCheck(courseCode);
            } while (courseSelected == null);

            showIndexesInCourse(courseSelected);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void changeIndex() {
        String courseCode = "";
        String indexNum;

        //TODO: Very spaghetti, some parts seem repetitive
        try {
            System.out.println(studHandler.getRegisteredCourses());
            Course courseSelected = null;
            while (courseSelected == null) {
                System.out.print("Choose course for changing of index (e.g. CZ2002):");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                courseSelected = getCourseInputAndCheck(courseCode);
                if (courseSelected == null)
                    continue;
                if (!checkIfRegistered(studHandler.currentStudent, courseCode))
                    courseSelected = null;
            }

            Index indexToDrop = getIndexInputAndCheck(courseSelected, this.studHandler.currentStudent.retrieveIndex(courseCode));
            showIndexesInCourse(courseSelected);
            Index indexSelected;

            do {
                showIndexesInCourse(courseSelected);
                System.out.println("Enter the index to swap to.\n" +
                        "You will be dropped from course and added to wait-list if index with no vacancies chosen:");
                indexNum = getInput(typeOfInput.INDEX_NUM);
                indexSelected = getIndexInputAndCheck(courseSelected, indexNum);
                if (checkForClash(indexSelected, studHandler.currentStudent, indexToDrop))
                    indexSelected = null;
            } while (indexSelected == null);

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, indexToDrop);
            printStatusOfAddCourse(status, indexSelected);
            studHandler.refreshWaitList(courseSelected, indexToDrop);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void swapIndex() {
        String courseCode = "";

        //TODO: Very spaghetti
        try {
            System.out.println(studHandler.getRegisteredCourses());
            Course courseSelected = null;
            while (courseSelected == null) {
                System.out.print("Choose course for swapping of index (e.g. CZ2002):");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                courseSelected = getCourseInputAndCheck(courseCode);
                if (courseSelected == null)
                    continue;
                if (!checkIfRegistered(studHandler.currentStudent, courseCode))
                    courseSelected = null;
            }

            showIndexesInCourse(courseSelected);
            Index indexToSwapOut = getIndexInputAndCheck(courseSelected, this.studHandler.currentStudent.retrieveIndex(courseCode));

            boolean validUser;

            //TODO: I think some inputs will crash here
            do {
                System.out.println("Enter the particulars of the student to swap with:");
                    try {
                        studHandler.retrieveOtherStudent(sc);
                    } catch (AccessDeniedException e){
                        System.out.println(e.getMessage());
                    }
                    validUser = checkIfRegistered(studHandler.otherStudent, courseCode);
            } while (validUser == false);

            Index indexToSwapIn = getIndexInputAndCheck(courseSelected, studHandler.otherStudent.retrieveIndex(courseCode));

            //TODO: Include these in the loop
            boolean currentStudentClash = checkForClash(indexToSwapOut, studHandler.currentStudent, indexToSwapIn);
            boolean otherStudentClash = checkForClash(indexToSwapIn, studHandler.otherStudent, indexToSwapOut);
            if (currentStudentClash || otherStudentClash)
                return;

            System.out.println("\nSummary of Indexes after swap performed:\n" +
                    courseSelected.getCourseName());

            System.out.println(studHandler.currentStudent.getName() + " | " + studHandler.otherStudent.getName() + "\n" +
                    indexToSwapIn.getIndexNum() + " | " + indexToSwapOut.getIndexNum() + "\n" +
                    "Confirm to proceed with the swap? Press 'Y/y' to continue and any other key otherwise.");

            char ans = getInput(typeOfInput.STANDARD).toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                //Perform the swap. AddCourse() also drops the current index they were previously in
                //TODO: Wait-list of index should not be triggered because I separated the refreshing of wait-list
                //TODO: to a different method that is not called here. But should still test
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

    private void logout(){
        System.out.println("\nSaving Data...");
        studHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    }
}
