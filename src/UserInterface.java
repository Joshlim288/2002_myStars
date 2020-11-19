import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Enumeration to indicates what types of inputs are allowed for getInput
 */
enum typeOfInput{
    NAME, INT, TIME, DATETIME, USERID, EMAIL, MATRIC_NUM, GENDER, STAFF_NUM,
    COURSE_CODE, COURSE_TYPE, INDEX_NUM, LESSON_TYPE, DAY, GROUP_NAME, STANDARD
}

/**
 * Abstract class that defines a general userInterface that would show the menu for a particular type of user.<p>
 * Also contains an input getter that force users to enter correct input, or abort the function with the ~ character
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.8
 * @since 1.1
 */
public abstract class UserInterface {
    protected Scanner sc;
    protected StudentValidator studentValidator;
    protected AdminValidator adminValidator;
    protected CourseValidator courseValidator;
    protected DateTimeFormatter dateTimeFormatter;
    protected DateTimeFormatter timeFormatter;

    /**
     * Constructor for UserInterface, creates Validators and Formatters for formatting of input
     * @param sc Scanner used to get input
     */
    public UserInterface(Scanner sc){
        this.sc = sc;
        studentValidator = new StudentValidator();
        courseValidator = new CourseValidator();
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    }

    /**
     * Function which every UserInterface must implement.<p>
     * Shows the menu and handles the control logic for the particular User type
     */
    public abstract void start();

    /**
     * Gets string input from user and validates it
     * @param inputType enum of typeOfInput representing type of input
     * @return user input that is valid as String
     * @throws EscapeException Used to abort function and return to main menu
     */
    public String getInput(typeOfInput inputType) throws EscapeException{
        while (true) {
            String input;
            input = sc.nextLine();
            if (input.equals("~")) {
                throw new EscapeException("Exiting to previous menu...");
            } else {
                if (validateInput(input, inputType))
                    return input;
                System.out.print("Please enter input again: ");
            }
        }
    }

    /**
     * Validates input from user<p>
     * Called by {@link UserInterface#getInput(typeOfInput)}.
     * @param input input to be validated
     * @param inputType enum of typeOfInput representing type of input
     * @return true if input is valid and false otherwise
     */
    private boolean validateInput(String input, typeOfInput inputType) {
        return switch (inputType) {
            case NAME -> studentValidator.validateName(input);
            case INT -> studentValidator.validateInt(input);
            case TIME -> studentValidator.validateTime(input);
            case DATETIME -> studentValidator.validateDateTime(input);
            case USERID -> studentValidator.validateUserID(input);
            case EMAIL -> studentValidator.validateEmail(input);
            case MATRIC_NUM -> studentValidator.validateMatricNum(input);
            case GENDER -> studentValidator.validateGender(input);
            case STAFF_NUM -> adminValidator.validateStaffNum(input);
            case COURSE_CODE -> courseValidator.validateCourseCode(input);
            case COURSE_TYPE -> courseValidator.validateCourseType(input);
            case INDEX_NUM -> courseValidator.validateIndexNum(input);
            case LESSON_TYPE -> courseValidator.validateLessonType(input);
            case DAY -> courseValidator.validateDay(input);
            case GROUP_NAME -> courseValidator.validateGroupName(input);
            case STANDARD -> true;
        };
    }

    /**
     * Waits for the User to press Enter before continuing with program flow. <p>
     * Used to allow program to wait for User to read output results from a method's execution first before continuing
     * its execution.
     */
    public void waitForEnterInput(){
        System.out.println("Press Enter to continue...");
        System.console().readPassword(); //Hides any characters that the user may type before pressing Enter

    }

    /**
     * Asks the User to confirm logout, automatically logs out in 10 seconds
     * @return true if user wishes to logout, false otherwise
     */
    public boolean exit() {
            int waitPeriodMillis = 10*1000;
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Confirm logout? Automatically logging out in 10 seconds.\n" +
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
