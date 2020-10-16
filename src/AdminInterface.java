import java.util.Scanner;

public class AdminInterface {
    private AdminHandler adHandler;
    private Scanner sc;
    public AdminInterface (User currentUser, Scanner sc) {
        adHandler = new AdminHandler((Admin)currentUser);
        this.sc = sc;
    }

    /**
     * Admin UI is displayed here
     */
    public void start() {

    }
}
