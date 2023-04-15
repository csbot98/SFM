package com.uszogumi.uszogumi;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField heightField;

    @FXML
    private TextField weightField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField genderField;

    @FXML
    private Button rentButton;

    @FXML
    private Label resultLabel;

    public void rent() {
        // Ide jön a bérlési logika
        resultLabel.setText("Az úszógumi bérlése sikeres volt!");
    }
}