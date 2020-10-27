
import java.io.File;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;

public class AdminHandler implements AdminInterface{
    Scanner sc = new Scanner(System.in);
    Admin currentAdmin;

    public AdminHandler(Admin currentAdmin) {
        this.currentAdmin = currentAdmin;
    }
    /**
     * Edits the course details
     * @param  courseCode, courseName,courseType, academicUnits,school, indexes
     * Retrieve the course based on coursecode. Then edits and saves the new details
     */
    public void editCourse(String courseCode, String courseName,typeOfCourse courseType, int academicUnits, String school, ArrayList<Index> indexes){
        Course course = FileHandler.getCourse(courseCode);
        course.setCourseName(courseName);
        course.setCourseType(courseType);
        course.setAcademicUnits(academicUnits);
        course.setSchool(school);
        course.setIndexes(indexes);

    }

    public void addCourse(String courseCode, String courseName,typeOfCourse courseType, int academicUnits, String school, ArrayList<Index> indexes){
        Course newcourse = new Course(courseCode, courseName,courseType, academicUnits,school, indexes);
        FileHandler.addCourse(newcourse);
        FileHandler.saveCourses();
    }

    /**
     * Check the number of available slot for an index number
     * @param  course, indexnum
     * @return int number of vacancys
     */
    public int checkSlot(String course, int indexnum){
        Index index = FileHandler.getCourse(course).getIndex(indexnum);
        return index.getCurrentVacancy();
    }

    /**
     * Prints student list
     * @param byIndex
     * Sort student list by index if byIndex is True, by course if False
     */
    public void printStudentList(boolean byIndex){
        if (byIndex){
            for (int i = 0; i < FileHandler.getCourseList().size();i++){
                FileHandler.getCourse(i);
                for (int i = 0; i < FileHandler.index.getEnrolledStudent().size(); i++) {
                    System.out.println((i + 1) + ". " + index.getStudent().get(i).getName());
            }}}
        else{
            for (int k = 0; k < FileHandler.getCourse().getIndex().get(i).getStudent().size(); k++) {
                System.out.println((i + 1) + ". " + course.getIndex().get(i).getStudent().get(k).getName());
            }
        }
        }



    public void editAccessPeriod(String matricNum, LocalDateTime start, LocalDateTime end){
        Student student = FileHandler.getStudent(matricNum);
        student.setAccessTime(start, end);
        FileHandler.saveStudents();
    }

    public void addStudent(Student student){
        FileHandler.getStudentList().add(student);
        FileHandler.saveStudents();
        FileHandler.saveUsers();

    }
    /**
     * Check if the matric number is available.
     * @param String matric
     * @returns false if matric number already exist
     */
    public boolean checkmatricexist(String matric){
        for(int i =0;i<FileHandler.getStudentList().size();i++)
        {
            if(FileHandler.getStudentList().get(i).getMatricNum().equals(matric))
            {
                System.out.println("Matriculation number already exists!");
                return true;
            }
        }
        return false;
    }
}
