public class CourseValidator extends Validator {

    public boolean validateCourseCode(String courseCode) {
        if (courseCode.matches("[A-Z]{2}[0-9]{4}")) {
            return true;
        }
        System.out.println("ERROR: Course code format is invalid.");
        return false;
    }

    public boolean validateCourseType(String courseType) {
        try {
            courseType.valueOf(courseType);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Course type can only be CORE / MPE / GER / UE.");
            return false;
        }
    }

    public boolean validateIndexNum(String indexNum) {
        if (indexNum.matches("[0-9]{5}")) {
            return true;
        }
        System.out.println("ERROR: Index number can only 5 digits.");
        return false;
    }

    public boolean validateLessonType(String lessonType) {
        try {
            typeOfLesson.valueOf(lessonType);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Lesson type can only be LEC / TUT / LAB / DES / PRJ / SEM");
            return false;
        }
    }

    public boolean validateDay(String day) {
        try {
            dayOfWeek.valueOf(day);
            return true;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: Day can only be MON / TUES / WED / THURS / FRI / SAT / SUN");
            return false;
        }
    }

    public boolean validateGroupName(String groupName) {
        if (groupName.matches("^[0-9A-Z]+$")) {
            return true;
        }
        System.out.println("ERROR: Group name can only contain alphabets (in uppercase) and numbers.");
        return false;
    }
}
