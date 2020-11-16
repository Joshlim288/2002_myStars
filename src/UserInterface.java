import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

enum typeOfInput{
    NAME, INT, TIME, DATETIME, USERID, EMAIL, MATRIC_NUM, GENDER, STAFF_NUM,
    COURSE_CODE, COURSE_TYPE, INDEX_NUM, LESSON_TYPE, DAY, GROUP_NAME, STANDARD
}

/**
 * Abstract class that defines a general userInterface that would show the menu for a particular type of user
 * Also contains an input getter that force users to enter correct input, or abort the function with the ~ character
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-11-16
 */
public abstract class UserInterface {
    protected Scanner sc;
    protected UserValidator userValidator;
    protected CourseValidator courseValidator;
    protected DateTimeFormatter dateTimeFormatter;
    protected DateTimeFormatter timeFormatter;

    /**
     * Constructor for UserInterface, creates Validators and Formatters for formatting of input
     * @param sc Scanner used to get input
     */
    public UserInterface(Scanner sc){
        this.sc = sc;
        userValidator = new UserValidator();
        courseValidator = new CourseValidator();
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    /**
     * Function which every UserInterface must implement
     * Shows the menu and handles the control logic for the particular User type
     */
    public abstract void start();

    /**
     * Gets string input from user and validates it
     * @return user input that is valid as String
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getInput(typeOfInput inputType) throws EscapeException{
        while (true) {
            String input;
            input = sc.nextLine();
            if (input.equals("~")) {
                throw new EscapeException("Exiting to main menu");
            } else {
                if (validateInput(input, inputType))
                    return input;
                System.out.print("Please enter again: ");
            }
        }
    }

    /**
     * Validates input from user
     * @param input input to be validated
     * @param inputType enum representing type of input
     * @return true if input is valid
     */
    private boolean validateInput(String input, typeOfInput inputType) {
        switch (inputType) {
            case NAME: // names
                return userValidator.validateName(input);
            case INT: // integer inputs
                return userValidator.validateInt(input);
            case TIME: // time
                return userValidator.validateTime(input);
            case DATETIME: // datetime
                return userValidator.validateDateTime(input);
            case USERID: // userID
                return userValidator.validateUserID(input);
            case EMAIL: // email
                return userValidator.validateEmail(input);
            case MATRIC_NUM: // matricNum
                return userValidator.validateMatricNum(input);
            case GENDER: // gender
                return userValidator.validateGender(input);
            case STAFF_NUM: // staff num
                return userValidator.validateStaffNum(input);
            case COURSE_CODE: // course code
                return courseValidator.validateCourseCode(input);
            case COURSE_TYPE: // course type
                return courseValidator.validateCourseType(input);
            case INDEX_NUM:  // index num
                return courseValidator.validateIndexNum(input);
            case LESSON_TYPE: // lesson type
                return courseValidator.validateLessonType(input);
            case DAY: // day
                return courseValidator.validateDay(input);
            case GROUP_NAME: // group name
                return courseValidator.validateGroupName(input);
            case STANDARD:
                return true;
            default:
                throw new IllegalArgumentException("ERROR: Invalid input type.");
        }
    }

    /**
     * Used to allow User to read the results of their chosen function before returning to the menu
     */
    public void waitForEnterInput(){
        System.out.println("Press Enter to continue...");
        System.console().readPassword();

    }

    /**
     * Asks the User to confirm logout, automatically logs out in 10 seconds
     * @return true if user wishes to logout, false otherwise
     */
    public boolean exit() {
            int waitPeriodMillis = 10*1000;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Confirm logout? automatically logging out in 10 seconds\n" +
                                "Type \"Y\" or \"N\" then enter: ");
            long startTime = System.currentTimeMillis();
            try {
                while ((System.currentTimeMillis() - startTime) < waitPeriodMillis && !in.ready());
                return !in.ready() || !in.readLine().toUpperCase().equals("N");
            } catch (IOException e){
                System.out.println(e.getMessage());
                return false;
            }
     }

}
