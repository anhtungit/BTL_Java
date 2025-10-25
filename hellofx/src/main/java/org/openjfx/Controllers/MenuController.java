package org.openjfx.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MenuController {

    @FXML private Button btnXemDanhSach;
    @FXML private Button btnThemDanhMuc;
    @FXML private Button btnChinhSuaDanhMuc;
    @FXML private Button btnXoaDanhMuc;
    @FXML private Button btnTimKiemDanhMuc;

    @FXML
    private void initialize() {
        btnXemDanhSach.setOnAction(e -> loadView("category.fxml"));
        btnThemDanhMuc.setOnAction(e -> loadView("menu_add.fxml"));
        btnChinhSuaDanhMuc.setOnAction(e -> loadView("menu_edit.fxml"));
        btnXoaDanhMuc.setOnAction(e -> loadView("menu_delete.fxml"));
        btnTimKiemDanhMuc.setOnAction(e -> loadView("menu_search.fxml"));
    }

    private void loadView(String fxml) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/org/openjfx/" + fxml));
            StackPane parentStack = (StackPane) btnXemDanhSach.getScene().lookup("#contentPane");
            if (parentStack != null) {
                parentStack.getChildren().setAll(view);
            } else {
                System.err.println("Khong tim thay contentPane trong Scene");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
