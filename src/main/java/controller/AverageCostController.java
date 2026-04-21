package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import lms.Book;
import lms.FileManager;

import java.util.List;

public class AverageCostController {

    @FXML private Label lblTotalBooks;
    @FXML private Label lblTotalPrice;
    @FXML private Label lblAverageCost;

    @FXML private Button btnCalculate;

    @FXML private TableView<Book> booksTableView;

    @FXML
    public void initialize() {
        btnCalculate.setOnAction(e -> handleCalculate());
    }

    @FXML
    private void handleCalculate() {
        List<Book> books = FileManager.loadBooks("books.csv");

        if (books.isEmpty()) {
            showAlert("No Data", "No books found in books.csv. Please import book data first.");
            lblTotalBooks.setText("0");
            lblTotalPrice.setText("0.00");
            lblAverageCost.setText("N/A");
            return;
        }

        int totalBooks = books.size();
        double totalPrice = 0;
        for (Book b : books) {
            totalPrice += b.getPrice();
        }
        double averageCost = totalPrice / totalBooks;

        lblTotalBooks.setText(String.valueOf(totalBooks));
        lblTotalPrice.setText(String.format("%.2f", totalPrice));
        lblAverageCost.setText(String.format("%.2f", averageCost));

        booksTableView.setItems(FXCollections.observableArrayList(books));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
