package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML private Button upcoming_contest_button;
    @FXML private Button upcoming_workshop_button;
    @FXML private Button view_members_button;
    @FXML private Button committee_button;
    @FXML private Button my_profile_button;
    @FXML private Button logout_button;

    private String userRole;

    public void setRole(String role) {
        this.userRole = role;
        System.out.println("Logged in as: " + userRole);
    }

    @FXML
    public void handleUpcomingContests(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("upcoming_contests.fxml"));
            Parent root = loader.load();

            UpcomingContestController contestController = loader.getController();
            contestController.setUserRole(userRole);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("SGIPC - Upcoming Contests");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error: Could not load upcoming_contests.fxml");
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("SGIPC - Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML public void handleUpcomingWorkshops(ActionEvent event) { System.out.println("Workshops clicked"); }
    @FXML public void handleViewMembers(ActionEvent event) { System.out.println("Members clicked"); }
    @FXML public void handleViewCommittee(ActionEvent event) { System.out.println("Committee clicked"); }
    @FXML public void handleMyProfile(ActionEvent event) { System.out.println("Profile clicked"); }
}