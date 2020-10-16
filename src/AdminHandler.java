/**
 * Control class for handling admin matters
 * Try not to ask for input here, get input using scanner in AdminInterface
 * Then pass the input as arguments
 *
 * Return types also no need to remain void, change to your requirements
 *
 * If you need any data, you can call the static methods in FileHandler with the class name
 * e.g. FileHandler.getStudent(name)
 *
 * Try to add JavaDocs as you go
 */
public class AdminHandler {
    Admin currentAdmin;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }

    public void editCourse(){

    }

    public void addCourse(){

    }

    public void checkSlot(){

    }

    /**
     * Prints student list
     * @param byIndex
     * Sort student list by index if byIndex is True, by course if False
     */
    public void printStudentList(boolean byIndex){

    }

    public void editAccessPeriod(){

    }

    public void addStudent(){

    }
}
