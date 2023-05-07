package com.uszogumi.uszogumi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;	//X
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class HelloController {

    @FXML
    private TextField azFelnottGumi;

    @FXML
    private TextField azFelnottMelleny;

    @FXML
    private Button azFoglalasGomb;

    @FXML
    private TextField azFoglaloNev;

    @FXML
    private CheckBox azGyerekFelCheckBox;

    @FXML
    private TextField azGyerekGumi;

    @FXML
    private TextField azGyerekMelleny;

    @FXML
    private BarChart<?, ?> barChart;

    @FXML
    private Button csekkolasGomb;

    @FXML
    private CheckBox csonakCheckBox;

    @FXML
    private DatePicker foglalasDatePicker;

    @FXML
    private DatePicker foglalasDelDatePicker;

    @FXML
    private Button foglalasDelGomb;

    @FXML
    private TextField foglalasFelModGumi;

    @FXML
    private TextField foglalasFelModMelleny;

    @FXML
    private CheckBox foglalasFelnottModCheckBox;

    @FXML
    private Button foglalasGomb;

    @FXML
    private CheckBox foglalasGyerekCheckBox;

    @FXML
    private CheckBox foglalasGyerekModCheckBox;

    @FXML
    private Group foglalasHiddenGyerekGroup;

    @FXML
    private DatePicker foglalasModDatePicker;

    @FXML
    private Button foglalasModGomb;

    @FXML
    private Group foglalasModHiddenFelnottGroup;

    @FXML
    private Group foglalasModHiddenGyerekGroup;

    @FXML
    private TextField foglaloDelNeve;

    @FXML
    private TextField foglaloFelnottGumi;

    @FXML
    private TextField foglaloFelnottMelleny;

    @FXML
    private TextField foglaloGyerekGumi;

    @FXML
    private TextField foglaloGyerekMelleny;

    @FXML
    private TextField foglaloModGyGumi;

    @FXML
    private TextField foglaloModGyMelleny;

    @FXML
    private TextField foglaloModNeve;

    @FXML
    private TextField foglaloNeve;

    @FXML
    private VBox hiddenAzGyerekVBox;

    @FXML
    private Label hiddenMeglepetes;

    @FXML
    private Button leirasMeglepetes;

    @FXML
    private Text szabadCsonak;

    @FXML
    public Text szabadFelnottGumi;

    @FXML
    private Text szabadFelnottMellenyek;

    @FXML
    private Text szabadGyerekGumi;

    @FXML
    private Text szabadGyerekMellenyek;


    //SQL adatok (bár nem ajánlott ilyen nyíltan érzékeny adatokat kezelni, ettől kérem tekintsenek el!)
    String url = "jdbc:mysql://localhost:3306/test"; // adatbázis elérési útvonala
    String user = "root"; // adatbázis felhasználó neve
    String password = ""; // adatbázis jelszó (ha van)

    //String url ="jdbc:postgresql://rogue.db.elephantsql.com:5432/alirdwit";
    //String user ="alirdwit";
    //String password = "xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG";

    //Event kezelések kezdete
        //CheckBox-ok láthatósága
    @FXML
    void azGyerekFelCheckBoxOn(ActionEvent event) {
        if (azGyerekFelCheckBox.isSelected()) {
            hiddenAzGyerekVBox.setVisible(true);
        } else {
            hiddenAzGyerekVBox.setVisible(false);
        }
    }

    @FXML
    void foglalasFelnottModCheckBoxOn(ActionEvent event) {
        if (foglalasFelnottModCheckBox.isSelected()) {
            foglalasModHiddenFelnottGroup.setVisible(true);
        } else {
            foglalasModHiddenFelnottGroup.setVisible(false);
        }
    }

    @FXML
    void foglalasGyerekCheckBoxOn(ActionEvent event) {
        if (foglalasGyerekCheckBox.isSelected()) {
            foglalasHiddenGyerekGroup.setVisible(true);
        } else {
            foglalasHiddenGyerekGroup.setVisible(false);
        }
    }

    @FXML
    void foglalasGyerekModCheckBoxOn(ActionEvent event) {
        if (foglalasGyerekModCheckBox.isSelected()) {
            foglalasModHiddenGyerekGroup.setVisible(true);
        } else {
            foglalasModHiddenGyerekGroup.setVisible(false);
        }
    }

    @FXML
    public void initialize() {
        handleCsekkolasGomb(null);

    }

    //Szabad felszerelések kezdete
        private void updateSzabadGyerekMellenyek() {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("WITH felszereles_osszesites AS (\n" +
                        "\t\tSELECT \n" +
                        "\t\tSUM(CASE WHEN nev = 'gyerek_melleny' THEN darab ELSE 0 END) AS felszereles_gyerek_melleny\n" +
                        "\t\tFROM felszereles),\n" +
                        "foglalasok_osszesites AS (\n" +
                        "\t\tSELECT\n" +
                        "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS foglalasok_gyerek_melleny\n" +
                        "\t\tFROM foglalasok),\n" +
                        "jovobeni_foglalasok_osszesites AS (\n" +
                        "\t\tSELECT\n" +
                        "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS jovobeni_foglalasok_gyerek_melleny\n" +
                        "\t\tFROM jovobeni_foglalasok\n" +
                        ")\n" +
                        "SELECT\n" +
                        "\t-- gyerek_melleny:\n" +
                        "\t--\tfelszereles_osszesites.felszereles_gyerek_melleny,\n" +
                        "\t--\tfoglalasok_osszesites.foglalasok_gyerek_melleny,\n" +
                        "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny,\n" +
                        "\t\tfelszereles_osszesites.felszereles_gyerek_melleny - foglalasok_osszesites.foglalasok_gyerek_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny AS elerheto_gyerek_melleny\n" +
                        "FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;");

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
            ResultSet rs = stmt.executeQuery("WITH felszereles_osszesites AS (\n" +
                    "\t\tSELECT \n" +
                    "\t\tSUM(CASE WHEN nev = 'felnott_melleny' THEN darab ELSE 0 END) AS felszereles_felnott_melleny\n" +
                    "\t\tFROM felszereles),\n" +
                    "foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS foglalasok_felnott_melleny\n" +
                    "\t\tFROM foglalasok),\n" +
                    "jovobeni_foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS jovobeni_foglalasok_felnott_melleny\n" +
                    "\t\tFROM jovobeni_foglalasok\n" +
                    ")\n" +
                    "SELECT\n" +
                    "\t-- felnott_melleny:\n" +
                    "\t--\tfelszereles_osszesites.felszereles_felnott_melleny,\n" +
                    "\t--\tfoglalasok_osszesites.foglalasok_felnott_melleny,\n" +
                    "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny,\n" +
                    "\t\tfelszereles_osszesites.felszereles_felnott_melleny - foglalasok_osszesites.foglalasok_felnott_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny AS elerheto_felnott_melleny\n" +
                    "FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;");

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
            ResultSet rs = stmt.executeQuery("WITH felszereles_osszesites AS (\n" +
                    " SELECT\n" +
                    " SUM(CASE WHEN nev = 'csonak' THEN darab ELSE 0 END) AS felszereles_csonak\n" +
                    " FROM felszereles\n" +
                    "),\n" +
                    "foglalasok_osszesites AS (\n" +
                    " SELECT\n" +
                    " SUM(csonak) AS foglalasok_csonak\n" +
                    " FROM foglalasok\n" +
                    " WHERE aktiv = TRUE\n" +
                    ")\n" +
                    "SELECT\n" +
                    " felszereles_csonak - foglalasok_csonak AS elerheto_csonak\n" +
                    "FROM felszereles_osszesites, foglalasok_osszesites\n");

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
            ResultSet rs = stmt.executeQuery("WITH felszereles_osszesites AS (\n" +
                    "\t\tSELECT \n" +
                    "\t\tSUM(CASE WHEN nev = 'gyerek_gumi' THEN darab ELSE 0 END) AS felszereles_gyerek_gumi\n" +
                    "\t\tFROM felszereles),\n" +
                    "foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS foglalasok_gyerek_gumi\n" +
                    "\t\tFROM foglalasok),\n" +
                    "jovobeni_foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS jovobeni_foglalasok_gyerek_gumi\n" +
                    "\t\tFROM jovobeni_foglalasok\n" +
                    ")\n" +
                    "SELECT\n" +
                    "\t-- gyerek_gumi:\n" +
                    "\t--\tfelszereles_osszesites.felszereles_gyerek_gumi,\n" +
                    "\t--\tfoglalasok_osszesites.foglalasok_gyerek_gumi,\n" +
                    "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi,\n" +
                    "\t\tfelszereles_osszesites.felszereles_gyerek_gumi - foglalasok_osszesites.foglalasok_gyerek_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi AS elerheto_gyerek_gumi\n" +
                    "FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;\n");

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
            ResultSet rs = stmt.executeQuery("WITH felszereles_osszesites AS (\n" +
                    "\t\tSELECT \n" +
                    "\t\tSUM(CASE WHEN nev = 'felnott_gumi' THEN darab ELSE 0 END) AS felszereles_felnott_gumi\n" +
                    "\t\tFROM felszereles),\n" +
                    "foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS foglalasok_felnott_gumi\n" +
                    "\t\tFROM foglalasok),\n" +
                    "jovobeni_foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS jovobeni_foglalasok_felnott_gumi\n" +
                    "\t\tFROM jovobeni_foglalasok\n" +
                    ")\n" +
                    "SELECT\n" +
                    "\t-- felnott_gumi:\n" +
                    "\t--\tfelszereles_osszesites.felszereles_felnott_gumi,\n" +
                    "\t--\tfoglalasok_osszesites.foglalasok_felnott_gumi,\n" +
                    "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi,\n" +
                    "\t\tfelszereles_osszesites.felszereles_felnott_gumi - foglalasok_osszesites.foglalasok_felnott_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi AS elerheto_felnott_gumi\n" +
                    "FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;");

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

        //Itt mondom meg, hogy ha 0 elérhető felszerelés van, akkor legyen disabled a felhasználó számára a foglalás bizonyos része.
        String szabadFelnottGumiText = szabadFelnottGumi.getText().trim();
        if (Integer.parseInt(szabadFelnottGumiText) <= 0) {
            azFelnottGumi.setDisable(true);
        } else {
            azFelnottGumi.setDisable(false);
        }

        String szabadFelnottMellenyText = szabadFelnottMellenyek.getText().trim();
        if (Integer.parseInt(szabadFelnottMellenyText) <= 0) {
            azFelnottMelleny.setDisable(true);
        } else {
            azFelnottMelleny.setDisable(false);
        }

        String szabadGyerekGumiText = szabadGyerekGumi.getText().trim();
        if (Integer.parseInt(szabadGyerekGumiText) <= 0) {
            azGyerekGumi.setDisable(true);
        } else {
            azGyerekGumi.setDisable(false);
        }

        String szabadGyerekMellenyText = szabadGyerekMellenyek.getText().trim();
        if (Integer.parseInt(szabadGyerekMellenyText) <= 0) {
            azGyerekMelleny.setDisable(true);
        } else {
            azGyerekMelleny.setDisable(false);
        }

        String csonakText = szabadCsonak.getText();
        if (Integer.parseInt(String.valueOf(csonakText)) <= 0) {
            csonakCheckBox.setDisable(true);
        } else {
            csonakCheckBox.setDisable(false);
        }
    }
        //Szabad felszerelések vége

        //Csekkolás Tab-on az azonnali foglalás
        ///////////////////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void handleAzFoglalasGomb(ActionEvent event) {
        String AzFoglaloNev = azFoglaloNev.getText();
        if (AzFoglaloNev.matches(".*\\d+.*") || AzFoglaloNev.split("\\s").length == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("A név mező csak betűket fogad el!");
            alert.showAndWait();
            return;
        }

        int AzFelnottGumi = 0;
        int AzFelnottMelleny = 0;

        if (!azFelnottMelleny.isDisabled()) {
            try {
                AzFelnottMelleny = Integer.parseInt(azFelnottMelleny.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("Kérem a darabszámot írja be a (felnőtt-mellény) felszerelés mezőkbe!");
                alert.showAndWait();
                return;
            }
        }

        if (!azFelnottGumi.isDisabled()) {
            try {
                AzFelnottGumi = Integer.parseInt(azFelnottGumi.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("Kérem a darabszámot írja be a (felnőtt-gumi) felszerelés mezőkbe!");
                alert.showAndWait();
                return;
            }
        }

        boolean AzGyerekFelCheckBox = azGyerekFelCheckBox.isSelected();

        int AzGyerekGumi = 0;
        int AzGyerekMelleny = 0;

        //Ezt ha visszarakom, akkor kötelező számot írni, minden gyerek mezőbe  || ezt így hagyom a felnőtt meg marad olyan, hogy lássák mind a két módon.
        // if (AzGyerekFelCheckBox && !azGyerekMelleny.isDisabled()) {
        //   try {
        //        AzGyerekMelleny = Integer.parseInt(azGyerekMelleny.getText());
        //   } catch (NumberFormatException e) {
        //      Alert alert = new Alert(Alert.AlertType.ERROR);
        //      alert.setTitle("Hiba");
        //      alert.setHeaderText(null);
        //       alert.setContentText("Kérem a darabszámot írja be a (gyerek-mellény) felszerelés mezőkbe!");
        //      alert.showAndWait();
        //      return;
        //  }
        //}

        //Ezt ha visszarakom, akkor kötelező számot írni, minden gyerek mezőbe  || ezt így hagyom a felnőtt meg marad olyan, hogy lássák mind a két módon.
        //if (AzGyerekFelCheckBox && !azGyerekGumi.isDisabled()) {
        //  try {
        //      AzGyerekGumi = Integer.parseInt(azGyerekGumi.getText());
        //  } catch (NumberFormatException e) {
        //      Alert alert = new Alert(Alert.AlertType.ERROR);
        //      alert.setTitle("Hiba");
        //      alert.setHeaderText(null);
        //      alert.setContentText("Kérem a darabszámot írja be a (gyerek-gumi) felszerelés mezőkbe!");
        //    alert.showAndWait();
        //      return;
        //  }
        //}

        if (AzGyerekFelCheckBox && !azGyerekGumi.isDisabled() || !azGyerekMelleny.isDisabled()) {
            String azGyerekGumiText = azGyerekGumi.getText().trim();
            String azGyerekMellenyText = azGyerekMelleny.getText().trim();

            if (AzGyerekFelCheckBox && azGyerekGumiText.isEmpty() && azGyerekMellenyText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("Nem hagyhatja üresen mindekét gyerekmezőt, ha már bepipálta!");
                alert.showAndWait();
                return;
            }
            if (AzGyerekFelCheckBox && !azGyerekGumiText.isEmpty() && azGyerekMellenyText.isEmpty()) {
                AzGyerekGumi = Integer.parseInt(azGyerekGumiText);
            }
            if (AzGyerekFelCheckBox && azGyerekGumiText.isEmpty() && !azGyerekMellenyText.isEmpty()) {
                AzGyerekMelleny = Integer.parseInt(azGyerekMellenyText);
            } else {
                AzGyerekGumi = 0;
                AzGyerekMelleny = 0;
            }

        }

        //Ha minden üres és be van pipálva a gyerek mező, akkor error       //ez nem tudom hogy mükdöik vagy sem
        if (AzGyerekFelCheckBox && !azGyerekGumi.isDisabled() && !azGyerekMelleny.isDisabled() && azFelnottMelleny.isDisabled() && azFelnottGumi.isDisabled()) {
            try {
                AzGyerekGumi = Integer.parseInt(azGyerekGumi.getText());
                AzGyerekMelleny=Integer.parseInt(azGyerekMelleny.getText());
                AzFelnottGumi = Integer.parseInt(azFelnottGumi.getText());
                AzFelnottMelleny = Integer.parseInt(azFelnottMelleny.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("Full üres adatot, nem küldhet be!");
                alert.showAndWait();
                return;
            }
        }
        //teszt: itt nem kötelező számot megadni, cserébe automatikusan 0, lesz a foglalandó felszerelés.
        if (AzGyerekFelCheckBox && !azGyerekMelleny.isDisabled()) {
            try {
                AzGyerekMelleny = Integer.parseInt(azGyerekMelleny.getText());
            } catch (NumberFormatException e) {
                AzGyerekMelleny = 0;
            }
        }
        //teszt itt is ez a párja.
        if (AzGyerekFelCheckBox && !azGyerekGumi.isDisabled()) {
            try {
                AzGyerekGumi = Integer.parseInt(azGyerekGumi.getText());
            } catch (NumberFormatException e) {
                AzGyerekGumi = 0;
            }
        }

        ///////////teszt vége ::Ha kiszedném majd valaha..


        //boolean AzCsonak = csonakCheckBox.isSelected();

        boolean AzCsonak = csonakCheckBox.isSelected();

        //Tökmindegy ugyse tudja megenni a dátum formázást.
        //String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy. MM. dd"));

        String tableName = "foglalasok";

        //Még ide valahová kell egy ellenőrzés vagy vmi hogy -minusz foglalás ne történjen meg!
        try {
        Connection conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(false); // transaction kezdése
        //Teszt0.1
            String sql1 = "WITH felszereles_osszesites AS (\n" +
                    "\t\tSELECT \n" +
                    "\t\tSUM(CASE WHEN nev = 'felnott_melleny' THEN darab ELSE 0 END) AS felszereles_felnott_melleny,\n" +
                    "\t\tSUM(CASE WHEN nev = 'felnott_gumi' THEN darab ELSE 0 END) AS felszereles_felnott_gumi,\n" +
                    "\t\tSUM(CASE WHEN nev = 'gyerek_melleny' THEN darab ELSE 0 END) AS felszereles_gyerek_melleny,\n" +
                    "\t\tSUM(CASE WHEN nev = 'gyerek_gumi' THEN darab ELSE 0 END) AS felszereles_gyerek_gumi,\n" +
                    "\t\tSUM(CASE WHEN nev = 'csonak' THEN darab ELSE 0 END) AS felszereles_csonak\n" +
                    "\t\tFROM felszereles),\n" +
                    "foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS foglalasok_felnott_melleny,\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS foglalasok_felnott_gumi,\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS foglalasok_gyerek_melleny,\n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS foglalasok_gyerek_gumi, \n" +
                    "\t\tSUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND csonak > 0 THEN csonak ELSE 0 END) AS foglalasok_csonak\n" +
                    "\t\tFROM foglalasok),\n" +
                    "jovobeni_foglalasok_osszesites AS (\n" +
                    "\t\tSELECT\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS jovobeni_foglalasok_felnott_melleny,\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS jovobeni_foglalasok_felnott_gumi,\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS jovobeni_foglalasok_gyerek_melleny,\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS jovobeni_foglalasok_gyerek_gumi,\n" +
                    "\t\tSUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=CURRENT_DATE AND csonak > 0 THEN csonak ELSE 0 END) AS jovobeni_foglalasok_csonak\n" +
                    "\t\tFROM jovobeni_foglalasok\n" +
                    ")\n" +
                    "SELECT\n" +
                    "\t-- felnott_melleny:\n" +
                    "\t--\tfelszereles_osszesites.felszereles_felnott_melleny,\n" +
                    "\t--\tfoglalasok_osszesites.foglalasok_felnott_melleny,\n" +
                    "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny,\n" +
                    "\t\tfelszereles_osszesites.felszereles_felnott_melleny - foglalasok_osszesites.foglalasok_felnott_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny AS elerheto_felnott_melleny,\n" +
                    "\t\t\n" +
                    "\t-- felnott_gumi:\n" +
                    "\t-- felszereles_osszesites.felszereles_felnott_gumi,\n" +
                    "    -- foglalasok_osszesites.foglalasok_felnott_gumi,\n" +
                    "    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi,\n" +
                    "\t felszereles_osszesites.felszereles_felnott_gumi - foglalasok_osszesites.foglalasok_felnott_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi AS elerheto_felnott_gumi,\n" +
                    "\t\n" +
                    "\t-- gyerek_melleny:\n" +
                    "\t-- felszereles_osszesites.felszereles_gyerek_melleny,\n" +
                    "    -- foglalasok_osszesites.foglalasok_gyerek_melleny,\n" +
                    "    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny,\n" +
                    "\t felszereles_osszesites.felszereles_gyerek_melleny - foglalasok_osszesites.foglalasok_gyerek_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny AS elerheto_gyerek_melleny,\n" +
                    "\t\n" +
                    "\t-- gyerek_gumi:\n" +
                    "\t-- felszereles_osszesites.felszereles_gyerek_gumi,\n" +
                    "    -- foglalasok_osszesites.foglalasok_gyerek_gumi,\n" +
                    "    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi,\n" +
                    "\t felszereles_osszesites.felszereles_gyerek_gumi - foglalasok_osszesites.foglalasok_gyerek_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi AS elerheto_gyerek_gumi,\n" +
                    "\t\n" +
                    "\t-- csonak:\n" +
                    "\t--\tfelszereles_osszesites.felszereles_csonak,\n" +
                    "\t--\tfoglalasok_osszesites.foglalasok_csonak,\n" +
                    "\t--\tjovobeni_foglalasok_osszesites.jovobeni_foglalasok_csonak,\n" +
                    "\t\tfelszereles_osszesites.felszereles_csonak  - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_csonak  - foglalasok_osszesites.foglalasok_csonak AS elerheto_csonak\n" +
                    "FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;\n";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
        int elerhetoFelnottMelleny = rs.getInt("elerheto_felnott_melleny");
        int elerhetoFelnottGumi = rs.getInt("elerheto_felnott_gumi");
        int elerhetoGyerekMelleny = rs.getInt("elerheto_gyerek_melleny");
        int elerhetoGyerekGumi = rs.getInt("elerheto_gyerek_gumi");
        int elerhetoCsonak = rs.getInt("elerheto_csonak");

        //Összehasonlítás
            //Ha minden adat 0, akkor azt ne töltse fel a db-be
            if (AzFelnottMelleny == 0 &&
                    AzFelnottGumi == 0 &&
                    AzGyerekMelleny == 0 &&
                    AzGyerekGumi == 0 &&
                    (!csonakCheckBox.isSelected() && !AzCsonak) ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Üres tábla");
                alert.setHeaderText(null);
                alert.setContentText("Semmilyen foglalási adatot nem adott meg!");
                alert.showAndWait();
            }
            else if (!(AzFelnottMelleny > elerhetoFelnottMelleny ||
                    AzFelnottGumi > elerhetoFelnottGumi ||
                    AzGyerekMelleny > elerhetoGyerekMelleny ||
                    AzGyerekGumi > elerhetoGyerekGumi ||
                    (csonakCheckBox.isDisable() && !AzCsonak) ||
                    (!szabadCsonak.isDisable() && !AzCsonak && elerhetoCsonak == 0))) {
            // Ha minden rendben van, akkor a foglalás rögzítése az adatbázisban

            String sql = "INSERT INTO " + tableName + " (nev, datum, felnott_melleny, felnott_gumi, gyerek_melleny, gyerek_gumi, csonak, aktiv) VALUES (?, NOW(), ?, ?, ?, ?, ?, 1)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, AzFoglaloNev);
            if (azFelnottMelleny.isDisable()) {
                pstmt.setInt(2, 0);
            } else {
                pstmt.setInt(2, AzFelnottMelleny);
            }
            if (azFelnottGumi.isDisable()) {
                pstmt.setInt(3, 0);
            } else {
                pstmt.setInt(3, AzFelnottGumi);
            }
            if (azGyerekMelleny.isDisable()) {
                pstmt.setInt(4, 0);
            } else {
                pstmt.setInt(4, AzGyerekMelleny);
            }
            if (azGyerekGumi.isDisable()) {
                pstmt.setInt(5, 0);
            } else {
                pstmt.setInt(5, AzGyerekGumi);
            }
            if (csonakCheckBox.isDisable() && !csonakCheckBox.isSelected()) {
                pstmt.setBoolean(6, false);
            } else {
                pstmt.setBoolean(6, AzCsonak);
            }

            pstmt.executeUpdate();
            conn.commit(); // transaction végrehajtása
            conn.setAutoCommit(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sikeres foglalás");
            alert.setHeaderText(null);
            alert.setContentText("A foglalás sikeresen rögzítve az adatbázisba!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            //alert.setContentText(String.valueOf(AzCsonak));
            alert.setContentText("Hiba történt az adatok mentése során- nincs annyi cucc!");
            alert.showAndWait();
        }
        } else {Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hiba történt az adatok mentése során- dasz tudjac!");
            alert.showAndWait();}
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hiba történt az adatok mentése során!");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void handleAzFoglalasKlikk(MouseEvent event) {

    }

    @FXML
    void handleFoglalasDelGomb(ActionEvent event) {
        String FoglaloDelNev = foglaloDelNeve.getText();
        String datum = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (FoglaloDelNev.matches(".*\\d+.*") || FoglaloDelNev.split("\\s").length == 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("A név mező csak betűket fogad el!");
            alert.showAndWait();
            return;
        }
        String sql = "UPDATE foglalasok SET aktiv = 0 WHERE nev = ? AND datum = ? AND aktiv = 1";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, FoglaloDelNev);
            stmt.setDate(2, java.sql.Date.valueOf(datum));
            int result = stmt.executeUpdate();
            if (result > 0) {
                //System.out.println("A foglalás törlése sikeresen megtörtént.");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sikeres törlés!");
                alert.setHeaderText(null);
                alert.setContentText("A foglalás törlése sikeresen megtörtént.");
                alert.showAndWait();
                return;
            } else {
                //System.out.println("Nem található aktív foglalás a megadott névvel és dátummal.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Hiba");
                alert.setHeaderText(null);
                alert.setContentText("Nem található aktív foglalás a megadott névvel és dátummal.");
                alert.showAndWait();
                return;
            }
        } catch (SQLException e) {
            //System.out.println("Hiba történt az adatbázis kapcsolat során!");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hiba");
            alert.setHeaderText(null);
            alert.setContentText("Hiba történt az adatbázis kapcsolat során!");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    void handleFoglalasGomb(ActionEvent event) {
        

    }

    @FXML
    void handleFoglalasModGomb(ActionEvent event) {

    }

    @FXML
    void handleLeirasMeglepetes(ActionEvent event) {
        String base64Image = " data:image/jpeg;base64,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"; // itt kell beírni a kép base64 kódolt verzióját
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        //ImageIcon imageIcon = new ImageIcon(decodedBytes);
        //JButton button = new JButton(imageIcon);

    }

}

