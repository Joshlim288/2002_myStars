import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminInterface extends UserInterface {

    private final AdminHandler adHandler;

    public AdminInterface (User currentUser, Scanner sc) {
        super(sc);
        adHandler = new AdminHandler((Admin)currentUser);
    }

    /**
     * Admin UI is displayed here
     */
    public void start() {
        int choice;

        do{
            System.out.println("Welcome, Admin " + adHandler.currentAdmin.getAdminName()
                    + ", " + adHandler.currentAdmin.getStaffNum() + "!");
            System.out.println("Choose an action: ");
            System.out.println("1. Edit student access period");
            System.out.println("2. Add a student (name, matric number, gender, nationality, etc)");
            System.out.println("3. Add a course (course code, school, its index numbers and vacancy)");
            System.out.println("4. Check available slot for an index number (vacancy in a class)");
            System.out.println("5. Print student list by index number");
            System.out.println("6. Print student list by course (all students registered for the selected course)");
            System.out.println("7. Update a course");
            System.out.println("8. Update a student");
            System.out.println("9. Log out");
            while (true) {
                tempString = sc.nextLine();
                if (userValidator.validateInt(tempString)) {
                    choice = Integer.parseInt(tempString);
                    break;
                }
            }

            switch(choice){
                case (1)-> editAccessPeriod();
                case (2)-> createStudent();
                case (3)-> createCourse();
                case (4)-> checkIndex();
                case (5)-> printByIndex();
                case (6)-> printByCourse();
                case (7)-> updateCourse();
                case (8)-> updateStudent();
                case (9)-> logout();
            }
        } while (choice != 9);
    }

    private void editAccessPeriod() {
        try {
            LocalDateTime[] accessTime = null;
            String newStart;
            String newEnd;
            String matricNum = null;
            while (true) {
                System.out.print("Enter student matriculation number: ");
                matricNum = getInput();
                if (userValidator.validateMatricNum(matricNum)) {
                    accessTime = adHandler.getAccessPeriod(matricNum);
                    if (accessTime != null)
                        break;
                }
            }

            do {
                System.out.printf("Student %s current access period is from %s to %s", matricNum, accessTime[0].format(dateTimeFormatter), accessTime[1].format(dateTimeFormatter));
                while (true) {
                    System.out.print("Enter new access start date: ");
                    newStart = getInput();
                    if (userValidator.validateDateTime(newStart)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter new access end date: ");
                    newEnd = getInput();
                    if (userValidator.validateDateTime(newEnd)) {
                        break;
                    }
                }
            } while (!adHandler.editAccessPeriod(matricNum, LocalDateTime.parse(newStart, dateTimeFormatter), LocalDateTime.parse(newEnd, dateTimeFormatter)));
            System.out.println("Access time successfully changed");
        } catch (EscapeException e) {
            return;
        }
    }

    private void createCourse() {
        try {
            String courseCode;
            String school;
            String courseName;
            String courseType;
            int aus;
            do {
                while (true) {
                    System.out.print("Enter course code: ");
                    courseCode = getInput();
                    if (courseValidator.validateCourseCode(courseCode)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter school: ");
                    school = getInput();
                    if (courseValidator.validateName(school)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter course name: ");
                    courseName = getInput();
                    if (courseValidator.validateName(courseName)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter course type (CORE, MPE, GER, UE): ");
                    courseType = getInput();
                    if (courseValidator.validateCourseType(courseType)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter academic units: ");
                    tempString = getInput();
                    if (courseValidator.validateInt(tempString)) {
                        aus = Integer.parseInt(tempString);
                        break;
                    }
                }
            } while (!adHandler.addCourse(courseCode, courseName, courseType, aus, school));
            
            int numIndexes;
            while (true) {
                System.out.print("Enter number of indexes: ");
                tempString = getInput();
                if (courseValidator.validateInt(tempString)) {
                    numIndexes = Integer.parseInt(tempString);
                    break;
                }
            }
            for (int i = 0; i < numIndexes; i++) {
                System.out.printf("Creating Index %d:\n", i + 1);
                createIndex(courseCode);
            }
            adHandler.finalizeCourse();
            System.out.println("Course successfully added");
        } catch (EscapeException e) {
            return;
        }
    }

    private void createIndex(String courseCode) {
        try {
            String indexNum;
            int indexVacancies;
            int numLessons;
            do {
                while (true) {
                    System.out.print("Enter index number: ");
                    indexNum = getInput();
                    if (courseValidator.validateIndexNum(indexNum)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter number of vacancies: ");
                    tempString = getInput();
                    if (courseValidator.validateInt(tempString)) {
                        indexVacancies = Integer.parseInt(tempString);
                        break;
                    }
                }
            } while (!adHandler.addIndex(courseCode, indexNum, indexVacancies));
            
            while (true) {
                System.out.print("Enter number of Lessons: ");
                tempString = getInput();
                if (courseValidator.validateInt(tempString)) {
                    numLessons = Integer.parseInt(tempString);
                    break;
                }
            }
            for (int i = 0; i < numLessons; i++) {
                System.out.printf("Creating Lesson %d:\n", i + 1);
                createLesson(courseCode, indexNum);
            }
        } catch (EscapeException e) {
            return;
        }
    }

    private void createLesson(String courseCode, String indexNum) {
        try {
            String lessonType;
            String group;
            String day;
            LocalTime startTime;
            LocalTime endTime;
            String venue;
            String[] inputWeeks;
            ArrayList<Integer> teachingWeeks;
            do {
                while (true) {
                    System.out.print("Enter lesson type: LEC, TUT, LAB, DES, PRJ, SEM");
                    lessonType = getInput();
                    if (courseValidator.validateLessonType(lessonType)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter group: ");
                    group = getInput();
                    if (courseValidator.validateGroupName(group)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter day of week: (First 3 letters of day)");
                    day = getInput();
                    if (courseValidator.validateDay(day)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter start time (HH:MM)");
                    tempString = getInput();
                    if (courseValidator.validateTime(tempString)) {
                        startTime = LocalTime.parse(tempString, timeFormatter);
                        break;
                    }

                }
                while (true) {
                    System.out.print("Enter end time (HH:MM)");
                    tempString = getInput();
                    if (courseValidator.validateTime(tempString)) {
                        endTime = LocalTime.parse(tempString, timeFormatter);
                        break;
                    }
                }
                System.out.print("Enter venue");
                venue = getInput();
                while (true) {
                    System.out.print("Enter teaching weeks, separated with a comma (1-13)");
                    inputWeeks = getInput().split(",");
                    teachingWeeks = new ArrayList<>();
                    for (String week : inputWeeks) {
                        if (courseValidator.validateInt(week) && Integer.parseInt(week) < 13 && Integer.parseInt(week) > 0) {
                            teachingWeeks.add(Integer.parseInt(week));
                        } else {
                            System.out.println("ERROR: Invalid week entered.");
                            break;
                        }
                    }
                    if (inputWeeks.length == teachingWeeks.size()) {
                        break;
                    }
                }
            } while (!adHandler.addLesson(indexNum, lessonType, group, day, startTime, endTime,
                    venue, teachingWeeks));
        } catch (EscapeException e) {
            return;
        }
    }

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
            int maxAUs;

            do {
                while (true) {
                    System.out.print("Enter matriculation number: ");
                    studentMatric = getInput();
                    if (userValidator.validateMatricNum(studentMatric)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter student name: ");
                    studentName = getInput();
                    if (userValidator.validateName(studentName)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter email: ");
                    email = getInput();
                    if (userValidator.validateEmail(email)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter gender: ");
                    gender = getInput();
                    if (userValidator.validateGender(gender)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter nationality: ");
                    nationality = getInput();
                    if (userValidator.validateName(nationality)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter userID: ");
                    userid = getInput();
                    if (userValidator.validateUserID(userid)) {
                        break;
                    }
                }
                System.out.print("Enter password: "); //TODO: put restrictions on password?
                password = BCrypt.hashpw(Arrays.toString(System.console().readPassword()), BCrypt.gensalt());
                while (true) {
                    System.out.print("Enter major: ");
                    major = getInput();
                    if (userValidator.validateName(major)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter maxAUs: ");
                    tempString = getInput();
                    if (userValidator.validateInt(tempString)) {
                        maxAUs = Integer.parseInt(tempString);
                        break;
                    }
                }
            } while (!adHandler.addStudent(userid, password, studentName, studentMatric, email,
                    gender, nationality, major, maxAUs));
        } catch (EscapeException e) {
            return;
        }
    }

    private void checkIndex() {
        try {
            String course;
            String indexNum;
            int vacancies = -1;
            do {
                while (true) {
                    System.out.print("Enter course code: ");
                    course = getInput();
                    if (courseValidator.validateCourseCode(course)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter index number: ");
                    indexNum = getInput();
                    if (courseValidator.validateIndexNum(indexNum)) {
                        vacancies = adHandler.checkSlot(course, indexNum);
                        break;
                    }
                }
            } while (vacancies == -1);
            System.out.println("The vacancy for" + indexNum + "is: " + vacancies);
        } catch (EscapeException e) {
            return;
        }
    }

    private void printByIndex() {
        try {
            String courseCode;
            String indexNum;
            ArrayList<Student> studentList;
            do {
                while (true) {
                    System.out.print("Enter course code: ");
                    courseCode = getInput();
                    if (courseValidator.validateCourseCode(courseCode)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter index number: ");
                    indexNum = getInput();
                    if (courseValidator.validateIndexNum(indexNum)) {
                        break;
                    }
                }
                studentList = adHandler.getStudentListByIndex(courseCode, indexNum);
            } while (studentList == null);

            for (Student stud : studentList) {
                System.out.println(stud);
            }
        } catch (EscapeException e) {
            return;
        }
    }

    private void printByCourse() {
        try {
            String courseCode;
            ArrayList<Student> studentList;
            do {
                while (true) {
                    System.out.print("Enter course code: ");
                    courseCode = getInput();
                    if (courseValidator.validateCourseCode(courseCode)) {
                        break;
                    }
                }
                studentList = adHandler.getStudentListByCourse(courseCode);
            } while (studentList == null);

            for (Student stud : studentList) {
                System.out.println(stud);
            }
        } catch (EscapeException e) {
            return;
        }
    }

    private void updateCourse() {
        try {
            String courseCode;
            String changedValue;
            int choice;
            do {
                while (true) {
                    System.out.print("Enter course to edit:");
                    courseCode = getInput();
                    if (courseValidator.validateCourseCode(courseCode)) {
                        break;
                    }
                }
            } while (!adHandler.checkCourseExists(courseCode));

            do {
                System.out.println("Choose attribute to edit:");
                System.out.println("1: courseCode\n" +
                        "2: courseName\n" +
                        "3: courseType\n" +
                        "4: academicUnits\n" +
                        "5: school\n" +
                        "6: indexes\n" +
                        "7: exit");
                choice = Integer.parseInt(getInput());
                switch (choice) {
                    case (1) -> { // edit course code
                        do {
                            while (true) {
                                System.out.print("Enter new course code: ");
                                changedValue = getInput();
                                if (courseValidator.validateCourseCode(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> { // edit course name
                        do {
                            while (true) {
                                System.out.print("Enter new course name: ");
                                changedValue = getInput();
                                if (courseValidator.validateName(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> { // edit course type
                        do {
                            while (true) {
                                System.out.print("Enter new course type (CORE, MPE, GER, UE): ");
                                changedValue = getInput();
                                if (courseValidator.validateCourseType(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (4) -> { // edit academic units
                        do {
                            while (true) {
                                System.out.print("Enter new academic units: ");
                                changedValue = getInput();
                                if (courseValidator.validateInt(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (5) -> { // edit school
                        do {
                            while (true) {
                                System.out.print("Enter new school name: ");
                                changedValue = getInput();
                                if (courseValidator.validateName(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (6) -> editIndex(courseCode); // edit index
                }
            } while (choice != 7);
        } catch (EscapeException e) {
            return;
        }
    }

    private void editIndex(String courseCode) {
        try {
            String changedValue;
            String indexNum;
            int choice;
            System.out.println("Indexes:");
            for (Index idx : adHandler.getTempIndexes()) {
                System.out.println(idx);
            }
            do {
                System.out.print("Enter index to edit:");
                while (true) {
                    indexNum = getInput();
                    if (courseValidator.validateIndexNum(indexNum)) {
                        break;
                    }
                }
            } while (!adHandler.checkIndexExists(indexNum));

            do {
                System.out.println("Choose attribute to edit:");
                System.out.println("1: indexNum\n" +
                        "2: indexVacancy\n" +
                        "3: lessons\n" +
                        "4: exit");
                while (true) {
                    tempString = getInput();
                    if (courseValidator.validateInt(tempString)) {
                        choice = Integer.parseInt(tempString);
                        break;
                    }
                }
                switch (choice) {
                    case (1) -> { // edit index num
                        do {
                            while (true) {
                                System.out.print("Enter new index number: ");
                                changedValue = getInput();
                                if (courseValidator.validateIndexNum(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> {
                        do { // edit index vacancies
                            while (true) {
                                System.out.print("Enter new index vacancies: ");
                                changedValue = getInput();
                                if (courseValidator.validateInt(changedValue)) {
                                    break;
                                }
                            }
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> editLesson(courseCode, indexNum); // edit lessons
                }
            } while (choice != 4);
        } catch (EscapeException e) {
            return;
        }
    }

    private void editLesson(String courseCode, String indexNum) {
        try {
            String changedValue;
            String lessonGroup;
            int choice;
            System.out.println("Lessons:");
            for (Index idx : adHandler.getTempIndexes()) {
                for (Lesson lsn : idx.getLessons()) {
                    System.out.println(lsn);
                }
            }
            do {
                System.out.println("Enter lesson group to edit:");
                lessonGroup = getInput();
            } while (courseValidator.validateGroupName(lessonGroup) && !adHandler.checkLessonExists(courseCode, indexNum, lessonGroup));

            do {
                System.out.println("Choose attribute to edit:");
                System.out.println("1: lessonType\n" +
                        "2: group\n" +
                        "3: day\n" +
                        "4: startTime\n" +
                        "5: endTime\n" +
                        "6: venue\n" +
                        "7: exit");
                while (true) {
                    tempString = getInput();
                    if (courseValidator.validateInt(tempString)) {
                        choice = Integer.parseInt(tempString);
                        break;
                    }
                }
                switch (choice) {
                    // waiting for input validation to change these cases
                    case (1) -> { // edit lesson type
                        do {
                            System.out.print("Enter new lesson type (LEC, TUT, LAB, DES, PRJ, SEM): ");
                            changedValue = getInput();
                        } while (courseValidator.validateLessonType(changedValue) &&
                                !adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> { // edit group
                        do {
                            System.out.print("Enter new group name: ");
                            changedValue = getInput();
                        } while (courseValidator.validateGroupName(changedValue) &&
                                !adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> { // edit day
                        do {
                            System.out.print("Enter new lesson day: ");
                            changedValue = getInput();
                        } while (courseValidator.validateDay(changedValue) &&
                                !adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (4) -> { // edit start time
                        do {
                            System.out.print("Enter new start time: ");
                            changedValue = getInput();
                        } while (courseValidator.validateTime(changedValue) &&
                                !adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (5) -> { // edit end time
                        do {
                            System.out.print("Enter new end time: ");
                            changedValue = getInput();
                        } while (courseValidator.validateTime(changedValue) &&
                                !adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (6) -> { // edit venue
                        do {
                            changedValue = getInput();
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                }
            } while (choice != 7);
        } catch (EscapeException e) {
            return;
        }
    }

    /**
     * todo bring in update access time
     */
    private void updateStudent(){
        try {
            String matric;
            String updatedValue;
            int choice;
            do {
                System.out.print("Enter Matriculation Number of Student: ");
                matric = getInput();
            } while (userValidator.validateMatricNum(matric) && !adHandler.checkStudentExists(matric));

            do {
                System.out.println("Choose attribute to edit: ");
                System.out.println(" 1: userID\n" +
                        " 2: Password\n" +
                        " 3: Name\n" +
                        " 4: matricNum\n" +
                        " 5: email\n" +
                        " 6: gender\n" +
                        " 7: nationality\n" +
                        " 8: major\n" +
                        " 9: maxAUs\n" +
                        "10: exit");
                while (true) {
                    tempString = getInput();
                    if (userValidator.validateInt(tempString)) {
                        choice = Integer.parseInt(tempString);
                        break;
                    }
                }
                switch (choice) {
                    case (1) -> { // user id
                        do {
                            System.out.print("Enter new user ID: ");
                            updatedValue = getInput();
                        } while (userValidator.validateUserID(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (2) -> { // password
                        do {
                            updatedValue = BCrypt.hashpw(Arrays.toString(System.console().readPassword()), BCrypt.gensalt());
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (3) -> { // name
                        do {
                            System.out.print("Enter student's new name: ");
                            updatedValue = getInput();
                        } while (userValidator.validateName(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (4) -> { // matric
                        do {
                            System.out.print("Enter student's new matriculation number: ");
                            updatedValue = getInput();
                        } while (userValidator.validateMatricNum(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (5) -> { // email
                        do {
                            System.out.print("Enter student's new email: ");
                            updatedValue = getInput();
                        } while (userValidator.validateEmail(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (6) -> { // gender
                        do {
                            System.out.print("Enter student's new gender: ");
                            updatedValue = getInput();
                        } while (userValidator.validateGender(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (7) -> { // nationality
                        do {
                            System.out.print("Enter student's new nationality: ");
                            updatedValue = getInput();
                        } while (userValidator.validateName(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (8) -> { // major
                        do {
                            System.out.print("Enter student's new major: ");
                            updatedValue = getInput();
                        } while (userValidator.validateName(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                    case (9) -> { // maxAUs
                        do {
                            updatedValue = getInput();
                        } while (userValidator.validateInt(updatedValue) && !adHandler.editStudent(matric, updatedValue, choice));
                    }
                }
            } while (choice != 10);
        } catch (EscapeException e) {
            return;
        }
    }

    private void logout() {
        System.out.println("Saving data...");
        adHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    }



}
