import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniversityCourseManagementSystem {
    private static final ArrayList<String> COMMANDS = new ArrayList<String>() {{
        add("course");
        add("student");
        add("professor");
        add("enroll");
        add("drop");
        add("teach");
        add("exempt");
    }};
    private static Student student;
    private static Professor professor;
    private static Course course;
    protected static Scanner input = new Scanner(System.in);

    /**
     * Entry point of a program
     *
     * @param args - console data
     */
    public static void main(String[] args) {
        fillInitialData();
        while (input.hasNextLine()) {
            String currentCommand = input.nextLine();
            if (currentCommand.isEmpty()) {
                System.exit(0);
            }
            switch (currentCommand) {
                case "course":
                    addCourse();
                    System.out.println("Added successfully");
                    break;
                case "student":
                    String studentName = input.nextLine();
                    nameValidate(studentName);
                    student = new Student(studentName);
                    System.out.println("Added successfully");
                    break;
                case "professor":
                    String professorName = input.nextLine();
                    nameValidate(professorName);
                    professor = new Professor(professorName);
                    System.out.println("Added successfully");
                    break;
                case "enroll":
                    enrollStudent();
                    System.out.println("Enrolled successfully");
                    break;
                case "drop":
                    dropStudent();
                    System.out.println("Dropped successfully");
                    break;
                case "teach":
                    teachCourse();
                    System.out.println("Professor is successfully assigned to teach this course");
                    break;
                case "exempt":
                    exemptCourse();
                    System.out.println("Professor is exempted");
                    break;
                default:
                    Execution.terminate("Wrong inputs");
            }
        }
    }

    private static void exemptCourse() {
        try {
            List<Integer> numbers = getNumbers("professor");
            Professor currentProfessor = Professor.getUniversityProfessors().get(numbers.get(0));
            Course currentCourse = course.getUniversityCourses().get(numbers.get(1));
            currentProfessor.exempt(currentCourse);
        } catch (NumberFormatException e) {
            Execution.terminate("Wrong inputs");
        }
    }

    private static void teachCourse() {
        try {
            List<Integer> numbers = getNumbers("professor");
            Professor currentProfessor = Professor.getUniversityProfessors().get(numbers.get(0));
            Course currentCourse = course.getUniversityCourses().get(numbers.get(1));
            currentProfessor.teach(currentCourse);
        } catch (NumberFormatException e) {
            Execution.terminate("Wrong inputs");
        }
    }

    private static List<Integer> getNumbers(String type) {
        List<Integer> output = new ArrayList<>();
        int memberNumber = Integer.parseInt(input.nextLine());
        validateMemberNumber(memberNumber, type);
        output.add(memberNumber);
        int courseNumber = Integer.parseInt(input.nextLine());
        validateCourseNumber(courseNumber);
        output.add(courseNumber);
        return output;
    }

    private static void dropStudent() {
        try {
            List<Integer> numbers = getNumbers("student");
            Student currentStudent = Student.getUniversityStudents().get(numbers.get(0));
            Course currentCourse = course.getUniversityCourses().get(numbers.get(1));
            currentStudent.drop(currentCourse);
        } catch (NumberFormatException e) {
            Execution.terminate("Wrong inputs");
        }
    }

    private static void enrollStudent() {
        try {
            List<Integer> numbers = getNumbers("student");
            Student currentStudent = Student.getUniversityStudents().get(numbers.get(0));
            Course currentCourse = course.getUniversityCourses().get(numbers.get(1));
            currentStudent.enroll(currentCourse);
        } catch (NumberFormatException e) {
            Execution.terminate("Wrong inputs");
        }
    }

    private static void validateCourseNumber(int courseNumber) {
        boolean flag = false;
        for (Integer s : course.getUniversityCourses().keySet()) {
            if (s == courseNumber) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            Execution.terminate("Wrong inputs");
        }
    }

    private static void validateMemberNumber(int memberNumber, String type) {
        Set<Integer> currentKeys = type.equals("student") ? Student.getUniversityStudents().keySet()
                : Professor.getUniversityProfessors().keySet();
        boolean flag = false;
        for (Integer s : currentKeys) {
            if (s == memberNumber) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            Execution.terminate("Wrong inputs");
        }
    }

    /**
     * @param name member's name from inputs
     * @throws Execution exception for invalid inputs
     */
    private static void nameValidate(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z]+");
        Matcher matcher = pattern.matcher(name);
        if (!(matcher.matches()) || COMMANDS.contains(name)) {
            Execution.terminate("Wrong inputs");
        }
    }

    /**
     * Checking the correctness of input data and adding a course
     */
    static void addCourse() {
        String courseName = input.nextLine();
        validateCourseName(courseName);
        courseExistsValidate(courseName);
        String courseLevel = input.nextLine();
        validateCourseLevel(courseLevel);
        course = new Course(courseName, courseLevel.equals("bachelor") ? CourseLevel.BACHELOR : CourseLevel.MASTER);
    }

    /**
     * @param courseName course name from inputs
     * @throws Execution exception for invalid course to add (course exists already)
     */
    private static void courseExistsValidate(String courseName) {
        for (Course c : course.getUniversityCourses().values()) {
            if (c.getCourseName().equalsIgnoreCase(courseName)) {
                Execution.terminate("Course exists");
            }
        }
    }

    /**
     * @param name course name from inputs
     * @throws Execution exception for invalid inputs
     */
    static void validateCourseName(String name) {
        Pattern pattern = Pattern.compile("[A-Za-z]+(_[A-Za-z]+)*");
        Matcher matcher = pattern.matcher(name);

        boolean nameValidate = matcher.matches() & !COMMANDS.contains(name);

        if (!(nameValidate)) {
            Execution.terminate("Wrong inputs");
        }
    }

    /**
     * @param level course level from inputs
     * @throws Execution exception for invalid inputs
     */
    static void validateCourseLevel(String level) {
        boolean levelValidate = level.equalsIgnoreCase("bachelor") || level.equalsIgnoreCase("master");
        if (!(levelValidate)) {
            Execution.terminate("Wrong inputs");
        }
    }

    /**
     * Function to fill initial data (courses, students and professors)
     */
    public static void fillInitialData() {
        List<Course> aliceCourses = new ArrayList<>();
        List<Course> bobCourses = new ArrayList<>();
        List<Course> alexCourses = new ArrayList<>();
        List<Course> aliCourses = new ArrayList<>();
        List<Course> ahmedCourses = new ArrayList<>();
        List<Course> andreyCourses = new ArrayList<>();
        course = new Course("java_beginner", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        bobCourses.add(course);
        aliCourses.add(course);
        course = new Course("java_intermediate", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        aliCourses.add(course);
        course = new Course("python_basics", CourseLevel.BACHELOR);
        aliceCourses.add(course);
        ahmedCourses.add(course);
        course = new Course("algorithms", CourseLevel.MASTER);
        bobCourses.add(course);
        course = new Course("advanced_programming", CourseLevel.MASTER);
        alexCourses.add(course);
        ahmedCourses.add(course);
        course = new Course("mathematical_analysis", CourseLevel.MASTER);
        andreyCourses.add(course);
        course = new Course("computer_vision", CourseLevel.MASTER);
        student = new Student("Alice");
        student.setEnrolledCourses(aliceCourses);
        student = new Student("Bob");
        student.setEnrolledCourses(bobCourses);
        student = new Student("Alex");
        student.setEnrolledCourses(alexCourses);
        professor = new Professor("Ali");
        professor.setAssignedCourses(aliCourses);
        professor = new Professor("Ahmed");
        professor.setAssignedCourses(ahmedCourses);
        professor = new Professor("Andrey");
        professor.setAssignedCourses(andreyCourses);
    }
}

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
        for (Course c : enrolledCourses) {
            List<Student> enrolledStudents = c.getEnrolledStudents();
            enrolledStudents.add(this);
            c.setEnrolledStudents(enrolledStudents);
        }
    }

    public static HashMap<Integer, Student> getUniversityStudents() {
        return universityStudents;
    }

    @Override
    public boolean drop(Course course) {
        if (!(this.enrolledCourses.contains(course))) {
            Execution.terminate("Student is not enrolled in this course");
        }
        this.enrolledCourses.remove(course);
        List<Student> courseEnrolled = course.getEnrolledStudents();
        courseEnrolled.remove(this);
        course.setEnrolledStudents(courseEnrolled);
        return true;
    }

    @Override
    public boolean enroll(Course course) {
        if (this.enrolledCourses.contains(course)) {
            Execution.terminate("Student is already enrolled in this course");
        }
        if (this.enrolledCourses.size() + 1 > MAX_ENROLLMENT) {
            Execution.terminate("Maximum enrollment is reached for the student");
        }
//        if (course.isFull()) {
//            Execution.terminate("Course is full");
//        }
        this.enrolledCourses.add(course);
        List<Student> courseEnrolled = course.getEnrolledStudents();
        courseEnrolled.add(this);
        course.setEnrolledStudents(courseEnrolled);
        return true;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
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
        if (this.assignedCourses.size() + 1 > MAX_LOAD) {
            Execution.terminate("Professor's load is complete");
        }
        if (this.assignedCourses.contains(course)) {
            Execution.terminate("Professor is already teaching this course");
        }
        this.assignedCourses.add(course);
        return true;
    }

    public static HashMap<Integer, Professor> getUniversityProfessors() {
        return universityProfessors;
    }

    public void setAssignedCourses(List<Course> assignedCourses) {
        this.assignedCourses = assignedCourses;
    }

    public boolean exempt(Course course) {
        if (!this.assignedCourses.contains(course)) {
            Execution.terminate("Professor is not teaching this course");
        }
        this.assignedCourses.remove(course);
        return true;
    }

    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }
}

class Course {
    private static final int CAPACITY = 3;
    private static int numberOfCourses;
    private int courseId;
    private String courseName;
    private List<Student> enrolledStudents = new ArrayList<>();
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
        return CAPACITY == this.enrolledStudents.size();
    }

    public static int getNumberOfCourses() {
        return numberOfCourses;
    }

    public static void setNumberOfCourses(int numberOfCourses) {
        Course.numberOfCourses = numberOfCourses;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<Student> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public HashMap<Integer, Course> getUniversityCourses() {
        return universityCourses;
    }
}

class Execution {
    public static void terminate(String errorMessage) {
        System.out.println(errorMessage);
        System.exit(0);
    }
}
