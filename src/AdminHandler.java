
import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Creates objects and handles logic for Admin actions
 */
public class AdminHandler{
    Scanner sc = new Scanner(System.in);
    Admin currentAdmin;
    CourseDataManager cdm;
    StudentDataManager sdm;
    UserDataManager udm;
    Course tempCourse;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
        this.cdm = new CourseDataManager();
        this.sdm = new StudentDataManager();
        this.udm = new UserDataManager();
        cdm.load();
        sdm.load();
    }

    public boolean checkIndexExists(String indexNum) {
        for (Course crs: cdm.getCourseList()){
            for (Index idx: crs.getIndexes()){
                if (idx.getIndexNum().equals(indexNum)) {
                    System.out.println("Index in use");
                    return true;
                }
            }
        }
        System.out.println("Index not in use");
        return false;
    }

    public boolean checkCourseExists(String courseCode) {
        return cdm.getCourse(courseCode) != null;
    }
    public ArrayList<Index> getTempIndexes() {
        return new ArrayList<>(tempCourse.getIndexes());
    }
    public boolean checkLessonExists(String courseCode, String indexNum, String group){
        if (checkIndexExists(indexNum)) {
            for (Lesson lsn: cdm.getCourse(courseCode).getIndex(indexNum).getLessons()){
                if (lsn.getGroup().equals(group)) return true;
            }
        }
        return false;
    }
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
            }
            case(2)-> tempCourse.setCourseName(input);
            case(3)-> tempCourse.setCourseType(input);
            case(4)-> tempCourse.setAcademicUnits(Integer.parseInt(input));
            case(5)-> tempCourse.setSchool(input);
        }
        tempCourse = null; // this doesn't destroy the course... right?
        return true;
    }

    public boolean editIndex(String courseCode, String indexNum, String input, int choice){
        Index tempIndex = cdm.getCourse(courseCode).getIndex(indexNum);
        switch(choice){
            case(1)-> {
                if (checkIndexExists(input)){
                    return false;
                }
                tempIndex.setIndexNum(input);
            }
            case(2)-> {
                // im very tired is this correct
                // maybe just shove the whole thing to index
                int newVacancy = Integer.parseInt(input);
                if (newVacancy < tempIndex.getIndexVacancy()-tempIndex.getCurrentVacancy()) {
                    return false;
                }
                tempIndex.setCurrentVacancy(newVacancy-tempIndex.getIndexVacancy());
                tempIndex.setIndexVacancy(newVacancy);;
            }
        }
        return true;
    }

    public boolean editLesson(String courseCode, String indexNum, String lessonGroup, String input, int choice) {
        ArrayList<Lesson> allIndexLesson = cdm.getCourse(courseCode).getIndex(indexNum).getLessons();
        Lesson tempLesson = null;
        for (Lesson check: allIndexLesson) {
            if (check.getGroup().equals(lessonGroup))
                tempLesson = check;
        }
        switch(choice){
            case(1)-> tempLesson.setLessonType(input);
            case(2)-> {
                if (checkLessonExists(courseCode, indexNum, input)) {
                    System.out.println("lesson group already in use");
                    return false;
                }
                tempLesson.setGroup(input);
            }
            case(3)-> {
                // probably better to move out to own method
                // also please help the if statement is disgusting
                // i am tired
                for (Lesson check : allIndexLesson) {
                    if (check.getDay().toString().equals(input)) {
                        if ((tempLesson.getStartTime().isAfter(check.getStartTime()) &&
                                tempLesson.getStartTime().isBefore(check.getEndTime())) ||
                                (tempLesson.getEndTime().isAfter(check.getStartTime()) &&
                                        tempLesson.getEndTime().isBefore(check.getEndTime()))) {
                            System.out.println("Lesson clashes");
                            return false;
                        }
                    }
                }
                tempLesson.setDay(input);
            }
            case(4)->{
                for (Lesson check : allIndexLesson) {
                    if (check.getDay().equals(tempLesson.getDay())) {
                        if (LocalTime.parse(input).isAfter(check.getStartTime()) &&
                            LocalTime.parse(input).isBefore(check.getEndTime())) {
                            System.out.println("Lesson clashes");
                            return false;
                        }
                    }
                }
                tempLesson.setStartTime(LocalTime.parse(input));
            }
            case(5)->{
                for (Lesson check : allIndexLesson) {
                    if (check.getDay().equals(tempLesson.getDay())) {
                        if (LocalTime.parse(input).isAfter(check.getStartTime()) &&
                                LocalTime.parse(input).isBefore(check.getEndTime())) {
                            System.out.println("Lesson clashes");
                            return false;
                        }
                    }
                }
                tempLesson.setEndTime(LocalTime.parse(input));
            }
            case(6)->tempLesson.setVenue(input); // need to check if being used at the time?????
        }
        return true;
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
        }
        return true;
    }

    public boolean addCourse(String courseCode, String courseName,String courseType, int academicUnits, String school){
        try {
            tempCourse = new Course(courseCode, courseName,courseType, academicUnits,school);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //TODO make sure no duplicate index
    public boolean addIndex(String courseCode, String indexNum, int indexVacancies) {
        try {
            tempCourse.addIndex(indexNum, indexVacancies);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean addLesson(String indexNum, String lessonType, String group, String day,
                          LocalTime startTime, LocalTime endTime, String venue, ArrayList<Integer>teachingWeeks) {
        try {
            tempCourse.getIndex(indexNum).addLesson(lessonType, group, day, startTime, endTime, venue, teachingWeeks);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
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
     * TODO Just use indexNum since its unique
     * @param indexNum
     */
    public ArrayList<Student> getStudentListByIndex(String courseCode, String indexNum){         /*getStudentList()*/
        try {
            Course foundCourse = cdm.getCourse(courseCode);
            Index foundIndex = foundCourse.getIndex(indexNum);
            return foundIndex.getEnrolledStudents();
        } catch (NullPointerException e) {
            System.out.println("Could not find course or index");
            return null;
        }
    }

    /**
     * Prints student list in the index specified by the admin
     * @param courseCode
     * @returns array list of students
     */
    public ArrayList<Student> getStudentListByCourse(String courseCode){         /*getStudentList()*/
        try {
            ArrayList<Index> courseIndexes = cdm.getCourse(courseCode).getIndexes();
            ArrayList<Student> courseStudents = new ArrayList<>();
            for (Index idx : courseIndexes) {
                courseStudents.addAll(idx.getEnrolledStudents());
            }
            return courseStudents;
        } catch (NullPointerException e) {
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

    public boolean editAccessPeriod(String matricNum, LocalDateTime start, LocalDateTime end){
        // check start time < end time
        if (end.compareTo(start) > 0) {
            sdm.getStudent(matricNum).setAccessTime(start, end);
            return true;
        }
        System.out.println("ERROR: End of access period cannot be earlier or same as start of access period.");
        return false;
    }

    public boolean addStudent(String userid, String password, String studentName, String studentMatric, String email,
                           String gender, String nationality, String major, int maxAUs){
        try {
            Student newStudent = new Student(userid, password, studentName, studentMatric, email,
                    gender, nationality, major, maxAUs);
            sdm.getStudentList().add(newStudent);
            System.out.println("Student "+studentName+" has been added successfully!");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void close() {
        cdm.save();
        sdm.save();
    }
}
