package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class MemberListController {

    @FXML private VBox membersContainer;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private String userRole;

    public void setRole(String role) {
        this.userRole = role;
    }

    @FXML
    public void initialize() {
        displayMembers();
    }

    private void displayMembers() {
        membersContainer.getChildren().clear();
        List<Member> members = dbHandler.getAllMembers();

        for (Member member : members) {
            VBox card = new VBox(5);
            card.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #eee; -fx-border-radius: 5;");

            Label nameLabel = new Label(member.getName());
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #212121;");

            Label designationLabel = new Label(member.getDesignation());
            designationLabel.setStyle("-fx-text-fill: #757575; -fx-font-size: 13px;");

            card.getChildren().addAll(nameLabel, designationLabel);
            membersContainer.getChildren().add(card);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home_page.fxml"));
            Parent root = loader.load();
            HomeController controller = loader.getController();
            controller.setRole(userRole);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}