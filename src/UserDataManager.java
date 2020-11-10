import java.io.*;
import java.util.ArrayList;

public class UserDataManager implements DataManager{
    private ArrayList<User> userList;
    public UserDataManager(){
        this.userList = new ArrayList<>();
    }

    /**
     * Load userList from data/users
     * Will check all files within the data/users folders, read in all .dat files
     * Appending all Users to the userList
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
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (userList.isEmpty()) {
            System.out.println("...user data loaded but is empty");
        } else {
            System.out.println("...user data loaded");
        }
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

    public ArrayList<User> getUserList(){
        return userList;
    }
}
