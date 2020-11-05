import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
     * @param message Prompt to user to enter input, specify what you are looking for
     * @param regex Expression to be matched to
     * @return String that matches regex expression
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getString(String message, String regex) throws EscapeException{
        String input;
        System.out.println(message);
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
        return getString("Enter a string: ", "");
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
            input = Integer.parseInt(getString("Enter number: ", "^[0-9]*$"));
            if (input < startInt && input > endInt) {
                return input;
            }
            System.out.printf("Number must be between %d and %d, try again or enter \"~\" to exit\n", startInt, endInt);
        }
    }

    public LocalDateTime getLocalDateTime() throws EscapeException{
        String input;
        while(true) {
            input = sc.nextLine();
            if (input.equals("~")) throw new EscapeException("Exiting to main menu");
            try {
                return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("datetime must be entered in format yyyy-MM-dd HH:mm, " +
                        "try again or enter \"~\" to exit\n");
            }
        }
    }

    public LocalTime getLocalTime() throws EscapeException{
        String input;
        while(true) {
            input = sc.nextLine();
            if (input.equals("~")) throw new EscapeException("Exiting to main menu");
            try {
                return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
            } catch (DateTimeParseException e) {
                System.out.println("datetime must be entered in format HH:mm, " +
                        "try again or enter \"~\" to exit\n");
            }
        }
    }
}
