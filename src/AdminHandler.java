import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Control class that handles the logic for functions available to Admins
 * Operates on and uses the entity classes using DataManagers
 * @author Josh, Joshua, Jun Wei, Shen Rui, Daryl
 * @version 1.0
 * @since 2020-10-24
 */
public class AdminHandler{
    private final CourseDataManager cdm;
    private final StudentDataManager sdm;
    private final UserDataManager udm;

    /**
     * Constructor for the handler
     * Initializes the DataManagers and calls their load() methods to load in the required data from the data folder
     */
    public AdminHandler() {
        this.cdm = new CourseDataManager();
        this.sdm = new StudentDataManager();
        this.udm = new UserDataManager();
        cdm.load();
        sdm.load();
        udm.load();
    }

    /**
     * Retrieves a list of all Courses in the database
     * @return ArrayList of Course objects
     */
    public ArrayList<Course> getCourses(){
        return cdm.getCourseList();
    }

    /**
     * Retrieves a list of all Indexes under a particular Course in the database
     * @param courseCode The course code to retrieve the Indexes for
     * @return ArrayList of Index objects
     */
    public ArrayList<Index> getIndexes(String courseCode) {
        return cdm.getCourse(courseCode).getIndexes();
    }

    /**
     * Retrieves a list of all Lessons under a particular Index in the database
     * @param courseCode the course code of the Course that the Index is associated with
     * @param indexNum The Index number to retrieve the Lessons for
     * @return ArrayList of Lesson objects
     */
    public ArrayList<Lesson> getLessons(String courseCode, String indexNum){
        return cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
    }

    /**
     * Retrieves a list of all Students in the database
     * @return ArrayList of Student objects
     */
    public ArrayList<Student> getStudents(){
        return sdm.getStudentList();
    }

    /**
     * Prints out the vacancy information for the specified Index
     * @param indexNum indexNum of Index to examine
     */
    public void printIndexVacancy(String indexNum){
        Index tempIndex = null;
        for (Course crs: cdm.getCourseList()) {
            tempIndex = crs.getIndex(indexNum);
            if(tempIndex!=null)
                break;
        }
        System.out.println("The vacancy for index " + indexNum + " is: " +
                tempIndex.getCurrentVacancy() + "/" + tempIndex.getIndexVacancy());
    }

    /**
     * Checks if an Index object with the given indexNum exists
     * @param indexNum Index number to check
     * @return true if a matching Index exists, false if not
     */
    public boolean checkIndexExists(String indexNum) {
        for (Course crs: cdm.getCourseList()){
            for (Index idx: crs.getIndexes()){
                if (idx.getIndexNum().equals(indexNum)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if an Course object with the given courseCode exists
     * @param courseCode Course code to check
     * @return true if a matching Course exists, false if not
     */
    public boolean checkCourseExists(String courseCode) {
        return cdm.getCourse(courseCode) != null;
    }

    /**
     * Checks if a Student object with the given matriculation number exists
     * @param matric Matriculation number of Student to check
     * @return true if a matching Student exists, false if not
     */
    public boolean checkStudentExists(String matric){
        return sdm.getStudent(matric) != null;
    }

    /**
     * Checks if the course corresponding to the given course code currently has Students enrolled
     * @param courseCode course code of Course to check
     * @return true if Students are currently enrolled in the course, false if not
     */
    public boolean checkCourseOccupied(String courseCode){
        for (Index idx: cdm.getCourse(courseCode).getIndexes()){
            if (idx.getCurrentVacancy() != idx.getIndexVacancy()){
                return true;
            }
        }
        return false;
    }

    /**
     * Edits a Course object's attributes based on the given parameters
     * Some parameters cannot be edited if there are Students already enrolled in the course
     * This is to ensure Students are not unfairly removed from courses that they have planned their timetables around
     * @param courseCode course code of the Course object to be edited
     * @param input user's input for new value of desired attribute
     * @param choice indicates which attribute is to be edited, based on the menu in AdminInterface
     * @return true if the edit was successful, false otherwise
     */
    public boolean editCourse(String courseCode, String input, int choice){
        System.out.println("Editing Course...");
        Course tempCourse = cdm.getCourse(courseCode);
        switch(choice){
            case(1)->{ // edit courseCode
                if (checkCourseExists(input)) {
                    System.out.println("Course with this code already exists");
                    return false;
                }
                tempCourse.setCourseCode(input);
                HashMap<String, String> coursesRegistered, coursesWaitlisted;
                String holdIndex;
                // Update the courseCode for all Students enrolled and waitlisted in the course
                for (Student stud: sdm.getStudentList()){
                    coursesRegistered = stud.getCoursesRegistered();
                    coursesWaitlisted = stud.getWaitList();
                    // Replace registered course code
                    if (coursesRegistered.containsKey(courseCode)){
                        // keys cannot be directly replaced, we need a temp variable to hold the Index they are enrolled in
                        holdIndex = coursesRegistered.get(courseCode);
                        coursesRegistered.remove(courseCode);
                        coursesRegistered.put(input, holdIndex);
                    }
                    // Replace wait listed course code
                    if (coursesWaitlisted.containsKey(courseCode)){
                        holdIndex = coursesWaitlisted.get(courseCode);
                        coursesWaitlisted.remove(courseCode);
                        coursesWaitlisted.put(input, holdIndex);
                    }
                }
            }
            case(2)-> tempCourse.setCourseName(input); // edit course name
            case(3)-> tempCourse.setCourseType(input); // edit course type
            case(4)-> tempCourse.setAcademicUnits(Integer.parseInt(input)); // edit Academic Units
            case(5)-> tempCourse.setSchool(input); // edit school
            case(8)-> {
                if (tempCourse.getIndex(input) == null){
                    System.out.println("Index number not found");
                    return false;
                }
                removeIndex(courseCode, input);
            }
            case(9)-> { // edit exam datetime
                String newStart = input.split("&")[0];
                String newEnd = input.split("&")[1];
                tempCourse.setExamDateTime(newStart, newEnd);
            }
        }
        return true;
    }

    /**
     * Edits an Index object's attributes based on the given parameters
     * @param courseCode Course code of the Course that contains the Index to be edited
     * @param indexNum Index number of the Index to be edited
     * @param input user's input for new value of desired attribute
     * @param choice indicates which attribute is to be edited, based on the menu in AdminInterface
     * @return true if the edit was successful, false otherwise
     */
    public boolean editIndex(String courseCode, String indexNum, String input, int choice){
        System.out.println("Editing Index...");
        Index tempIndex = cdm.getCourse(courseCode).getIndex(indexNum);

        switch(choice){
            case(1)-> {
                if (checkIndexExists(input)){
                    System.out.println("Index with this index number already exists");
                    return false;
                }
                tempIndex.setIndexNum(input);
                HashMap<String, String> coursesRegistered, coursesWaitlisted;
                // Update the Index number for all Students enrolled and waitlisted in the index
                for (Student stud: sdm.getStudentList()){
                    coursesRegistered = stud.getCoursesRegistered();
                    coursesWaitlisted = stud.getWaitList();
                    if (coursesRegistered.containsValue(indexNum)){
                        coursesRegistered.put(courseCode, input);
                    }
                    if(coursesWaitlisted.containsValue(indexNum)){
                        coursesWaitlisted.put(courseCode, input);
                    }
                }
            }
            case(2)-> {
                int newVacancy = Integer.parseInt(input);
                // Do not allow if the new vacancy is < number of students enrolled
                if (newVacancy < tempIndex.getIndexVacancy()-tempIndex.getCurrentVacancy()) {
                    return false;
                }
                // set new indexVacancy
                tempIndex.setIndexVacancy(newVacancy);
                updateWaitlist(courseCode, indexNum);
            }
            case(3)->{
                // check if there exists an index within the course with the same group
                for (Index idx: cdm.getCourse(courseCode).getIndexes()){
                    if (idx.getGroup().equals(input)){
                        return false;
                    }
                }
                tempIndex.setGroup(input);
            }
            case(6)->{
                if (tempIndex.getLessons().size() < Integer.parseInt(input)-1){
                    System.out.println("Index out of range");
                    return false;
                }
                removeLesson(courseCode, indexNum, Integer.parseInt(input)-1);
            }
        }
        return true;
    }

    /**
     * Edits a Lesson object's attributes based on the given parameters
     * @param courseCode Course code of the Course that contains the relevant Index
     * @param indexNum Index number of the Index that contains the Lesson to be edited
     * @param lessonIndex index of the lesson to be removed from the ArrayList of lessons
     * @param input user's input for new value of desired attribute
     * @param choice indicates which attribute is to be edited, based on the menu in AdminInterface
     * @return true if the edit was successful, false otherwise
     */
    public boolean editLesson(String courseCode, String indexNum, int lessonIndex, String input, int choice) {
        System.out.println("Editing Lesson...");
        ArrayList<Lesson> allIndexLesson = cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
        Lesson tempLesson = allIndexLesson.get(lessonIndex);

        switch(choice){
            case(1)-> tempLesson.setLessonType(input); // edit lesson type
            case(2)-> { // edit lesson day
                allIndexLesson.remove(lessonIndex); // Remove "old" lesson before checking clash
                if (checkClash(allIndexLesson, input, new LocalTime[]{tempLesson.getStartTime(), tempLesson.getEndTime()})) {
                    allIndexLesson.add(lessonIndex, tempLesson); // add it back once we check clash
                    return false;
                }
                allIndexLesson.add(lessonIndex, tempLesson);
                tempLesson.setDay(input);
            }
            case(3)->{ // edit lesson time
                LocalTime startTime = LocalTime.parse(input.split("&")[0]);
                LocalTime endTime = LocalTime.parse(input.split("&")[1]);
                allIndexLesson.remove(lessonIndex); // Remove "old" lesson before checking clash
                if (checkClash(allIndexLesson, tempLesson.getDay().toString(),
                        new LocalTime[]{startTime, endTime})) {
                    allIndexLesson.add(lessonIndex, tempLesson); // add it back once we check clash
                    return false;
                }
                allIndexLesson.add(lessonIndex, tempLesson);
                tempLesson.setStartTime(startTime);
                tempLesson.setEndTime(endTime);
            }
            case(4)->tempLesson.setVenue(input); // edit lesson venue
            case(5)->{
                String[] inputWeeks = input.split(",");
                ArrayList<Integer> teachingWeeks = new ArrayList<>();
                for (String week : inputWeeks)
                    teachingWeeks.add(Integer.parseInt(week));
                tempLesson.setTeachingWeeks(teachingWeeks);
            }
            case(6)-> {
                return true; // since lesson was temporarily removed, we return early so we don't add back the lesson
            }
        }
        return true;
    }

    /**
     * Checks if a lesson period clashes with any existing lessons
     * @param allIndexLessons ArrayList of Lesson objects to check against
     * @param day day of the Lesson to be checked
     * @param timeArray [Start time, End time] for the Lesson to be checked
     * @return true if lessons clash, false if not
     */
    private boolean checkClash(ArrayList<Lesson> allIndexLessons, String day, LocalTime[] timeArray) {
        for (Lesson check : allIndexLessons) {
            if (check.getDay().toString().equals(day)) {
                // Start < End && End > Start will clash
                if (timeArray[0].isBefore(check.getEndTime()) && timeArray[1].isAfter(check.getStartTime())){
                        System.out.println("Lesson clashes");
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Edits a Student based on the given parameters
     * @param matricNum Matriculation number of student to be edited
     * @param updatedValue user's input for new value of desired attribute
     * @param choice indicates which attribute is to be edited, based on the menu in AdminInterface
     * @return true if the edit was successful, false otherwise
     */
    public boolean editStudent(String matricNum, String updatedValue, int choice){
        System.out.println("Editing Student...");
        Student tempStudent = sdm.getStudent(matricNum);
        switch(choice){
            case(1)-> { // edit userID
                if (udm.getUser(updatedValue) != null) {
                    System.out.println("UserID already in use");
                    return false;
                }
                tempStudent.setUserID(updatedValue);
            }
            case(2)->tempStudent.setPassword(updatedValue); // edit password
            case(3)->tempStudent.setName(updatedValue); //edit name
            case(4)->{ // edit matriculation number
                if (sdm.getStudent(updatedValue) != null){
                    System.out.println("Matriculation Number already in use");
                    return false;
                }
                tempStudent.setMatricNum(updatedValue);
                ArrayList<String> enrolledStudents, waitlistedStudents;
                int positionInQueue;
                // update enrolled and waitlisted matric numbers
                for (Course crs: cdm.getCourseList()){
                    for (Index idx: crs.getIndexes()){
                        enrolledStudents = idx.getEnrolledStudents();
                        waitlistedStudents = idx.getWaitlist();
                        if (enrolledStudents.contains(matricNum)){
                            enrolledStudents.remove(matricNum);
                            enrolledStudents.add(updatedValue);
                        }
                        if (waitlistedStudents.contains(matricNum)){
                            // ensure queue position is maintained
                            positionInQueue = waitlistedStudents.indexOf(matricNum);
                            enrolledStudents.remove(matricNum);
                            enrolledStudents.add(positionInQueue, updatedValue);
                        }
                    }
                }
            }
            case(5)->{ // edit email
                for (User u: udm.getUserList()){
                    if (u.getEmail().equals(updatedValue)){
                        System.out.println("Email already in use");
                        return false;
                    }
                }
                tempStudent.setEmail(updatedValue);
            }
            case(6)->tempStudent.setGender(updatedValue); // edit gender
            case(7)->tempStudent.setNationality(updatedValue); // edit nationality
            case(8)->tempStudent.setMajor(updatedValue); // edit major
            case(9)->{ // edit max AUs
                if (Integer.parseInt(updatedValue) < tempStudent.getCurrentAUs()){
                    System.out.println("Student currently taking more than updated max AUs");
                }
                tempStudent.setMaxAUs(Integer.parseInt(updatedValue));
            }
            case(10)->{ // edit Access Period
                String newStart = updatedValue.split("&")[0];
                String newEnd = updatedValue.split("&")[1];
                tempStudent.setAccessTime(newStart, newEnd);
            }
        }
        System.out.println("Successfully changed");
        System.out.println(tempStudent);
        System.out.println(tempStudent.getMaxAUs());
        System.out.println(tempStudent.getUserID());
        System.out.println(tempStudent.getEmail());
        System.out.println(Arrays.toString(tempStudent.getAccessTime()));
        return true;
    }

    /**
     * Adds a new course into the CourseDataManager
     * @param courseCode Course code for the new course
     * @param courseName Name of the new course
     * @param courseType CourseType of the new course
     * @param academicUnits AUs the new course is worth
     * @param school School the new course belongs to
     * @return true on success, false otherwise
     */
    public boolean addCourse(String courseCode, String courseName,String courseType, int academicUnits, String school,
                             boolean hasExam,String examStart, String examEnd){
        if (checkCourseExists(courseCode)) {
            System.out.println("Course with this course code already exists");
            return false;
        }

        if(hasExam)
            cdm.addCourse(new Course(courseCode, courseName,courseType, academicUnits,school, examStart, examEnd));
        else
            cdm.addCourse(new Course(courseCode, courseName,courseType, academicUnits,school, null, null));
        return true;
    }

    /**
     * Adds a new Index to a Course object
     * @param courseCode Course the Index belongs to
     * @param indexNum Index number of new Index
     * @param indexVacancies Vacancies for the new index
     * @param group Group of the new Index
     * @return true on success, false otherwise
     */
    public boolean addIndex(String courseCode, String indexNum, int indexVacancies, String group) {
        if (checkIndexExists(indexNum)) {
            System.out.println("Index with this index number already exists");
            return false;
        }

        for (Index idx: cdm.getCourse(courseCode).getIndexes()){
            if (idx.getIndexNum().equals(indexNum)){
                System.out.println("You have previously created this index");
                return false;
            }
        }
        cdm.getCourse(courseCode).addIndex(indexNum, indexVacancies, group);
        return true;
    }


    /**
     * Adds a new Lesson to a n Index object
     * @param courseCode Course the Index belongs to
     * @param indexNum Index number of new Index
     * @param lessonType Type of Lesson
     * @param day Day that the Lesson is conducted on
     * @param startTime Start time of the Lesson
     * @param endTime End time of the Lesson
     * @param venue Venue that the Lesson is conducted at
     * @param teachingWeeks Teaching Weeks that the Lesson is held on
     * @return true on success, false otherwise
     */
    public boolean addLesson(String courseCode, String indexNum, String lessonType, String day,
                          String startTime, String endTime, String venue, ArrayList<Integer>teachingWeeks) {
        if (checkClash(cdm.getCourse(courseCode).getIndex(indexNum).getLessons(), day, new LocalTime[]{LocalTime.parse(startTime), LocalTime.parse(endTime)}))
            return false;

        cdm.getCourse(courseCode).getIndex(indexNum).addLesson(lessonType, day, startTime, endTime, venue, teachingWeeks);
        return true;
    }

    /**
     * Retrieves student list for the Index specified by the admin
     * @param indexNum Index number of the Index we want to get the Students of
     * @param byWaitlist true if want to list waitListed students only, false for only registered waitlist
     * @return ArrayList of Students who are enrolled in the Index
     */
    public ArrayList<Student> getStudentListByIndex(String indexNum, boolean byWaitlist){
        if (checkIndexExists(indexNum)) {
            for (Course curCourse : cdm.getCourseList()) {
                Index foundIndex = curCourse.getIndex(indexNum);
                if (foundIndex != null) {
                    ArrayList<Student> studentList = new ArrayList<>();
                    if (byWaitlist){
                        for(String matricNum: foundIndex.getWaitlist()) {
                            studentList.add(sdm.getStudent(matricNum));
                        }
                    } else {
                        for (String matricNum : foundIndex.getEnrolledStudents()) {
                            studentList.add(sdm.getStudent(matricNum));
                        }
                    }
                    return studentList;
                }
            }
        }
        System.out.println("Index not found");
        return null;
    }

    /**
     * Retrieves student list in the Course specified by the admin
     * @param courseCode Course code of the Course we want to get the Students of
     * @return ArrayList of Students that are enrolled in the Course
     */
    public ArrayList<Student> getStudentListByCourse(String courseCode){
        if (checkCourseExists(courseCode)){
            ArrayList<Index> courseIndexes = cdm.getCourse(courseCode).getIndexes();
            ArrayList<Student> courseStudents = new ArrayList<>();
            for (Index idx : courseIndexes) {
                for (String matricNum: idx.getEnrolledStudents()){
                    courseStudents.add(sdm.getStudent(matricNum));
                }
            }
            return courseStudents;
        } else {
            System.out.println("Could not find course");
            return null;
        }
    }

    /**
     * Create a new Student and add the object to the database
     * @param userid Userid for the new Student
     * @param password Password for the new Student
     * @param studentName Name of the new Student
     * @param studentMatric Matriculation number of the new Student
     * @param email Email of the new Student
     * @param gender Gender of the new Student
     * @param nationality Nationality of the new Student
     * @param major Major the new Student is studying
     * @param maxAUs Maximum AUs the new Student can enroll in
     * @param startAccessPeriod Earliest time Student is allowed to log into the system
     * @param endAccessPeriod Cut off time for the Student to use the system
     * @return true on success, false otherwise
     */
    public boolean addStudent(String userid, String password, String studentName, String studentMatric, String email,
                              String gender, String nationality, String major, int maxAUs, String startAccessPeriod,
                              String endAccessPeriod){
        if (checkStudentExists(studentMatric)){
            System.out.println("Matriculation number already in use");
            System.out.println("Student creation failed");
            System.out.println("Please try again");
            return false;
        }
        Student newStudent = new Student(userid, password, studentName, studentMatric, email,
                gender, nationality, major, maxAUs, startAccessPeriod, endAccessPeriod);
        sdm.getStudentList().add(newStudent);
        System.out.println("Student "+studentName+" has been added successfully!");
        return true;
    }

    /**
     * Retrieves an overview of all Students
     * @return String containing overview information of Students in the database
     */
    public String getStudentOverview(){
        return sdm.generateStudentOverview();
    }

    /**
     * Retrieves an overview of all Courses
     * @param choice Indicates whether courses, courses and indexes or courses, indexes and lessons should be returned
     * @return String containing overview of Courses with varying amounts of info depending on the choice given
     */
    public String getCourseOverview(int choice){
        return cdm.generateCourseOverview(choice);
    }

    /**
     * Removes a Student from the database
     * @param matricNum Matriculation number of the Student to be removed
     */
    public void removeStudent(String matricNum){
        Student toBeRemoved = sdm.getStudent(matricNum);
        HashMap<String, String> registeredMap = toBeRemoved.getCoursesRegistered();
        HashMap<String, String> waitlistMap = toBeRemoved.getWaitList();
        Course regCourse;
        Index regIndex;
        // remove student from waitlists
        for (String courseCode: waitlistMap.keySet()){
            regCourse = cdm.getCourse(courseCode);
            regIndex = regCourse.getIndex(waitlistMap.get(courseCode));
            regIndex.getWaitlist().remove(matricNum);
        }
        // remove student from registered courses
        for (String courseCode: registeredMap.keySet()){
            regCourse = cdm.getCourse(courseCode);
            regIndex = regCourse.getIndex(registeredMap.get(courseCode));
            regIndex.getEnrolledStudents().remove(matricNum);
            regIndex.setCurrentVacancy(regIndex.getCurrentVacancy()+1);
            updateWaitlist(courseCode, regIndex.getIndexNum());
        }
        sdm.removeStudent(matricNum);
    }

    /**
     * Updates the Waitlist of an index
     * Registers Students from the waitlist into the index if there are available slots
     * @param courseCode Course code of the Course the index belong to
     * @param indexNum Index number of the Index to be refreshed
     */
    private void updateWaitlist(String courseCode, String indexNum){
        String matricNum;
        Student toAdd;
        Index tempIndex = cdm.getCourse(courseCode).getIndex(indexNum);
        // enroll students from waitlist if slots are now available
        while (tempIndex.getCurrentVacancy() > 0 && !tempIndex.getWaitlist().isEmpty()){
            matricNum = tempIndex.removeFromWaitlist();
            toAdd = sdm.getStudent(matricNum);
            tempIndex.addToEnrolledStudents(matricNum);
            toAdd.addCourse(courseCode, indexNum, cdm.getCourse(courseCode).getAcademicUnits());
            MailHandler.sendMail(toAdd.getEmail(),
                    "You have been removed from a wait-list!",
                    "Successful Registration of Course");
        }
    }

    /**
     * Removes a Course object from the database
     * @param courseCode Course code of the Course to be removed
     */
    public void removeCourse(String courseCode){
        Course toBeRemoved = cdm.getCourse(courseCode);
        for (Index idx: toBeRemoved.getIndexes()){
            // delete all instances of the course in Student's waitlists
            for (String wlMatricNum: idx.getWaitlist()){
                sdm.getStudent(wlMatricNum).removeCourse(courseCode, toBeRemoved.getAcademicUnits());
            }
            // delete all instances of the course in Student's registered courses
            for (String enMatricNum: idx.getEnrolledStudents()){
                sdm.getStudent(enMatricNum).removeCourse(courseCode, toBeRemoved.getAcademicUnits());
            }
        }
        cdm.removeCourse(courseCode);
    }

    /**
     * Used to remove an Index from a Course
     * Not allowed while students are enrolled
     * @param courseCode Course code of Course containing Index to be removed
     * @param indexNum Index number of Index to be removed
     */
    public void removeIndex(String courseCode, String indexNum){
        ArrayList<Index> toRemove = cdm.getCourse(courseCode).getIndexes();
        toRemove.removeIf(idx -> idx.getIndexNum().equals(indexNum));
    }

    /**
     * Used to remove a Lesson from a Course
     * Not allowed while students are enrolled
     * @param courseCode Course code of Course containing relevant Index
     * @param indexNum Index number of Index containing relevant Lesson
     * @param index Index of Lesson in Lesson ArrayList to be removed
     */
    public void removeLesson(String courseCode, String indexNum, int index) {
        ArrayList<Lesson> toRemove = cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
        toRemove.remove(index);
    }

    /**
     * Forcefully enroll a Student currently in the wait list of an index
     * Will increase the current vacancy of an Index to accommodate the Student
     * @param indexNum Index number of the Index to forcefully enroll the Student in
     * @param matricNum Matriculation number of the Student to be enrolled. Must currently be on the wait list
     * @return true if successful, false otherwise
     */
    public boolean forceEnrollStudent(String indexNum, String matricNum){
        Index tempIndex;
        Student tempStudent;
        for (Course crs: cdm.getCourseList()){
            tempIndex = crs.getIndex(indexNum);
            if (tempIndex != null && tempIndex.getWaitlist().contains(matricNum)){
                // Update Index object
                tempIndex.setIndexVacancy(tempIndex.getIndexVacancy()+1);
                tempIndex.getWaitlist().remove(matricNum);
                tempIndex.addToEnrolledStudents(matricNum);
                // Update Student object
                tempStudent = sdm.getStudent(matricNum);
                tempStudent.addCourse(crs.getCourseCode(), indexNum, crs.getAcademicUnits());
                tempStudent.removeCourseFromWaitList(crs.getCourseCode());
                return true;
            }
        }
        return false;
    }

    /**
     * Saves any changed data back to file
     */
    public void close() {
        cdm.save();
        sdm.save();
    }
}
