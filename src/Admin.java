/**
 * Admin represents an academic staff in the university.
 * Admins can add a new Student, and modify his/her course registrations.
 * Admins can also add a new course, and modify a course's details.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */

public class Admin extends User{

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
     * @param hashedPassword This admin's password (hashed).
     * @param adminName This admin's name.
     * @param faculty This admin's faculty.
     * @param staffNum This admin's unique staffNum
     */
    public Admin(String userID, String hashedPassword, String adminName, String faculty, String staffNum) {
        super(userID, hashedPassword);
        this.adminName = adminName;
        this.faculty = faculty;
        this.staffNum = staffNum;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        this.staffNum = staffNum;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return false;
        }
        if (!(o instanceof Admin)) {
            return false;
        }
        Admin other = (Admin) o;
        return other.getStaffNum() == this.staffNum;
    }
}
