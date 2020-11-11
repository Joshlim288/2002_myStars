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
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentHandler {
    Student currentStudent;
    Student otherStudent;
    StudentDataManager sdm;
    CourseDataManager cdm;

    public StudentHandler(Student currentStudent) {
        this.currentStudent = currentStudent;
        this.otherStudent = null;
        this.sdm = new StudentDataManager();
        this.cdm = new CourseDataManager();
        sdm.load();
        cdm.load();
    }

    public boolean studentInCourse(Course courseSelected){
        if(currentStudent.retrieveIndex(courseSelected) != null)
            return true;
        return false;
    }

    //TODO: Improve input validation

    // Used for both changing/swapping index and adding new course
    // For changing/swapping index, indexToDrop will be the one to drop
    // For adding new course, indexToDrop will be null
    // If clash found, returns the index new course clashed with through
    // Converts course index as integer first for switch statement of printStatusOfAddCourse
    //TODO: To improve, feels like it can be done better
    public int addCourse(Student student, Course course, Index indexToAdd, Index indexToDrop) {
        Index clashWithRegistered = hasClash(indexToAdd, student.getCoursesRegistered());
        Index clashWithWaitList = hasClash(indexToAdd, student.getWaitList());

        if(clashWithRegistered != null)
            return Integer.parseInt(clashWithRegistered.getIndexNum());
        if(clashWithWaitList != null)
            return Integer.parseInt(clashWithWaitList.getIndexNum());

        else if (indexToAdd.isAtMaxCapacity()) {
            if (indexToDrop != null) dropCourse(course, indexToDrop);
            updateWaitList(course, indexToAdd);
            return 1;
        } else{
            if (indexToDrop != null) dropCourse(course, indexToDrop);
            indexToAdd.addToEnrolledStudents(indexToAdd.getEnrolledStudents(), student);
            student.addCourse(course, indexToAdd);
            return 2;
        }
    }

    public void dropCourse(Course course,Index cIndex) {
        //Remove student from list of enrolled students in index
        cIndex.removeFromEnrolledStudents(cIndex.getEnrolledStudents(), this.currentStudent);

        //Remove course from student's registered courses
        currentStudent.removeCourse(course);

        //Register student at start of waitlist for the course
        if(!cIndex.getWaitlist().isEmpty()) {
            Student studentRemoved = cIndex.removeFromWaitlist(cIndex.getWaitlist());
            studentRemoved.removeCourseFromWaitList(course);
            studentRemoved.addCourse(course, cIndex);

            //Create a MailHandler object to send an email to student removed from wait-list
            MailHandler.sendMail(studentRemoved.getEmail(),
                      "You have been removed from a wait-list!",
                          "Successful Registration of Course");
        }
    }

    private void updateWaitList(Course course, Index index) {
        index.addToWaitlist(index.getWaitlist(), this.currentStudent);
        currentStudent.addCourseToWaitList(course, index);
    }

    private Index hasClash(Index indexToAdd, HashMap<Course,Index> coursesRegistered) {
        //Retrieve lessons to be added for new index
        ArrayList<Lesson> lessonsToCheck = indexToAdd.getLessons();

        // For all indexes registered for the student
        for (Map.Entry<Course, Index> entry : coursesRegistered.entrySet()) {
            //For each lesson in index to check against
            for (Lesson existingLesson : entry.getValue().getLessons()) {
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
        HashMap<Course, Index> coursesRegistered = currentStudent.getCoursesRegistered();

        //Use StringBuilder to create required output and return to StudentInterface
        StringBuilder stringBuilder = new StringBuilder();
        coursesRegistered.forEach((course, index) -> stringBuilder.append(course.getCourseName() + " " +
                course.getCourseName() + ": Index" + index.getIndexNum()));
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
