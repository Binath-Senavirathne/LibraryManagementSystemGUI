package lms;

public class Transaction {

    private String date;
    private String bookId;
    private String studentId;
    private int type;

    public Transaction(String date, String bookId, String studentId, int type) {
        this.date = date;
        this.bookId = bookId;
        this.studentId = studentId;
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public String getBookId() {
        return bookId;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getType() {
        return type;
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
