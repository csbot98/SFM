package com.uszogumi.uszogumi;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.control.Alert;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
//ez lesz az utolsó móka...


public class HelloController {
    //0501 menet
    @FXML
    private TextField azFelnottSzama;

    @FXML
    private TextField azFogNev;

    @FXML
    private Button azFoglalasGomb;

    @FXML
    private TextField azGyermekSzama;

    @FXML
    private Button csekkolasGomb;

    @FXML
    private Tab csekkolasTab;

    @FXML
    private CheckBox csonakCheckBox;

    @FXML
    private DatePicker foglalasDatePicker;

    @FXML
    private DatePicker foglalasDelDatePicker;

    @FXML
    private Button foglalasDelGomb;

    @FXML
    private TextField foglalasDelNev;

    @FXML
    private Button foglalasGomb;

    @FXML
    private DatePicker foglalasModDatePicker;

    @FXML
    private TextField foglalasModFo;

    @FXML
    private Button foglalasModGomb;

    @FXML
    private TextField foglalasModGyerek;

    @FXML
    private TextField foglalasModNev;

    @FXML
    private TextField foglaloFo;

    @FXML
    private TextField foglaloGyermek;

    @FXML
    private TextField foglaloNeve;

    @FXML
    private RadioButton gumiRadio;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis jokeCategory;

    @FXML
    private NumberAxis jokeNumber;

    @FXML
    private RadioButton mellenyRadio;

    @FXML
    private Text szabadCsonak;

    @FXML
    private Text szabadFelnottGumi;

    @FXML
    private Text szabadFelnottMellenyek;

    @FXML
    private Text szabadGyerekGumi;

    @FXML
    private Text szabadGyerekMellenyek;

    public void initialize() {
        // Az adatsorok létrehozása
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("A projektbe fektetett energia");
        series1.getData().add(new XYChart.Data<>("Csengeri", 100));
        series1.getData().add(new XYChart.Data<>("Ullmann", 20));
        series1.getData().add(new XYChart.Data<>("Babály", 5));
        series1.getData().add(new XYChart.Data<>("Forgács", 10));
        series1.getData().add(new XYChart.Data<>("Google", 60));

        //XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        //series2.setName("Második adatsor");
        //series2.getData().add(new XYChart.Data<>("Kategória 1", 12));
        //series2.getData().add(new XYChart.Data<>("Kategória 2", 34));
        //series2.getData().add(new XYChart.Data<>("Kategória 3", 56));
        //series2.getData().add(new XYChart.Data<>("Kategória 4", 78));
        //series2.getData().add(new XYChart.Data<>("Kategória 5", 90));

        //Lehetne akár pluszban egy olyat is csinálni, hogy összevetünk korábbi (általunk megadott) adatokat és abból egy diagrammot csinálni [bar, areaChart]. Teljesen felesleges, de mutatós.
        //Amik ki vannak kommentelve ebben a methódusban, az azért van hogy lássátok, lehetne akár több adatot is összevetni.

        // Az adatsorok hozzáadása az AreaChart-hoz
        //barChart.getData().addAll(series1, series2);
        barChart.getData().addAll(series1);
    }

    public void AzFoglalas(ActionEvent event) {
        // itt lehet kezelni a foglalás gombra kattintást | például kiolvasni az adatokat a mezőkből és elvégezni a szükséges műveleteket
        String AzNev= azFogNev.getText();
        int AzFelnottSzama= Integer.parseInt(azFelnottSzama.getText());
        int AzGyermekSzama= Integer.parseInt(azGyermekSzama.getText());
        boolean Azcsonak = csonakCheckBox.isSelected();
        //String Azfelszereles = mellenyRadio.isSelected() ? "úszómellény" : "úszógumi";
        boolean AzfelszerelesMelleny = mellenyRadio.isSelected();
        boolean AzfelszerelesGumi = gumiRadio.isSelected();

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = AzNev.replaceAll(" ", "_") + "_" + currentTime + ".csv";

        Path path = Paths.get(fileName);

        try (PrintWriter writer = new PrintWriter(new FileWriter(path.toFile(), StandardCharsets.UTF_8))) {
            writer.printf("Név: %s%n", AzNev);
            writer.printf("Felnőttek száma: %d%n", AzFelnottSzama);
            writer.printf("Gyerekek száma: %d%n", AzGyermekSzama);
            writer.printf("Csónak bérelve: %s%n", Azcsonak ? "igen" : "nem");
            writer.printf("Úszómellény: %s%n", AzfelszerelesMelleny);
            writer.printf("Úszógumi: %s%n", AzfelszerelesGumi);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sikeres foglalás");
            alert.setHeaderText(null);
            alert.setContentText("A foglalás sikeresen rögzítve a következő fájlba: " + path.toAbsolutePath().toString());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hiba történt a fájl írása során!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}

//@FXML
//    public void initialize() {
//        foglalasGomb.setOnAction(e -> foglalas());
//    }
//
//    private void foglalas() {
//        String nev = nevField.getText();
//        int fofoglalas = Integer.parseInt(fofoglalasField.getText());
//        int gyermekfoglalas = Integer.parseInt(gyermekfoglalasField.getText());
//        LocalDate datum = datePicker.getValue();
//        // TODO: adatok eltárolása az adatbázisban
//        System.out.println("Foglalás rögzítve: " + nev + " - " + datum + " - " + fofoglalas + " fő - " + gyermekfoglalas + " gyermek");
//    }