package lms;

public class Student {

    private int studentID;
    private String firstName;

    public Student(int studentID, String firstName) {
        this.studentID = studentID;
        this.firstName = firstName;
    }

    public int getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String toCsv(){
        return studentID + " " + firstName + " ";
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Name: %s", studentID, firstName);
    }
}
