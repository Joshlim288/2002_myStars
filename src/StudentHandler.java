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

    /**
     * Version 2 of hasClash() yay :D
     * Checks for a clash of timetable. Timetable consists of courses registered and courses on waitlist.
     * @param indexToAdd The index to be checked against the student's timetable.
     * @param studentToCheck The student who is trying to add the new index.
     * @param indexToExclude Used for swapping of indexes, to exclude the index it is swapping from.
     * (e.g. Swapping from index 33333 to index 44444, index 33333 should not be included in the check)
     * @return The index in the timetable that has clashed with the new index, otherwise null if no clashes. */
    public String hasClash(Index indexToAdd, Student studentToCheck, Index indexToExclude) {

        HashMap<String, String> timetable = studentToCheck.getCoursesRegistered();
        timetable.putAll(studentToCheck.getWaitList());
        ArrayList<Lesson> newLessons = indexToAdd.getLessons();
        ArrayList<Lesson> oldLessons = new ArrayList<Lesson>();

        /* For all currently registered indexes, retrieve their lessons and then compare with lessons of
         *  the new index. Comparison only done if the lessons fall on the same day and the index is not
         *  the index to be excluded. */
        for (Map.Entry<String, String> entry : timetable.entrySet()) {
            for (Index index : cdm.getCourse(entry.getKey()).getIndexes())
                if (!index.equals(indexToExclude)){
                    oldLessons.addAll(index.getLessons());
                    for (Lesson oldLesson : oldLessons)
                        for (Lesson newLesson : newLessons)
                            if (newLesson.getDay().equals(oldLesson.getDay()))
                                /* Start of new lesson < End of old lesson && End of new lesson > Start of old lesson */
                                if(newLesson.getStartTime().isBefore(oldLesson.getEndTime()) &&
                                        newLesson.getEndTime().isAfter(oldLesson.getStartTime()))
                                    return index.getIndexNum();
                }
        }
        return null;
    }

    //TODO: Consider whether to split the dropping and adding of course for swapping indexes
    //TODO: Currently there is alr a separate dropCourse() that is called within this addCourse()
    /** Version 2 of addCourse() yay :D
     * Adds the selected index to the selected student.
     * @param student The student whose timetable to add the index to.
     * @param course The course of the index to be added.
     * @param indexToAdd The index to be added.
     * @param indexToDrop The index to be dropped, if no index to be dropped, use null. Useful for swapping indexes.
     * (e.g. Swapping from index 33333 to index 44444 requires a drop from index 33333 first)
     * @return The status of adding the course, to be used by printStatusOfAddCourse() in Student Interface */
    public int addCourse(Student student, Course course, Index indexToAdd, Index indexToDrop) {
        if (indexToAdd.isAtMaxCapacity()) {
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum());
            indexToAdd.addToWaitlist(currentStudent.getMatricNum());
            currentStudent.addCourseToWaitList(course.getCourseCode(), indexToAdd.getIndexNum());
            return 1;
        } else{
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum());
            indexToAdd.addToEnrolledStudents(student.getMatricNum());
            student.addCourse(course.getCourseCode(), indexToAdd.getIndexNum(), course.getAcademicUnits());
            return 2;
        }
    }

    //Refreshing of waitlist in separate function
    public void dropCourse(Student student, Course course,String index) {
        Index cIndex = course.getIndex(index);
        //Remove student from list of enrolled students in index
        cIndex.removeFromEnrolledStudents(student.getMatricNum());

        //Remove course from student's registered courses
        student.removeCourse(course.getCourseCode(), course.getAcademicUnits());
    }

    public void refreshWaitList(Course course, Index index) {
        if (!index.getWaitlist().isEmpty()) {
            Student studentRemoved = sdm.getStudent(index.removeFromWaitlist());
            studentRemoved.removeCourseFromWaitList(course.getCourseCode());
            studentRemoved.addCourse(course.getCourseCode(), index.getIndexNum(), course.getAcademicUnits());

            //Create a MailHandler object to send an email to student removed from wait-list
            MailHandler.sendMail(studentRemoved.getEmail(),
                    "You have been removed from a wait-list!",
                    "Successful Registration of Course");
        }
    }

    public boolean willGoOverMaxAU(Course courseSelected) {
        int totalAUs = currentStudent.getCurrentAUs();
        for (Map.Entry<String, String> entry : currentStudent.getWaitList().entrySet())
            totalAUs += cdm.getCourse(entry.getKey()).getAcademicUnits();
        return (totalAUs + courseSelected.getAcademicUnits()) > currentStudent.getMaxAUs();
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
