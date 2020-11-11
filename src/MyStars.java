import java.util.Arrays;
import java.util.Scanner;

public class MyStars {
    public static void main(String[] args){
        String menuChoice;
        Scanner sc = new Scanner(System.in);

        System.out.println("  __  __         ____  _                 \n" +
                            " |  \\/  |_   _  / ___|| |_ __ _ _ __ ___ \n" +
                            " | |\\/| | | | | \\___ \\| __/ _` | '__/ __|\n" +
                            " | |  | | |_| |  ___) | || (_| | |  \\__ \\\n" +
                            " |_|  |_|\\__, | |____/ \\__\\__,_|_|  |___/\n" +
                            "         |___/                           ");

        do{
            AccessControl.initialize();
            System.out.print("------------------------------------------\n" +
                            "|          Welcome to MyStars!           |\n" +
                            "|----------------------------------------|\n" +
                            "|               1 - Login                |\n" +
                            "|               2 - Quit                 |\n" +
                            "------------------------------------------\n" +
                            "Enter your choice: ");

            menuChoice = sc.nextLine();
            if (!menuChoice.matches("^[0-9]+$"))
                menuChoice = "3";

            switch(Integer.parseInt(menuChoice)){
                case(1) -> {
                    try {
                        User currentUser = login(sc);
                        UserInterface ui = UserInterfaceCreator.makeInterface(currentUser.getDomain(), currentUser, sc);
                        if (ui == null) {
                            System.out.println("Invalid domain, please check record.");
                            continue;
                        }
                        ui.start();
                    } catch (AccessDeniedException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case(2) -> System.out.println("Exiting MyStars!");
                default -> System.out.println("Invalid, please enter a valid menu choice");
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
