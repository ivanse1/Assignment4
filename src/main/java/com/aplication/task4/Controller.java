package com.aplication.task4;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

public class Controller {
    @FXML
    private TextField idTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField cityTextField;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public void initialize() {
        // Connect to MongoDB
        try {
            mongoClient = MongoClients.create("mongodb://localhost:27017");
            database = mongoClient.getDatabase("CRUM");
            collection = database.getCollection("documents");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e);
        }
    }
    @FXML
    public void onAddButtonClick() {
        String name = nameTextField.getText();
        int age = Integer.parseInt(ageTextField.getText());
        String city = cityTextField.getText();

        Document document = new Document()
                .append("name", name)
                .append("age", age)
                .append("city", city);
        collection.insertOne(document);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Document added successfully");
        alert.showAndWait();
        clearFields();
    }

    @FXML
    public void onReadButtonClick() {
        String id = idTextField.getText();
        Document document = collection.find(Filters.eq("_id", new ObjectId(id))).first();

        if (document != null) {
            nameTextField.setText(document.getString("name"));
            ageTextField.setText(String.valueOf(document.getInteger("age")));
            cityTextField.setText(document.getString("city"));
        } else {
            clearFields();
        }

        // Create a custom dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setTitle("Message");

        TextArea textArea = new TextArea();
        textArea.setText(document.toJson());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        VBox vbox = new VBox(new Label("Document read successfully"), textArea);
        Scene scene = new Scene(vbox);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    @FXML
    public void onUpdateButtonClick() {
        String id = idTextField.getText();
        String name = nameTextField.getText();
        int age = Integer.parseInt(ageTextField.getText());
        String city = cityTextField.getText();

        Document filter = new Document("_id", new ObjectId(id));
        Document update = new Document("$set",
                new Document("name", name)
                        .append("age", age)
                        .append("city", city));
        collection.updateOne(filter, update);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Document updated successfully");
        alert.showAndWait();
        clearFields();
    }

    @FXML
    public void onDeleteButtonClick() {
        String id = idTextField.getText();
        Document filter = new Document("_id", new ObjectId(id));
        collection.deleteOne(filter);

        clearFields();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText("Document deleted successfully");
        alert.showAndWait();
        clearFields();
    }

    private void clearFields() {
        idTextField.clear();
        nameTextField.clear();
        ageTextField.clear();
        cityTextField.clear();
    }
}