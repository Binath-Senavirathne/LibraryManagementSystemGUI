package lms;

public class Book {

    private String bookId;
    private String title;
    private String isbn;
    private String author;
    private int copies;
    private int availability;
    private double price;

    public Book() {
    }

    public Book(String bookId, String title, String isbn, String author, int copies, int availability, double price) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.author = author;
        this.copies = copies;
        this.availability = availability;
        this.price = price;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String toCsv() {
        return bookId + "," + title + "," + isbn + "," + author + "," + copies + "," + availability + "," + String.format("%.2f", price);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %-20s | ISBN: %s | Author: %-15s | Copies: %d | Available: %d | Price: %.2f",
                bookId, title, isbn, author, copies, availability, price);
    }
}
