import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Control class that handles the logic for functions available to Students
 * Operates on and uses the entity classes using DataManagers
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.15
 * @since 1.1
 */
public class StudentHandler {
    Student currentStudent;
    StudentDataManager sdm;
    CourseDataManager cdm;

    /**
     * Constructor for the handler
     * Initializes the DataManagers and calls their load() methods to load in the required data from the data folder
     * @param matricNum the matriculation number of the student currently using myStars
     * @see StudentDataManager#load()
     * @see CourseDataManager#load()
     */
    public StudentHandler(String matricNum) {
        this.sdm = new StudentDataManager();
        this.cdm = new CourseDataManager();
        sdm.load();
        cdm.load();
        this.currentStudent = sdm.getStudent(matricNum);
    }

    /** Adds the selected index to the student's list of registered courses.
     * Student can be added to waitlist or list of enrolled students for the course.
     * Updates student's registered courses and updates index's list of enrolled students/waitlist.
     * Also drops any prior index if required before adding new index.
     * @param student The student whose timetable to add the index to.
     * @param course The course of the index to be added.
     * @param indexToAdd The index to be added.
     * @param indexToDrop The index to be dropped, if no index to be dropped, use null. Useful for swapping/changing indexes.
     * (e.g. Swapping from index 33333 to index 44444 requires a drop from index 33333 first)
     * @param checkVacancy passed in as false only for swapIndex() in StudentInterface,
     *         as we still want to be able to swap their indexes rather than add them to wait-list
     *         even if their indexes are already at max vacancy at the time of swap
     * @return The status of adding the course, 1 if added to student's registered courses, 2 if added to waitlisted courses.
     * */
    public int addCourse(Student student, Course course, Index indexToAdd, Index indexToDrop, boolean checkVacancy) {

        if (!checkVacancy || !indexToAdd.isAtMaxCapacity()) {
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum(), false);
            indexToAdd.addToEnrolledStudents(student.getMatricNum());
            student.addCourse(course.getCourseCode(), indexToAdd.getIndexNum(), course.getAcademicUnits());
            return 1;
        }
        else{
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum(), false);
            indexToAdd.addToWaitlist(student.getMatricNum());
            student.addCourseToWaitList(course.getCourseCode(), indexToAdd.getIndexNum());
            return 2;
        }
    }

    /** Drops the selected index from the student's list of registered courses/waitlisted courses.
     *  Updates student's registered/waitlisted courses and updates index's list of enrolled students.
     * @param student The student whose timetable to drop the index from.
     * @param course The course of the index to be dropped.
     * @param index The index to be dropped.
     * @param waitlisted <code>true</code> if course is waitlisted for student, <code>false</code> if registered for student.
     */
    public void dropCourse(Student student, Course course, String index, boolean waitlisted) {
        Index indexToDrop = course.getIndex(index);
        if (!waitlisted) {
            indexToDrop.removeFromEnrolledStudents(student.getMatricNum());
            student.removeCourse(course.getCourseCode(), course.getAcademicUnits());
        }
        else{
            indexToDrop.removeFromWaitlist(student.getMatricNum());
            student.removeCourseFromWaitList(course.getCourseCode());
        }
    }

    /** Triggers update of waitlist for selected index and is only used after a student drops or changes a index.
     * If waitlist is not empty, remove student at head of waitlist and register him for the course.
     * Email will be sent to student's email informing the successful registration of course from waitlist.
     * @param course The course of the index for updating of waitlist.
     * @param index The index that contains the waitlist to be updated.
     */
    public void refreshWaitList(Course course, Index index) {
        if (!index.getWaitlist().isEmpty()) {
            Student studentRemoved = sdm.getStudent(index.removeFromWaitlist());
            studentRemoved.removeCourseFromWaitList(course.getCourseCode());
            studentRemoved.addCourse(course.getCourseCode(), index.getIndexNum(), course.getAcademicUnits());
            index.addToEnrolledStudents(studentRemoved.getMatricNum());

            MailHandler.sendMail(studentRemoved.getEmail(),
                    "Dear " + studentRemoved.getName() +
                            ", you have been successfully removed from the wait-list for the following courses:\n\n" +
                            course.getCourseCode() + ", " + course.getCourseName() + "\n" +
                            "Index Registered: " + index.getIndexNum() +  "\n" +
                            "Current AUs registered: " + studentRemoved.getCurrentAUs() ,
                    "Successful Registration for " + course.getCourseCode() + ", " + course.getCourseName() +
                            ": Index " + index.getIndexNum());
        }
    }

    /**
     * Retrieves course based on input from user with CourseDataManager.
     * If course does not exist in database, user is prompted to enter a valid course.
     * @param courseCode Course input from user to search database with.
     * @return Course if it exists in database, otherwise <code>null</code> is returned.
     * @see CourseDataManager#getCourse(String)
     */
    public Course retrieveCourse(String courseCode){
        Course courseSelected = cdm.getCourse(courseCode);
        if (courseSelected == null)
            System.out.println("Course does not exist in the database!\n" +
                    "Please re-enter course again.\n");
        return courseSelected;
    }

    /** Checks if course selected exists in database and is used after calling <code>retrieveCourse</code>.
     * @param courseSelected Course selected to check.
     * @return <code>true</code> if course selected exists in database, <code>false</code> if it does not.
     */
    public boolean checkValidCourse(Course courseSelected){
        return courseSelected != null;
    }

    /** Retrieves index based on input from user and course selected.
     * If index does not exist in the selected course, user is prompted to enter a valid index.
     * @param courseSelected Course selected to check.
     * @param indexNum Index selected to check.
     * @return Index if it exists in the course selected, otherwise <code>null</code> is returned.
     */
    public Index retrieveIndex(Course courseSelected, String indexNum){
        Index indexSelected = courseSelected.getIndex(indexNum);

        if (indexSelected == null)
            System.out.println("Index does not exist in this course!\n" +
                               "Please re-enter index again.");
        return indexSelected;
    }

    /** Checks if index selected is in course previously selected and is used after calling <code>retrieveIndex</code>.
     * Also checks if index will clash with current timetable of student through <code>hasTimetableClash</code>.
     * @param indexSelected Index selected to check.
     * @param studentToCheck Student to retrieve timetable and check for clashes.
     * @param indexToExclude Index to exclude from checking for clashes with timetable. Useful for changing/swapping index.
     * (e.g. For changing from index 33333 to index 44444, index 33333 should not be included in the check for clash)
     * @return <code>true</code> if index selected is available to be added, otherwise <code>false</code> is returned.
     */
    public boolean checkValidIndex(Index indexSelected, Student studentToCheck, Index indexToExclude) {
        if (indexSelected == null) return false;
        return !hasTimetableClash(indexSelected, studentToCheck, indexToExclude);
    }

    /** Retrieves index that the student is registered in based on the course chosen.
     * Uses <code>retrieveIndex</code> but indexNum passed in is retrieved from student's list of registered indexes instead.
     * @param student Student to get index registered in.
     * @param courseSelected Course to get index registered.
     * @return Index that the student is registered in.
     * @see #retrieveIndex(Course, String)
     */
    public Index getIndexRegistered(Student student, Course courseSelected){
        return retrieveIndex(courseSelected, student.retrieveIndexFromRegistered(courseSelected.getCourseCode()));
    }

    /** Checks if the student will go over maximum AUs after adding selected course.
     * Adds AUs of courses registered and courses waitlisted with the course to be added to compare with max AUs allowed.
     * @param courseSelected The course to be added by the student.
     * @return <code>true</code> if student will go over max AUs allowed, otherwise <code>false</code>.
     */
    public boolean willGoOverMaxAU(Course courseSelected) {
        int totalAUs = currentStudent.getCurrentAUs();
        for (Map.Entry<String, String> entry : currentStudent.getWaitList().entrySet())
            totalAUs += cdm.getCourse(entry.getKey()).getAcademicUnits();
        return (totalAUs + courseSelected.getAcademicUnits()) > currentStudent.getMaxAUs();
    }

    //Check for exam clashes and returns true if clash, otherwise false.
    public boolean hasExamClash(Course courseSelected){

        LocalDateTime[] oldExamTime;
        LocalDateTime[] newExamTime = cdm.getCourse(courseSelected.getCourseCode()).getExamDateTime();
        if (newExamTime[0] == null)
            return false;

        HashMap<String, String> timetable = (HashMap<String, String>) currentStudent.getCoursesRegistered().clone();
        timetable.putAll(currentStudent.getWaitList());
        ArrayList<Course> coursesToCheck = new ArrayList<>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            coursesToCheck.add(cdm.getCourse(entry.getKey()));

        for (Course courseToCheck : coursesToCheck) {
            oldExamTime = courseToCheck.getExamDateTime();
            if (!(oldExamTime[0] == null))
                if (newExamTime[0].isBefore(oldExamTime[1]) && newExamTime[1].isAfter(oldExamTime[0])) {
                    System.out.println("\nUnable to add " + courseSelected.getCourseCode() + "!");
                    System.out.println(courseSelected.getCourseCode() + "'s exam clashes with " +
                                       courseToCheck.getCourseCode() + "'s exam!\n");
                    return true;
            }
        }
        return false;
    }

    /** Checks for a clash of timetable. Timetable consists of courses registered and courses on waitlist.
     * @param indexToAdd The index to be checked against the student's timetable.
     * @param studentToCheck The student who is trying to add the new index.
     * @param indexToExclude Used for swapping of indexes, to exclude the index it is swapping from.
     * (e.g. Swapping from index 33333 to index 44444, index 33333 should not be included in the check)
     * @return The index in the timetable that has clashed with the new index, otherwise null if no clashes. */
    public boolean hasTimetableClash(Index indexToAdd, Student studentToCheck, Index indexToExclude) {

        /* Combine courses registered and waitlist for the student into a HashMap.
         * We then retrieve all the actual indexes the student is enrolled in and put
         * them into a ArrayList<Index> to iterate through*/
        HashMap<String, String> timetable = (HashMap<String, String>) studentToCheck.getCoursesRegistered().clone();
        timetable.putAll(studentToCheck.getWaitList());
        ArrayList<Index> indexesToCheck = new ArrayList<>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            indexesToCheck.add(cdm.getCourse(entry.getKey()).getIndex(entry.getValue()));

        ArrayList<Lesson> newLessons = indexToAdd.getLessons();
        ArrayList<Lesson> oldLessons = new ArrayList<>();

        /* For all currently registered indexes, retrieve their lessons and then compare with lessons of
         *  the new index. Comparison only done if the lessons fall on the same day and the index is not
         *  the index to be excluded. */
        for (Index indexToCheck : indexesToCheck) {
            if (!indexToCheck.equals(indexToExclude)) {
                oldLessons.addAll(indexToCheck.getLessons());
                for (Lesson oldLesson : oldLessons)
                    for (Lesson newLesson : newLessons) {
                        if ((newLesson.getDay().equals(oldLesson.getDay()))) {
                            /* Start of new lesson < End of old lesson && End of new lesson > Start of old lesson */
                            ArrayList<Integer> oldLessonWeeks = (ArrayList<Integer>) oldLesson.getTeachingWeeks().clone();
                            oldLessonWeeks.retainAll(newLesson.getTeachingWeeks());
                            if (newLesson.getStartTime().isBefore(oldLesson.getEndTime()) &&
                                    newLesson.getEndTime().isAfter(oldLesson.getStartTime()) &&
                                        !oldLessonWeeks.isEmpty()) {
                                System.out.println("There is a clash with Index " + indexToCheck.getIndexNum() + "!");
                                System.out.println("Please choose another index!\n");
                                return true;
                            }
                        }
                    }
            }
        }
        return false;
    }
    
    //TODO: Shenrui double check javadocs for this point on pls
    /**
     * Retrieves the Student object of the other student for swapCourse(). <br>
     * Swapping of index with another student requires the other student to enter his/her login credentials
     * to retrieve his/her Student object to perform the actual swap.<br>
     * Calls MyStars.login to perform the login.
     * @param sc Scanner object to pass to MyStars.login
     * @return Student object of the student to perform swap with
     */
    public Student retrieveOtherStudent(Scanner sc) {
        try {
            User targetUser = MyStars.login(sc);
            while (targetUser.equals(currentStudent)) {
                System.out.println("Error! You have chosen yourself.");
                targetUser = MyStars.login(sc);
            }
            return sdm.getStudent(((Student) targetUser).getMatricNum());
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    /**
     * Sends a confirmation email to both students doing a swap if the swap has been performed successfully.
     * @param currentStudent the Student object of the student doing the swap
     * @param otherStudent the Student object of the other student doing the same swap
     * @param courseSelected the Course object of the course both students are doing a swap of index for
     * @param oldIndex the Index object of the currentStudent's current enrolled index (index currentStudent is swapping out)
     * @param newIndex the Index object of the otherStudent's current enrolled index (index currentStudent is swapping for)
     */
    public void emailStudent(Student currentStudent, Student otherStudent,  Course courseSelected, Index oldIndex, Index newIndex){
        MailHandler.sendMail(currentStudent.getEmail(),
                  otherStudent.getName() + " has swapped indexes with you for " + courseSelected.getCourseCode() +
                             " " + courseSelected.getCourseName() + ". Your index " + oldIndex.getIndexNum() +
                             " has been updated to " + newIndex.getIndexNum() + ".",
                      "Successful Swap of Index for " + courseSelected.getCourseCode() + ", "
                             + courseSelected.getCourseName());
    }

    /**
     * Checks if a student is registered in a course
     * @param student the Student object of the student to check
     * @param courseSelected the Course object of the course student is potentially registered for
     * @return true if student is enrolled in courseSelected
     */
    public boolean checkIfRegistered(Student student, Course courseSelected){
        if (courseSelected == null) return false;
        return student.retrieveIndexFromRegistered(courseSelected.getCourseCode()) != null;
    }

    /**
     * Checks if a student is waitlisted in a course
     * @param student the Student object of the student to check
     * @param courseSelected the Course object of the course student is potentially waitlisted for
     * @return true if student is waitlisted in courseSelected
     */
    public boolean checkIfWaitListed(Student student, Course courseSelected){
        if (courseSelected == null) return false;
        return student.retrieveIndexFromWaitList(courseSelected.getCourseCode()) != null;
    }

    /**
     * Gets all courses registered by currentStudent
     * @return a String representing all courses registered by currentStudent
     */
    public String getRegisteredCourses() {
        Course course;
        Index index;
        HashMap<String, String> coursesRegistered = currentStudent.getCoursesRegistered();
        HashMap<String, String> coursesWaitListed = currentStudent.getWaitList();
        StringBuilder stringBuilder = new StringBuilder();

        if (coursesRegistered.isEmpty())
            stringBuilder.append("\nNo registered courses currently.\n");
        else {
            stringBuilder.append("\nCourse | Index | AUs |   Status   | Course Type\n");
            stringBuilder.append("-------------------------------------------------\n");
            for (Map.Entry<String, String> pair : coursesRegistered.entrySet()) {
                course = cdm.getCourse(pair.getKey());
                index = cdm.getCourse(pair.getKey()).getIndex(pair.getValue());
                stringBuilder.append(course.getCourseCode() + " | " + index.getIndexNum() + " |  " + course.getAcademicUnits()
                        + "  | REGISTERED |  " + course.getCourseType() + "\n");
            }
        }

        if(!coursesWaitListed.isEmpty()) {
            for (Map.Entry<String, String> pair2 : coursesWaitListed.entrySet()) {
                course = cdm.getCourse(pair2.getKey());
                index = cdm.getCourse(pair2.getKey()).getIndex(pair2.getValue());
                stringBuilder.append(course.getCourseCode() + " | " + index.getIndexNum() + " |  " + course.getAcademicUnits()
                        + "  | WAITLISTED |  " + course.getCourseType() + "\n");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Gets an overview of all courses in the system
     * @param choice int to pass to cdm.generateCourseOverview
     * @return a String of all courses in the system
     * @see CourseDataManager#generateCourseOverview(int) 
     */
    public String getCourseOverview(int choice){
        return cdm.generateCourseOverview(choice);
    }

    /**
     * Saves any changed data back to file
     * @see StudentDataManager#save()
     * @see CourseDataManager#save()
     */
    public void close() {
        sdm.save();
        cdm.save();
    }

}
