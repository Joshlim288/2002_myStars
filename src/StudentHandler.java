import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Control class that handles the logic for functions available to Students.<p>
 * Operates on and uses the entity classes using DataManagers
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.15
 * @since 1.1
 */
public class StudentHandler {
    protected final Student currentStudent;
    private final StudentDataManager sdm;
    private final CourseDataManager cdm;

    /**
     * Constructor for the handler.<p>
     * Initializes the DataManagers and calls their <code>load</code> methods to load in the required data from the data folder
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

    /** Adds the selected index to the student's list of registered courses.<p>
     * Student can be added to index's waitlist or enrolled students list depending on vacancy.<p>
     * Updates student's registered courses and updates index's list of enrolled students/waitlist.<p>
     * Also drops any prior index if required before adding new index.
     * @param student the student whose timetable to add the index to
     * @param course the course of the index to be added
     * @param indexToAdd the index to be added
     * @param indexToDrop The index to be dropped, if no index to be dropped, use <code>null</code>. Useful for swapping/changing indexes.
     * (e.g. Swapping from index 33333 to index 44444 requires a drop from index 33333 first)
     * @param checkVacancy Passed in as false only for swapIndex() in StudentInterface,
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

    /** Drops the selected index from the student's list of registered courses/waitlisted courses.<p>
     *  Updates student's registered/waitlisted courses and updates index's list of enrolled students.
     * @param student the student whose timetable to drop the index from
     * @param course the course of the index to be dropped
     * @param index the index to be dropped
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

    /** Triggers update of waitlist for selected index and is only used after a student drops or changes a index.<p>
     * If waitlist is not empty, remove student at head of waitlist and register him for the course.<p>
     * Email will be sent to student's email informing the successful registration of course from waitlist.
     * @param course the course of the index for updating of waitlist
     * @param index the index that contains the waitlist to be updated
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
     * Retrieves course based on input from user with <code>CourseDataManager</code>.<p>
     * If course does not exist in database, user is prompted to enter a valid course.
     * @param courseCode course input from user to search database with
     * @return <code>Course</code> if it exists in database, otherwise <code>null</code> is returned
     * @see CourseDataManager#getCourse(String)
     */
    public Course retrieveCourse(String courseCode){
        Course courseSelected = cdm.getCourse(courseCode);
        if (courseSelected == null)
            System.out.println("Course does not exist in the database!\n" +
                    "Please re-enter course again.\n");
        return courseSelected;
    }

    /** Checks if course selected exists in database and is used after calling {@link #retrieveCourse(String)}.
     * @param courseSelected <code>Course</code> selected to check.
     * @return <code>true</code> if course selected exists in database, <code>false</code> if it does not.
     */
    public boolean checkValidCourse(Course courseSelected){
        return courseSelected != null;
    }

    /** Retrieves index based on input from user and course selected.<p>
     * If index does not exist in the selected course, user is prompted to enter a valid index.
     * @param courseSelected <code>Course</code> selected to check
     * @param indexNum <code>Index</code> selected to check
     * @return <code>Index</code> if it exists in the course, otherwise <code>null</code> is returned
     */
    public Index retrieveIndex(Course courseSelected, String indexNum){
        Index indexSelected = courseSelected.getIndex(indexNum);

        if (indexSelected == null)
            System.out.println("Index does not exist in this course!\n" +
                               "Please re-enter index again.\n");
        return indexSelected;
    }

    /** Checks if index selected is in course previously selected and is used after calling {@link #retrieveIndex(Course, String)}.<p>
     * Also checks if index will clash with current timetable of student through {@link #hasTimetableClash(Index, Student, Index)}.
     * @param indexSelected <code>Index</code> selected to check
     * @param studentToCheck <code>Student</code> to retrieve timetable and check for clashes
     * @param indexToExclude <code>Index</code> to exclude from checking for clashes with timetable, useful for changing/swapping index
     * (e.g. For changing from index 33333 to index 44444, index 33333 should not be included in the check for clash)
     * @return <code>true</code> if index selected is available to be added, otherwise <code>false</code> is returned.
     */
    public boolean checkValidIndex(Index indexSelected, Student studentToCheck, Index indexToExclude) {
        if (indexSelected == null) return false;
        return !hasTimetableClash(indexSelected, studentToCheck, indexToExclude);
    }

    /** Retrieves index that the student is registered in based on the course chosen.<p>
     * Uses <code>retrieveIndex</code> but indexNum passed in is retrieved from student's list of registered indexes instead.
     * @param student <code>Student</code> to get index registered in.
     * @param courseSelected <code>Course</code> to get index registered.
     * @return <code>Index</code> that the student is registered in.
     * @see #retrieveIndex(Course, String)
     */
    public Index getIndexRegistered(Student student, Course courseSelected){
        return retrieveIndex(courseSelected, student.retrieveIndexFromRegistered(courseSelected.getCourseCode()));
    }

    /** Checks if the student will go over maximum AUs after adding selected course.<p>
     * Adds AUs of courses registered and courses waitlisted with the course to be added to compare with max AUs allowed.
     * @param courseSelected The <code>Course</code> to be added by the student.
     * @return <code>true</code> if student will go over max AUs allowed, otherwise <code>false</code>.
     */
    public boolean willGoOverMaxAU(Course courseSelected) {
        int totalAUs = currentStudent.getCurrentAUs();
        for (Map.Entry<String, String> entry : currentStudent.getWaitList().entrySet())
            totalAUs += cdm.getCourse(entry.getKey()).getAcademicUnits();
        return (totalAUs + courseSelected.getAcademicUnits()) > currentStudent.getMaxAUs();
    }

    /** Checks if there is a clash between any two course's exam schedule if a new course is added to the student's timetable.<p>
     * A student's timetable consists of both registered and wait-listed courses.
     * @param courseSelected The <code>Course</code> to be checked against the rest of the courses in the timetable.
     * @return <code>true</code> if there exists a clash in exam schedule, <code>false</code> otherwise. */
    public boolean hasExamClash(Course courseSelected){

        /* If the new course has no final examinations,a clash will never occur,
        and false can be immediately returned */
        LocalDateTime[] oldExamTime;
        LocalDateTime[] newExamTime = cdm.getCourse(courseSelected.getCourseCode()).getExamDateTime();
        if (newExamTime[0] == null)
            return false;

        /* Combine courses registered and waitlist for the student into a new HashMap.
         * We then retrieve all the actual courses the student is enrolled in and put
         * them into a ArrayList<Course> to iterate through*/
        HashMap<String, String> timetable = (HashMap<String, String>) currentStudent.getCoursesRegistered().clone();
        timetable.putAll(currentStudent.getWaitList());
        ArrayList<Course> coursesToCheck = new ArrayList<>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            coursesToCheck.add(cdm.getCourse(entry.getKey()));

        /* If any of the old courses has no final examinations, it will not be included in the
        check as it will never clash and also prevent the null timings from throwing a NullPointerException. */
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

    /** Checks if there is a clash between any two lessons in a timetable if a new index is added.<p>
     * A student's timetable consists of both registered and wait-listed courses.
     * @param indexToAdd The index to be checked against the student's timetable.
     * @param studentToCheck The student who is trying to add the new index.
     * @param indexToExclude Used for swapping of indexes, to exclude the index it is swapping from in the checking of clash.
     * (e.g. Swapping from index 33333 to index 44444, index 33333 should not be included in the check)
     * @return The index in the timetable that has clashed with the new index, otherwise null if no clashes. */
    public boolean hasTimetableClash(Index indexToAdd, Student studentToCheck, Index indexToExclude) {

        /* Combine courses registered and waitlist for the student into a new HashMap.
         * We then retrieve all the actual indexes the student is enrolled in and put
         * them into a ArrayList<Index> to iterate through*/
        HashMap<String, String> timetable = (HashMap<String, String>) studentToCheck.getCoursesRegistered().clone();
        timetable.putAll(studentToCheck.getWaitList());
        ArrayList<Index> indexesToCheck = new ArrayList<>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            indexesToCheck.add(cdm.getCourse(entry.getKey()).getIndex(entry.getValue()));

        ArrayList<Lesson> newLessons = indexToAdd.getLessons();
        ArrayList<Lesson> oldLessons = new ArrayList<>();

        /* For all currently registered indexes, retrieve their lessons and then compare their start/end times
         with lessons of the new index. Comparison only done if the lessons fall on the same day, there exists
         a clash in teaching weeks and the index is not the index to be excluded. */
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
                                ArrayList<Course> courseList = cdm.getCourseList();
                                for (Course course: courseList)
                                    if (course.getIndex(indexToCheck.getIndexNum()) != null) {
                                        System.out.println("There is a clash with " + course.getCourseCode() +
                                                           ", Index " + indexToCheck.getIndexNum() + "!");
                                        System.out.println("Please choose another index!\n");
                                    }
                                return true;
                            }
                        }
                    }
            }
        }
        return false;
    }

    /**
     * Retrieves the Student object of the other student for swapCourse() in StudentInterface. <br>
     * Swapping of index with another student requires the other student to enter his/her login credentials
     * for validation and retrieving of his/her Student object to perform the actual swap.<br>
     * Calls {@link MyStars#login(Scanner)} to perform the login.
     * @param sc Scanner object to pass to MyStars.login
     * @return Student object to perform the indexswap with. If no valid Student found, <code>null</code> is returned.
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
     * Sends a confirmation email to a student when a swap of an index has been done successfully.<p>
     * Will be called twice, one for the student performing the swap and one for the other student.
     * @param currentStudent the Student object of the student to send the email to.
     * @param otherStudent the Student object of the other student in the swap.
     * @param courseSelected the Course object of the course both students are doing a swap of index for
     * @param oldIndex the Index object of the current student's initial enrolled index
     * @param newIndex the Index object of the current student's updated enrolled index
     */
    public void emailStudent(Student currentStudent, Student otherStudent, Course courseSelected, Index oldIndex, Index newIndex){
        MailHandler.sendMail(currentStudent.getEmail(),
                  otherStudent.getName() + " has swapped indexes with you for " + courseSelected.getCourseCode() +
                             " " + courseSelected.getCourseName() + ". Your index " + oldIndex.getIndexNum() +
                             " has been updated to " + newIndex.getIndexNum() + ".",
                      "Successful Swap of Index for " + courseSelected.getCourseCode() + ", "
                             + courseSelected.getCourseName());
    }

    /**
     * Checks if a student is currently registered in a course
     * @param student the Student object of the student to check
     * @param courseSelected the Course object of the course student is potentially registered for
     * @return true if student is enrolled in courseSelected and false otherwise
     */
    public boolean checkIfRegistered(Student student, Course courseSelected){
        if (courseSelected == null) return false;
        return student.retrieveIndexFromRegistered(courseSelected.getCourseCode()) != null;
    }

    /**
     * Checks if a student is currently wait-listed in a course
     * @param student the Student object of the student to check
     * @param courseSelected the Course object of the course student is potentially wait-listed for
     * @return true if student is wait-listed in courseSelected and false otherwise
     */
    public boolean checkIfWaitListed(Student student, Course courseSelected){
        if (courseSelected == null) return false;
        return student.retrieveIndexFromWaitList(courseSelected.getCourseCode()) != null;
    }

    /**
     * Formats output for all registered and waitlisted courses by currentStudent using {@link StringBuilder}.
     * @return  String representing all courses registered by currentStudent. If no registered
     * or wait-listed courses, String stating that no such courses exist is also returned.
     */
    public String getRegisteredCourses() {
        Course course;
        Index index;
        HashMap<String, String> coursesRegistered = currentStudent.getCoursesRegistered();
        HashMap<String, String> coursesWaitListed = currentStudent.getWaitList();
        StringBuilder stringBuilder = new StringBuilder();

        if (coursesRegistered.isEmpty() && coursesWaitListed.isEmpty())
            stringBuilder.append("No registered or wait-listed courses currently.\n");
        else {
            stringBuilder.append("-------------------------------------------------\n");
            stringBuilder.append("Course | Index | AUs |   Status   | Course Type\n");
            stringBuilder.append("-------------------------------------------------\n");
            for (Map.Entry<String, String> pair : coursesRegistered.entrySet()) {
                course = cdm.getCourse(pair.getKey());
                index = cdm.getCourse(pair.getKey()).getIndex(pair.getValue());
                stringBuilder.append(course.getCourseCode() + " | " + index.getIndexNum() + " |  " + course.getAcademicUnits()
                        + "  | REGISTERED | " + course.getCourseType() + "\n");
            }
        }

        if(!coursesWaitListed.isEmpty()) {
            for (Map.Entry<String, String> pair2 : coursesWaitListed.entrySet()) {
                course = cdm.getCourse(pair2.getKey());
                index = cdm.getCourse(pair2.getKey()).getIndex(pair2.getValue());
                stringBuilder.append(course.getCourseCode() + " | " + index.getIndexNum() + " |  " + course.getAcademicUnits()
                        + "  | WAITLISTED | " + course.getCourseType() + "\n");
            }
        }
        stringBuilder.append("-------------------------------------------------\n");
        return stringBuilder.toString();
    }

    /**
     * Gets an overview of all courses currently in the system.
     * @param choice int to be passed to cdm.generateCourseOverview
     * @return a String with the required information of all courses in the system
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
