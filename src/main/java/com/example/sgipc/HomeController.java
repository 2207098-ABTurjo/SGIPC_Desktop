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
    @FXML private Button my_profile_button;
    @FXML private Button logout_button;

    private String userRole;

    public void setRole(String role) {
        this.userRole = role;
    }

    @FXML
    public void handleUpcomingContests(ActionEvent event) {
        loadPage(event, "upcoming_contests.fxml", "Upcoming Contests");
    }

    @FXML
    public void handleUpcomingWorkshops(ActionEvent event) {
        loadPage(event, "upcoming_workshop.fxml", "Upcoming Workshops");
    }

    @FXML
    public void handleViewMembers(ActionEvent event) {
        loadPage(event, "member_list.fxml", "Member List");
    }

    @FXML
    public void handleMyProfile(ActionEvent event) {
        loadPage(event, "my_profile.fxml", "My Profile");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        UserSession.setEmail(null);
        loadPage(event, "hello-view.fxml", "Login");
    }

    private void loadPage(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UpcomingContestController) {
                ((UpcomingContestController) controller).setUserRole(userRole);
            } else if (controller instanceof UpcomingWorkshopController) {
                ((UpcomingWorkshopController) controller).setUserRole(userRole);
            } else if (controller instanceof MemberListController) {
                ((MemberListController) controller).setRole(userRole);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("SGIPC - " + title);

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}