package com.example.sgipc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class AddWorkshopController {
    @FXML private TextField titleInput;
    @FXML private DatePicker datePicker;
    @FXML private Spinner<Integer> hourSpinner;
    @FXML private Spinner<Integer> minuteSpinner;
    @FXML private TextField durationInput;

    private final DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    public void initialize() {
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }

    @FXML
    public void handleAddWorkshop(ActionEvent event) {
        try {
            String title = titleInput.getText().trim();
            String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String time = String.format("%02d:%02d", hourSpinner.getValue(), minuteSpinner.getValue());
            int duration = Integer.parseInt(durationInput.getText().trim());

            if (dbHandler.insertWorkshop(new Workshop(title, date + " " + time, duration))) {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please check your inputs.");
            alert.show();
        }
    }
}