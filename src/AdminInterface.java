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
            choice = Integer.parseInt(sc.nextLine());

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
                    newStart = sc.nextLine();
                    if (userValidator.validateDateTime(newStart)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter new access end date: ");
                    newEnd = sc.nextLine();
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
                    System.out.println("Enter course code: ");
                    courseCode = getInput();
                    if (courseValidator.validateCourseCode(courseCode)) {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter school: ");
                    school = getInput();
                    if (courseValidator.validateName(school)) {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter course name: ");
                    courseName = getInput();
                    if (courseValidator.validateName(courseName)) {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter number for course type: (CORE, MPE, GER, UE)");
                    courseType = getInput();
                    if (courseValidator.validateCourseType(courseType)) {
                        break;
                    }
                }
                while (true) {
                    System.out.println("Enter academic units: ");
                    tempString = getInput();
                    if (courseValidator.validateInt(tempString)) {
                        aus = Integer.parseInt(tempString);
                        break;
                    }
                }
            } while (!adHandler.addCourse(courseCode, courseName, courseType, aus, school));
            
            int numIndexes;
            while (true) {
                System.out.println("Enter number of indexes: ");
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
                    System.out.println("Enter course type: LEC, TUT, LAB, DES, PRJ, SEM");
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
                    System.out.println("Enter end time (HH:MM)");
                    tempString = getInput();
                    if (courseValidator.validateTime(tempString)) {
                        endTime = LocalTime.parse(tempString, timeFormatter);
                        break;
                    }
                }
                System.out.println("Enter venue");
                venue = sc.next();
                while (true) {
                    System.out.println("Enter teaching weeks, separated with a comma (1-13)");
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
    //TODO: Validation from this point on
    private void createStudent() {
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
            System.out.println("Enter matriculation number: ");
            studentMatric = sc.next();
            System.out.println("Enter student name: ");
            studentName = sc.next();
            System.out.println("Enter email: ");
            email = sc.next();
            System.out.println("Enter gender: ");
            gender = sc.next();
            System.out.println("Enter nationality: ");
            nationality = sc.next();
            System.out.println("Enter userID: ");
            userid = sc.next();
            System.out.println("Enter password: ");
            password = sc.next();
            System.out.println("Enter major: ");
            major = sc.next();
            System.out.println("Enter maxAUs: ");
            maxAUs = sc.nextInt();
        }while(!adHandler.addStudent(userid, password, studentName, studentMatric, email,
                gender, nationality, major, maxAUs));

    }

    private void checkIndex() {
        String course;
        String indexNum;
        int vacancies = -1;
        do {
            System.out.println("Enter course code: ");
            course = sc.next();
            System.out.println("Enter index number: ");
            indexNum = sc.nextLine();
            vacancies = adHandler.checkSlot(course, indexNum);
        } while(vacancies == -1);
        System.out.println("The vacancy for"+ indexNum +"is: "+ vacancies);
    }

    private void printByIndex() {
        String courseCode;
        String indexNum;
        ArrayList<Student> studentList;
        do {
            System.out.println("Enter course code: ");
            courseCode = sc.next();
            System.out.println("Enter index number: ");
            indexNum = sc.nextLine();
            studentList = adHandler.getStudentListByIndex(courseCode, indexNum);
        } while(studentList == null);

        for (Student stud: studentList) {
            System.out.println(stud);
        }
    }

    private void printByCourse() {
        String courseCode;
        ArrayList<Student> studentList;
        do {
            System.out.println("Enter course code: ");
            courseCode = sc.next();
            studentList = adHandler.getStudentListByCourse(courseCode);
        } while (studentList == null);

        for (Student stud: studentList) {
            System.out.println(stud);
        }
    }

    private void updateCourse() {
        String courseCode;
        String changedValue;
        int choice;
        do {
            System.out.println("Enter course to edit:");
            courseCode = sc.nextLine();
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
            choice = Integer.parseInt(sc.nextLine());
            switch(choice){
                // waiting for input validation to change these cases
                case(1)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(2)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(3)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(4)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(5)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editCourse(courseCode, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(6)-> editIndex(courseCode);

            }
        } while (choice != 7);
    }

    private void editIndex(String courseCode) {
        String changedValue;
        String indexNum;
        int choice;
        System.out.println("Indexes:");
        for (Index idx: adHandler.getTempIndexes()) {
            System.out.println(idx);
        }
        do {
            System.out.println("Enter index to edit:");
            indexNum = sc.nextLine();
        } while(!adHandler.checkIndexExists(indexNum));

        do {
            System.out.println("Choose attribute to edit:");
            System.out.println("1: indexNum\n" +
                    "2: indexVacancy\n" +
                    "3: lessons\n" +
                    "4: exit");
            choice = Integer.parseInt(sc.nextLine());
            switch(choice){
                // waiting for input validation to change these cases
                case(1)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(2)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editIndex(courseCode, indexNum, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(3)-> editLesson(courseCode, indexNum);
            }
        } while (choice != 4);
    }

    private void editLesson(String courseCode, String indexNum) {
        String changedValue;
        String lessonGroup;
        int choice;
        System.out.println("Lessons:");
        for (Index idx: adHandler.getTempIndexes()) {
            for (Lesson lsn: idx.getLessons()){
                System.out.println(lsn);
            }
        }
        do {
            System.out.println("Enter lesson group to edit:");
            lessonGroup = sc.nextLine();
        } while(!adHandler.checkLessonExists(courseCode, indexNum, lessonGroup));

        do {
            System.out.println("Choose attribute to edit:");
            System.out.println("1: lessonType\n" +
                    "2: group\n" +
                    "3: day\n" +
                    "4: startTime\n" +
                    "5: endTime\n" +
                    "6: venue\n" +
                    "7: exit");
            choice = Integer.parseInt(sc.nextLine());
            switch(choice){
                // waiting for input validation to change these cases
                case(1)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(2)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(3)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(4)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(5)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
                case(6)-> {
                    do {
                        changedValue = sc.nextLine();
                    } while (!adHandler.editLesson(courseCode, indexNum, lessonGroup, changedValue, choice));
                    System.out.println("Successfully changed");
                }
            }
        } while (choice != 7);
    }

    /**
     * todo bring in update access time
     */
    private void updateStudent(){
        String matric;
        String updatedValue;
        int choice;
        do {
            System.out.println("Enter Matriculation Number of Student: ");
            matric = sc.nextLine();
        } while(!adHandler.checkStudentExists(matric));

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
            choice = Integer.parseInt(sc.nextLine());
            switch(choice){
                case(1)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(2)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(3)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(4)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(5)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(6)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(7)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(8)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
                case(9)-> {
                    do {
                        updatedValue = sc.nextLine();
                    } while (!adHandler.editStudent(matric, updatedValue, choice));
                }
            }
        } while (choice != 10);
    }

    private void logout() {
        System.out.println("Saving data...");
        adHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    }



}
