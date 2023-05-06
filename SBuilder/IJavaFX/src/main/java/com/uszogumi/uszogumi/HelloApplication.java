package com.uszogumi.uszogumi;
import java.sql.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Úszó BiszBasz Bérlő!");
        stage.setScene(scene);
        stage.show();

        String url = "jdbc:mysql://localhost:3306/test"; // adatbázis elérési útvonala
        //String url ="jdbc:postgresql://rogue.db.elephantsql.com:5432/alirdwit";
        String user = "root"; // adatbázis felhasználó neve
        //String user ="alirdwit";
        String password = ""; // adatbázis jelszó (ha van)
        //String password = "xR9tAtXzVj6DON-LTQiQbZaCL2aWVLSG";

        try {

            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            //System.out.println("Siker kapcsolat az adatbázissal!");
            ResultSet rs = stmt.executeQuery("SELECT SUM(gyermek_szam) FROM AzFoglalasok WHERE gyermek_szam IS NOT NULL");
            rs.next();
            int count = rs.getInt(1);
            System.out.println("Szabad Gyerek Mellények száma: " + count );
            conn.close();
        } catch (SQLException e) {
            System.out.println("Sikertelen kapcsolat az adatbázissal!");
            e.printStackTrace();
        }
    }
}