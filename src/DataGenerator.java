import java.util.ArrayList;

public class DataGenerator {
    public static void main(String[] args) throws ObjectCreationException {
        CourseDataManager cdm = new CourseDataManager();
        StudentDataManager sdm = new StudentDataManager();
        AdminDataManager adm = new AdminDataManager();

        Course newcourse1 = new Course("CZ2001","Algorithm", "CORE",3,"SCSE");
        Course newcourse2 = new Course("CZ2002","OODP", "CORE",3,"SCSE");
        Course newcourse3 = new Course("CZ2003","CGV", "CORE",3,"SCSE");
        cdm.addCourse(newcourse1);

        Index index1 = new Index(10202,50);
        Index index2 = new Index(10203, 50);
        Index index3 = new Index(10204,50);
        Index index4 = new Index(10205, 50);
        Index index5 = new Index(10206, 50);
        Index index6 = new Index(10207,50);

        ArrayList<Integer> allweeks = new ArrayList<Integer>();
        for (int i=0;i<13;i++)
            allweeks.add(i);
        ArrayList<Integer> oddweeks = new ArrayList<Integer>();
        for (int i=0;i<13;i++)
            if (i%2==1)
            allweeks.add(i);
        ArrayList<Integer> evenweeks = new ArrayList<Integer>();
        for (int i=0;i<13;i++)
            if (i%2==0)
            allweeks.add(i);

        Lesson lesson1 = new Lesson("LEC","CS2","TUE","09:30","10:30","Online", allweeks);
        Lesson lesson2 = new Lesson("LEC","CS2","FRI","11:30","12:30","Online", allweeks);
        Lesson lesson3 = new Lesson("TUT","SS2","WED","10:30","11:30","TR16", allweeks);
        Lesson lesson4 = new Lesson("LAB","SS2","THURS","14:30","16:30","SPL", evenweeks);

        Student student1 = new Student("abc000","12345","John Tay","U1921234A","abc000@e.ntu.edu.sg","Male","Singaporean","Computer Science",25);
        Student student2 = new Student("abc001","12345","Sharon Fong","U1921235B","abc001@e.ntu.edu.sg","Female","Singaporean","Computer Science",25);
        Student student3 = new Student("abc002","12345","Lennon Ong","U1921236C","abc002@e.ntu.edu.sg","Male","Singaporean","Computer Science",25);
        Admin admin1 = new Admin("tan0001","12345","Albert Tan","SCSE","A123","tan0001@e.ntu.edu.sg");
        Admin admin2 = new Admin("ang0001","12345","Lionel Ang","SCSE","A156","ang0001@e.ntu.edu.sg");

        index1.addLesson(typeOfLesson.LEC,"CS2",dayOfWeek.TUES,"09:30:00","10:30:00","Online", allweeks);
        index1.addToEnrolledStudents(enrolledStudents, student1);
        index1.addToWaitlist(waitlist, student2);

    }

}
