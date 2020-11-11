import java.util.Arrays;
import java.util.Scanner;

public class MyStars {
    public static void main(String[] args){
        String menuChoice;
        Scanner sc = new Scanner(System.in);
        do {
            AccessControl.initialize();
            System.out.print( "Welcome to MyStars\n" +
                                "------------------\n\n" +
                                "1 - Login\n" +
                                "2 - Quit\n\n" +
                                "Enter your choice:");
            menuChoice = sc.nextLine();
            if (menuChoice.length() == 1 && Character.isDigit(menuChoice.toCharArray()[0])) {
                switch(Integer.parseInt(menuChoice)){
                    case(1) -> {
                        try {
                            User currentUser = login(sc);
                            UserInterface ui = UserInterfaceCreator.makeInterface(currentUser.getDomain(), currentUser, sc);
                            if (ui == null) {
                                System.out.println("Invalid domain, check record");
                                continue;
                            }
                            ui.start();
                        } catch (AccessDeniedException e) {
                            System.out.println(e.getMessage());
                        }


                    }
                    case(2) -> {
                        System.out.println("Exiting MyStars");
                    }

                    default -> System.out.println("Enter a valid menu choice");
                }
            }
        } while(Integer.parseInt(menuChoice) != 2);
    }

    /**
     * Handles the login
     *
     * Gets userID and password, hashes the password, then sends it to AccessControl for validation
     * @param sc
     * Main scanner passed down
     */
    public static User login(Scanner sc) throws AccessDeniedException{
        String userId, password;
        System.out.print("Enter user id (case sensitive): ");
        userId = sc.nextLine();
        System.out.print("Enter password (case sensitive): ");
        password = new String(System.console().readPassword());
        return AccessControl.validate(userId, password);
    }
}
