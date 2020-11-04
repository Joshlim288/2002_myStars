import java.util.Scanner;

/**
 * Abstract class that defines a general userInterface that would show the menu for a particular type of user
 * Also contains input getters that force users to enter correct input, or abort the function with the ~ character
 */
public abstract class UserInterface {
    protected Scanner sc;
    public UserInterface(Scanner sc){
        this.sc = sc;
    }

    public abstract void start();

    /**
     * Gets input from user that matches the given regex
     * @param regex Expression to be matched to
     * @return String that matches regex expression
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getString(String regex) throws EscapeException{
        String input;
        System.out.println("Enter string: (\"~\" to abort)");
        while (true) {
            input = sc.nextLine();
            if (input.equals("~")) {
                throw new EscapeException("Exiting to main menu");
            } else if(input.matches(regex)){
                return input;
            } else {
                System.out.println("Invalid input, try again or enter \"~\" to exit");
            }
        }
    }

    /**
     * Gets input from user, allows user to abort function if they wish by entering "~"
     * @return String input from user
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getString() throws EscapeException{
        String input;
        System.out.println("Enter string: (\"~\" to abort)");
        input = sc.nextLine();
        if (input.equals("~")) {
            throw new EscapeException("Exiting to main menu");
        }
        return input;
    }

    /**
     * Gets integer from user, allows user to abort function if they wish by entering "~"
     * @param startInt lower limit of integer
     * @param endInt lower limit of integer
     * @return int that the user entered
     * @throws EscapeException Used to abort function and return to main menu
     */
    public int getInt(int startInt, int endInt) throws EscapeException {
        int input;
        while(true) {
            System.out.println("Enter number: (\"~\" to abort)");
            input = Integer.parseInt(getString("^[0-9]*$"));
            if (input < startInt && input > endInt) {
                return input;
            }
            System.out.println("Number out of range, try again or enter \"~\" to exit");
        }
    }
}
