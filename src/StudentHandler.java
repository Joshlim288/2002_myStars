import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** Control class for handling student matters
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
public class StudentHandler {
    Student currentStudent;
    Student otherStudent;
    StudentDataManager sdm;
    CourseDataManager cdm;

    public StudentHandler(String matricNum) {
        this.sdm = new StudentDataManager();
        this.cdm = new CourseDataManager();
        sdm.load();
        cdm.load();
        this.currentStudent = sdm.getStudent(matricNum);
        this.otherStudent = null;
    }

    public boolean studentInCourse(Course courseSelected){
        return currentStudent.retrieveIndex(courseSelected.getCourseCode()) != null;
    }

    //TODO: Improve input validation

    // Used for both changing/swapping index and adding new course
    // For changing/swapping index, indexToDrop will be the one to drop
    // For adding new course, indexToDrop will be null
    // If clash found, returns the index new course clashed with through
    // Converts course index as integer first for switch statement of printStatusOfAddCourse
    //TODO: To improve, feels like it can be done better
    public int addCourse(Student student, Course course, Index indexToAdd, Index indexToDrop) {
        String clashWithRegistered = hasClash(indexToAdd, student.getCoursesRegistered());
        String clashWithWaitList = hasClash(indexToAdd, student.getWaitList());

        if(clashWithRegistered != null)
            return Integer.parseInt(clashWithRegistered);
        if(clashWithWaitList != null)
            return Integer.parseInt(clashWithWaitList);

        else if (indexToAdd.isAtMaxCapacity()) {
            if (indexToDrop != null) dropCourse(course, indexToDrop.getIndexNum());
            updateWaitList(course, indexToAdd);
            return 1;
        } else{
            if (indexToDrop != null) dropCourse(course, indexToDrop.getIndexNum());
            indexToAdd.addToEnrolledStudents(indexToAdd.getEnrolledStudents(), student.getMatricNum());
            student.addCourse(course.getCourseCode(), indexToAdd.getIndexNum(), course.getAcademicUnits());
            return 2;
        }
    }

    public void dropCourse(Course course,String index) {
        Index cIndex = course.getIndex(index);
        //Remove student from list of enrolled students in index
        ArrayList<Student> enrolledStudents = new ArrayList<>();
        for(String matricNum: cIndex.getEnrolledStudents()){
            enrolledStudents.add(sdm.getStudent(matricNum));
        }
        cIndex.removeFromEnrolledStudents(enrolledStudents, this.currentStudent);

        //Remove course from student's registered courses
        currentStudent.removeCourse(course.getCourseCode(), course.getAcademicUnits());

        //Register student at start of waitlist for the course
        if(!cIndex.getWaitlist().isEmpty()) {
            String matricNum = cIndex.removeFromWaitlist(cIndex.getWaitlist());
            Student studentRemoved = sdm.getStudent(matricNum);
            studentRemoved.removeCourseFromWaitList(course.getCourseCode());
            studentRemoved.addCourse(course.getCourseCode(), index, course.getAcademicUnits());

            //Create a MailHandler object to send an email to student removed from wait-list
            MailHandler.sendMail(studentRemoved.getEmail(),
                      "You have been removed from a wait-list!",
                          "Successful Registration of Course");
        }
    }

    private void updateWaitList(Course course, Index index) {
        index.addToWaitlist(index.getWaitlist(), currentStudent.getMatricNum());
        currentStudent.addCourseToWaitList(course.getCourseCode(), index.getIndexNum());
    }

    private String hasClash(Index indexToAdd, HashMap<String,String> coursesRegistered) {
        //Retrieve lessons to be added for new index
        ArrayList<Lesson> lessonsToCheck = indexToAdd.getLessons();

        // For all indexes registered for the student
        for (Map.Entry<String, String> entry : coursesRegistered.entrySet()) {
            Course courseIndexBelongsTo = cdm.getCourse(entry.getKey());
            //For each lesson in index to check against
            for (Lesson existingLesson : courseIndexBelongsTo.getIndex(entry.getValue()).getLessons()) {
                //For each lesson in new index
                for (Lesson newLesson : lessonsToCheck) {
                    LocalTime startTime = newLesson.getStartTime();
                    LocalTime endTime = newLesson.getEndTime();

                    //Return clashing index and break if found
                    if (startTime.isBefore(existingLesson.getEndTime()) &&
                            startTime.isAfter(existingLesson.getStartTime()))
                        return entry.getValue();

                    //Return clashing index and break if found
                    if (endTime.isBefore(existingLesson.getEndTime()) &&
                            endTime.isAfter(existingLesson.getStartTime()))
                        return entry.getValue();
                }
            }
        }
        return null;
    }

    public boolean willGoOverMaxAU(Course courseSelected) {
        return currentStudent.getCurrentAUs() + courseSelected.getAcademicUnits() > currentStudent.getMaxAUs();
    }

    public String getRegisteredCourses() {
        //Get list of courses student is registered in.
        HashMap<String, String> coursesRegistered = currentStudent.getCoursesRegistered();

        //Use StringBuilder to create required output and return to StudentInterface
        StringBuilder stringBuilder = new StringBuilder();
        coursesRegistered.forEach((course, index) -> stringBuilder.append(course + " " + ": Index " + index + "\n"));
        return stringBuilder.toString();
    }

    public void retrieveOtherStudent(Scanner sc) throws AccessDeniedException{
        User targetUser = MyStars.login(sc);
        while (targetUser.equals(currentStudent)){
            System.out.println("Error! You have chosen yourself.");
            targetUser = MyStars.login(sc);
        }
        otherStudent = sdm.getStudent(((Student) targetUser).getMatricNum());
    }

    //Send email to other student if a swap has been performed successfully
    public void updateOtherStudent(Course courseSelected, Index oldIndex, Index newIndex){
        MailHandler.sendMail(otherStudent.getEmail(),
                  currentStudent.getName() + "has swapped indexes for " +
                             courseSelected.getCourseName() + ". Your index " + oldIndex.getIndexNum() +
                             "is now " + newIndex.getIndexNum() + ".",
                      "Successful Swap of Indexes for " + courseSelected.getCourseName());
    }

    public void close() {
        sdm.save();
        cdm.save();
    }
}
