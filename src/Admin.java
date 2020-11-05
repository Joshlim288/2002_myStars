/**
 * Admin represents an academic staff in the university.
 * Admins can add a new Student, and modify his/her course registrations.
 * Admins can also add a new course, and modify a course's details.
 *
 * @author Jun Wei
 * @version 1.0
 * @since 2020-10-17
 */
//TODO: add validations and error messages
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
     * @param hashedPassword This admin's password (hashed).
     * @param adminName This admin's name.
     * @param faculty This admin's faculty.
     * @param staffNum This admin's unique staffNum
     * @param email This admin's email
     */
    public Admin(String userID, String hashedPassword, String adminName, String faculty, String staffNum, String email) throws ObjectCreationException {
        super(userID, hashedPassword, "Admin", adminName, email);
        if (validateAdminName(adminName) && validateStaffNum(staffNum) && validateFaculty(faculty)) {
            this.adminName = adminName;
            this.faculty = faculty;
            this.staffNum = staffNum;
        } else {
            throw new ObjectCreationException();
        }
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        if (validateAdminName(adminName))
            this.adminName = adminName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        if (validateFaculty(faculty))
            this.faculty = faculty;
    }

    public String getStaffNum() {
        return staffNum;
    }

    public void setStaffNum(String staffNum) {
        if (validateStaffNum(staffNum))
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

    private boolean validateAdminName(String adminName) {
        if (adminName.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Name can only contain alphabets and spaces.");
        return false;
    }

    private boolean validateFaculty(String faculty) {
        if (faculty.matches("^[ A-Za-z]+$"))
            return true;
        System.out.println("ERROR: Faculty name can only contain alphabets and spaces.");
        return false;
    }

    /**
     * Assumes format of staffNum as a string of 10 characters where:
     * <ul>
     *     <li>ADMIN must be the first 5 characters.</li>
     *     <li>The last character is any letter from A-Z (capital).</li>
     *     <li>The remaining 4 characters are any combination of digits from 0-9.</li>
     * </ul>
     * @param staffNum
     * @return
     */
    private boolean validateStaffNum(String staffNum) {
        if (staffNum.matches("ADMIN[0-9]{4}[A-Z]")) {
            return true;
        }
        System.out.println("ERROR: Invalid staff number format.");
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(adminName + ", " + staffNum + "\n");
        stringBuilder.append(faculty + "\n");
        return stringBuilder.toString();
    }
}
