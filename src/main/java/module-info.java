module com.example.messengerproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdom2;
    requires java.desktop;
    requires java.sql;


    opens com.example.messengerproject to javafx.fxml;
    exports com.example.messengerproject;
}