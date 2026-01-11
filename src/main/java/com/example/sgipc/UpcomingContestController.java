package com.example.sgipc;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    @FXML
    public void initialize() {
        loadContests();
    }

    public void setUserRole(String role) {
        if ("Committee Member".equalsIgnoreCase(role)) {
            addContestButton.setVisible(true);
            addContestButton.setManaged(true);
        } else {
            addContestButton.setVisible(false);
            addContestButton.setManaged(false);
        }
    }

    private void loadContests() {
        List<Contest> contests = dbHandler.getAllContests();
        contestContainer.getChildren().clear();

        if (contests.isEmpty()) {
            Label noContest = new Label("No upcoming contests found.");
            contestContainer.getChildren().add(noContest);
        } else {
            for (Contest contest : contests) {
                addContestCard(contest);
            }
        }
    }

    private void addContestCard(Contest contest) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");

        Label title = new Label(contest.getTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #2c3e50;");

        Label details = new Label("Starts at: " + contest.getTime() + " | Duration: " + contest.getDuration() + " mins");
        details.setStyle("-fx-text-fill: #7f8c8d;");

        Label countdownLabel = new Label("Calculating...");
        countdownLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #e67e22;");

        Button linkButton = new Button("Go to Contest");
        linkButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
        linkButton.setMaxWidth(150);

        linkButton.setOnAction(e -> openBrowser(contest.getLink()));

        setupCountdown(contest.getTime(), countdownLabel);

        card.getChildren().addAll(title, details, countdownLabel, linkButton);
        contestContainer.getChildren().add(card);
    }

    private void openBrowser(String url) {
        if (url == null || url.isEmpty()) {
            System.out.println("No URL provided for this contest.");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCountdown(String timeStr, Label label) {
        try {
            LocalDateTime contestTime = LocalDateTime.parse(timeStr, formatter);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                LocalDateTime now = LocalDateTime.now();
                long diff = ChronoUnit.SECONDS.between(now, contestTime);

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
        } catch (Exception e) {
            label.setText("Date error");
        }
    }

    @FXML
    private void openAddContestWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_contest.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Contest");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadContests();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}