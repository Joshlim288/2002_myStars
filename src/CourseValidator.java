/**
 * CourseValidator extends the Validator class and adds additional validation methods specialized for course-related
 * data in the system.
 * Assumes user knows what the input formats are (through a guide, manual, etc).
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.12
 * @since 1.10
 */
public class CourseValidator extends Validator {

    /**
     * Validates a course code. Assumes format of course code as a string of 6 characters where:
     * <ul>
     *     <li>First two characters are uppercase letters</li>
     *     <li>Last four characters are digits</li>
     * </ul>
     * @param courseCode String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateCourseCode(String courseCode) {
        if (courseCode.matches("[A-Z]{2}[0-9]{4}")) {
            return true;
        }
        System.out.println("ERROR: Course code format is invalid.");
        return false;
    }

    /**
     * Validates course type. Accepted course types are according to typeOfCourse enum defined under Course class.
     * @param courseType String to match against the pattern
     * @return true if input matches, false otherwise
     * @see typeOfCourse
     */
    public boolean validateCourseType(String courseType) {
        try {
            typeOfCourse.valueOf(courseType);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Course type can only be CORE / MPE / GER / UE.");
            return false;
        }
    }

    /**
     * Validates index number. Assumes index number is a String of 5 digits.
     * @param indexNum String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateIndexNum(String indexNum) {
        if (indexNum.matches("[0-9]{5}")) {
            return true;
        }
        System.out.println("ERROR: Index number can only 5 digits.");
        return false;
    }

    /**
     * Validates lesson type. Accepted lesson types are according to typeOfLesson defined under Lesson class.
     * @param lessonType String to match against the pattern
     * @return true if input matches, false otherwise
     * @see typeOfLesson
     */
    public boolean validateLessonType(String lessonType) {
        try {
            typeOfLesson.valueOf(lessonType);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Lesson type can only be LEC / TUT / LAB / DES / PRJ / SEM");
            return false;
        }
    }

    /**
     * Validates an index's group name. Group name may only contain uppercase alphabets and digits.
     * @param groupName String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateGroupName(String groupName) {
        if (groupName.matches("^[0-9A-Z]+$")) {
            return true;
        }
        System.out.println("ERROR: Group name can only contain alphabets (in uppercase) and numbers.");
        return false;
    }

    /**
     * Validates a lesson's teaching weeks. Teaching weeks are 1-13 inclusive. Each week must be checked individually.
     * @param week String to match against the pattern
     * @return true if input matches, false otherwise
     */
    public boolean validateTeachingWeek(String week) {
        if (validateInt(week)) {
            int teachingWeek = Integer.parseInt(week);
            if (teachingWeek <= 13 && teachingWeek >= 1) {
                return true;
            }
            System.out.println("ERROR: Invalid week entered.");
        } else {
            System.out.println("Ensure input is of the correct format (e.g. 1,2,3,4).");
        }
        return false;
    }
}
