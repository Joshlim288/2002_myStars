/**
 * Admin represents an academic staff in the university.<p>
 * Admins can add a new Student, and modify his/her course registrations.<p>
 * Admins can also add a new course, and modify a course's details.<p>
 *
 * To see the format of all the data fields, refer to UserValidator
 *
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.6
 * @since 1.1
 */
public class Admin extends User {
    /**
     * This admin's name.
     */
    private String adminName;

    /**
     * The faculty this admin belongs to.
     */
    private String faculty;

    /**
     * The admin's staff number. It is unique.
     */
    private String staffNum;

    /**
     * Constructor for <code>Admin</code>.
     * @param userID This admin's user ID.
     * @param password This admin's password (hashed).
     * @param adminName This admin's name.
     * @param faculty This admin's faculty.
     * @param staffNum This admin's unique staffNum
     * @param email This admin's email
     */
    public Admin(String userID, String password, String adminName, String faculty, String staffNum, String email) {
        super(userID, password, "Admin", adminName, email);
        this.adminName = adminName;
        this.faculty = faculty;
        this.staffNum = staffNum;
    }

    /**
     * @return Full Name of the admin
     */
    public String getAdminName() {
        return adminName;
    }

    /**
     * @param adminName Full name to change the admin's name to
     */
    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    /**
     * @return Faculty as a String, that the admin belongs to (e.g. SCSE, CEE)
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * @param faculty Faculty name to assign to the admin
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**
     * @return staffNum as a String, that uniquely identifies the staff
     */
    public String getStaffNum() {
        return staffNum;
    }

    /**
     * @param staffNum that uniquely identifies the staff. To see format, view UserValidator
     */
    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    /**
     * Checks if two Admins are the same based on staffNum
     * @param o the other Admin to compare against
     * @return true if this Admins
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return false;
        }
        if (!(o instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) o;
        return other.getStaffNum().equals(this.staffNum);
    }

    /**
     * @return Admin details as a String, suitable for printing
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(adminName + ", " + staffNum + "\n");
        stringBuilder.append(faculty + "\n");
        return stringBuilder.toString();
    }
}
