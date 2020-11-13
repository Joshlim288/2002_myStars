import java.util.ArrayList;
import java.util.Arrays;

public class DataGenerator {
    public static void main(String[] args) {
        AdminDataManager adm = new AdminDataManager();
        Admin admin = new Admin("admin1", "admin", "James Tan", "SCSE",
                                "ADMIN0000A", "admin1@e.ntu.edu.sg");
        adm.addAdmin(admin);
        adm.save();

        StudentDataManager sdm = new StudentDataManager();
        CourseDataManager cdm = new CourseDataManager();

        /* Initialising Students */

        Student studentA = new Student("studenta1", "memes", "Student A",
                "U1111111A", "schong031@e.ntu.edu.sg", "MALE", "Singaporean",
                "CSC", 24, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentB = new Student("studentb2", "memes", "Student B",
                "U2222222B", "studentb2@e.ntu.edu.sg", "FEMALE", "Malaysian",
                "CSC", 24, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentC = new Student("studentc3", "memes", "Student C",
                "U3333333C", "studentc3@e.ntu.edu.sg", "OTHER", "Chinese",
                "CSC", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentD = new Student("studentd4", "memes", "Student D",
                "U4444444D", "studentd4@e.ntu.edu.sg", "MALE", "Indian",
                "CSC", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentE = new Student("studente5", "memes", "Student E",
                "U5555555E", "studente5@e.ntu.edu.sg", "FEMALE", "Australian",
                "CSC", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentF = new Student("studentf6", "memes", "Student F",
                "U6666666F", "studentf6@e.ntu.edu.sg", "OTHER", "American",
                "CSC", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentG = new Student("studentg7", "memes", "Student G",
                "U7777777G", "studentg7@e.ntu.edu.sg", "MALE", "Singaporean",
                "CEE", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentH = new Student("studenth8", "memes", "Student H",
                "U8888888H", "studenth8@e.ntu.edu.sg", "FEMALE", "Malaysian",
                "CEE", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentJ = new Student("studentj9", "memes", "Student J",
                "U9999999J", "studentj9@e.ntu.edu.sg", "OTHER", "Chinese",
                "CEE", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentK = new Student("studentk10", "memes", "Student K",
                "U0000000K", "studentk10@e.ntu.edu.sg", "MALE", "Indian",
                "EEE", 21, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentL = new Student("studentl11", "memes", "Student L",
                "U1010101L", "studentl11@e.ntu.edu.sg", "FEMALE", "Australian",
                "EEE", 18, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentM = new Student("studentm12", "memes", "Student M",
                "U2020202M", "josh0047@e.ntu.edu.sg", "OTHER", "American",
                "EEE", 18, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentN = new Student("studentn13", "memes", "Student N",
                "U3030303N", "studentn13@e.ntu.edu.sg", "MALE", "Singaporean",
                "DSAI", 9, "2020-11-11 00:00", "2021-01-01 00:00");

        Student studentP = new Student("studentp14", "memes", "Student P",
                "U4040404P", "studentp14@e.ntu.edu.sg", "FEMALE", "Malaysian",
                "DSAI", 18, "2000-01-01 11:00", "2000-03-03 11:00");

        Student studentQ = new Student("studentq15", "memes", "Student Q",
                "U5050505Q", "studentq15@e.ntu.edu.sg", "OTHER", "Chinese",
                "DSAI", 18, "2030-01-01 11:00", "2030-03-03 11:00");


        /*Initialising Courses */
        ArrayList<Integer> allWeeks = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14));
        ArrayList<Integer> evenWeeks = new ArrayList<>(Arrays.asList(2,4,6,8,10,12,14));
        ArrayList<Integer> oddWeeks = new ArrayList<>(Arrays.asList(1,3,5,7,9,11,13));

        Course cz2002 = new Course("CZ2002", "Object-Oriented Design & Programming",
                "CORE", 3, "SCSE", "2021-05-01 17:00", "2021-05-01 19:00");

        Course cz2004 = new Course("CZ2004", "Human Computer Interaction",
                "CORE", 4, "SCSE", "2021-05-04 11:30", "2021-05-04 13:30");

        Course cz2007 = new Course("CZ2007", "Introduction To Databases",
                "CORE", 3, "SCSE", null, null);

        Course eg0001 = new Course("EG0001", "Engineers and Society",
                "GER", 3, "CEE", "2021-05-04 10:00", "2021-05-04 12:00");

        Course lk9001 = new Course("LK9001", "Korean Language Level One",
                "UE", 3, "SOH", "2021-05-04 09:00", "2021-05-04 11:00");

        cz2002.addIndex("11111", 10, "SS1");
        Index index11111 = cz2002.getIndex("11111");
        index11111.addLesson("LEC", "MON", "09:00", "11:00", "LT2A", allWeeks);
        index11111.addLesson("TUT", "TUE", "09:00", "11:00", "TR01", allWeeks);

        cz2002.addIndex("22222", 10, "SS2");
        Index index22222 = cz2002.getIndex("22222");
        index22222.addLesson("LEC", "MON", "09:00", "11:00", "LT2A", allWeeks);
        index22222.addLesson("TUT", "THU", "15:00", "17:30", "TR02", allWeeks);

        cz2004.addIndex("33333", 10, "SS3");
        Index index33333 = cz2004.getIndex("33333");
        index33333.addLesson("TUT", "MON", "13:00", "14:00", "TR03", evenWeeks);
        index33333.addLesson("TUT", "TUE", "13:00", "14:00", "TR04", allWeeks);

        cz2004.addIndex("44444", 10, "SS4");
        Index index44444 = cz2004.getIndex("44444");
        index44444.addLesson("TUT", "FRI", "09:00", "11:00", "TR05", oddWeeks);
        index44444.addLesson("TUT", "THU", "13:00", "14:00", "TR06", allWeeks);

        cz2007.addIndex("55555", 10, "SS5");
        Index index55555 = cz2007.getIndex("55555");
        index55555.addLesson("TUT", "WED", "09:00", "11:00", "TR07", allWeeks);
        index55555.addLesson("LAB", "THU", "09:00", "11:00", "SWLAB2", oddWeeks);

        cz2007.addIndex("66666", 10, "SS6");
        Index index66666 = cz2007.getIndex("66666");
        index66666.addLesson("TUT", "MON", "10:00", "12:00", "TR08", allWeeks);
        index66666.addLesson("LAB", "WED", "19:00", "20:00", "SPL", evenWeeks);

        eg0001.addIndex("77777", 10, "SS7");
        Index index77777 = eg0001.getIndex("77777");
        index77777.addLesson("TUT", "FRI", "09:00", "11:00", "TR09", allWeeks);
        index77777.addLesson("TUT", "MON", "17:00", "18:00", "TR10", allWeeks);

        eg0001.addIndex("88888", 10, "SS8");
        Index index88888 = eg0001.getIndex("88888");
        index88888.addLesson("TUT", "FRI", "13:00", "14:00", "TR11", allWeeks);
        index88888.addLesson("TUT", "TUE", "09:00", "11:00", "TR12", allWeeks);

        lk9001.addIndex("99999", 10, "SS9");
        Index index99999 = lk9001.getIndex("99999");
        index99999.addLesson("TUT", "MON", "13:00", "14:00", "TR13", evenWeeks);
        index99999.addLesson("SEM", "TUE", "13:00", "14:00", "TR14", allWeeks);

        lk9001.addIndex("10101", 10, "SS10");
        Index index10101 = lk9001.getIndex("10101");
        index10101.addLesson("TUT", "WED", "13:00", "14:00", "TR15", oddWeeks);
        index10101.addLesson("SEM", "THU", "13:00", "14:00", "TR16", allWeeks);


        /* Adding Students to Courses */

        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U1111111A");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U22222222B");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U3333333C");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U4444444D");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U5555555E");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U6666666F");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U7777777G");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U8888888H");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U0000000K");
        index11111.addToEnrolledStudents(index11111.getEnrolledStudents(), "U4040404P");
        studentA.addCourse("CZ2002", "11111", 3);
        studentB.addCourse("CZ2002", "11111", 3);
        studentC.addCourse("CZ2002", "11111", 3);
        studentD.addCourse("CZ2002", "11111", 3);
        studentE.addCourse("CZ2002", "11111", 3);
        studentF.addCourse("CZ2002", "11111", 3);
        studentG.addCourse("CZ2002", "11111", 3);
        studentH.addCourse("CZ2002", "11111", 3);
        studentK.addCourse("CZ2002", "11111", 3);
        studentP.addCourse("CZ2002", "11111", 3);

        index11111.addToWaitlist(index11111.getWaitlist(), "U2020202M");
        index11111.addToWaitlist(index11111.getWaitlist(), "U3030303N");
        studentM.addCourseToWaitList("CZ2002", "11111");
        studentN.addCourseToWaitList("CZ2002", "11111");

        index22222.addToEnrolledStudents(index22222.getEnrolledStudents(), "U1010101L");
        studentL.addCourse("CZ2002", "22222", 3);

        index55555.addToEnrolledStudents(index55555.getEnrolledStudents(), "U2222222B");
        studentB.addCourse("CZ2007", "55555", 3);

        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U1111111A");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U22222222B");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U3333333C");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U4444444D");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U5555555E");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U6666666F");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U7777777G");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U8888888H");
        index77777.addToEnrolledStudents(index77777.getEnrolledStudents(), "U0000000K");
        studentA.addCourse("EG0001", "77777", 3);
        studentB.addCourse("EG0001", "77777", 3);
        studentC.addCourse("EG0001", "77777", 3);
        studentD.addCourse("EG0001", "77777", 3);
        studentE.addCourse("EG0001", "77777", 3);
        studentF.addCourse("EG0001", "77777", 3);
        studentG.addCourse("EG0001", "77777", 3);
        studentH.addCourse("EG0001", "77777", 3);
        studentK.addCourse("EG0001", "77777", 3);

        index88888.addToEnrolledStudents(index88888.getEnrolledStudents(), "U9999999J");
        studentJ.addCourse("EG0001", "88888", 3);

        index99999.addToEnrolledStudents(index99999.getEnrolledStudents(), "U9999999J");
        index99999.addToEnrolledStudents(index88888.getEnrolledStudents(), "U3030303N");
        studentJ.addCourse("LK9001", "99999", 3);
        studentN.addCourse("LK9001", "99999", 3);

        /* Saving data */
        ArrayList<Student> allStudents = new ArrayList<>(Arrays.asList(studentA, studentB, studentC, studentD, studentE,
                                                        studentF, studentG, studentH, studentJ, studentK, studentL,
                                                        studentM, studentN, studentP, studentQ));
        for (Student student: allStudents)
            sdm.addStudent(student);

        ArrayList<Course> allCourses = new ArrayList<>(Arrays.asList(cz2002, cz2004, cz2007, eg0001, lk9001));
        for (Course course: allCourses)
            cdm.addCourse(course);

        cdm.save();
        sdm.save();
    }
}
