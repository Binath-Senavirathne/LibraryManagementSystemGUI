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
import javafx.stage.FileChooser;
import lms.Book;
import lms.FileManager;
import lms.Validator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchBooksController {

    @FXML
    private TextField searchTextField;

    @FXML
    private Button searchButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button exportButton;

    @FXML
    private TableView<Book> searchResultsTableView;

    @FXML
    private TableColumn<Book, String> bookIdColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> isbnColumn;

    @FXML
    private TableColumn<Book, Integer> availabilityColumn;

    private List<Book> allBooks = new ArrayList<>();

    @FXML
    public void initialize() {
        allBooks = FileManager.loadBooks("books.csv");

        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));

        searchButton.setOnAction(event -> handleSearch());
        clearButton.setOnAction(event -> handleClear());
        exportButton.setOnAction(event -> handleExport());

        searchTextField.setOnAction(event -> handleSearch());
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchTextField.getText().trim();

        if (searchTerm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Search", "Please enter a book title to search.");
            return;
        }

        String lowerSearchTerm = searchTerm.toLowerCase();
        boolean useWildcard = searchTerm.endsWith("*");
        String pattern = useWildcard ? searchTerm.substring(0, searchTerm.length() - 1).toLowerCase() : lowerSearchTerm;

        // Search books based on pattern
        List<Book> searchResults = new ArrayList<>();
        for (Book book : allBooks) {
            String bookTitle = book.getTitle().toLowerCase();

            if (useWildcard) {
                if (bookTitle.startsWith(pattern)) {
                    searchResults.add(book);
                }
            } else {
                if (bookTitle.contains(pattern)) {
                    searchResults.add(book);
                }
            }
        }

        if (searchResults.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No books found matching: " + searchTerm);
            searchResultsTableView.setItems(FXCollections.observableArrayList());
        } else {
            searchResultsTableView.setItems(FXCollections.observableArrayList(searchResults));
            showAlert(Alert.AlertType.INFORMATION, "Search Results", "Found " + searchResults.size() + " book(s).");
        }
    }

    @FXML
    private void handleClear() {
        searchTextField.clear();
        searchResultsTableView.setItems(FXCollections.observableArrayList());
    }

    @FXML
    private void handleExport() {
        ObservableList<Book> tableData = searchResultsTableView.getItems();

        if (tableData.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Data", "No search results to export.");
            return;
        }

        // Open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Search Results");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("search_results.txt");

        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try {
                StringBuilder content = new StringBuilder();
                content.append("BOOK SEARCH RESULTS\n");
                content.append("==================\n");
                content.append("Search Term: ").append(searchTextField.getText()).append("\n");
                content.append("Total Results: ").append(tableData.size()).append("\n");
                content.append("==================\n\n");

                content.append(String.format("%-10s %-25s %-20s %-15s %-10s\n",
                        "Book_ID", "Title", "Author", "ISBN", "Available"));
                content.append("=".repeat(90)).append("\n");

                for (Book book : tableData) {
                    content.append(String.format("%-10s %-25s %-20s %-15s %-10d\n",
                            book.getBookId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getIsbn(),
                            book.getAvailability()));
                }

                FileManager.exportToFile(file.getAbsolutePath(), content.toString());
                showAlert(Alert.AlertType.INFORMATION, "Export Successful",
                        "Search results exported to: " + file.getName());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Export Failed",
                        "Error exporting file: " + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
