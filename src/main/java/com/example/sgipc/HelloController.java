package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML private TextField editTextTextEmailAddress;
    @FXML private PasswordField editTextTextPassword;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void login(ActionEvent event) {
        String email = editTextTextEmailAddress.getText().trim();
        String password = editTextTextPassword.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        String role = dbHandler.validateLogin(email, password);

        if (role != null) {
            System.out.println("Login Successful! Role: " + role);
            switchScene(event, "Home_page.fxml", "SGIPC Home", role);
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    @FXML
    public void signup(ActionEvent event) {
        switchScene(event, "Sign_up.fxml", "Create Account", null);
    }

    private void switchScene(ActionEvent event, String fxmlFile, String title, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Could not load " + fxmlFile);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}