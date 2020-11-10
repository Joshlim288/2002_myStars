import java.time.LocalTime;
import java.util.ArrayList;

public class DataGenerator {
    public static void main(String[] args) {
        //CourseDataManager cdm = new CourseDataManager();
        //StudentDataManager sdm = new StudentDataManager();
        AdminDataManager adm = new AdminDataManager();

        Admin admin = new Admin("admin1", BCrypt.hashpw("admin", BCrypt.gensalt()), "James Tan", "SCSE",
                                "ADMIN0000A", "admin1@e.ntu.edu.sg");
        adm.addAdmin(admin);
        adm.save();
        /*
        Course newcourse1 = new Course("CZ2001","Algorithms", "CORE",3,"SCSE");
        Course newcourse2 = new Course("CZ2002","Object Oriented Design and Programming", "CORE",3,"SCSE");
        Course newcourse3 = new Course("CZ2003","Computer Graphics and Visualization", "CORE",3,"SCSE");
        Course newcourse4 = new Course("CZ2004","Human Computer Interaction", "CORE",3,"SCSE");
        Course newcourse5 = new Course("EE8084", "Cyber Security", "GER", 3, "EEE");
        cdm.addCourse(newcourse1);
        cdm.addCourse(newcourse2);
        cdm.addCourse(newcourse3);
        cdm.addCourse(newcourse4);
        cdm.addCourse(newcourse5);

        Index index1 = new Index("10202",50);
        Index index2 = new Index("10203", 50);
        Index index3 = new Index("10204",50);
        Index index4 = new Index("10205", 50);
        Index index5 = new Index("10206", 50);
        Index index6 = new Index("10207",50);

        ArrayList<Integer> allweeks = new ArrayList<Integer>();
        for (int i=1;i<=13;i++)
            allweeks.add(i);
        ArrayList<Integer> oddweeks = new ArrayList<Integer>();
        for (int i=1;i<=13;i++)
            if (i%2==1)
                oddweeks.add(i);
        ArrayList<Integer> evenweeks = new ArrayList<Integer>();
        for (int i=1;i<=13;i++)
            if (i%2==0)
                evenweeks.add(i);

        Lesson lesson1 = new Lesson("LEC","CS2","TUE",LocalTime.parse("09:30"),LocalTime.parse("10:30"),"Online", allweeks);
        Lesson lesson2 = new Lesson("LEC","CS2","FRI",LocalTime.parse("11:30"),LocalTime.parse("12:30"),"Online", allweeks);
        Lesson lesson3 = new Lesson("TUT","SS2","WED",LocalTime.parse("10:30"),LocalTime.parse("11:30"),"TR16", allweeks);
        Lesson lesson4 = new Lesson("LAB","SS2","THURS",LocalTime.parse("14:30"),LocalTime.parse("16:30"),"SPL", evenweeks);

        Student student1 = new Student("abc000",BCrypt.hashpw("12345", BCrypt.gensalt()),"John Tay","U1921234A","abc000@e.ntu.edu.sg","Male","Singaporean","Computer Science",25);
        Student student2 = new Student("abc001",BCrypt.hashpw("12345", BCrypt.gensalt()),"Sharon Fong","U1921235B","abc001@e.ntu.edu.sg","Female","Singaporean","Computer Science",25);
        Student student3 = new Student("abc002",BCrypt.hashpw("12345", BCrypt.gensalt()),"Lennon Ong","U1921236C","abc002@e.ntu.edu.sg","Male","Singaporean","Computer Science",25);
        Admin admin1 = new Admin("tan0001",BCrypt.hashpw("12345", BCrypt.gensalt()),"Albert Tan","SCSE","A123","tan0001@e.ntu.edu.sg");
        Admin admin2 = new Admin("ang0001",BCrypt.hashpw("12345", BCrypt.gensalt()),"Lionel Ang","SCSE","A156","ang0001@e.ntu.edu.sg");

        index1.addLesson("LEC","CS2","TUES", LocalTime.parse("09:30:00"),LocalTime.parse("10:30:00"),"Online", allweeks);
        index1.addToEnrolledStudents(index1.getEnrolledStudents(), student1);
        index1.addToWaitlist(index1.getWaitlist(), student2);
        */
    }

}
