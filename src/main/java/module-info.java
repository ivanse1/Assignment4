module com.aplication.task4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.controlsfx.controls;


    opens com.aplication.task4 to javafx.fxml;
    exports com.aplication.task4;
}