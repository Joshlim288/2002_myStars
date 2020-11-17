import java.util.Scanner;
import java.util.ArrayList;

/**
 * Control class to display to the Admin what functions are available to them
 * Gets input for performing these functions and passes them to the handler to be executed
 * TODO allow forceful adding of student to course
 * TODO test deletion of index and lesson
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-10-24
 */
public class AdminInterface extends UserInterface {

    private final AdminHandler adHandler;

    /**
     * Constructor for the Admin Interface, called from UserInterfaceCreator
     * @param currentUser User which has logged in
     * @param sc Scanner to be used for getting input
     */
    public AdminInterface (User currentUser, Scanner sc) {
        super(sc);
        adHandler = new AdminHandler();
        System.out.println("\nWelcome, " + currentUser.getName() + "!");
    }

    /**
     * Menu for Admins is displayed here
     */
    public void start() {
        int choice;
        boolean exitFlag = false;
        do{
            System.out.println("\n-------------------------------------------------------");
            System.out.println("| What would you like to do today?                    |");
            System.out.println("-------------------------------------------------------");
            System.out.println("|  1. Add a new Student                               |");
            System.out.println("|  2. Add a new Course                                |");
            System.out.println("|  3. Check vacancy of an Index                       |");
            System.out.println("|  4. Check students registered in an Index           |");
            System.out.println("|  5. Check students registered in a Course           |");
            System.out.println("|  6. Update a Course's Details                       |");
            System.out.println("|  7. Update a Student's Details                      |");
            System.out.println("|  8. Print Overview of Database                      |");
            System.out.println("|  9. Delete a Student from Database                  |");
            System.out.println("| 10. Delete a Course from Database                   |");
            System.out.println("| 11. Force enroll Student on wait list               |");
            System.out.println("| 12. Log Out of MyStars                              |");
            System.out.println("| (Enter ~ at any time to exit back to previous menu) |");
            System.out.println("-------------------------------------------------------");

            try {
                System.out.print("Please enter your choice: ");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
            } catch (EscapeException e) {
                logout();
                return;
            }
            switch(choice){
                case (1)-> createStudent();
                case (2)-> createCourse();
                case (3)-> checkIndex();
                case (4)-> printByIndex();
                case (5)-> printByCourse();
                case (6)-> updateCourse();
                case (7)-> updateStudent();
                case (8)-> printOverview();
                case (9)-> deleteStudent();
                case (10)-> deleteCourse();
                case (11)-> forceEnroll();
                case (12)-> exitFlag = logout();
                default -> System.out.println("ERROR: Invalid menu option selected.");
            }
        } while (!exitFlag);
    }

    /**
     * Get input for the creation of a new Course object
     * Escape exception is handled within this method as we need to remove the added Course object from the database
     * if the user wishes to abort creation of the Course
     */
    private void createCourse() {
        String courseCode = null;
        String school;
        String courseName;
        String courseType;
        String examStart;
        String examEnd;
        boolean hasExams;
        int aus;
        try {
            do {
                System.out.print("Enter course code: ");
                courseCode = getInput(typeOfInput.COURSE_CODE);

                System.out.print("Enter school: ");
                school = getInput(typeOfInput.NAME);

                System.out.print("Enter course name: ");
                courseName = getInput(typeOfInput.NAME);

                System.out.print("Enter course type (CORE, MPE, GER, UE): ");
                courseType = getInput(typeOfInput.COURSE_TYPE);

                System.out.print("Enter academic units: ");
                aus = Integer.parseInt(getInput(typeOfInput.INT));

                System.out.print("Enter 'y' if this course has final exams, any keys otherwise: ");
                hasExams = getInput(typeOfInput.STANDARD).equals("y");

                if (hasExams) {
                    do {
                        System.out.print("Enter exam start datetime: ");
                        examStart = getInput(typeOfInput.DATETIME);

                        System.out.print("Enter exam end datetime: ");
                        examEnd = getInput(typeOfInput.DATETIME);
                    } while (!courseValidator.validateDateTimePeriod(examStart, examEnd));
                }
                else {
                    examStart = null;
                    examEnd = null;
                }

            } while (!adHandler.addCourse(courseCode, courseName, courseType, aus, school, hasExams, examStart, examEnd));

            System.out.print("Enter number of indexes: ");
            int numIndexes = Integer.parseInt(getInput(typeOfInput.INT));
            for (int i = 0; i < numIndexes; i++) {
                System.out.printf("\nCreating Index %d:\n", i + 1);
                createIndex(courseCode);
            }
            System.out.println("Course successfully added");
            System.out.println(adHandler.getCourseOverview(1));
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
            adHandler.removeCourse(courseCode); // Removes the unfinished Course object from the database
        }
    }

    /**
     * Get input for the creation of a new Index object
     * @param courseCode Course Code of the Course where the Index is to be created
     * @throws EscapeException Occurs when the User enters "~" to abort function
     */
    private void createIndex(String courseCode) throws EscapeException {
        String indexNum;
        int indexVacancies;
        int numLessons;
        String group;

            do {
                System.out.print("Enter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);

                System.out.print("Enter number of vacancies: ");
                indexVacancies = Integer.parseInt(getInput(typeOfInput.INT));

                System.out.print("Enter group name for the index: ");
                group = getInput(typeOfInput.GROUP_NAME);
            } while (!adHandler.addIndex(courseCode, indexNum, indexVacancies, group));
        try {
            System.out.print("Enter number of lessons: ");
            numLessons = Integer.parseInt(getInput(typeOfInput.INT));
            for (int i = 0; i < numLessons; i++) {
                System.out.printf("\nCreating lesson %d:\n", i + 1);
                createLesson(courseCode, indexNum);
            }
        } catch (EscapeException e) {
            adHandler.removeIndex(courseCode, indexNum);
            throw e;
        }
    }

    /**
     * Get input for the creation of a new Lesson object
     * @param courseCode Course Code for Course that contains the relevant Index
     * @param indexNum Index number of Index to create the new Lesson object in
     * @throws EscapeException Occurs when the User enters "~" to abort function
     */
    private void createLesson(String courseCode, String indexNum) throws EscapeException{
        String lessonType;
        String day;
        String startTime;
        String endTime;
        String venue;
        String[] inputWeeks;
        ArrayList<Integer> teachingWeeks;
        do {
            System.out.print("Enter lesson type (LEC, TUT, LAB, DES, PRJ, SEM): ");
            lessonType = getInput(typeOfInput.LESSON_TYPE);

            System.out.print("Enter day of week (First 3 letters of day): ");
            day = getInput(typeOfInput.DAY);

            do {
                System.out.print("Enter start time (HH:MM): ");
                startTime = getInput(typeOfInput.TIME);

                System.out.print("Enter end time (HH:MM): ");
                endTime = getInput(typeOfInput.TIME);
            } while (!courseValidator.validateTimePeriod(startTime, endTime));

            System.out.print("Enter venue: ");
            venue = getInput(typeOfInput.STANDARD);

            do {
                System.out.print("Enter teaching weeks (1-13), separated with a comma: ");
                inputWeeks = getInput(typeOfInput.STANDARD).split(",");
                teachingWeeks = new ArrayList<>();
                for (String week : inputWeeks) {
                    if (courseValidator.validateTeachingWeek(week)) {
                        teachingWeeks.add(Integer.parseInt(week));
                    } else {
                        System.out.println("Please enter the teaching weeks again.");
                        break;
                    }
                }
            } while (inputWeeks.length != teachingWeeks.size());
        } while (!adHandler.addLesson(courseCode, indexNum, lessonType, day, startTime, endTime,
                venue, teachingWeeks));
    }

    /**
     * Get input for the creation of a new Student object
     */
    private void createStudent() {
        try {
            String studentName;
            String studentMatric;
            String email;
            String gender;
            String nationality;
            String userid;
            String password;
            String major;
            String startAccessPeriod;
            String endAccessPeriod;
            int maxAUs;

            do {
                System.out.print("Enter Matriculation Number of Student: ");
                studentMatric = getInput(typeOfInput.MATRIC_NUM);

                System.out.print("Enter student name: ");
                studentName = getInput(typeOfInput.NAME);

                System.out.print("Enter email: ");
                email = getInput(typeOfInput.EMAIL);

                System.out.print("Enter gender: ");
                gender = getInput(typeOfInput.GENDER);

                System.out.print("Enter nationality: ");
                nationality = getInput(typeOfInput.NAME);

                System.out.print("Enter userID: ");
                userid = getInput(typeOfInput.USERID);

                System.out.print("Enter password: ");
                password = new String(System.console().readPassword());

                System.out.print("Enter major: ");
                major = getInput(typeOfInput.NAME);

                System.out.print("Enter maxAUs: ");
                maxAUs = Integer.parseInt(getInput(typeOfInput.INT));

                System.out.print("Enter start access period: ");
                startAccessPeriod = getInput(typeOfInput.DATETIME);

                System.out.print("Enter end access period: ");
                endAccessPeriod = getInput(typeOfInput.DATETIME);
            } while (!adHandler.addStudent(userid, password, studentName, studentMatric, email,
                    gender, nationality, major, maxAUs, startAccessPeriod, endAccessPeriod));
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }

        for (Student stud: adHandler.getStudents()){
            System.out.println("\n" + stud.getName() + ", " + stud.getMatricNum());
        }
        waitForEnterInput();
    }

    /**
     * Displays the vacancies of an Index
     */
    private void checkIndex() {
        try {
            String indexNum;
            do {
                System.out.print("\nEnter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);
            } while (!adHandler.checkIndexExists(indexNum));
            adHandler.printIndexVacancy(indexNum);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints the list of Students from a specified Index
     */
    private void printByIndex() {
        try {
            String indexNum;
            ArrayList<Student> studentList;
            do {
                System.out.print("Enter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);

                studentList = adHandler.getStudentListByIndex(indexNum, false);
            } while (studentList == null);

            System.out.println("\nStudents currently registered for Index " + indexNum + ": ");
            for (Student stud : studentList) {
                System.out.println(stud.getMatricNum() +", "+ stud.getName());
            }
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Prints the list of Students from a specified Course
     */
    private void printByCourse() {
        try {
            String courseCode;
            ArrayList<Student> studentList;
            do {
                System.out.print("Enter course code: ");
                courseCode = getInput(typeOfInput.COURSE_CODE);

                studentList = adHandler.getStudentListByCourse(courseCode);
            } while (studentList == null);

            System.out.println("\nStudents currently registered for " + courseCode + ":");
            for (Student stud : studentList) {
                System.out.println(stud.getMatricNum() +", "+ stud.getName());
            }
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get input for updating a Course object
     * Will relay the user's input and which attribute of the Course the user wishes to edit to the handler
     * Some parameters cannot be edited if there are Students already enrolled in the course
     * This is to ensure Students are not unfairly removed from courses that they have planned their timetables around
     */
    private void updateCourse() {
        try {
            String courseCode;
            String changedValue;
            int choice;
            System.out.println("Courses:");
            for (Course crs: adHandler.getCourses()){
                System.out.println(crs);
            }
            while (true) {
                System.out.print("Enter course code to edit: ");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                if (adHandler.checkCourseExists(courseCode)) {
                    break;
                }
                System.out.println("Course does not exist");
            }

            do {
                System.out.println("\nWhat attribute would you like to edit?");
                System.out.println(" 1: courseCode\n" +
                        " 2: courseName\n" +
                        " 3: courseType\n" +
                        " 4: academicUnits\n" +
                        " 5: school\n" +
                        " 6: edit existing indexes\n" +
                        " 7: add new index\n" +
                        " 8: delete an index\n" +
                        " 9: examDate\n" +
                        "10: exit\n" +
                        "--------------------------");
                System.out.print("Choose attribute to edit:");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit course code
                        do {
                            System.out.print("Enter new course code: ");
                            changedValue = getInput(typeOfInput.COURSE_CODE);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                        courseCode = changedValue; // to allow further edits of same course in the same method call
                    }
                    case (2) -> { // edit course name
                        do {
                            System.out.print("Enter new course name: ");
                            changedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (3) -> { // edit course type
                        do {
                            System.out.print("Enter new course type (CORE, MPE, GER, UE): ");
                            changedValue = getInput(typeOfInput.COURSE_TYPE);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (4) -> { // edit academic units
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, AUs cannot be changed");
                            waitForEnterInput();
                            continue;
                        }
                        do {
                            System.out.print("Enter new academic units: ");
                            changedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (5) -> { // edit school
                        do {
                            System.out.print("Enter new school name: ");
                            changedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (6) -> editIndex(courseCode); // edit index
                    case (7) -> createIndex(courseCode); // create index
                    case (8) -> {
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, Indexes cannot be deleted");
                            waitForEnterInput();
                            continue;
                        }
                        for (Index idx: adHandler.getIndexes(courseCode)) {
                            System.out.println(idx);
                        }
                        do {
                            System.out.print("Enter index number to delete: ");
                            changedValue = getInput(typeOfInput.INDEX_NUM);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (9) -> { // edit finals
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, Final exams cannot be changed");
                            waitForEnterInput();
                            continue;
                        }
                        String newStart;
                        String newEnd;
                        do {
                            System.out.print("Enter new exam start datetime: ");
                            newStart = getInput(typeOfInput.DATETIME);
                            System.out.print("Enter new exam end datetime: ");
                            newEnd = getInput(typeOfInput.DATETIME);
                        } while (!userValidator.validateDateTimePeriod(newStart, newEnd) ||
                                !adHandler.editCourse(courseCode, newStart+"&"+newEnd, choice));
                    }
                    case (10) -> System.out.println("Exiting update course..."); // exit
                    default -> System.out.println("ERROR: Invalid menu option selected");
                }
                if (choice >= 0 && choice < 10) {
                    System.out.println("Successfully changed");
                    waitForEnterInput();
                }
            } while (choice != 10);
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get input for updating an index object
     * Will relay the user's input and which attribute of the Index the user wishes to edit to the handler
     * Some parameters cannot be edited if there are Students already enrolled in the course
     * This is to ensure Students are not unfairly removed from courses that they have planned their timetables around
     * @param courseCode Course code of Course object which contains the Index we wish to edit
     */
    private void editIndex(String courseCode) {
        try {
            String changedValue;
            String indexNum;
            int choice;
            System.out.println("Indexes:");
            for(Index idx: adHandler.getIndexes(courseCode)){
                System.out.println(idx);
            }
            while(true) {
                System.out.print("Enter index number to edit: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);
                if (adHandler.checkIndexExists(indexNum)){
                    break;
                }
                System.out.println("Index number not found");
            }

            do {
                System.out.println("\nWhat attribute would you like to edit?");
                System.out.println("1: indexNum\n" +
                        "2: indexVacancy\n" +
                        "3: group\n" +
                        "4. add a new lesson\n" +
                        "5: edit existing lessons\n" +
                        "6: remove a lesson\n" +
                        "7: exit\n" +
                        "--------------------------");
                System.out.print("Choose attribute to edit:");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit index num
                        do {
                            System.out.print("Enter new index number: ");
                            changedValue = getInput(typeOfInput.INDEX_NUM);
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                        indexNum = changedValue; // to allow further edits of same index in the same method call
                    }
                    case (2) -> {
                        do { // edit index vacancies
                            System.out.print("Enter new index vacancies: ");
                            changedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                    }
                    case (3) -> {
                        do { // edit group number
                            System.out.print("Enter new group number for index: ");
                            changedValue = getInput(typeOfInput.GROUP_NAME);
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                    }
                    case (4) -> {
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, Lessons cannot be added");
                            waitForEnterInput();
                            continue;
                        }
                        createLesson(courseCode, indexNum);
                    }
                    case (5) -> {
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, Lessons cannot be edited");
                            waitForEnterInput();
                            continue;
                        }
                        editLesson(courseCode, indexNum);
                    }
                    case (6) -> {
                        if(adHandler.checkCourseOccupied(courseCode)){
                            System.out.println("Course already has students enrolled, Lessons cannot be deleted");
                            waitForEnterInput();
                            continue;
                        }
                        int i = 1;
                        for (Lesson lsn: adHandler.getLessons(courseCode, indexNum)) {
                            System.out.println("\n"+i+":");
                            System.out.println(lsn);
                            i++;
                        }
                        do {
                            System.out.print("Enter number of lesson to delete: ");
                            changedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    }
                    case (7) -> System.out.println("Exiting update index...");
                    default -> System.out.println("ERROR: Invalid menu option selected");
                }
                if (choice >= 0 && choice < 7) {
                    System.out.println("Successfully changed");
                    waitForEnterInput();
                }
            } while (choice != 7);

        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Get input for updating a Lesson object
     * Will relay the user's input and which attribute of the Lesson the user wishes to edit to the handler
     * Lessons cannot be modified if there are Students already enrolled in the course
     * This is to ensure Students are not unfairly removed from courses that they have planned their timetables around
     * @param courseCode Course code of Course object which contains the Index we wish to edit
     * @param indexNum Index Number of Index object which contains the Lesson we wish to edit
     */
    private void editLesson(String courseCode, String indexNum) {
        try {
            String changedValue;
            int lessonIndex;
            int choice;
            int i=1;
            System.out.println("Lessons:");
            for (Lesson lsn: adHandler.getLessons(courseCode, indexNum)){
                System.out.println("\n" + i +":\n"+lsn);
                i++;
            }
            while(true) {
                System.out.print("Enter lesson number to edit:");
                lessonIndex = Integer.parseInt(getInput(typeOfInput.INT))-1;
                if (lessonIndex<=i && lessonIndex>0){
                    break;
                }
                System.out.println("Index out of range, enter lesson number < "+ i);
            }

            do {
                System.out.println("\nWhat attribute would you like to edit?");
                System.out.println("1: lessonType\n" +
                        "2: day\n" +
                        "3: lesson time\n" +
                        "4: venue\n" +
                        "5: teaching weeks\n" +
                        "6: exit\n"+
                        "--------------------------");
                System.out.print("Choose attribute to edit:");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit lesson type
                        do {
                            System.out.print("Enter new lesson type (LEC, TUT, LAB, DES, PRJ, SEM): ");
                            changedValue = getInput(typeOfInput.LESSON_TYPE);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonIndex, changedValue, choice));
                    }
                    case (2) -> { // edit day
                        do {
                            System.out.print("Enter new lesson day: ");
                            changedValue = getInput(typeOfInput.DAY);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonIndex, changedValue, choice));
                    }
                    case (3) -> { // edit time
                        String newStart;
                        String newEnd;
                        do {
                            System.out.print("Enter new start time: ");
                            newStart = getInput(typeOfInput.TIME);
                            System.out.print("Enter new end time: ");
                            newEnd = getInput(typeOfInput.TIME);
                        } while (!courseValidator.validateTimePeriod(newStart, newEnd) ||
                                !adHandler.editLesson(courseCode, indexNum, lessonIndex, newStart+"&"+newEnd, choice));
                    }
                    case (4) -> { // edit venue
                        do {
                            System.out.println("Enter new venue: ");
                            changedValue = getInput(typeOfInput.STANDARD);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonIndex, changedValue, choice));
                    }
                    case (5) -> { // edit teaching weeks
                        String[] inputWeeks;
                        boolean validWeeks;
                        do {
                            System.out.print("Enter new teaching weeks, separated with a comma (1-13): ");
                            changedValue = getInput(typeOfInput.STANDARD);
                            inputWeeks = changedValue.split(",");
                            validWeeks = true;
                            for (String week : inputWeeks) {
                                if (!courseValidator.validateTeachingWeek(week)) {
                                    System.out.println("Please enter the teaching weeks again.");
                                    validWeeks = false;
                                    break;
                                }
                            }
                        } while (!validWeeks);
                        adHandler.editLesson(courseCode, indexNum, lessonIndex, changedValue, choice);
                    }
                    case (6) -> System.out.println("Exiting update lesson...");
                    default -> System.out.println("ERROR: Invalid menu option selected");
                }
                if (choice >= 0 && choice < 6) {
                    System.out.println("Successfully changed");
                    waitForEnterInput();
                }
            } while (choice != 6);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Get input for updating a Student object
     */
    private void updateStudent(){
        try {
            String matric;
            String updatedValue;
            int choice;

            System.out.println(adHandler.getStudentOverview());

            while(true) {
                System.out.print("Enter Matriculation Number of Student to edit: ");
                matric = getInput(typeOfInput.MATRIC_NUM);
                if(adHandler.checkStudentExists(matric)){
                    break;
                }
                System.out.println("Student not found, please try again");
            }

            do {
                System.out.println("Choose attribute to edit: ");
                System.out.print(" 1: userID\n" +
                                 " 2: Password\n" +
                                 " 3: Name\n" +
                                 " 4: matricNum\n" +
                                 " 5: email\n" +
                                 " 6: gender\n" +
                                 " 7: nationality\n" +
                                 " 8: major\n" +
                                 " 9: maxAUs\n" +
                                 "10: access period\n" +
                                 "11: exit\n" +
                                 "--------------------------\n" +
                                 "Enter choice: ");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // user id
                        do {
                            System.out.print("Enter new user ID: ");
                            updatedValue = getInput(typeOfInput.USERID);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (2) -> { // password
                        do {
                            System.out.print("Enter new password: ");
                            updatedValue = new String(System.console().readPassword());
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (3) -> { // name
                        do {
                            System.out.print("Enter student's new name: ");
                            updatedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (4) -> { // matric
                        do {
                            System.out.print("Enter student's new matriculation number: ");
                            updatedValue = getInput(typeOfInput.MATRIC_NUM);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                        matric = updatedValue;
                    }
                    case (5) -> { // email
                        do {
                            System.out.print("Enter student's new email: ");
                            updatedValue = getInput(typeOfInput.EMAIL);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (6) -> { // gender
                        do {
                            System.out.print("Enter student's new gender: ");
                            updatedValue = getInput(typeOfInput.GENDER);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (7) -> { // nationality
                        do {
                            System.out.print("Enter student's new nationality: ");
                            updatedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (8) -> { // major
                        do {
                            System.out.print("Enter student's new major: ");
                            updatedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (9) -> { // maxAUs
                        do {
                            System.out.print("Enter student's new max AUs: ");
                            updatedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (10) -> { // access period
                        String newStart;
                        String newEnd;
                        do {
                            System.out.print("Enter student's new start access datetime: ");
                            newStart = getInput(typeOfInput.DATETIME);
                            System.out.print("Enter student's new end access datetime: ");
                            newEnd = getInput(typeOfInput.DATETIME);
                        } while (!userValidator.validateDateTimePeriod(newStart, newEnd) ||
                                !adHandler.editStudent(matric, newStart+"&"+newEnd, choice));
                    }
                    case (11) -> System.out.println("Exiting update student...");
                    default -> System.out.println("ERROR: Invalid menu option selected");
                }
                if (choice >= 0 && choice < 11) {
                    System.out.println("Successfully changed");
                    waitForEnterInput();
                }
            } while (choice != 11);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Allows the user to prints an overview of the current data
     */
    private void printOverview(){
        try{
            int choice;
            System.out.println("Choose overview :");
            System.out.println("1) Print all Students\n" +
                               "2) Print all Courses\n" +
                               "3) Print all Courses + Indexes\n" +
                               "4) Print all Courses + Indexes + Lessons");
            choice = Integer.parseInt(getInput(typeOfInput.INT));
            switch (choice) {
                case (1) -> System.out.println(adHandler.getStudentOverview());
                case (2) -> System.out.println(adHandler.getCourseOverview(1));
                case (3) -> System.out.println(adHandler.getCourseOverview(2));
                case (4) -> System.out.println(adHandler.getCourseOverview(3));
                default -> System.out.println("ERROR: Invalid menu option selected");
            }
            waitForEnterInput();
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a Student from the database
     */
    private void deleteStudent(){
        try{
            String matricNum;
            System.out.println(adHandler.getStudentOverview());
            while (true) {
                System.out.print("Enter Matriculation number of student to delete: ");
                matricNum = getInput(typeOfInput.MATRIC_NUM);
                if (adHandler.checkStudentExists(matricNum)){
                    break;
                }
                System.out.println("Student not found");
            }
            adHandler.removeStudent(matricNum);
            System.out.println("Student "+matricNum+" has been removed");
            waitForEnterInput();
        } catch (EscapeException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Removes a Course from the database
     */
    private void deleteCourse(){
        try{
            String courseCode;
            System.out.println(adHandler.getCourseOverview(1));
            while (true) {
                System.out.print("Enter course code of course to delete: ");
                courseCode = getInput(typeOfInput.COURSE_CODE);
                if (adHandler.checkCourseExists(courseCode)) {
                    break;
                }
                System.out.println("Course not found");
            }
            adHandler.removeCourse(courseCode);
            System.out.println("Course "+courseCode+" has been removed");
            waitForEnterInput();
        } catch(EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Forcefully enroll a Student currently on a wait list into an Index
     */
    private void forceEnroll() {
        String indexNum, matricNum;
        try {
            System.out.println(adHandler.getCourseOverview(2));
            while (true) {
                System.out.print("\nChoose an index to force enroll student: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);
                if (!adHandler.checkIndexExists(indexNum)){
                    System.out.println("Invalid index number");
                    continue;
                }
                for (Student stud: adHandler.getStudentListByIndex(indexNum, true)){
                    System.out.println(stud.getMatricNum() + ", "+ stud.getName());
                }
                System.out.print("\nChoose a student from waitlist to force enroll: ");
                matricNum = getInput(typeOfInput.MATRIC_NUM);
                if (!adHandler.forceEnrollStudent(indexNum, matricNum)){
                    System.out.println("Student not found");
                    continue;
                }
                System.out.println("Successfully enrolled student");
                break;
            }
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
        waitForEnterInput();
    }

    /**
     * Initiates logout process, confirms if user wants to logout
     * saves data back to file by closing the handler
     * @return true if user wishes to logout, false otherwise
     */
    private boolean logout() {
        if (exit()) {
            System.out.println("\nSaving data...");
            adHandler.close();
            System.out.println("Thank you for using MyStars!");
            System.out.println("Goodbye!");
            return true;
        } else {
            return false;
        }
    }
}
