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
            while (true) {
                tempString = sc.nextLine();
                if (userValidator.validateInt(tempString)) {
                    choice = Integer.parseInt(tempString);
                    break;
                }
            }

            switch (choice) {
                case (1) -> addCourse();
                case (2) -> dropCourse();
                case (3) -> checkRegisteredCourses();
                case (4) -> checkIndexVacancies();
                case (5) -> changeIndex();
                case (6) -> swapIndex();
                case (7) -> logout();
            }
        } while (choice != 7);
    }

    //Used in adding of course, changing index and swapping index
    private ArrayList<Index> getIndexesInCourse(Course courseSelected){

        ArrayList<Index> indexList = courseSelected.getIndexes();

        System.out.println("You have selected: " +
                           courseSelected.getCourseName() + ", " +
                           courseSelected.getCourseCode() + ".\n" +
                           "Index Number | Remaining Vacancies" +
                           "----------------------------------");

        for (Index index : indexList)
            System.out.println(index);

        return indexList;
    }

    //Used in adding of course, dropping of course, changing and swapping index
    private Course getCourseInputAndCheck(String courseCode){
        Course courseSelected = studHandler.cdm.getCourse(courseCode);

        if (courseSelected == null)
            System.out.println("Course does not exist in the database!" +
                               "Please re-enter course again.");

        return courseSelected;
    }

    //Used in adding of course, changing index and swapping index
    private Index getIndexInputAndCheck(Course courseSelected, String indexNum){
        Index indexSelected = courseSelected.getIndex(indexNum);

        if (indexSelected == null)
            System.out.println("Index does not exist in this course!\n" +
                               "Please re-enter index again.");

        return indexSelected;
    }

    //Default when an integer representing clash is returned
    private void printStatusOfAddCourse(int status, Index indexSelected){
        switch (status) {
            case (1) -> System.out.println("Added to wait-list for Index " + indexSelected.getIndexNum() + "successfully!");
            case (2) -> System.out.println("Registered for Index " + indexSelected.getIndexNum() + "successfully!");
            default -> System.out.println("Unsuccessful, clash found with Index" + status + "!");
        }
    }

    private void addCourse() {
        String courseCode;
        String indexNum;
        try {
            while (true) {
                System.out.print("Enter course to add (e.g. CZ2002):");
                courseCode = getInput();
                if (courseValidator.validateCourseCode(courseCode)) {
                    break;
                }
            }

            Course courseSelected = null;
            while (courseSelected == null)
                courseSelected = getCourseInputAndCheck(courseCode);

            if (studHandler.willGoOverMaxAU(courseSelected)) {
                System.out.println("Cannot register for course, will exceed maximum AUs!");
                return;
            }

            while (true) {
                System.out.println("Enter the index you would like to enroll in.\n" +
                        "You will be added to wait-list if you choose an index with no vacancies:");
                indexNum = getInput();
                if (courseValidator.validateIndexNum(indexNum)) {
                    break;
                }
            }

            Index indexSelected = null;
            while (indexSelected == null)
                indexSelected = getIndexInputAndCheck(courseSelected, indexNum);

            System.out.println("You have selected to add : \n" +
                    courseSelected.getCourseCode() + " " + courseSelected.getCourseName() + "\n" +
                    "Index Number: " + indexSelected.getIndexNum());

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, null);
            printStatusOfAddCourse(status, indexSelected);
        } catch (EscapeException e) {
            return;
        }
    };

    private void dropCourse() {
        String courseCode;
        checkRegisteredCourses();
        try {
            while (true) {
                System.out.print("Enter course to drop (e.g. CZ2002):");
                courseCode = getInput();
                if (courseValidator.validateCourseCode(courseCode)) {
                    break;
                }
            }
            Course courseSelected = null;
            while (courseSelected == null)
                courseSelected = getCourseInputAndCheck(courseCode);

            if (!studHandler.currentStudent.getCoursesRegistered().containsKey(courseSelected)) {
                System.out.println("You are not enrolled in this course!");
                return;
            }

            Index index = studHandler.currentStudent.retrieveIndex(courseSelected);
            System.out.println("You have selected to drop: \n" +
                    courseSelected.getCourseCode() + " " + courseSelected.getCourseName() + "\n" +
                    "Index Number: " + index.getIndexNum());
            studHandler.dropCourse(courseSelected, index);
            System.out.println("Successfully dropped " + index.getIndexNum() + "!");
        } catch (EscapeException e) {
            return;
        }
    };

    private void checkRegisteredCourses(){
        System.out.println("Here are your currently registered courses:");
        System.out.println(studHandler.getRegisteredCourses());
    };

    private void checkIndexVacancies(){
        try {
            String courseCode;
            while (true) {
                System.out.println("Enter course to check (e.g. CZ2002):");
                courseCode = getInput();
                if (courseValidator.validateCourseCode(courseCode)) {
                    break;
                }
            }
            Course courseSelected = studHandler.cdm.getCourse(courseCode);

            if (courseSelected == null) {
                System.out.println("Course does not exist!");
                return;
            }

            System.out.println("Here are the vacancies for the indexes in this course:");
            getIndexesInCourse(courseSelected);
        } catch (EscapeException e) {
            return;
        }
    };

    private void changeIndex() {
        String courseCode;
        String indexNum;
        try {
            System.out.println(studHandler.getRegisteredCourses());
            while (true) {
                System.out.print("Choose course for changing of index (e.g. CZ2002):");
                courseCode = getInput();
                if (courseValidator.validateCourseCode(courseCode)) {
                    break;
                }
            }

            Course courseSelected = null;
            while (courseSelected == null)
                courseSelected = getCourseInputAndCheck(courseCode);

            if (!studHandler.currentStudent.getCoursesRegistered().containsKey(courseSelected)) {
                System.out.println("You are not enrolled in this course!");
                return;
            }

            Index indexToDrop = this.studHandler.currentStudent.retrieveIndex(courseSelected);

            while (true) {
                System.out.println("Enter the index to swap to.\n" +
                        "You will be dropped from course and added to wait-list if index with no vacancies chosen:");
                indexNum = getInput();
                if (courseValidator.validateIndexNum(indexNum)) {
                    break;
                }
            }

            Index indexSelected = null;
            while(indexSelected == null)
                indexSelected = getIndexInputAndCheck(courseSelected, indexNum);

            if (indexSelected.equals(indexToDrop)) {
                System.out.println("You have chosen the index you are currently in!");
                return;
            }

            int status = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexSelected, indexToDrop);
            printStatusOfAddCourse(status, indexSelected);
        } catch (EscapeException e) {
            return;
        }
    }

    private void swapIndex() {
        String courseCode;
        String indexNum;
        try {
            System.out.println(studHandler.getRegisteredCourses());
            while (true) {
                System.out.print("Choose course for swapping of index (e.g. CZ2002):");
                courseCode = getInput();
                if (courseValidator.validateCourseCode(courseCode)) {
                    break;
                }
            }

            Course courseSelected = null;
            while (courseSelected == null)
                courseSelected = getCourseInputAndCheck(courseCode);

            if (!studHandler.currentStudent.getCoursesRegistered().containsKey(courseSelected)) {
                System.out.println("You are not enrolled in this course!");
                return;
            }

            getIndexesInCourse(courseSelected);
            Index indexToSwapOut = this.studHandler.currentStudent.retrieveIndex(courseSelected);

            boolean validUser = false;
            System.out.println("Enter the particulars of the student to swap with:");
            while (!validUser) {
                try {
                    studHandler.retrieveOtherStudent(sc);
                    validUser = true;
                } catch (AccessDeniedException e){}
            }

            if (!studHandler.otherStudent.getCoursesRegistered().containsKey(courseSelected)) {
                System.out.println("The other student is not enrolled in this course!");
                return;
            }

            Index indexToSwapIn = studHandler.otherStudent.retrieveIndex(courseSelected);

            System.out.println("Summary of Indexes after swap performed:\n" +
                    courseSelected.getCourseName() + "\n");

            System.out.println(studHandler.currentStudent.getName() + " | " + studHandler.otherStudent.getName() + "\n" +
                    indexToSwapIn.getIndexNum() + " | " + indexToSwapOut.getIndexNum() + "\n" +
                    "Confirm to proceed with the swap? Press 'Y/y' to continue and any other key otherwise.");

            char ans = sc.nextLine().toCharArray()[0];
            if (ans == 'Y' || ans == 'y') {
                //Perform the swap. AddCourse() also drops the current index they were previously in
                //TODO: To ensure wait-list of indexes not triggered in the middle of swap
                int status1 = studHandler.addCourse(studHandler.currentStudent, courseSelected, indexToSwapIn, indexToSwapOut);
                int status2 = studHandler.addCourse(studHandler.otherStudent, courseSelected, indexToSwapOut, indexToSwapIn);
                System.out.println("For current student: ");
                printStatusOfAddCourse(status1, indexToSwapIn);
                System.out.println("\n For other student");
                printStatusOfAddCourse(status2, indexToSwapOut);
                studHandler.updateOtherStudent(courseSelected, indexToSwapIn, indexToSwapOut);
            } else
                System.out.println("Swap not performed.\n" + "Exiting to main menu...");
        } catch (EscapeException e) {
            return;
        }
    }

    private void logout(){
        System.out.println("Saving Data...");
        studHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    };
}
