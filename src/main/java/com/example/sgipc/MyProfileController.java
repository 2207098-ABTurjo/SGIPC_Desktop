package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class MyProfileController {

    @FXML private TextField nameField, rollField, emailField, handleField;
    @FXML private ComboBox<String> typeComboBox;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private String userRole; // To keep role for the back button

    @FXML
    public void initialize() {
        typeComboBox.getItems().addAll("General Member", "Committee Member");
        loadUserData();
    }

    private void loadUserData() {
        String email = UserSession.getEmail();
        if (email != null) {
            User user = dbHandler.getUserByEmail(email);
            if (user != null) {
                nameField.setText(user.name);
                rollField.setText(user.roll);
                emailField.setText(user.email);
                handleField.setText(user.codeforcesHandle);
                typeComboBox.setValue(user.memberType);
                this.userRole = user.memberType;
            }
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String name = nameField.getText().trim();
        String roll = rollField.getText().trim();
        String handle = handleField.getText().trim();
        String type = typeComboBox.getValue();
        String email = emailField.getText();

        if (name.isEmpty() || roll.isEmpty() || type == null) {
            showAlert("Error", "Please fill Name, Roll, and Member Type.");
            return;
        }

        if (dbHandler.updateUser(name, roll, handle, type, email)) {
            this.userRole = type;
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Profile Updated Successfully!");
            alert.showAndWait();
        } else {
            showAlert("Error", "Failed to update profile.");
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_page.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setRole(userRole);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        new Alert(Alert.AlertType.ERROR, content).show();
    }
}