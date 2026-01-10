package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HomeController {

    private String userRole;

    public void setUserRole(String role) {
        this.userRole = role;
    }

    @FXML
    public void handleUpcomingContests(ActionEvent event) {
        loadScene(event, "UpcomingContests.fxml", "Upcoming Contests");
    }

    @FXML
    public void handleUpcomingWorkshops(ActionEvent event) {
        loadScene(event, "UpcomingWorkshops.fxml", "Upcoming Workshops");
    }

    @FXML
    public void handleViewMembers(ActionEvent event) {
        loadScene(event, "MemberList.fxml", "Member List");
    }

    @FXML
    public void handleViewCommittee(ActionEvent event) {
        loadScene(event, "CommitteeList.fxml", "Committee Members");
    }

    @FXML
    public void handleMyProfile(ActionEvent event) {
        loadScene(event, "MyProfile.fxml", "My Profile");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        loadScene(event, "hello-view.fxml", "SGIPC Login");
    }

    private void loadScene(ActionEvent event, String fxmlFile, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlFile);
            e.printStackTrace();
        }
    }
}