package org.openjfx.Controllers;

import java.io.IOException;

import org.openjfx.App;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}