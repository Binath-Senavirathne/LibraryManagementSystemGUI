package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import lms.Book;
import lms.FileManager;
import lms.Student;
import lms.Transaction;
import lms.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportController {

    @FXML private TabPane tabPane;
    @FXML private Tab tabBooks;
    @FXML private Tab tabStudents;
    @FXML private Tab tabTransactions;

    @FXML private Button btnLoadCsv;
    @FXML private Button btnConfirm;

    @FXML private TableView<Book>          tvBooks;
    @FXML private TableColumn<Book, String>  colBookId;
    @FXML private TableColumn<Book, String>  colTitle;
    @FXML private TableColumn<Book, String>  colIsbn13;
    @FXML private TableColumn<Book, String>  colAuthor;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, Integer> colAvailability;
    @FXML private TableColumn<Book, Double>  colPrice;

    @FXML private TableView<Student>          tvStudents;
    @FXML private TableColumn<Student, String> colStudentId;
    @FXML private TableColumn<Student, String> colFirstName;
    @FXML private TableView<Transaction>          tvTransactions;
    @FXML private TableColumn<Transaction, String>  colDate;
    @FXML private TableColumn<Transaction, String>  colTransBookId;
    @FXML private TableColumn<Transaction, String>  colTransStudentId;
    @FXML private TableColumn<Transaction, Integer> colType;

    private List<Book>        loadedBooks        = new ArrayList<>();
    private List<Student>     loadedStudents     = new ArrayList<>();
    private List<Transaction> loadedTransactions = new ArrayList<>();

    @FXML
    public void initialize() {
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colIsbn13.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCopies.setCellValueFactory(new PropertyValueFactory<>("copies"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("availability"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTransBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTransStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        btnLoadCsv.setOnAction(e -> handleLoadCsv());
        btnConfirm.setOnAction(e -> handleConfirm());
    }

    @FXML
    private void handleLoadCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV Files (books, students, transactions)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        List<File> files = fileChooser.showOpenMultipleDialog(btnLoadCsv.getScene().getWindow());

        if (files == null || files.isEmpty()) return;

        loadedBooks.clear();
        loadedStudents.clear();
        loadedTransactions.clear();

        for (File file : files) {
            String name = file.getName().toLowerCase();
            if (name.contains("book")) {
                loadedBooks = importBooks(file.getAbsolutePath());
                tvBooks.setItems(FXCollections.observableArrayList(loadedBooks));
            } else if (name.contains("student")) {
                loadedStudents = importStudents(file.getAbsolutePath());
                tvStudents.setItems(FXCollections.observableArrayList(loadedStudents));
            } else if (name.contains("transaction")) {
                loadedTransactions = importTransactions(file.getAbsolutePath());
                tvTransactions.setItems(FXCollections.observableArrayList(loadedTransactions));
            } else {
                showAlert(Alert.AlertType.WARNING, "Unknown File",
                        "Could not determine type for: " + file.getName() +
                        "\nFile name must contain 'book', 'student', or 'transaction'.");
            }
        }

        showAlert(Alert.AlertType.INFORMATION, "Files Loaded",
                "Books: " + loadedBooks.size() +
                "\nStudents: " + loadedStudents.size() +
                "\nTransactions: " + loadedTransactions.size());
    }

    @FXML
    private void handleConfirm() {
        if (loadedBooks.isEmpty() && loadedStudents.isEmpty() && loadedTransactions.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "Please load CSV files first.");
            return;
        }

        if (!loadedBooks.isEmpty())        FileManager.saveBooks("books.csv", loadedBooks);
        if (!loadedStudents.isEmpty())     FileManager.saveStudents("students.csv", loadedStudents);
        if (!loadedTransactions.isEmpty()) FileManager.saveTransactions("transactions.csv", loadedTransactions);

        showAlert(Alert.AlertType.INFORMATION, "Saved",
                "Data saved successfully to the data folder.");
    }

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
                    String bookId      = data[0].trim();
                    String title       = data[1].trim();
                    String isbn        = data[2].trim();
                    String author      = data[3].trim();
                    int    copies      = Integer.parseInt(data[4].trim());
                    int    availability = Integer.parseInt(data[5].trim());
                    double price       = Double.parseDouble(data[6].trim());

                    List<String> errors = new ArrayList<>();
                    if (!Validator.isValidBookID(bookId))
                        errors.add("Invalid Book ID: " + bookId + " (format: XX##)");
                    if (!Validator.isValidBookTitle(title))
                        errors.add("Invalid Title: " + title + " (letters/spaces only)");
                    if (!Validator.isValidISBN(isbn))
                        errors.add("Invalid ISBN: " + isbn + " (must be 13 digits with valid check digit)");
                    if (!Validator.isValidCopies(copies))
                        errors.add("Invalid Copies: " + copies + " (must be 0-2)");
                    if (!Validator.isValidAvailability(availability, copies))
                        errors.add("Invalid Availability: " + availability + " (must be 0-" + copies + ")");
                    if (!Validator.isValidPrice(price))
                        errors.add("Invalid Price: " + price + " (must be positive)");

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        books.add(new Book(bookId, title, isbn, author, copies, availability, price));
                    }
                } catch (NumberFormatException e) {
                    invalidRecords.add("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
                }
            }
            br.close();

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

                    List<String> errors = new ArrayList<>();
                    if (!Validator.isValidStudentId(studentId))
                        errors.add("Invalid Student ID: " + studentId + " (must be 8 digits)");
                    if (!Validator.isValidName(firstName))
                        errors.add("Invalid Name: " + firstName + " (letters only)");

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        students.add(new Student(studentId, firstName));
                    }
                } catch (Exception e) {
                    invalidRecords.add("Line " + lineNumber + ": " + e.getMessage());
                }
            }
            br.close();

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
                    String date      = data[0].trim();
                    String bookId    = data[1].trim();
                    String studentId = data[2].trim();
                    int    type      = Integer.parseInt(data[3].trim());

                    List<String> errors = new ArrayList<>();
                    if (!Validator.isValidDate(date))
                        errors.add("Invalid Date: " + date + " (format: DD/MM/YYYY)");
                    if (!Validator.isValidBookID(bookId))
                        errors.add("Invalid Book ID: " + bookId);
                    if (!Validator.isValidStudentId(studentId))
                        errors.add("Invalid Student ID: " + studentId);
                    if (type != 1 && type != 2)
                        errors.add("Invalid Type: " + type + " (must be 1=issued or 2=returned)");

                    if (!errors.isEmpty()) {
                        invalidRecords.add("Line " + lineNumber + ": " + String.join("; ", errors));
                    } else {
                        transactions.add(new Transaction(date, bookId, studentId, type));
                    }
                } catch (NumberFormatException e) {
                    invalidRecords.add("Line " + lineNumber + ": Invalid number format - " + e.getMessage());
                }
            }
            br.close();

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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
