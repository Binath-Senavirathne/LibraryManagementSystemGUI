package controller;

import javafx.scene.control.Alert;
import lms.Book;
import lms.FileManager;
import lms.Student;
import lms.Transaction;
import lms.Validator;

import java.util.ArrayList;
import java.util.List;

public class ImportController {

    public static List<Book> importBooks(String fileName) {
        List<Book> books = new ArrayList<>();
        List<String> invalidRecords = new ArrayList<>();

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);
                if (data.length != 7) {
                    invalidRecords.add("Line " + lineNumber + ": Wrong column count (expected 7, got " + data.length + ")");
                    continue;
                }

                try {
                    String bookId = data[0].trim();
                    String title = data[1].trim();
                    String isbn = data[2].trim();
                    String author = data[3].trim();
                    int copies = Integer.parseInt(data[4].trim());
                    int availability = Integer.parseInt(data[5].trim());
                    double price = Double.parseDouble(data[6].trim());

                    // Validate each field
                    List<String> errors = new ArrayList<>();

                    if (!Validator.isValidBookID(bookId)) {
                        errors.add("Invalid Book ID: " + bookId + " (format: XX##)");
                    }
                    if (!Validator.isValidBookTitle(title)) {
                        errors.add("Invalid Title: " + title + " (letters only)");
                    }
                    if (!Validator.isValidISBN(isbn)) {
                        errors.add("Invalid ISBN: " + isbn + " (must be 13 digits with valid check digit)");
                    }
                    if (!Validator.isValidCopies(copies)) {
                        errors.add("Invalid Copies: " + copies + " (must be 0-2)");
                    }
                    if (!Validator.isValidAvailability(availability, copies)) {
                        errors.add("Invalid Availability: " + availability + " (must be 0-" + copies + ")");
                    }
                    if (!Validator.isValidPrice(price)) {
                        errors.add("Invalid Price: " + price + " (must be positive)");
                    }

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        Book book = new Book(bookId, title, isbn, author, copies, availability, price);
                        books.add(book);
                    }

                } catch (NumberFormatException e) {
                    invalidRecords.add("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
                }
            }
            br.close();

            // Report invalid records
            if (!invalidRecords.isEmpty()) {
                reportInvalidRecords("Invalid Book Records", invalidRecords);
            }

        } catch (Exception e) {
            System.out.println("Error importing books: " + e.getMessage());
        }

        return books;
    }


    public static List<Student> importStudents(String fileName) {
        List<Student> students = new ArrayList<>();
        List<String> invalidRecords = new ArrayList<>();

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);
                if (data.length != 2) {
                    invalidRecords.add("Line " + lineNumber + ": Wrong column count (expected 2, got " + data.length + ")");
                    continue;
                }

                try {
                    String studentId = data[0].trim();
                    String firstName = data[1].trim();

                    // Validate each field
                    List<String> errors = new ArrayList<>();

                    if (!Validator.isValidStudentId(studentId)) {
                        errors.add("Invalid Student ID: " + studentId + " (must be 8 digits)");
                    }
                    if (!Validator.isValidName(firstName)) {
                        errors.add("Invalid Name: " + firstName + " (letters only)");
                    }

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        Student student = new Student(studentId, firstName);
                        students.add(student);
                    }

                } catch (Exception e) {
                    invalidRecords.add("Line " + lineNumber + ": " + e.getMessage());
                }
            }
            br.close();

            // Report invalid records
            if (!invalidRecords.isEmpty()) {
                reportInvalidRecords("Invalid Student Records", invalidRecords);
            }

        } catch (Exception e) {
            System.out.println("Error importing students: " + e.getMessage());
        }

        return students;
    }


    public static List<Transaction> importTransactions(String fileName) {
        List<Transaction> transactions = new ArrayList<>();
        List<String> invalidRecords = new ArrayList<>();

        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);
                if (data.length != 4) {
                    invalidRecords.add("Line " + lineNumber + ": Wrong column count (expected 4, got " + data.length + ")");
                    continue;
                }

                try {
                    String date = data[0].trim();
                    String bookId = data[1].trim();
                    String studentId = data[2].trim();
                    int type = Integer.parseInt(data[3].trim());

                    // Validate each field
                    List<String> errors = new ArrayList<>();

                    if (!Validator.isValidDate(date)) {
                        errors.add("Invalid Date: " + date + " (format: DD/MM/YYYY)");
                    }
                    if (!Validator.isValidBookID(bookId)) {
                        errors.add("Invalid Book ID: " + bookId);
                    }
                    if (!Validator.isValidStudentId(studentId)) {
                        errors.add("Invalid Student ID: " + studentId);
                    }
                    if (type != 1 && type != 2) {
                        errors.add("Invalid Type: " + type + " (must be 1 for issued or 2 for returned)");
                    }

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        Transaction transaction = new Transaction(date, bookId, studentId, type);
                        transactions.add(transaction);
                    }

                } catch (NumberFormatException e) {
                    invalidRecords.add("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
                }
            }
            br.close();

            // Report invalid records
            if (!invalidRecords.isEmpty()) {
                reportInvalidRecords("Invalid Transaction Records", invalidRecords);
            }

        } catch (Exception e) {
            System.out.println("Error importing transactions: " + e.getMessage());
        }

        return transactions;
    }

    private static void reportInvalidRecords(String title, List<String> records) {
        StringBuilder message = new StringBuilder();
        message.append("Found ").append(records.size()).append(" invalid record(s):\n\n");
        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            message.append(records.get(i)).append("\n");
        }
        if (records.size() > 10) {
            message.append("\n... and ").append(records.size() - 10).append(" more");
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("Data Validation Issues");
        alert.setContentText(message.toString());
        alert.showAndWait();
    }
}
