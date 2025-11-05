module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires java.desktop;

    opens org.openjfx to javafx.fxml;
    opens org.openjfx.Controllers to javafx.fxml;
    exports org.openjfx;
    exports org.openjfx.entity;
    exports org.openjfx.Controllers;
}
