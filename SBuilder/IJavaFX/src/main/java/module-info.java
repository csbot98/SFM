module com.uszogumi.uszogumi {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.uszogumi.uszogumi to javafx.fxml;
    exports com.uszogumi.uszogumi;
}