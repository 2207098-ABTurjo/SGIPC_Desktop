package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class AddContestController {

    @FXML private TextField contestTitleInput;
    @FXML private DatePicker contestDatePicker;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private TextField contestDurationInput;
    @FXML private TextField contestLinkInput;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12);
        hourSpinner.setValueFactory(hourFactory);

        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteFactory);
    }

    @FXML
    public void handleAddContest(ActionEvent event) {
        String title = contestTitleInput.getText().trim();
        String durationStr = contestDurationInput.getText().trim();
        String link = contestLinkInput.getText().trim();

        if (title.isEmpty() || contestDatePicker.getValue() == null || durationStr.isEmpty()) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        try {
            String dateStr = contestDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String timeStr = String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());

            String dateTime = dateStr + " " + timeStr;
            int duration = Integer.parseInt(durationStr);

            Contest newContest = new Contest(title, dateTime, duration, link);

            if (dbHandler.insertContest(newContest)) {
                closeWindow(event);
            } else {
                showAlert("Error", "Failed to save contest to database.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Duration must be a number.");
        }
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}