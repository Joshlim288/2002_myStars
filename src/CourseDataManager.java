import java.io.*;
import java.util.ArrayList;

public class CourseDataManager implements DataManager{
    private ArrayList<Course> courseList;
    public CourseDataManager() {
        courseList = new ArrayList<>();
    }

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
            e.printStackTrace();
        }
    }

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

    public ArrayList<Course> getCourseList(){
        return courseList;
    }

    public String generateCourseOverview(int choice){
        StringBuilder stringBuilder = new StringBuilder();
        switch (choice){
            case(1) -> {
                stringBuilder.append("-----------------------------------");
                ArrayList<Course> courseList = getCourseList();
                for (Course course: courseList)
                    stringBuilder.append("\n" + course);
                stringBuilder.append("-----------------------------------");
            }
            case(2) -> {
                ArrayList<Course> courseList = getCourseList();
                stringBuilder.append("-----------------------------------");
                for (Course course: courseList) {
                    stringBuilder.append("\n" + course);
                    ArrayList<Index> indexList = course.getIndexes();
                    for (Index index : indexList)
                        stringBuilder.append("\n" + index);
                    stringBuilder.append("-----------------------------------");
                }
            }
            case(3) -> {
                ArrayList<Course> courseList = getCourseList();
                stringBuilder.append("-----------------------------------");
                stringBuilder.append("-----------------------------------");
                for (Course course: courseList) {
                    stringBuilder.append("\n" + course);
                    ArrayList<Index> indexList = course.getIndexes();
                    for (Index index: indexList) {
                        stringBuilder.append("\n" + index);
                        ArrayList<Lesson> lessonList = index.getLessons();
                        for (Lesson lesson: lessonList)
                            stringBuilder.append("\n" + lesson);
                        stringBuilder.append("-----------------------------------");
                    }
                    stringBuilder.append("-----------------------------------");
                }
            }
        }
        return stringBuilder.toString();
    }
}
