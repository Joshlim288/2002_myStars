import java.io.*;
import java.util.ArrayList;

/**
 * DataManager for Student data
 * Handles the loading, and holding of User objects from the data/users
 * It aggregates all data within the users folder, and is only meant for examining the data
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-10-24
 */
public class UserDataManager implements DataManager{
    private final ArrayList<User> userList;

    /**
     * Constructor for UserDataManager, instantiates the list of Users
     */
    public UserDataManager(){
        this.userList = new ArrayList<>();
    }

    /**
     * Load userList from data/users
     * Will check all files within the data/users folders, read in all .dat files, appending all Users to the userList
     */
    public void load() {
        FileInputStream fileIn;
        ObjectInputStream in;
        File userData = new File("data/users/");
        for (File userFile : userData.listFiles()) {
            try {
                fileIn = new FileInputStream(userFile.getPath());
                in = new ObjectInputStream(fileIn);
                userList.addAll((ArrayList<User>) in.readObject());
            } catch (IOException e) {
                System.out.println("User data file not found or is blank. Initializing file...");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        if (userList.isEmpty())
            System.out.println("...user data loaded but is empty");
    }

    /**
     * Dummy function, userData is aggregated from all files of the User folder
     * There should be no modifications to the userList, any changes should be made in the appropriate handlers
     */
    public void save() {
    }

    /**
     * Retrieves a user object from userList based on userID.
     * @param userID The user ID of the user we are retrieving.
     * @return the user if the user ID exists; null otherwise
     */
    public User getUser(String userID) {
        for (User user : userList) {
            if (user.getUserID().equals(userID)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Retrieves the List of all Users currently in the database
     * @return ArrayList of all Users
     */
    public ArrayList<User> getUserList(){
        return userList;
    }
}
