import java.lang.reflect.Constructor;
import java.util.Scanner;

/**
 * Utility class used to dynamically create UserInterfaces
 *
 * Depends on a strict naming convention of having a class with the name user_domain+"Interface" e.g. StudentInterface
 * defined in the project scope
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.5
 * @since 1.4
 */
public class UserInterfaceCreator {
    /**
     * Creates a UserInterface object to be run by the main program
     * @param domain Indicates what type of User has logged in, to find the corresponding UserInterface
     * @return UserInterface object based on the domain of the logged in User
     */
    public static UserInterface makeInterface(String domain, User currentUser, Scanner sc) {
        Class uiClass;
        Constructor uiConstructor;
        try{
            uiClass = Class.forName(domain+"Interface"); // finds a matching UserInterface for the given domain
            Class[] types = {User.class, Scanner.class};
            uiConstructor = uiClass.getConstructor(types); // finds a constructor which takes arguments of type (User, Scanner)
            return (UserInterface) uiConstructor.newInstance(currentUser, sc);
        } catch (Exception e) {
            return null; // could not find a matching UserInterface
        }
    }
}
