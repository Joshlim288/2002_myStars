import java.util.Scanner;

public class StudentInterface {
    private StudentHandler studHandler;
    private Scanner sc;
    public StudentInterface (User currentUser, Scanner sc) {
        studHandler = new StudentHandler((Student)currentUser);
        this.sc = sc;
    }

    /**
     * Student UI is displayed here
     */
    public void start() {

    }
}
