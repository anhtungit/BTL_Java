package org.openjfx.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class MenuItemController {

    @FXML private Button btnViewMenuItem;
    @FXML private Button btnAddMenuItem;
    @FXML private Button btnEditMenuItem;
    @FXML private Button btnDeleteMenuItem;
    @FXML private Button btnSearchMenuItem;

    @FXML private StackPane contentPane;
    @FXML private GridPane buttonPanel;

    @FXML
    private void initialize() {
        btnViewMenuItem.setOnAction(e -> loadView("menuitem_view.fxml"));
        btnAddMenuItem.setOnAction(e -> loadView("menuitem_add.fxml"));
        btnEditMenuItem.setOnAction(e -> loadView("menuitem_edit.fxml"));
        btnDeleteMenuItem.setOnAction(e -> loadView("menuitem_delete.fxml"));
        btnSearchMenuItem.setOnAction(e -> loadView("menuitem_search.fxml"));
    }

    private void loadView(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/org/openjfx/" + fxml));
            buttonPanel.setVisible(false);
            buttonPanel.setManaged(false); 
            contentPane.getChildren().setAll(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
