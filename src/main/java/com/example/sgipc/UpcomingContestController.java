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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class UpcomingContestController {

    @FXML private VBox contestContainer;
    @FXML private Button addContestButton;

    private final DatabaseHandler dbHandler = new DatabaseHandler();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private String userRole;

    @FXML
    public void initialize() {
        loadContests();
    }

    public void setUserRole(String role) {
        this.userRole = role;
        boolean isCommittee = "Committee Member".equalsIgnoreCase(role);
        addContestButton.setVisible(isCommittee);
        addContestButton.setManaged(isCommittee);
    }

    private void loadContests() {
        List<Contest> contests = dbHandler.getAllContests();
        contestContainer.getChildren().clear();
        for (Contest contest : contests) {
            addContestCard(contest);
        }
    }

    private void addContestCard(Contest contest) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");


        Label title = new Label(contest.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #212121;");

        Label details = new Label("Starts: " + contest.getTime() + " | Duration: " + contest.getDuration() + " min");
        details.setStyle("-fx-text-fill: #555555; -fx-font-size: 14px;");

        Label countdown = new Label("Calculating...");
        countdown.setStyle("-fx-font-weight: bold; -fx-text-fill: #1976D2; -fx-font-size: 14px;");
        setupCountdown(contest.getTime(), countdown);

        Button linkBtn = new Button("Go to Contest");
        linkBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        linkBtn.setOnAction(e -> openBrowser(contest.getLink()));

        card.getChildren().addAll(title, details, countdown, linkBtn);
        contestContainer.getChildren().add(card);
    }

    private void setupCountdown(String timeStr, Label label) {
        try {
            LocalDateTime target = LocalDateTime.parse(timeStr, formatter);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                long diff = ChronoUnit.SECONDS.between(LocalDateTime.now(), target);
                if (diff > 0) {
                    long hours = diff / 3600;
                    long minutes = (diff % 3600) / 60;
                    long seconds = diff % 60;
                    label.setText(String.format("Starts in: %02d:%02d:%02d", hours, minutes, seconds));
                } else {
                    label.setText("Contest Live / Ended");
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } catch (Exception e) { label.setText("Date Error"); }
    }

    private void openBrowser(String url) {
        try {
            if (url != null && !url.isEmpty()) {
                if (!url.startsWith("http")) url = "https://" + url;
                Desktop.getDesktop().browse(new URI(url));
            }
        } catch (Exception e) { e.printStackTrace(); }
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
    private void openAddContestWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_contest.fxml"));
        Stage stage = new Stage();
        stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));
        stage.showAndWait();
        loadContests();
    }
}