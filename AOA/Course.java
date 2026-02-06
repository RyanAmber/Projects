package AOA;
public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private String instructor;
    public Course(String courseCode, String courseName, int credits, String instructor) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
    }
    public int hashCode() {
        int result = 17;
        result = 31 * result + (courseCode != null ? courseCode.hashCode() : 0);
        result = 31 * result + (courseName != null ? courseName.hashCode() : 0);
        result = 31 * result + credits;
        result = 31 * result + (instructor != null ? instructor.hashCode() : 0);
        return result;
    }
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return this.hashCode() ==course.hashCode();
    }
}
