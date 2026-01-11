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
import java.net.URL;

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
            navigateToHome(event, role);
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    private void navigateToHome(ActionEvent event, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_page.fxml"));
            Parent root = loader.load();

            HomeController homeController = loader.getController();
            homeController.setRole(role);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("FXML Load Error: Check if Home_page.fxml is in the correct folder.");
        }
    }

    @FXML
    public void signup(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Sign_up.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}