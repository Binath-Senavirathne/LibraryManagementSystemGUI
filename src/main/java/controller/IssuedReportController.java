package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lms.FileManager;
import lms.Transaction;
import lms.Validator;

import java.util.ArrayList;
import java.util.List;

public class IssuedReportController {

    @FXML
    private TextField dateTextField;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button clearButton;

    @FXML
    private TableView<Transaction> reportTableView;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> bookIdColumn;

    @FXML
    private TableColumn<Transaction, String> studentIdColumn;

    private List<Transaction> allTransactions = new ArrayList<>();

    @FXML
    public void initialize() {
        allTransactions = FileManager.loadTransactions("transactions.csv");

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));

        generateReportButton.setOnAction(event -> handleGenerateReport());
        clearButton.setOnAction(event -> handleClear());

        dateTextField.setOnAction(event -> handleGenerateReport());
    }

    @FXML
    private void handleGenerateReport() {
        String dateInput = dateTextField.getText().trim();

        if (dateInput.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Date", "Please enter a date in DD/MM/YYYY format.");
            return;
        }

        if (!Validator.isValidDate(dateInput)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Date Format",
                    "Please enter date in DD/MM/YYYY format. Example: 27/04/2026");
            return;
        }

        List<Transaction> issuedBooks = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getDate().equals(dateInput) && transaction.getType() == 1) {
                issuedBooks.add(transaction);
            }
        }

        if (issuedBooks.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Issues",
                    "No books were issued on " + dateInput);
            reportTableView.setItems(FXCollections.observableArrayList());
        } else {
            reportTableView.setItems(FXCollections.observableArrayList(issuedBooks));
            showAlert(Alert.AlertType.INFORMATION, "Report Generated",
                    "Found" + issuedBooks.size() + " book(s) : issued on " + dateInput);
        }
    }

    @FXML
    private void handleClear() {
        dateTextField.clear();
        reportTableView.setItems(FXCollections.observableArrayList());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
