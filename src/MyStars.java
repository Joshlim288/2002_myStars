import java.util.Arrays;
import java.util.Scanner;

public class MyStars {
    public static void main(String[] args){
        String menuChoice;
        User currentUser;
        StudentInterface sUI;
        AdminInterface aUI;
        FileHandler.initialize();
        Scanner sc = new Scanner(System.in);
        do {
            System.out.println( "Welcome to MyStars\n" +
                                "------------------\n\n" +
                                "1 - Student Login\n" +
                                "2 - Admin Login\n" +
                                "3 - Quit\n\n" +
                                "Enter your choice:");
            menuChoice = sc.nextLine();
            if (menuChoice.length() == 1 && Character.isDigit(menuChoice.toCharArray()[0])) {
                switch(Integer.parseInt(menuChoice)){
                    case(1) -> {
                        currentUser = login("Student", sc);
                        if (currentUser == null) {
                            System.out.println("Invalid UserID or password");
                        } else {
                            sUI = new StudentInterface(currentUser, sc);
                            sUI.start();
                        }
                    }

                    case(2) -> {
                        currentUser = login("Admin", sc);
                        if (currentUser == null) {
                            System.out.println("Invalid UserID or password");
                        } else {
                            aUI = new AdminInterface(currentUser, sc);
                            aUI.start();
                        }
                    }

                    case(3) -> {
                        System.out.println("Exiting MyStars");
                        quit();
                    }

                    default -> System.out.println("Enter a valid menu choice");
                }
            }
        } while(Integer.parseInt(menuChoice) != 3);
    }

    /**
     * Handles the login
     *
     * Gets userID and password, hashes the password, then sends it to AccessControl for validation
     * @param domain
     * Domain that the user is trying to logon to
     */
    public static User login(String domain, Scanner sc) {
        String userId, password;
        System.out.print("Enter userId: ");
        userId = sc.nextLine();
        System.out.println("Enter password: ");
        password = Arrays.toString(System.console().readPassword());
        return AccessControl.validate(userId, password, domain);
    }

    /**
     * Data is only written back to files when this is called.
     * Ensure to always quit via the menu option if you want
     * data to be saved
     * Needs to close fileIO and write back all updated data
     */
    public static void quit() {
        FileHandler.close();
    }
}
