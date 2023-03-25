module com.example.demo_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires com.oracle.database.jdbc;


    opens com.etisalat.RAproject;
    exports com.etisalat.RAproject;
}