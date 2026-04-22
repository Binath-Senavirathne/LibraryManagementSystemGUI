module app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports app;
    exports controller;
    exports lms;

    opens controller to javafx.fxml;
    opens app to javafx.fxml;
    opens lms to javafx.fxml;
}
