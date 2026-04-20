package lms;

public class Student {

    private String studentID;
    private String firstName;

    public Student() {
    }

    public Student(String studentID, String firstName) {
        this.studentID = studentID;
        this.firstName = firstName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String toCsv() {
        return studentID + "," + firstName;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s", studentID, firstName);
    }
}