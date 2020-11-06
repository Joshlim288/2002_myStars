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
    protected UserValidator userValidator;
    protected CourseValidator courseValidator;
    protected DateTimeFormatter dateTimeFormatter;
    protected DateTimeFormatter timeFormatter;
    String tempString;

    public UserInterface(Scanner sc){
        this.sc = sc;
        userValidator = new UserValidator();
        courseValidator = new CourseValidator();
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    public abstract void start();

    /**
     * Gets string input from user
     * @return String that matches regex expression
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getInput() throws EscapeException{
        String input;
        input = sc.nextLine();
        if (input.equals("~")) {
            throw new EscapeException("Exiting to main menu");
        } else {
            return input;
        }
    }

//    /**
//     * Gets integer from user, allows user to abort function if they wish by entering "~"
//     * @return int that the user entered
//     * @throws EscapeException Used to abort function and return to main menu
//     */
//    public int getInt() throws EscapeException {
//        int input;
//        while(true) {
//            try {
//                input = Integer.parseInt(getString());
//                return input;
//            } catch (NumberFormatException e) {
//                System.out.println("ERROR: Please enter a number.");
//            }
//        }
//    }
//
//    public LocalDateTime getLocalDateTime(String datetime){
//        while(true) {
//            if (input.equals("~")) throw new EscapeException("Exiting to main menu");
//            return LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//        }
//    }
//
//    public LocalTime getLocalTime() throws EscapeException{
//        String input;
//        while(true) {
//            input = sc.nextLine();
//            if (input.equals("~")) throw new EscapeException("Exiting to main menu");
//            return LocalTime.parse(input, DateTimeFormatter.ofPattern("HH:mm"));
//            }
//        }
//    }
}
