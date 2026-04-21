module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Export the package containing MainApplication
    exports app;
    exports controller;
    exports lms;

    opens controller to javafx.fxml;
    opens app to javafx.fxml;
}
