import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AdminInterface {

    private AdminHandler adHandler;
    Scanner sc = new Scanner(System.in);
    public AdminInterface (User currentUser, Scanner sc) {
        adHandler = new AdminHandler((Admin)currentUser);
        this.sc = sc;
    }

    /**
     * Admin UI is displayed here
     */
    public void start() {
        int choice;
        do{
            System.out.println("Welcome Admin ");
            System.out.println("Enter your choice: ");
            System.out.println("1. Edit student access period");
            System.out.println("2. Add a student (name, matric number, gender, nationality, etc)");
            System.out.println("3. Add/Update a course (course code, school, its index numbers and vacancy)");
            System.out.println("4. Check available slot for an index number (vacancy in a class)");
            System.out.println("5. Print student list by index number");
            System.out.println("6. Print student list by course (all students registered for the selected course)");
            System.out.println("7. Back to main menu");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    System.out.println("Enter student matriculation number");
                    String matricnum = sc.next();
                    Student student=FileHandler.getStudent(matricnum);
                    System.out.println("Student " + matricnum +" current access period is from: "+ student.getAccessTime());
                    System.out.println("Enter new access start date: ");
                    String strnewstartdate = sc.next();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy hh:mm:ss a");
                    LocalDateTime newstartdate = LocalDateTime.parse(strnewstartdate, formatter);
                    System.out.println("Enter new access end date: ");
                    String strnewenddate = sc.next();
                    LocalDateTime newenddate = LocalDateTime.parse(strnewenddate, formatter);
                    adHandler.editAccessPeriod(matricnum,newstartdate,newenddate);
                    System.out.println("Success! Student "+ matricnum +" new access period is from" + student.getAccessTime());
                    break;
                case 2:
                    System.out.println("Enter student name: ");
                    String studentname = sc.next();
                    System.out.println("Enter matriculation number: ");
                    String studentmatric = sc.next();
                    if (adHandler.checkmatricexist(studentmatric))
                        break;
                    System.out.println("Enter gender: ");
                    String studentgender = sc.next();
                    System.out.println("Enter nationality: ");
                    String studentnationality = sc.next();
                    System.out.println("Enter userID: ");
                    String userid = sc.next();
                    System.out.println("Enter password: ");
                    String password = sc.next();
                    System.out.println("Enter maxAUs: ");
                    int maxaus = sc.nextInt();
                    System.out.println("Enter major: ");
                    String major = sc.next();
                    Student newstudent = new Student(userid,password,studentname,studentmatric,maxaus,major);
                    adHandler.addStudent(newstudent);
                    System.out.println("Student "+studentmatric+" has been added successfully!");
                    break;
                case 3:
                    /**
                     * A lot of redundancy, a lot should be moved to studentHandler, a lot of input validation
                     * needed to be done. This is just a skeleton, will continue working on it
                     * //TODO Josh
                     */
                    Course newCourse;
                    System.out.println("Enter course code: ");
                    String courseCode = sc.next();
                    System.out.println("Enter school: ");
                    String school = sc.next();
                    System.out.println("Enter course name: ");
                    String courseName = sc.next();
                    System.out.println("Enter number for course type: (CORE-1, MPE-2, GER-3, UE-4)");
                    int useropt = sc.nextInt();
                    typeOfCourse courseType = adHandler.choosecoursetype(useropt);
                    System.out.println("Enter academic units: ");
                    int aus = sc.nextInt();
                    ArrayList<Index> index = new ArrayList<Index>();
                    System.out.println("Enter number of indexes: ");
                    int indexlength = sc.nextInt();
                    newCourse = new Course(courseCode, courseName, courseType, aus, school);
                    for (int i=0; i<indexlength; i++){
                        int indexNum;
                        int indexVacancy;
                        int numLessons;
                        System.out.printf("Creating Index %d:\n", i+1);
                        System.out.print("Enter index number: ");
                        indexNum = sc.nextInt();
                        System.out.print("Enter number of vacancies: ");
                        indexVacancy = sc.nextInt();
                        newCourse.addIndex(indexNum, indexVacancy);
                        System.out.print("Enter number of Lessons for Index "+i+1);
                        numLessons = sc.nextInt();
                        for (int j=0; j<numLessons; j++) {
                            System.out.printf("Creating Lesson %d for Index %d\n",j+1, i+1);
                            System.out.print("Enter lesson type: ");
                            System.out.println("Enter number for course type: \n0: LEC, \n1:TUT, \n2:LAB, \n3:DES, \nPRJ, \nSEM");
                            typeOfLesson lessonType = adHandler.chooseLessonType(sc.nextInt());
                            System.out.print("Enter group: ");
                            String group = sc.next();
                            System.out.print("Enter day of week: ");
                            dayOfWeek day = adHandler.chooseDayOfWeek(sc.nextInt());
                            System.out.println("Enter start time");
                            LocalDateTime startTime = LocalDateTime.parse(sc.next());
                            System.out.println("Enter end time");
                            LocalDateTime endTime = LocalDateTime.parse(sc.next());
                            System.out.println("Enter venue");
                            String venue = sc.next();
                            System.out.println("Enter teaching weeks, separated with a comma (1-13)");
                            String[] inputWeeks = sc.next().split(",");
                            ArrayList<Integer> teachingWeeks = new ArrayList<>();
                            for (String week: inputWeeks) {
                                if (Integer.parseInt(week) < 13 && Integer.parseInt(week) > 0) {
                                    teachingWeeks.add(Integer.parseInt(week));
                                }
                            }
                            newCourse.searchIndex(indexNum).addLesson(lessonType, group, day, startTime, endTime, venue,
                                    teachingWeeks);
                        }
                    }
                case 4:
                    System.out.println("Enter course code: ");
                    String course = sc.next();
                    System.out.println("Enter index number: ");
                    int indexno = sc.nextInt();
                    adHandler.checkSlot(course, indexno);
                    if(FileHandler.getCourse(course).searchIndex(indexno)!=null){
                        System.out.println("The vacancy for"+ indexno +"is: "+ adHandler.checkSlot(course, indexno));
                    }else{
                        System.out.println("Index not found!");
                    }
                    break;
                case 5:
                    System.out.println("Enter course code: ");
                    String printstudentcourse = sc.next();
                    System.out.println("Enter index number: ");
                    int printstudentindex = sc.nextInt();
                    adHandler.printStudentListbyIndex(printstudentcourse, printstudentindex);
                    break;
                case 6:
                    System.out.println("Enter course code: ");
                    String printstudentcourse2 = sc.next();
                    adHandler.printStudentListbyCourse(printstudentcourse2);
                    break;
                case 7:
                    break;



            }
        } while (choice != 7);


    }




}
