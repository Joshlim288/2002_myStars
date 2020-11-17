import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentHandler {
    Student currentStudent;
    StudentDataManager sdm;
    CourseDataManager cdm;

    public StudentHandler(String matricNum) {
        this.sdm = new StudentDataManager();
        this.cdm = new CourseDataManager();
        sdm.load();
        cdm.load();
        this.currentStudent = sdm.getStudent(matricNum);
    }

    public Index retrieveIndex(Course courseSelected, String indexNum){
        Index indexSelected = courseSelected.getIndex(indexNum);

        if (indexSelected == null)
            System.out.println("Index does not exist in this course!\n" +
                               "Please re-enter index again.");
        return indexSelected;
    }

    public boolean checkValidIndex(Index indexSelected, Student studentToCheck, Index indexToExclude) {
        if (indexSelected == null) return false;
        if(hasTimetableClash(indexSelected, studentToCheck, indexToExclude))
            return false;
        return true;
    }

    public Course retrieveCourse(String courseCode){
        Course courseSelected = cdm.getCourse(courseCode);
        if (courseSelected == null)
            System.out.println("Course does not exist in the database!\n" +
                    "Please re-enter course again.\n");
        return courseSelected;
    }

    public boolean checkValidCourse(Course courseSelected){
        if (courseSelected == null) return false;
        if (willGoOverMaxAU(courseSelected))
            System.out.println("Cannot register for course, will exceed maximum AUs!\n");
        else return true;
        return false;
    }

    public boolean willGoOverMaxAU(Course courseSelected) {
        int totalAUs = currentStudent.getCurrentAUs();
        for (Map.Entry<String, String> entry : currentStudent.getWaitList().entrySet())
            totalAUs += cdm.getCourse(entry.getKey()).getAcademicUnits();
        return (totalAUs + courseSelected.getAcademicUnits()) > currentStudent.getMaxAUs();
    }

    public boolean checkIfRegistered(Student student, Course courseSelected){
        if (courseSelected == null) return false;
        return student.retrieveIndex(courseSelected.getCourseCode()) != null;
    }

    public Index getIndexRegistered(Student student, Course courseSelected){
        return retrieveIndex(courseSelected, student.retrieveIndex(courseSelected.getCourseCode()));
    }

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

    public boolean hasExamClash(Course courseSelected){

        LocalDateTime[] oldExamTime;
        LocalDateTime[] newExamTime = cdm.getCourse(courseSelected.getCourseCode()).getExamDateTime();
        if (newExamTime[0] == null)
            return false;

        HashMap<String, String> timetable = currentStudent.getCoursesRegistered();
        timetable.putAll(currentStudent.getWaitList());
        ArrayList<Course> coursesToCheck = new ArrayList<Course>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            coursesToCheck.add(cdm.getCourse(entry.getKey()));


        for (Course courseToCheck : coursesToCheck) {
            oldExamTime = courseToCheck.getExamDateTime();
            if (!(oldExamTime[0] == null))
            if (newExamTime[0].isBefore(oldExamTime[1]) && newExamTime[1].isAfter(oldExamTime[0])) {
                System.out.println("\nUnable to add " + courseSelected.getCourseCode() + "!");
                System.out.println(courseSelected.getCourseCode() + "'s exam clashes with " + courseToCheck.getCourseCode() + "'s exam!\n");
                return true;
            }
        }
        return false;
    }

    //TODO: BROKEN AGAIN
    /**
     * Version 4 of hasTimetableClash() -.-
     * Checks for a clash of timetable. Timetable consists of courses registered and courses on waitlist.
     * @param indexToAdd The index to be checked against the student's timetable.
     * @param studentToCheck The student who is trying to add the new index.
     * @param indexToExclude Used for swapping of indexes, to exclude the index it is swapping from.
     * (e.g. Swapping from index 33333 to index 44444, index 33333 should not be included in the check)
     * @return The index in the timetable that has clashed with the new index, otherwise null if no clashes. */
    public boolean hasTimetableClash(Index indexToAdd, Student studentToCheck, Index indexToExclude) {

        /* Combine courses registered and waitlist for the student into a HashMap.
         * We then retrieve all the actual indexes the student is enrolled in and put
         * them into a ArrayList<Index> to iterate through*/
        HashMap<String, String> timetable = studentToCheck.getCoursesRegistered();
        timetable.putAll(studentToCheck.getWaitList());
        ArrayList<Index> indexesToCheck = new ArrayList<Index>();
        for(Map.Entry<String, String> entry : timetable.entrySet())
            indexesToCheck.add(cdm.getCourse(entry.getKey()).getIndex(entry.getValue()));

        ArrayList<Lesson> newLessons = indexToAdd.getLessons();
        ArrayList<Lesson> oldLessons = new ArrayList<Lesson>();

        /* For all currently registered indexes, retrieve their lessons and then compare with lessons of
         *  the new index. Comparison only done if the lessons fall on the same day and the index is not
         *  the index to be excluded. */
        for (Index indexToCheck : indexesToCheck) {
            if (!indexToCheck.equals(indexToExclude)) {
                oldLessons.addAll(indexToCheck.getLessons());
                for (Lesson oldLesson : oldLessons)
                    for (Lesson newLesson : newLessons) {
                        ArrayList<Integer> oldLessonWeeks = (ArrayList<Integer>) oldLesson.getTeachingWeeks().clone();
                        oldLessonWeeks.retainAll(newLesson.getTeachingWeeks());
                        //boolean noCommonWeek = (oldLesson.getTeachingWeeks().retainAll(newLesson.getTeachingWeeks())).isEmpty();
                        if ((newLesson.getDay().equals(oldLesson.getDay())) && oldLessonWeeks.isEmpty())
                            /* Start of new lesson < End of old lesson && End of new lesson > Start of old lesson */
                            if (newLesson.getStartTime().isBefore(oldLesson.getEndTime()) &&
                                    newLesson.getEndTime().isAfter(oldLesson.getStartTime())) {
                                System.out.println("There is a clash with Index " + indexToCheck.getIndexNum() + "!");
                                System.out.println("Please choose another index!");
                                return true;
                            }
                    }
                }
        }
        return false;
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
    public int addCourse(Student student, Course course, Index indexToAdd, Index indexToDrop, boolean checkVacancy) {

        if (!checkVacancy || !indexToAdd.isAtMaxCapacity()) {
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum());
            indexToAdd.addToEnrolledStudents(student.getMatricNum());
            student.addCourse(course.getCourseCode(), indexToAdd.getIndexNum(), course.getAcademicUnits());
            return 1;
        }
        else{
            if (indexToDrop != null) dropCourse(student, course, indexToDrop.getIndexNum());
            indexToAdd.addToWaitlist(student.getMatricNum());
            student.addCourseToWaitList(course.getCourseCode(), indexToAdd.getIndexNum());
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
            index.addToEnrolledStudents(studentRemoved.getMatricNum());

            //Uses MailHandler utility class to send an email to student removed from wait-list
            MailHandler.sendMail(studentRemoved.getEmail(),
                    "You have been removed from the wait-list and added to the course!",
                    "Successful Registration for " + course.getCourseName());
        }
    }

    public Student retrieveOtherStudent(Scanner sc) {
        try {
            User targetUser = MyStars.login(sc);
            while (targetUser.equals(currentStudent)) {
                System.out.println("Error! You have chosen yourself.");
                targetUser = MyStars.login(sc);
            }
            Student otherStudent = sdm.getStudent(((Student) targetUser).getMatricNum());
            return otherStudent;
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Send email to other student if a swap has been performed successfully
    public void emailStudent(Student currentStudent, Student otherStudent,  Course courseSelected, Index oldIndex, Index newIndex){
        MailHandler.sendMail(currentStudent.getEmail(),
                  otherStudent.getName() + " has swapped indexes with you for " + courseSelected.getCourseCode() +
                             " " + courseSelected.getCourseName() + ". Your index " + oldIndex.getIndexNum() +
                             " has been updated to " + newIndex.getIndexNum() + ".",
                      "Successful Swap of Index for " + courseSelected.getCourseCode() + ", "
                             + courseSelected.getCourseName());
    }

    public String getCourseOverview(int choice){
        return cdm.generateCourseOverview(choice);
    }

    public void close() {
        sdm.save();
        cdm.save();
    }

}
