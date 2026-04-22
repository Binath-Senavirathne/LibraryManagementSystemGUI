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

    @FXML
    public void initialize() {
        btnCalculate.setOnAction(e -> handleCalculate());
    }

    @FXML
    private void handleCalculate() {
        List<Book> books = FileManager.loadBooks("book.csv");

        if (books.isEmpty()) {
            showAlert("No Data", "No books found in book.csv. Load the Data file");
            lblTotalBooks.setText("0");
            lblTotalPrice.setText("0.00");
            lblAverageCost.setText("N/A");
            return;
        }

        int totalCopies = 0;
        double totalPrice = 0;
        for (Book b : books) {
            totalCopies += b.getCopies();
            totalPrice += (b.getPrice() * b.getCopies());
        }
        
        double averageCost = totalCopies > 0 ? totalPrice / totalCopies : 0.0;

        lblTotalBooks.setText(String.valueOf(totalCopies));
        lblTotalPrice.setText(String.format("%.2f", totalPrice));
        lblAverageCost.setText(String.format("%.2f", averageCost));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
