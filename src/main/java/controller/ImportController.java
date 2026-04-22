package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import lms.Book;
import lms.DataStore;
import lms.FileManager;
import lms.Student;
import lms.Transaction;
import lms.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ImportController {

    @FXML private TabPane tabPane;
    @FXML private Tab tabBooks;
    @FXML private Tab tabStudents;
    @FXML private Tab tabTransactions;

    @FXML private Button btnLoadCsv;
    @FXML private Button btnConfirm;

    @FXML private TableView<DataStore> tvBooks;
    @FXML private TableColumn<DataStore, String> colBookId;
    @FXML private TableColumn<DataStore, String> colTitle;
    @FXML private TableColumn<DataStore, String> colIsbn13;
    @FXML private TableColumn<DataStore, String> colAuthor;
    @FXML private TableColumn<DataStore, String> colCopies;
    @FXML private TableColumn<DataStore, String> colAvailability;
    @FXML private TableColumn<DataStore, String> colPrice;
    @FXML private TableColumn<DataStore, String> colBookError;
    @FXML private TableColumn<DataStore, Void> colBookAction;

    @FXML private TableView<DataStore> tvStudents;
    @FXML private TableColumn<DataStore, String> colStudentId;
    @FXML private TableColumn<DataStore, String> colFirstName;
    @FXML private TableColumn<DataStore, String> colStudentError;
    @FXML private TableColumn<DataStore, Void> colStudentAction;

    @FXML private TableView<DataStore> tvTransactions;
    @FXML private TableColumn<DataStore, String> colDate;
    @FXML private TableColumn<DataStore, String> colTransBookId;
    @FXML private TableColumn<DataStore, String> colTransStudentId;
    @FXML private TableColumn<DataStore, String> colType;
    @FXML private TableColumn<DataStore, String> colTransError;
    @FXML private TableColumn<DataStore, Void> colTransAction;

    private List<DataStore> loadedBooks = new ArrayList<>();
    private List<DataStore> loadedStudents = new ArrayList<>();
    private List<DataStore> loadedTransactions = new ArrayList<>();

    @FXML
    public void initialize() {
        setupBookColumns();
        setupStudentColumns();
        setupTransactionColumns();

        btnLoadCsv.setOnAction(e -> handleLoadCsv());
        btnConfirm.setOnAction(e -> handleConfirm());
    }

    private void setupBookColumns() {
        colBookId.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(0));
        colTitle.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(1));
        colIsbn13.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(2));
        colAuthor.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(3));
        colCopies.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(4));
        colAvailability.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(5));
        colPrice.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(6));
        colBookError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());
        
        setupActionColumn(colBookAction, "Book", new String[]{"Book ID", "Title", "ISBN", "Author", "Copies", "Availability", "Price"});
    }

    private void setupStudentColumns() {
        colStudentId.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(0));
        colFirstName.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(1));
        colStudentError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());

        setupActionColumn(colStudentAction, "Student", new String[]{"Student ID", "First Name"});
    }

    private void setupTransactionColumns() {
        colDate.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(0));
        colTransBookId.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(1));
        colTransStudentId.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(2));
        colType.setCellValueFactory(cellData -> cellData.getValue().getColumnProperty(3));
        colTransError.setCellValueFactory(cellData -> cellData.getValue().errorProperty());

        setupActionColumn(colTransAction, "Transaction", new String[]{"Date", "Book ID", "Student ID", "Type"});
    }

    private void setupActionColumn(TableColumn<DataStore, Void> col, String type, String[] fieldNames) {
        col.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Edit");
            {
                btn.setOnAction(event -> {
                    DataStore data = getTableView().getItems().get(getIndex());
                    editRow(data, type, fieldNames, getTableView());
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    DataStore data = (DataStore) getTableRow().getItem();
                    if (data.getError() != null && !data.getError().isEmpty()) {
                        setGraphic(btn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void editRow(DataStore dataStore, String type, String[] fieldNames, TableView<DataStore> table) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Edit " + type);
        dialog.setHeaderText("Correct the errors for this " + type);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<TextField> fields = new ArrayList<>();
        for (int i = 0; i < fieldNames.length; i++) {
            TextField tf = new TextField(dataStore.getColumn(i));
            fields.add(tf);
            grid.add(new Label(fieldNames[i] + ":"), 0, i);
            grid.add(tf, 1, i);
        }

        dialog.getDialogPane().setContent(grid);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                List<String> results = new ArrayList<>();
                for (TextField tf : fields) results.add(tf.getText());
                return results;
            }
            return null;
        });

        Optional<List<String>> result = dialog.showAndWait();
        result.ifPresent(newValues -> {
            for (int i = 0; i < newValues.size(); i++) {
                dataStore.setColumn(i, newValues.get(i));
            }
            // Re-validate based on type
            if (type.equals("Book")) {
                dataStore.setError(validateBook(dataStore));
            } else if (type.equals("Student")) {
                dataStore.setError(validateStudent(dataStore));
            } else if (type.equals("Transaction")) {
                dataStore.setError(validateTransaction(dataStore));
            }
            table.refresh();
        });
    }

    @FXML
    private void handleLoadCsv() {
        loadedBooks = importBooks("../data/book.csv");
        loadedStudents = importStudents("../data/student.csv");
        loadedTransactions = importTransactions("../data/transactions.csv");

        tvBooks.setItems(FXCollections.observableArrayList(loadedBooks));
        tvStudents.setItems(FXCollections.observableArrayList(loadedStudents));
        tvTransactions.setItems(FXCollections.observableArrayList(loadedTransactions));

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

        boolean hasErrors = false;
        for (DataStore ds : loadedBooks) if (!ds.getError().isEmpty()) hasErrors = true;
        for (DataStore ds : loadedStudents) if (!ds.getError().isEmpty()) hasErrors = true;
        for (DataStore ds : loadedTransactions) if (!ds.getError().isEmpty()) hasErrors = true;

        if (hasErrors) {
            showAlert(Alert.AlertType.ERROR, "Cannot Save", "Please fix all errors in the tables before saving.");
            return;
        }

        List<Book> validBooks = new ArrayList<>();
        for (DataStore ds : loadedBooks) {
            validBooks.add(new Book(ds.getColumn(0), ds.getColumn(1), ds.getColumn(2), ds.getColumn(3), 
                    Integer.parseInt(ds.getColumn(4)), Integer.parseInt(ds.getColumn(5)), Double.parseDouble(ds.getColumn(6))));
        }

        List<Student> validStudents = new ArrayList<>();
        for (DataStore ds : loadedStudents) {
            validStudents.add(new Student(ds.getColumn(0), ds.getColumn(1)));
        }

        List<Transaction> validTransactions = new ArrayList<>();
        for (DataStore ds : loadedTransactions) {
            validTransactions.add(new Transaction(ds.getColumn(0), ds.getColumn(1), ds.getColumn(2), Integer.parseInt(ds.getColumn(3))));
        }

        if (!validBooks.isEmpty()) FileManager.saveBooks("book.csv", validBooks);
        if (!validStudents.isEmpty()) FileManager.saveStudents("student.csv", validStudents);
        if (!validTransactions.isEmpty()) FileManager.saveTransactions("transactions.csv", validTransactions);

        showAlert(Alert.AlertType.INFORMATION, "Saved", "Data saved successfully to the data folder.");
    }

    private List<DataStore> importBooks(String fileName) {
        List<DataStore> books = new ArrayList<>();
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            if (!java.nio.file.Files.exists(path)) return books;
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 || line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < 7; i++) rowData.add(i < data.length ? data[i].trim() : "");
                
                DataStore ds = new DataStore(rowData, "");
                ds.setError(validateBook(ds));
                books.add(ds);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error importing books: " + e.getMessage());
        }
        return books;
    }

    private List<DataStore> importStudents(String fileName) {
        List<DataStore> students = new ArrayList<>();
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            if (!java.nio.file.Files.exists(path)) return students;
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 || line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < 2; i++) rowData.add(i < data.length ? data[i].trim() : "");
                
                DataStore ds = new DataStore(rowData, "");
                ds.setError(validateStudent(ds));
                students.add(ds);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
        return students;
    }

    private List<DataStore> importTransactions(String fileName) {
        List<DataStore> transactions = new ArrayList<>();
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(fileName);
            if (!java.nio.file.Files.exists(path)) return transactions;
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(path.toFile()));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 || line.trim().isEmpty()) continue;
                String[] data = line.split(",", -1);
                List<String> rowData = new ArrayList<>();
                for (int i = 0; i < 4; i++) rowData.add(i < data.length ? data[i].trim() : "");
                
                DataStore ds = new DataStore(rowData, "");
                ds.setError(validateTransaction(ds));
                transactions.add(ds);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error importing transactions: " + e.getMessage());
        }
        return transactions;
    }

    private String validateBook(DataStore ds) {
        List<String> errors = new ArrayList<>();
        if (!Validator.isValidBookID(ds.getColumn(0))) errors.add("Invalid ID");
        if (!Validator.isValidBookTitle(ds.getColumn(1))) errors.add("Invalid Title");
        if (!Validator.isValidISBN(ds.getColumn(2))) errors.add("Invalid ISBN");
        
        try {
            int copies = Integer.parseInt(ds.getColumn(4));
            if (!Validator.isValidCopies(copies)) errors.add("Invalid Copies");
            
            int avail = Integer.parseInt(ds.getColumn(5));
            if (!Validator.isValidAvailability(avail, copies)) errors.add("Invalid Availability");
        } catch (NumberFormatException e) {
            errors.add("Copies/Availability must be numbers");
        }
        
        try {
            double price = Double.parseDouble(ds.getColumn(6));
            if (!Validator.isValidPrice(price)) errors.add("Invalid Price");
        } catch (NumberFormatException e) {
            errors.add("Price must be a number");
        }
        return String.join("; ", errors);
    }

    private String validateStudent(DataStore ds) {
        List<String> errors = new ArrayList<>();
        if (!Validator.isValidStudentId(ds.getColumn(0))) errors.add("Invalid ID");
        if (!Validator.isValidName(ds.getColumn(1))) errors.add("Invalid Name");
        return String.join("; ", errors);
    }

    private String validateTransaction(DataStore ds) {
        List<String> errors = new ArrayList<>();
        if (!Validator.isValidDate(ds.getColumn(0))) errors.add("Invalid Date");
        if (!Validator.isValidBookID(ds.getColumn(1))) errors.add("Invalid Book ID");
        if (!Validator.isValidStudentId(ds.getColumn(2))) errors.add("Invalid Student ID");
        try {
            int type = Integer.parseInt(ds.getColumn(3));
            if (type != 1 && type != 2) errors.add("Type must be 1 or 2");
        } catch (NumberFormatException e) {
            errors.add("Type must be a number");
        }
        return String.join("; ", errors);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
