module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.base;
    requires java.sql;

    opens org.openjfx.Controllers to javafx.fxml;
    exports org.openjfx.Models;
    exports org.openjfx.Controllers;
}
