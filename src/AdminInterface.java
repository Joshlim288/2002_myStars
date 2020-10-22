import java.util.Scanner;

public class AdminInterface implements UserInterface{
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
