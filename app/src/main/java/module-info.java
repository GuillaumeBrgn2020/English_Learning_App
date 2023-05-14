module codingweek {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens codingweek to javafx.controls, javafx.fxml, com.google.gson, javafx.media;
    exports codingweek;
}
