module com.assignment4 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.assignment4 to javafx.fxml;
    opens com.a4basics to javafx.fxml;
    exports com.assignment4;
    exports com.a4basics;
}