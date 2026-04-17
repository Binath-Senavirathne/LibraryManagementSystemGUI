package lms;

public class Book {

    private String bookId;
    private String title;
    private String isbn;
    private String author;
    private int copies;
    private int availability;
    private double price;

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

    public String getTitle() {
        return title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }

    public int getCopies() {
        return copies;
    }

    public int getAvailability() {
        return availability;
    }

    public double getPrice() {
        return price;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }

    public String toCsv() {
        return bookId + "," + title + "," + isbn + "," + author + "," + copies + "," + availability + "," + price;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Title: %-20s | ISBN: %s | Author: %-15s | Copies: %d | Available: %d | Price: %.2f",
                bookId, title, isbn, author, copies, availability, price);
    }
}

