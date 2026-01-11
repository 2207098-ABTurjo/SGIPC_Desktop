package com.example.sgipc;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UpcomingWorkshopController {
    @FXML private VBox workshopContainer;
    @FXML private Button addWorkshopButton;
    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private String userRole;

    @FXML public void initialize() { loadWorkshops(); }

    public void setUserRole(String role) {
        this.userRole = role;
        boolean isCommittee = "Committee Member".equalsIgnoreCase(role);
        addWorkshopButton.setVisible(isCommittee);
        addWorkshopButton.setManaged(isCommittee);
    }

    private void loadWorkshops() {
        workshopContainer.getChildren().clear();
        List<Workshop> workshops = dbHandler.getAllWorkshops();
        for (Workshop w : workshops) {
            VBox card = new VBox(8);
            card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");

            Label title = new Label(w.getTitle());
            title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #212121;");

            Label time = new Label("Starts: " + w.getTime() + " (" + w.getDuration() + " min)");
            time.setStyle("-fx-text-fill: #555555; -fx-font-size: 14px;");

            Label countdown = new Label("Calculating...");
            countdown.setStyle("-fx-font-weight: bold; -fx-text-fill: #7B1FA2; -fx-font-size: 14px;");
            setupCountdown(w.getTime(), countdown);

            card.getChildren().addAll(title, time, countdown);
            workshopContainer.getChildren().add(card);
        }
    }

    private void setupCountdown(String timeStr, Label label) {
        try {
            LocalDateTime target = LocalDateTime.parse(timeStr, formatter);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                long diff = ChronoUnit.SECONDS.between(LocalDateTime.now(), target);
                if (diff > 0) {
                    label.setText(String.format("Starts in: %02d:%02d:%02d", diff/3600, (diff%3600)/60, diff%60));
                } else {
                    label.setText("Workshop Started/Ended");
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } catch (Exception e) { label.setText("Date Error"); }
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
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void openAddWorkshopWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_workshop.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        stage.showAndWait();
        loadWorkshops();
    }
}