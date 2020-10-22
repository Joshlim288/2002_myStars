import java.lang.reflect.Constructor;
import java.util.Scanner;

public class UserInterfaceCreator {
    /**
     * Requires interface to be named "domain"Interface i.e. StudentInterface
     * @param domain
     * @return
     */
    public static UserInterface makeInterface(String domain, User currentUser, Scanner sc) {
        Class uiClass;
        Constructor uiConstructor;
        try{
            uiClass = Class.forName(domain+"Interface");
            Class[] types = {User.class, Scanner.class};
            uiConstructor = uiClass.getConstructor(types);
            return (UserInterface) uiConstructor.newInstance(currentUser, sc);
        } catch (Exception e) {
            return null;
        }
    }
}
