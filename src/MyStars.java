import java.util.Scanner;

/**
 * Main class for the application
 * Contains the functionality for the start menu, handles user input for logging in
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.3
 * @since 1.1
 */
public class MyStars {
    /**
     * Starts the program
     * Allows user to login to or exit the program
     * If login has failed, message will be printed stating the reason
     * @param args
     */
    public static void main(String[] args){
        String menuChoice;
        Scanner sc = new Scanner(System.in);


        do{
            System.out.println("  __  __         ____  _                 \n" +
                    " |  \\/  |_   _  / ___|| |_ __ _ _ __ ___ \n" +
                    " | |\\/| | | | | \\___ \\| __/ _` | '__/ __|\n" +
                    " | |  | | |_| |  ___) | || (_| | |  \\__ \\\n" +
                    " |_|  |_|\\__, | |____/ \\__\\__,_|_|  |___/\n" +
                    "         |___/                           ");
            AccessControl.initialize();
            System.out.print("\n------------------------------------------\n" +
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
                        System.out.println(e.getMessage()); // prints out the reason for being denied
                    }
                }
                case(2) -> System.out.println("Exiting MyStars!");
                default -> System.out.println("Invalid, please enter a valid menu choice!");
            }
        } while(Integer.parseInt(menuChoice) != 2);
    }

    /**
     * Handles the login
     * Gets userID and password, sends it to AccessControl for validation
     * @param sc Main scanner passed down
     * @throws AccessDeniedException When login has failed, exception will be thrown with the reason as the message
     */
    public static User login(Scanner sc) throws AccessDeniedException{
        String userId, password;
        System.out.print("Enter user ID (case sensitive): ");
        userId = sc.nextLine();
        System.out.print("Enter password (case sensitive): ");
        password = new String(System.console().readPassword());
        return AccessControl.validate(userId, password);
    }
}
