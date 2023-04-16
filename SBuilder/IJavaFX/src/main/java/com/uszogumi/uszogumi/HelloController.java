package com.uszogumi.uszogumi;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import org.controlsfx.control.action.Action;

public class HelloController {
    @FXML private Text textTitle;
    @FXML private Text textHeight;
    @FXML private TextField textFieldHeight;
    @FXML private Text textHeightUnit;
    @FXML private Text textAge;
    @FXML private TextField textFieldAge;
    @FXML private Text textAgeUnit;
    @FXML private Text textWeight;
    @FXML private TextField textFieldWeight;
    @FXML private Text textWeightUnit;
    @FXML private ToggleGroup tgNeme;
    @FXML private RadioButton radioButtonMale;
    @FXML private RadioButton radioButtonFemale;
    @FXML private TabPane tabPane;
    @FXML private Text textFreeVests;
    @FXML private Text textFreeVestsAmount;
    @FXML private Text textCreators;
    @FXML private Text textCreator1;
    @FXML private Text textCreator2;
    @FXML private Text textCreator3;


    public void onSubmit(ActionEvent event){
            //Adatok begyűjtése
        //int Height = Integer.parseInt(textHeight.getText());
        //int Age = Integer.parseInt(textAge.getText());
        //int Weight = Integer.parseInt(textWeight.getText());

        String Height=textHeight.getText();
        String Age = textAge.getText();
        String Weight = textWeight.getText();
        String Nem = radioButtonMale.isSelected() ? "Férfi" : "Nő";

            //Adatok ellenőrzése
        if (radioButtonMale.isSelected()){
            Nem="Férfi";
        }else if(radioButtonFemale.isSelected()){
            Nem="Nő";
        }

        if (Height.isEmpty() || Age.isEmpty() || Weight.isEmpty()){
            System.out.println("Hiányzó adatok!");
            return;
        }else{
        String data="Height: " +Height + ", Age: "+ Age + ", Weight: " +Weight + ", Nem: " + Nem;
            //Meg kell oldani hogy valahogy kiírja az adatokat...
        }

        double height =Double.parseDouble(Height);
        double weight= Double.parseDouble(Weight);

            //Database conn.
        //Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");

        // Adatok elmentése az adatbázisba
        //PreparedStatement stmt = conn.prepareStatement(
        // "INSERT INTO testadatok (magassag, testsuly, nem) VALUES (?, ?, ?)");
        //stmt.setDouble(1, height);
        // stmt.setDouble(2, weight);
        //stmt.setString(3, genderStr);
        //stmt.executeUpdate();

        // Adatbáziskapcsolat bezárása
        //conn.close();

        System.out.println("Adatok sikeresen elmentve az adatbázisba!");
    }





    public void initialize() {
        // TODO: Add initialization code here
    }

    @FXML
    private void handleSubmitButtonAction() {
        // TODO: Add handling code here
    }
}