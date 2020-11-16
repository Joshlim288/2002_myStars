
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Creates objects and handles logic for Admin actions
 */
public class AdminHandler{
    private Admin currentAdmin;
    private CourseDataManager cdm;
    private StudentDataManager sdm;
    private UserDataManager udm;
    private Course tempCourse;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin; //TODO: remove?? dont think its used
        this.cdm = new CourseDataManager();
        this.sdm = new StudentDataManager();
        this.udm = new UserDataManager();
        cdm.load();
        sdm.load();
        udm.load();
    }
    public ArrayList<Course> getCourses(){
        return cdm.getCourseList();
    }
    public ArrayList<Index> getIndexes(String courseCode) {
        return cdm.getCourse(courseCode).getIndexes();
    }

    public ArrayList<Lesson> getLessons(String courseCode, String indexNum){
        return cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
    }

    public ArrayList<Student> getStudents(){
        return sdm.getStudentList();
    }

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

    public boolean checkCourseExists(String courseCode) {
        return cdm.getCourse(courseCode) != null;
    }

    public boolean checkCourseOccupied(String courseCode){
        for (Index idx: cdm.getCourse(courseCode).getIndexes()){
            if (idx.getCurrentVacancy() != idx.getIndexVacancy()){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Index> getTempIndexes() {
        return new ArrayList<>(tempCourse.getIndexes());
    }
//    public boolean checkLessonExists(String courseCode, String indexNum, String group){
//        if (checkIndexExists(indexNum)) {
//            for (Lesson lsn: cdm.getCourse(courseCode).getIndex(indexNum).getLessons()){
//                if (lsn.getGroup().equals(group)) return true;
//            }
//        }
//        return false;
//    }
    public boolean checkStudentExists(String matric){
        return sdm.getStudent(matric) != null;
    }

    /**
     * Edits the course details
     * @param  courseCode, courseName,courseType, academicUnits,school, indexes
     * Retrieve the course based on coursecode. Then edits and saves the new details
     */
    public boolean editCourse(String courseCode, String input, int choice){
        tempCourse = cdm.getCourse(courseCode);
        switch(choice){
            case(1)->{
                if (checkCourseExists(input)) {
                    System.out.println("Course with this code already exists");
                    return false;
                }
                tempCourse.setCourseCode(input);
                HashMap<String, String> coursesRegistered;
                String holdIndex;
                for (Student stud: sdm.getStudentList()){
                    coursesRegistered = stud.getCoursesRegistered();
                    if (coursesRegistered.containsKey(courseCode)){
                        holdIndex = coursesRegistered.get(courseCode);
                        coursesRegistered.remove(courseCode);
                        coursesRegistered.put(input, holdIndex);
                    }
                }
            }
            case(2)-> tempCourse.setCourseName(input);
            case(3)-> tempCourse.setCourseType(input);
            case(4)-> tempCourse.setAcademicUnits(Integer.parseInt(input));
            case(5)-> tempCourse.setSchool(input);
            case(7)-> {
                String newStart = input.split("&")[0];
                String newEnd = input.split("&")[1];
                tempCourse.setExamDateTime(newStart, newEnd);
            }
        }
        //tempCourse = null; // this doesn't destroy the course... right?
        return true;
    }

    public boolean editIndex(String courseCode, String indexNum, String input, int choice){
        Index tempIndex = cdm.getCourse(courseCode).getIndex(indexNum);

        switch(choice){
            case(1)-> {
                if (checkIndexExists(input)){
                    System.out.println("Index with this index number already exists");
                    return false;
                }
                tempIndex.setIndexNum(input);
                HashMap<String, String> coursesRegistered;
                for (Student stud: sdm.getStudentList()){
                    coursesRegistered = stud.getCoursesRegistered();
                    if (coursesRegistered.containsValue(indexNum)){
                        coursesRegistered.put(courseCode, input);
                    }
                }
            }
            case(2)-> {
                // im very tired is this correct
                // maybe just shove the whole thing to index
                int newVacancy = Integer.parseInt(input);
                if (newVacancy < tempIndex.getIndexVacancy()-tempIndex.getCurrentVacancy()) {
                    return false;
                }
                // set new current vacancy to be new vacancy - (num of people alr registered)
                tempIndex.setCurrentVacancy(newVacancy - (tempIndex.getIndexVacancy() - tempIndex.getCurrentVacancy()));
                tempIndex.setIndexVacancy(newVacancy);;
            }
            case(3)->{
                for (Course crs: cdm.getCourseList()){
                    for (Index idx: crs.getIndexes()){
                        if (idx.getGroup().equals(input)){
                            return false;
                        }
                    }
                }
                tempIndex.setGroup(input);
            }
        }
        return true;
    }

    public boolean editLesson(String courseCode, String indexNum, int lessonIndex, String input, int choice) {
        ArrayList<Lesson> allIndexLesson = cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
        Lesson tempLesson = allIndexLesson.get(lessonIndex);
        allIndexLesson.remove(lessonIndex); // removes the lesson first. If the changes succeed, we add it back

        switch(choice){
            case(1)-> tempLesson.setLessonType(input);
            case(2)-> {
                if (checkClash(allIndexLesson, input, new LocalTime[]{tempLesson.getStartTime(), tempLesson.getEndTime()}))
                    return false;

                tempLesson.setDay(input);
            }
            case(3)->{
                LocalTime startTime = LocalTime.parse(input.split("&")[0]);
                LocalTime endTime = LocalTime.parse(input.split("&")[1]);
                if (checkClash(allIndexLesson, tempLesson.getDay().toString(),
                        new LocalTime[]{startTime, endTime}))
                    return false;

                tempLesson.setStartTime(startTime);
                tempLesson.setEndTime(endTime);
            }
            case(4)->tempLesson.setVenue(input); // need to check if being used at the time?????
        }
        allIndexLesson.add(lessonIndex, tempLesson); // add back the lesson we removed, after the changes are accepted
        return true;
    }

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

    public boolean editStudent(String matricNum, String updatedValue, int choice){
        Student tempStudent = sdm.getStudent(matricNum);
        switch(choice){
            case(1)-> {
                if (udm.getUser(updatedValue) != null) {
                    System.out.println("UserID already in use");
                    return false;
                }
                tempStudent.setUserID(updatedValue);
            }
            case(2)->tempStudent.setPassword(updatedValue);
            case(3)->tempStudent.setName(updatedValue);
            case(4)->{
                if (sdm.getStudent(updatedValue) != null){
                    System.out.println("Matriculation Number already in use");
                    return false;
                }
                tempStudent.setMatricNum(updatedValue);
                ArrayList<String> enrolledStudents;
                for (Course crs: cdm.getCourseList()){
                    for (Index idx: crs.getIndexes()){
                        enrolledStudents = idx.getEnrolledStudents();
                        if (enrolledStudents.contains(matricNum)){
                            enrolledStudents.remove(matricNum);
                            enrolledStudents.add(updatedValue);
                        }
                    }
                }
            }
            case(5)->{
                for (User u: udm.getUserList()){
                    if (u.getEmail().equals(updatedValue)){
                        System.out.println("Email already in use");
                        return false;
                    }
                }
                tempStudent.setEmail(updatedValue);
            }
            case(6)->tempStudent.setGender(updatedValue);
            case(7)->tempStudent.setNationality(updatedValue);
            case(8)->tempStudent.setMajor(updatedValue);
            case(9)->{
                if (Integer.parseInt(updatedValue) < tempStudent.getCurrentAUs()){
                    System.out.println("Student currently taking more than updated max AUs");
                }
                tempStudent.setMaxAUs(Integer.parseInt(updatedValue));
            }
            case(10)->{
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
     * Initializes a temporary course object in the variable tempCourse if course code is not already in use
     * @param courseCode
     * @param courseName
     * @param courseType
     * @param academicUnits
     * @param school
     * @return
     */
    public boolean addCourse(String courseCode, String courseName,String courseType, int academicUnits, String school,
                             boolean hasExam,String examStart, String examEnd){
        if (checkCourseExists(courseCode)) {
            System.out.println("Course with this course code already exists");
            return false;
        }

        if(hasExam)
            tempCourse = new Course(courseCode, courseName,courseType, academicUnits,school, examStart, examEnd);
        else
            tempCourse = new Course(courseCode, courseName,courseType, academicUnits,school, null, null);
        return true;

    }

    /**
     * Adds a new index to the tempCourse object if the index number is not already in use
     * @param indexNum
     * @param indexVacancies
     * @param group
     * @return
     */
    public boolean addIndex(String indexNum, int indexVacancies, String group) {
        if (checkIndexExists(indexNum)) {
            System.out.println("Index with this index number already exists");
            return false;
        }

        for (Index idx: tempCourse.getIndexes()){
            if (idx.getIndexNum().equals(indexNum)){
                System.out.println("You have previously created this index");
                return false;
            }
        }
        tempCourse.addIndex(indexNum, indexVacancies, group);
        return true;
    }

    public boolean addLesson(String indexNum, String lessonType, String day,
                          String startTime, String endTime, String venue, ArrayList<Integer>teachingWeeks) {
        if (checkClash(tempCourse.getIndex(indexNum).getLessons(), day, new LocalTime[]{LocalTime.parse(startTime), LocalTime.parse(endTime)}))
            return false;

        tempCourse.getIndex(indexNum).addLesson(lessonType, day, startTime, endTime, venue, teachingWeeks);
        return true;
    }

    public void finalizeCourse(){
        cdm.addCourse(tempCourse);
        tempCourse = null;
    }

    /**
     * Check the number of available slot for an index number
     * @param  course, indexnum
     * @return int number of vacancies
     */
    public int checkSlot(String course, String indexnum){
        Index index = cdm.getCourse(course).getIndex(indexnum);
        if (index == null) {
            System.out.println("Index not found");
            return -1;
        }
        return index.getCurrentVacancy();
    }

    /**
     * Prints student list in the index specified by the admin
     * @param indexNum
     */
    public ArrayList<Student> getStudentListByIndex(String indexNum){         /*getStudentList()*/
        if (checkIndexExists(indexNum)) {
            for (Course curCourse : cdm.getCourseList()) {
                Index foundIndex = curCourse.getIndex(indexNum);
                if (foundIndex != null) {
                    ArrayList<Student> studentList = new ArrayList<>();
                    for(String matricNum: foundIndex.getEnrolledStudents()) {
                        studentList.add(sdm.getStudent(matricNum));
                    }
                    return studentList;
                }
            }
        }
        System.out.println("Index not found");
        return null;
    }

    /**
     * Prints student list in the index specified by the admin
     * @param courseCode
     * @returns array list of students
     */
    public ArrayList<Student> getStudentListByCourse(String courseCode){         /*getStudentList()*/
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

    public LocalDateTime[] getAccessPeriod(String matricNum){
        Student student = sdm.getStudent(matricNum);
        if (student != null)
            return sdm.getStudent(matricNum).getAccessTime();
        return null;
    }

    //TODO: Use validator method instead in the interface
    @Deprecated
    public boolean editAccessPeriod(String matricNum, String start, String end){
        // check start time < end time
        if (end.compareTo(start) > 0) {
            sdm.getStudent(matricNum).setAccessTime(start, end);
            return true;
        }
        System.out.println("ERROR: End of access period cannot be earlier or same as start of access period.");
        return false;
    }

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

    public String getStudentOverview(){
        return sdm.generateStudentOverview();
    }

    public String getCourseOverview(int choice){
        return cdm.generateCourseOverview(choice);
    }

    public void removeStudent(String matricNum){
        Student toBeRemoved = sdm.getStudent(matricNum);
        HashMap<String, String> registeredMap = toBeRemoved.getCoursesRegistered();
        Course regCourse;
        Index regIndex;
        for (String courseCode: registeredMap.keySet()){
            regCourse = cdm.getCourse(courseCode);
            regIndex = regCourse.getIndex(registeredMap.get(courseCode));
            regIndex.getEnrolledStudents().remove(matricNum);
            regIndex.setCurrentVacancy(regIndex.getCurrentVacancy()+1);
        }
        sdm.removeStudent(matricNum);
    }

    public void removeCourse(String courseCode){
        Course toBeRemoved = cdm.getCourse(courseCode);
        for (Index idx: toBeRemoved.getIndexes()){
            for (String matricNum: idx.getEnrolledStudents()){
                sdm.getStudent(matricNum).removeCourse(courseCode, toBeRemoved.getAcademicUnits());
            }
        }
        cdm.removeCourse(courseCode);
    }

    public void close() {
        cdm.save();
        sdm.save();
    }
}
