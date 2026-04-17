module org.example.librarymanagementsystemjava {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.librarymanagementsystemjava to javafx.fxml;
    exports org.example.librarymanagementsystemjava;
}