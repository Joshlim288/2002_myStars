import java.io.*;
import java.util.ArrayList;

public class AdminDataManager implements DataManager{
    private ArrayList<Admin> adminList;
    public AdminDataManager() {
        this.adminList = new ArrayList<>();
    }

    /**
     * Load adminList from data/adminData.dat
     */
    public void load() {
        try {
            FileInputStream fileIn = new FileInputStream("data/adminData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            adminList = (ArrayList<Admin>) in.readObject();
            if (adminList.isEmpty()) {
                System.out.println("...admin data loaded but is empty");
            } else {
                System.out.println("...admin data loaded");
            }
        } catch (IOException e) {
            System.out.println("Admin file not found or is blank. Initializing file...");
            adminList = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save adminList to data/adminData.dat
     */
    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/users/adminData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(adminList);
            out.close();
            fileOut.close();
            System.out.println("... admin data saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves an admin object from adminList based on staff number.
     * @param staffNum The staff number of the admin we are retrieving.
     * @return the admin if the staff number exists; null otherwise
     */
    public Admin getAdmin(String staffNum) {
        for (Admin admin : adminList) {
            if (admin.getStaffNum().equals(staffNum)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Removes an admin from adminList based on staff number.
     * @param staffNum The staff number of the admin we are removing.
     * @return true if removal is successful
     */
    public boolean removeAdmin(String staffNum) {
        Admin adminToRemove = getAdmin(staffNum);
        return adminList.remove(adminToRemove); // if adminToRemove is null, will return false
    }

    /**
     * Adds a new admin into adminList.
     * @param newAdmin The admin object to be added.
     * @return true if addition is successful.
     */
    public boolean addAdmin(Admin newAdmin) {
        // check if admin with identical staff number already exists in the list
        if (adminList.contains(newAdmin)) { // checks using equals method in Admin
            System.out.println("The admin already exists in the database!");
            return false;
        }
        adminList.add(newAdmin);
        return true;
    }

    public ArrayList<Admin> getAdminList(){
        return adminList;
    }
}
