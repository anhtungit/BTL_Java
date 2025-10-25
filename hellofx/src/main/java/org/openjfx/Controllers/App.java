package org.openjfx.Controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String currentUsername;
    private static String currentRole; // MANAGER or STAFF
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("login"), 900, 550);
        stage.setTitle("Phần mềm quản lý quán cà phê ver 1.0");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/org/openjfx/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void openInventoryManagement() throws IOException {
        scene.setRoot(loadFXML("inventory-view"));
        primaryStage.setTitle("Quản lý kho hàng - Phần mềm quản lý quán cà phê ver 1.0");
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setCurrentUser(String username, String role) {
        currentUsername = username;
        currentRole = role;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static String getCurrentRole() {
        return currentRole;
    }

}