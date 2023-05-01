package com.uszogumi.uszogumi;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    //SQL adatok
    String url = "jdbc:mysql://localhost:3306/test"; // adatbázis elérési útvonala
    String user = "root"; // adatbázis felhasználó neve
    String password = ""; // adatbázis jelszó (ha van)

    //Diagramm
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
    private void updateSzabadGyerekMellenyek() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(gyermek_szam) FROM AzFoglalasok WHERE gyermek_szam IS NOT NULL");

            if (rs.next()) {
                int count = rs.getInt(1);
                String count1 = Integer.toString(count);
                szabadGyerekMellenyek.setText(String.valueOf(count1));
            } else {
                szabadGyerekMellenyek.setText("Nincs eredmény.");
            }
        } catch (SQLException e) {
            szabadGyerekMellenyek.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

    private void updateSzabadFelnottMellenyek() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(felnott_szam) FROM AzFoglalasok WHERE felnott_szam IS NOT NULL");

            if (rs.next()) {
                int count = rs.getInt(1);
                String count1 = Integer.toString(count);
                szabadFelnottMellenyek.setText(String.valueOf(count1));
            } else {
                szabadFelnottMellenyek.setText("Nincs eredmény.");
            }
        } catch (SQLException e) {
            szabadFelnottMellenyek.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

    private void updateSzabadCsonak() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(csonak) FROM AzFoglalasok WHERE csonak IS NOT NULL");

            if (rs.next()) {
                int count = rs.getInt(1);
                String count1 = Integer.toString(count);
                szabadCsonak.setText(String.valueOf(count1));
            } else {
                szabadCsonak.setText("Nincs eredmény.");
            }
        } catch (SQLException e) {
            szabadCsonak.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

    private void updateSzabadGyerekGumi() { //Ezt még át kell alakítani + az sqlt is
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(uszogumi) FROM AzFoglalasok WHERE uszogumi IS NOT NULL");

            if (rs.next()) {
                int count = rs.getInt(1);
                String count1 = Integer.toString(count);
                szabadGyerekGumi.setText(String.valueOf(count1));
            } else {
                szabadGyerekGumi.setText("Nincs eredmény.");
            }
        } catch (SQLException e) {
            szabadGyerekGumi.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

    private void updateSzabadFelnottGumi() {
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(uszomelleny) FROM AzFoglalasok WHERE uszomelleny IS NOT NULL");

            if (rs.next()) {
                int count = rs.getInt(1);
                String count1 = Integer.toString(count);
                szabadFelnottGumi.setText(String.valueOf(count1));
            } else {
                szabadFelnottGumi.setText("Nincs eredmény.");
            }
        } catch (SQLException e) {
            szabadFelnottGumi.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

    //Csekkolas az adatbazisban
    public void handleCsekkolasGomb(ActionEvent event) {
        updateSzabadGyerekMellenyek();
        updateSzabadFelnottMellenyek();
        updateSzabadCsonak();
        updateSzabadGyerekGumi();
        updateSzabadFelnottGumi();
    }

    //Azonnali Foglalás Naptár nélkül...
    public void AzFoglalas(ActionEvent event) {

        String AzNev = azFogNev.getText();
        String felnottSzamInput = azFelnottSzama.getText();
        String gyermekSzamInput = azGyermekSzama.getText();

        //int AzFelnottSzam = Integer.parseInt(azFelnottSzama.getText());
        //int AzGyermekSzam = Integer.parseInt(azGyermekSzama.getText());

        int AzFelnottSzam;
        if (felnottSzamInput.matches("\\d+")) {
            AzFelnottSzam = Integer.parseInt(felnottSzamInput);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("A felnőtt szám mezőbe csak számokat lehet megadni!");
            alert.showAndWait();
            return;
        }

        int AzGyermekSzam;
        if (gyermekSzamInput == "" || gyermekSzamInput == null || gyermekSzamInput.isEmpty() || gyermekSzamInput.matches("\\d+")) {
            AzGyermekSzam = Integer.parseInt(gyermekSzamInput);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("A gyermek szám mezőbe csak számokat lehet megadni!");
            alert.showAndWait();
            return;
        }

        boolean AzCsonak = csonakCheckBox.isSelected();
        String AzfelszerelesMelleny = mellenyRadio.isSelected() ? "van" : "nincs";
        String AzfelszerelesGumi = gumiRadio.isSelected() ? "van" : "nincs";

        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String tableName = "AzFoglalasok";

        //Valamiért nem megy a postgresql-es téma, pedig minden jó elvileg. Máshol sikerül a connect... Biztos vmi Maven függőség miatt nem jóóóó...

        //String url = "jdbc:postgresql://alirdwit:xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG@rogue.db.elephantsql.com/alirdwit";
        //String user = "alirdwit";
        //String password = "xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG";

        String url = "jdbc:mysql://localhost:3306/test"; // adatbázis elérési útvonala
        //String url ="jdbc:postgresql://rogue.db.elephantsql.com:5432/alirdwit";
        String user = "root"; // adatbázis felhasználó neve
        //String user ="alirdwit";
        String password = ""; // adatbázis jelszó (ha van)
        //String password = "xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG";

        try {
            // Kapcsolódás az adatbázishoz
           // Connection conn = DriverManager.getConnection("jdbc:postgresql://rogue.db.elephantsql.com:5432/alirdwit", "alirdwit", "xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG");
            Connection conn = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO " + tableName + " (nev, felnott_szam, gyermek_szam, csonak, uszomelleny, uszogumi) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Csak nevet fogadunk el a Név mezőben
            if (AzNev.matches("[a-zA-Z ]+") && AzNev.split(" ").length >= 2) {
                pstmt.setString(1, AzNev);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("A név mező csak betűket fogad el!");
                alert.showAndWait();
                return;
            }
            pstmt.setInt(2, AzFelnottSzam);
            pstmt.setInt(3, AzGyermekSzam);
            pstmt.setBoolean(4, AzCsonak);
            pstmt.setString(5, AzfelszerelesMelleny);
            pstmt.setString(6, AzfelszerelesGumi);
            pstmt.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sikeres foglalás");
            alert.setHeaderText(null);
            alert.setContentText("A foglalás sikeresen rögzítve az adatbázisba!");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hiba történt az adatok mentése során!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    //Foglalás Törlése
    public void handleFoglalasDelGomb(ActionEvent event) {
        String nev = nevMezo.getText().trim();
        String datumString = datumMezo.getText().trim();

        // Név ellenőrzése
        if (!nev.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)+$")) {
            // Ha a név nem megfelelő formátumban van, hibaüzenetet jelenítünk meg
            hibaUzenet.setText("A név nem megfelelő formátumban van!");
            return;
        }

        // Dátum ellenőrzése
        LocalDate datum;
        try {
            datum = LocalDate.parse(datumString);
        } catch (DateTimeParseException e) {
            // Ha a dátum nem megfelelő formátumban van, hibaüzenetet jelenítünk meg
            hibaUzenet.setText("A dátum nem megfelelő formátumban van! (ÉÉÉÉ-HH-NN)");
            return;
        }

        // Adatbázisból törlés
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM AzFoglalasok WHERE nev = ? AND datum = ?");
            stmt.setString(1, nev);
            stmt.setDate(2, Date.valueOf(datum));
            int erintettSorokSzama = stmt.executeUpdate();
            if (erintettSorokSzama == 0) {
                hibaUzenet.setText("Nem található foglalás a megadott adatok alapján!");
            } else {
                hibaUzenet.setText("A foglalások sikeresen törölve lettek az adatbázisból.");
            }
        } catch (SQLException e) {
            hibaUzenet.setText("Hiba történt az adatbázis kapcsolat során!");
            e.printStackTrace();
        }
    }

}