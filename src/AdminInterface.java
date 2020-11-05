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
            System.out.println("7. Log out");
            choice = Integer.parseInt(sc.nextLine());

            switch(choice){
                case (1)-> editAccessPeriod();
                case (2)-> createStudent();
                case (3)-> createCourse();
                case (4)-> checkIndex();
                case (5)-> printByIndex();
                case (6)-> printByCourse();
                case (7)-> logout();
            }
        } while (choice != 7);
    }

    private void editAccessPeriod() {
        LocalDateTime[] accessTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String newStart;
        String newEnd;
        String matricNum = null;
        while (accessTime == null){
            System.out.println("Enter student matriculation number");
            matricNum = sc.nextLine();
            accessTime = adHandler.getAccessPeriod(matricNum);
        }

        do {
            System.out.printf("Student %s current access period is from %s to %s", matricNum, accessTime[0].format(formatter), accessTime[1].format(formatter));
            System.out.print("Enter new access start date: ");
            newStart = sc.nextLine();
            System.out.print("Enter new access end date: ");
            newEnd = sc.nextLine();
        } while(!adHandler.editAccessPeriod(matricNum, newStart, newEnd));
        System.out.println("Access time successfully changed");
    }

    private void createCourse() {
        String courseCode;
        String school;
        String courseName;
        String courseType;
        int aus;
        do {
            System.out.println("Enter course code: ");
            courseCode = sc.next();
            System.out.println("Enter school: ");
            school = sc.next();
            System.out.println("Enter course name: ");
            courseName = sc.next();
            System.out.println("Enter number for course type: (CORE, MPE, GER, UE)");
            courseType = sc.next();
            System.out.println("Enter academic units: ");
            aus = sc.nextInt();
        } while (!adHandler.addCourse(courseCode, courseName, courseType, aus, school));
        System.out.println("Enter number of indexes: ");
        int numIndexes = sc.nextInt();
        for (int i=0; i<numIndexes; i++) {
            System.out.printf("Creating Index %d:\n", i+1);
            createIndex(courseCode);
        }
    }

    private void createIndex(String courseCode) {
        int indexNum;
        int indexVacancies;
        int numLessons;
        do {
            System.out.print("Enter index number: ");
            indexNum = sc.nextInt();
            System.out.print("Enter number of vacancies: ");
            indexVacancies = sc.nextInt();
        } while (!adHandler.addIndex(courseCode, indexNum, indexVacancies));
        System.out.print("Enter number of Lessons: ");
        numLessons = sc.nextInt();
        for (int i = 0; i < numLessons; i++) {
            System.out.printf("Creating Lesson %d:\n", i + 1);
            createLesson(courseCode, indexNum);
        }
    }

    private void createLesson(String courseCode, int indexNum) {
        String lessonType;
        String group;
        String day;
        LocalTime startTime;
        LocalTime endTime;
        String venue;
        String[] inputWeeks;
        ArrayList<Integer> teachingWeeks;
        do {
            System.out.println("Enter course type: LEC, TUT, LAB, DES, PRJ, SEM");
            lessonType = sc.next();
            System.out.print("Enter group: ");
            group = sc.next();
            System.out.print("Enter day of week: (First 3 letters of day)");
            day = sc.next();
            System.out.println("Enter start time (HH:MM)");
            startTime = LocalTime.parse(sc.next());
            System.out.println("Enter end time (HH:MM)");
            endTime = LocalTime.parse(sc.next());
            System.out.println("Enter venue");
            venue = sc.next();
            System.out.println("Enter teaching weeks, separated with a comma (1-13)");
            inputWeeks = sc.next().split(",");
            teachingWeeks = new ArrayList<>();
            for (String week : inputWeeks) {
                if (Integer.parseInt(week) < 13 && Integer.parseInt(week) > 0) {
                    teachingWeeks.add(Integer.parseInt(week));
                }
            }
        }  while(!adHandler.addLesson(courseCode, indexNum, lessonType, group, day, startTime, endTime,
                    venue, teachingWeeks));
    }

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
        int indexNum;
        int vacancies = -1;
        do {
            System.out.println("Enter course code: ");
            course = sc.next();
            System.out.println("Enter index number: ");
            indexNum = sc.nextInt();
            vacancies = adHandler.checkSlot(course, indexNum);
        } while(vacancies == -1);
        System.out.println("The vacancy for"+ indexNum +"is: "+ vacancies);
    }

    private void printByIndex() {
        String courseCode;
        int indexNum;
        ArrayList<Student> studentList;
        do {
            System.out.println("Enter course code: ");
            courseCode = sc.next();
            System.out.println("Enter index number: ");
            indexNum = sc.nextInt();
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

    private void logout() {
        System.out.println("Saving data...");
        adHandler.close();
        System.out.println("Thank you for using MyStars!");
        System.out.println("Goodbye!");
    }

}
