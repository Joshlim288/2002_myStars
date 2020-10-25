import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
            System.out.println("Welcome Admin " + adHandler.adminName);
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
                    if (FileHandler.getStudentList().contains(studentmatric)){
                        System.out.println("Matriculation number already exist!");
                        break;}
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
                    System.out.println("Enter course code: ");
                    String coursecode = sc.next();
                    Course course = FileHandler.getCourse(coursecode);
                    System.out.println("Enter school: ");
                    String school = sc.next();
                    System.out.println("Enter index number: ");
                    String indexnum = sc.next();
                    if (FileHandler.courseList.contains(coursecode)){
                        adHandler.editCourse(course,school,indexnum);}
                    else
                        FileHandler.addCourse(course);
                    break;
                case 4:
                    System.out.println("Enter index number: ");
                    String index = sc.next();
                    Index indvac = FileHandler.getIndexNum(index);
                    if(indvac != null){
                        System.out.println("The vacancy for"+ index +"is: "+ indvac.getIndexVacancy());
                    }else{
                        System.out.println("Index not found!");
                    }
                    break;
                case 5:
                    adHandler.printStudentList(true);
                    break;
                case 6:
                    adHandler.printStudentList(false);
                    break;
                case 7:
                    adHandler.exit();



            }
        } while (choice != 7);


    }




}
