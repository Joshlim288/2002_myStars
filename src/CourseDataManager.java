import java.io.*;
import java.util.ArrayList;
/**
 * DataManager for Course data.<p>
 * Handles the loading, holding, editing and storing of Admin objects from the data/courseData.dat
 * @author Josh, Jun Wei, Shen Rui, Joshua, Daryl
 * @version 1.10
 * @since 1.9
 */
public class CourseDataManager implements DataManager{
    private ArrayList<Course> courseList;
    public CourseDataManager() {
        courseList = new ArrayList<>();
    }

    /**
     * Loads Course objects from data/courseData.dat into courseList.<p>
     * Also contains the relevant Indexes and Lessons that each Course contains.<p>
     *
     * Initializes the file if it has not yet been created
     */
    public void load() {
        try {
            FileInputStream fileIn = new FileInputStream("data/courseData.dat");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            courseList = (ArrayList<Course>) in.readObject();
            if (courseList.isEmpty()) {
                System.out.println("...Course data loaded but data is empty!");
            } else {
                System.out.println("...Course data loaded successfully!");
            }
        } catch (IOException e) {
            System.out.println("Course data file not found or is blank. Initializing file...");
            courseList = new ArrayList<>();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());;
        }
    }

    /**
     * Writes back the edited courseList to data/courseData.dat<p>
     * Must be called when exiting the program or updated data will not be retained between executions of the program
     */
    public void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("data/courseData.dat");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(courseList);
            out.close();
            fileOut.close();
            System.out.println("... Course Data saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a course object from courseList based on course code.
     * @param courseCode The course code of the course we are retrieving.
     * @return the course if the course code exists; null otherwise
     */
    public Course getCourse(String courseCode) {
        for (Course course : courseList) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Removes a course from courseList based on course code.
     * @param courseCode The course code of the course we are removing.
     * @return true if removal is successful
     */
    public boolean removeCourse(String courseCode) {
        Course courseToRemove = getCourse(courseCode);
        return courseList.remove(courseToRemove); // if courseToRemove is null, will return false
    }

    /**
     * Adds a new course into courseList.
     * @param newCourse The course object to be added.
     * @return true if addition is successful.
     */
    public boolean addCourse(Course newCourse) {
        // check if course with identical course code already exists in the list
        if (courseList.contains(newCourse)) { // checks using equals method in Course
            System.out.println("The course already exists in the database!");
            return false;
        }
        courseList.add(newCourse);
        return true;
    }

    /**
     * @return courseList as an ArrayList of Course objects
     */
    public ArrayList<Course> getCourseList(){
        return courseList;
    }

    /**
     * Returns a string that contains an overview of Courses
     * @param choice determines how much information should be included in the overview:
     *               <ol>
     *                  <li>Just Courses</li>
     *                  <li>Courses + Indexes</li>
     *                  <li>Courses + Indexes + Lessons</li>
     *               </ol>
     * @return String that contains the formatted overview
     */
    public String generateCourseOverview(int choice){
        StringBuilder stringBuilder = new StringBuilder();
        switch (choice){
            case(1) -> {
                stringBuilder.append("\n--------------------------------------------------------------------------------------------------------------------");
                stringBuilder.append("\nCourse | Course Name                              | AUs | School | Exam Timing                         | Course Type");
                stringBuilder.append("\n--------------------------------------------------------------------------------------------------------------------");
                ArrayList<Course> courseList = getCourseList();
                for (Course course: courseList)
                    stringBuilder.append("\n" + course);
                stringBuilder.append("\n--------------------------------------------------------------------------------------------------------------------");
            }
            case(2) -> {
                stringBuilder.append(generateCourseOverview(1));
                ArrayList<Course> courseList = getCourseList();
                stringBuilder.append("\n------------------------------------------------------");
                stringBuilder.append("\nCourse | Index | Group | Vacancies | Waitlist Length");
                stringBuilder.append("\n------------------------------------------------------");
                for (Course course: courseList) {
                    ArrayList<Index> indexList = course.getIndexes();
                    for (Index index : indexList)
                        stringBuilder.append("\n" + course.getCourseCode() + " | " + index);
                    stringBuilder.append("\n------------------------------------------------------");
                }
            }
            case(3) -> {
                stringBuilder.append(generateCourseOverview(2));
                ArrayList<Course> courseList = getCourseList();
                stringBuilder.append("\n----------------------------------------------------------------------------------------------------");
                stringBuilder.append("\nCourse | Index | Type | Venue   | Class Timing       | Teaching Weeks");
                stringBuilder.append("\n----------------------------------------------------------------------------------------------------");
                for (Course course: courseList) {
                    ArrayList<Index> indexList = course.getIndexes();
                    for (Index index: indexList) {
                        ArrayList<Lesson> lessonList = index.getLessons();
                        for (Lesson lesson: lessonList)
                            stringBuilder.append("\n" + course.getCourseCode() + " | " + index.getIndexNum() + " | " + lesson);
                        stringBuilder.append("\n----------------------------------------------------------------------------------------------------");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }
}
