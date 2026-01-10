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

public class SignUpController {

    @FXML private TextField editTextText2, editTextText3, editTextTextEmailAddress2, editTextText4;
    @FXML private PasswordField editTextTextPassword2, editTextTextPassword3;
    @FXML private ComboBox<String> spinner;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void handleSignUp(ActionEvent event) {
        String name = editTextText2.getText().trim();
        String roll = editTextText3.getText().trim();
        String email = editTextTextEmailAddress2.getText().trim();
        String password = editTextTextPassword2.getText().trim();
        String confirmPassword = editTextTextPassword3.getText().trim();
        String codeforcesHandle = editTextText4.getText().trim();
        String memberType = spinner.getValue();

        if (name.isEmpty() || roll.isEmpty() || email.isEmpty() || password.isEmpty() ||
                confirmPassword.isEmpty() || codeforcesHandle.isEmpty() || memberType == null) {
            showAlert("Error", "All fields should be filled");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        if (dbHandler.insertUser(name, roll, email, password, codeforcesHandle, memberType)) {
            System.out.println("Sign up successful!");
            navigateToHome(event, memberType);
        } else {
            showAlert("Database Error", "Registration failed. Please check if the email or roll is already used.");
        }
    }

    @FXML
    public void backToLogin(ActionEvent event) {
        switchScene(event, "hello-view.fxml", "SGIPC Login");
    }

    private void navigateToHome(ActionEvent event, String role) {
        switchScene(event, "Home_page.fxml", "SGIPC Home");
    }

    private void switchScene(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Could not load FXML: " + fxmlFile);
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}