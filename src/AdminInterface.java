import java.util.Arrays;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
            System.out.println("(Enter ~ at any time to exit back to menu)");
            System.out.print("Enter choice:");
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
            System.out.print("Enter student matriculation number: ");
            String matricNum = getInput(typeOfInput.MATRIC_NUM);

            do {
                System.out.printf("Student %s current access period is from %s to %s", matricNum, accessTime[0].format(dateTimeFormatter), accessTime[1].format(dateTimeFormatter));
                System.out.print("Enter new access start date: ");
                newStart = getInput(typeOfInput.DATETIME);

                System.out.print("Enter new access end date: ");
                newEnd = getInput(typeOfInput.DATETIME);

            } while (!adHandler.editAccessPeriod(matricNum, newStart, newEnd));
            System.out.println("Access time successfully changed");
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
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

            } while (!adHandler.addCourse(courseCode, courseName, courseType, aus, school));

            System.out.print("Enter number of indexes: ");
            int numIndexes = Integer.parseInt(getInput(typeOfInput.INT));
            for (int i = 0; i < numIndexes; i++) {
                System.out.printf("Creating Index %d:\n", i + 1);
                createIndex(courseCode);
            }
            adHandler.finalizeCourse();
            System.out.println("Course successfully added");
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createIndex(String courseCode) {
        try {
            String indexNum;
            int indexVacancies;
            int numLessons;
            do {
                System.out.print("Enter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);

                System.out.print("Enter number of vacancies: ");
                indexVacancies = Integer.parseInt(getInput(typeOfInput.INT));
            } while (!adHandler.addIndex(indexNum, indexVacancies));

            System.out.print("Enter number of Lessons: ");
            numLessons = Integer.parseInt(getInput(typeOfInput.INT));
            for (int i = 0; i < numLessons; i++) {
                System.out.printf("Creating Lesson %d:\n", i + 1);
                createLesson(indexNum);
            }
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createLesson(String indexNum) {
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
                System.out.print("Enter lesson type: LEC, TUT, LAB, DES, PRJ, SEM");
                lessonType = getInput(typeOfInput.LESSON_TYPE);

                System.out.print("Enter group: ");
                group = getInput(typeOfInput.GROUP_NAME);

                System.out.print("Enter day of week: (First 3 letters of day)");
                day = getInput(typeOfInput.DAY);

                System.out.print("Enter start time (HH:MM)");
                startTime = LocalTime.parse(getInput(typeOfInput.TIME));

                System.out.print("Enter end time (HH:MM)");
                endTime = LocalTime.parse(getInput(typeOfInput.TIME));

                System.out.print("Enter venue");
                venue = sc.nextLine();

                while (true) {
                    System.out.print("Enter teaching weeks, separated with a comma (1-13)");
                    inputWeeks = sc.nextLine().split(",");
                    teachingWeeks = new ArrayList<>();
                    for (String week : inputWeeks) {
                        if (courseValidator.validateTeachingWeek(week)) {
                            teachingWeeks.add(Integer.parseInt(week));
                        } else {
                            System.out.println("Please enter the teaching weeks again.");
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
            System.out.println(e.getMessage());
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
            String startAccessPeriod;
            String endAccessPeriod;
            int maxAUs;

            do {
                System.out.print("Enter matriculation number: ");
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

                System.out.print("Enter password: "); //TODO: put restrictions on password?
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
    }

    private void checkIndex() {
        try {
            Index index;
            String indexNum;
            do {
                System.out.print("Enter course code: ");
                String course = getInput(typeOfInput.COURSE_CODE);

                System.out.print("Enter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);
                index = adHandler.cdm.getCourse(course).getIndex(indexNum);
            } while (index == null);
            System.out.println("The vacancy for index " + indexNum + " is: " +
                               index.getCurrentVacancy() + "/" + index.getIndexVacancy());
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printByIndex() {
        try {
            String indexNum;
            ArrayList<Student> studentList;
            do {
                System.out.print("Enter index number: ");
                indexNum = getInput(typeOfInput.INDEX_NUM);

                studentList = adHandler.getStudentListByIndex(indexNum);
            } while (studentList == null);

            for (Student stud : studentList) {
                System.out.println(stud.getMatricNum() +", "+ stud.getName());
            }
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void printByCourse() {
        try {
            String courseCode;
            ArrayList<Student> studentList;
            do {
                System.out.print("Enter course code: ");
                courseCode = getInput(typeOfInput.COURSE_CODE);

                studentList = adHandler.getStudentListByCourse(courseCode);
            } while (studentList == null);

            for (Student stud : studentList) {
                System.out.println(stud.getMatricNum() +", "+ stud.getName());
            }
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateCourse() {
        try {
            String courseCode;
            String changedValue;
            int choice;
            do {
                System.out.print("Enter course to edit:");
                courseCode = getInput(typeOfInput.COURSE_CODE);
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
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit course code
                        do {
                            System.out.print("Enter new course code: ");
                            changedValue = getInput(typeOfInput.COURSE_CODE);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> { // edit course name
                        do {
                            System.out.print("Enter new course name: ");
                            changedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> { // edit course type
                        do {
                            System.out.print("Enter new course type (CORE, MPE, GER, UE): ");
                            changedValue = getInput(typeOfInput.COURSE_TYPE);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (4) -> { // edit academic units
                        do {
                            System.out.print("Enter new academic units: ");
                            changedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (5) -> { // edit school
                        do {
                            System.out.print("Enter new school name: ");
                            changedValue = getInput(typeOfInput.NAME);
                        } while (!adHandler.editCourse(courseCode, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (6) -> editIndex(courseCode); // edit index
                }
            } while (choice != 7);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
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
                indexNum = getInput(typeOfInput.INDEX_NUM);
            } while (!adHandler.checkIndexExists(indexNum));

            do {
                System.out.println("Choose attribute to edit:");
                System.out.println("1: indexNum\n" +
                        "2: indexVacancy\n" +
                        "3: lessons\n" +
                        "4: exit");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit index num
                        do {
                            System.out.print("Enter new index number: ");
                            changedValue = getInput(typeOfInput.INDEX_NUM);
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> {
                        do { // edit index vacancies
                            System.out.print("Enter new index vacancies: ");
                            changedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> editLesson(courseCode, indexNum); // edit lessons
                }
            } while (choice != 4);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
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
                lessonGroup = getInput(typeOfInput.GROUP_NAME);
            } while (!adHandler.checkLessonExists(courseCode, indexNum, lessonGroup));

            do {
                System.out.println("Choose attribute to edit:");
                System.out.println("1: lessonType\n" +
                        "2: group\n" +
                        "3: day\n" +
                        "4: startTime\n" +
                        "5: endTime\n" +
                        "6: venue\n" +
                        "7: exit");
                choice = Integer.parseInt(getInput(typeOfInput.INT));
                switch (choice) {
                    case (1) -> { // edit lesson type
                        do {
                            System.out.print("Enter new lesson type (LEC, TUT, LAB, DES, PRJ, SEM): ");
                            changedValue = getInput(typeOfInput.LESSON_TYPE);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (2) -> { // edit group
                        do {
                            System.out.print("Enter new group name: ");
                            changedValue = getInput(typeOfInput.GROUP_NAME);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (3) -> { // edit day
                        do {
                            System.out.print("Enter new lesson day: ");
                            changedValue = getInput(typeOfInput.DAY);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (4) -> { // edit start time
                        do {
                            System.out.print("Enter new start time: ");
                            changedValue = getInput(typeOfInput.TIME);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (5) -> { // edit end time
                        do {
                            System.out.print("Enter new end time: ");
                            changedValue = getInput(typeOfInput.TIME);
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                    case (6) -> { // edit venue
                        do {
                            changedValue = sc.nextLine();
                        } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                        System.out.println("Successfully changed");
                    }
                }
            } while (choice != 7);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
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
                matric = getInput(typeOfInput.MATRIC_NUM);
            } while (!adHandler.checkStudentExists(matric));

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
                            updatedValue = BCrypt.hashpw(Arrays.toString(System.console().readPassword()), BCrypt.gensalt());
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
                            updatedValue = getInput(typeOfInput.INT);
                        } while (!adHandler.editStudent(matric, updatedValue, choice));
                    }
                }
            } while (choice != 10);
        } catch (EscapeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void logout() {
        System.out.println("Saving data...");
        adHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    }
}
