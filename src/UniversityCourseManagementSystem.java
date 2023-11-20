import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum CourseLevel {
    BACHELOR,
    MASTER
}

abstract class UniversityMember {
    private static int numberOfMembers = 0;
    private int memberId;
    private String memberName;

    public UniversityMember(int memberId, String memberName) {
        numberOfMembers++;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public static int getNumberOfMembers() {
        return numberOfMembers;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }
}
interface Enrollable {
    public boolean drop(Course course);
    public boolean enroll(Course course);
}
class Student extends UniversityMember implements Enrollable {
    private static final int MAX_ENROLLMENT = 3;
    private List<Course> enrolledCourses = new ArrayList<>();
    private static HashMap<Integer, Student> universityStudents = new HashMap<>();
    public Student(String memberName) {
        super(getNumberOfMembers() + 1, memberName);
        universityStudents.put(this.getMemberId(), this);
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public static HashMap<Integer, Student> getUniversityStudents() {
        return universityStudents;
    }

    @Override
    public boolean drop(Course course) {
        if (this.enrolledCourses.contains(course)) {
            this.enrolledCourses.remove(course);
            return true;
        }
        return false;
    }
    @Override
    public boolean enroll(Course course) {
        if (!this.enrolledCourses.contains(course) && this.enrolledCourses.size() + 1 <= MAX_ENROLLMENT && !course.isFull()) {
            this.enrolledCourses.add(course);
            List<Student> courseEnrolled = course.getEnrolledStudents();
            courseEnrolled.add(this);
            course.setEnrolledStudents(courseEnrolled);
            return true;
        }
        return false;
    }
}
class Course {
    private static final int CAPACITY = 3;
    private static int NumberOfCourses;
    private int courseId;
    private String courseName;
    private List<Student> enrolledStudents;
    private CourseLevel courseLevel;
    private static HashMap<Integer, Course> universityCourses = new HashMap<>();

    public Course(String courseName, CourseLevel courseLevel) {
        this.courseId = getNumberOfCourses() + 1;
        setNumberOfCourses(this.courseId);
        this.courseName = courseName;
        this.courseLevel = courseLevel;
        universityCourses.put(this.getCourseId(), this);
    }

    public boolean isFull() {
        return CAPACITY == NumberOfCourses;
    }

    public static int getNumberOfCourses() {
        return NumberOfCourses;
    }

    public static void setNumberOfCourses(int numberOfCourses) {
        NumberOfCourses = numberOfCourses;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public CourseLevel getCourseLevel() {
        return courseLevel;
    }

    public void setCourseLevel(CourseLevel courseLevel) {
        this.courseLevel = courseLevel;
    }

    public HashMap<Integer, Course> getUniversityCourses() {
        return universityCourses;
    }
}

class Professor extends UniversityMember {
    private static final int MAX_LOAD = 2;
    private List<Course> assignedCourses = new ArrayList<>();
    private static HashMap<Integer, Professor> universityProfessors = new HashMap<>();
    public Professor(String memberName) {
        super(getNumberOfMembers() + 1, memberName);
        universityProfessors.put(this.getMemberId(), this);
    }
    public boolean teach(Course course) {
        if (this.assignedCourses.size() < MAX_LOAD && !this.assignedCourses.contains(course)) {
            this.assignedCourses.add(course);
            return true;
        }
        return false;
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    public static HashMap<Integer, Professor> getUniversityProfessors() {
        return universityProfessors;
    }

    public boolean exempt(Course course) {
        if (this.assignedCourses.contains(course)) {
            this.assignedCourses.remove(course);
            return true;
        }
        return false;
    }
}

public class UniversityCourseManagementSystem {
    static Student student;
    static Professor professor;
    static Course course;
    static Scanner input = new Scanner(System.in);
    /**
     * Entry point of a program
     * @param args - console data
     */
    public static void main(String[] args) {
        fillInitialData();

        while (input.hasNextLine()) {
            String currentCommand = input.nextLine();
            if (currentCommand.equals("")) return;

            switch (currentCommand) {
                case "course":
                    addCourse();
                    System.out.println("Added successfully");
                    break;
                case "student":
                    String studentName = input.nextLine();
                    if (!nameValidate(studentName)) exitWithError("Wrong inputs", 9);
                    student = new Student(studentName);

                    System.out.println("Added successfully");
                    break;
                case "professor":
                    String professorName = input.nextLine();
                    if (!nameValidate(professorName)) exitWithError("Wrong inputs", 9);
                    professor = new Professor(professorName);

                    System.out.println("Added successfully");
                    break;
                case "enroll":
                    try {
                        int memberNumber = Integer.parseInt(input.nextLine());
                        int courseNumber = Integer.parseInt(input.nextLine());

                        if (!student.getUniversityStudents().containsKey(memberNumber) || !course.getUniversityCourses().containsKey(courseNumber)) exitWithError("Wrong inputs", 9);
                        student.getUniversityStudents().get(memberNumber).enroll(course.getUniversityCourses().get(courseNumber));
                        System.out.println("Enrolled successfully");

                    } catch (NumberFormatException e) {
                        exitWithError("Wrong inputs", 9);
                    }

                case "drop":
                    break;
                case "teach":
                    break;
                case "exempt":
                    break;
                default:
                    exitWithError("Wrong inputs", 9);
            }
        }
    }

    private static boolean nameValidate(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /*
    Function to fill initial data (courses, students and professors)
     */
    public static void fillInitialData() {
        List<Course> aliceCourses = new ArrayList<>();
        List<Course> bobCourses = new ArrayList<>();
        List<Course> alexCourses = new ArrayList<>();
        // Add initial courses
        course = new Course("java_beginner", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        bobCourses.add(course);
        course = new Course("java_intermediate", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        course = new Course("python_basics", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        course = new Course("algorithms", CourseLevel.MASTER);
        bobCourses.add(course);
        course = new Course("advanced_programming", CourseLevel.MASTER);
        alexCourses.add(course);
        course = new Course("mathematical_analysis", CourseLevel.MASTER);
        course = new Course("computer_vision", CourseLevel.MASTER);

        // Add initial students
        student = new Student("Alice");
        student.setEnrolledCourses(aliceCourses);
        student = new Student("Bob");
        student.setEnrolledCourses(bobCourses);
        student = new Student("Alex");
        student.setEnrolledCourses(alexCourses);
        // Add initial professors
        professor = new Professor("Ali");
        professor = new Professor("Ahmed");
        professor = new Professor("Andrey");
    }

    static void addCourse() {
        String courseName = input.nextLine();
        String courseLevel = input.nextLine();

        if (!courseValidate(courseName, courseLevel)) exitWithError("Wrong inputs", 9);
        course = new Course(courseName, courseLevel.equals("bachelor") ? CourseLevel.BACHELOR : CourseLevel.MASTER);
    }

    /**
     * Checking the course name for specified conditions
     * @param name course name from input
     * @return test result
     */
    static boolean courseValidate(String name, String level) {
        Pattern pattern = Pattern.compile("[A-Za-z]+(_[A-Za-z]+)*");
        Matcher matcher = pattern.matcher(name);

        boolean nameValidate = matcher.matches();
        boolean levelValidate = level.equals("bachelor") || level.equals("master");

        return nameValidate && levelValidate;
    }



    static void exitWithError(String errorMessage, int errorCode) {
        System.out.println(errorMessage);
        System.exit(errorCode);
    }
}