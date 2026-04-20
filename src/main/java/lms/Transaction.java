package lms;

public class Transaction {

    private String date;
    private String bookId;
    private String studentId;
    private int type;

    public Transaction() {
    }

    public Transaction(String date, String bookId, String studentId, int type) {
        this.date = date;
        this.bookId = bookId;
        this.studentId = studentId;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toCsv() {
        return date + "," + bookId + "," + studentId + "," + type;
    }

    @Override
    public String toString() {
        String typeName = (type == 1) ? "Issued" : "Returned";
        return String.format("Date: %s | Book: %s | Student: %s | Type: %s", date, bookId, studentId, typeName);
    }
}
