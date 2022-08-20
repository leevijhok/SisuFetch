package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;

public class Sisu extends Application {

    public static StudentInfoJson studentInfo;
    public static PlanViewController planViewController;

    @Override
    public void start(Stage stage) throws IOException {
        // Show start dialog
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Sisu");
        dialog.setHeaderText("Welcome to Sisu");
        dialog.setContentText("Please enter your student number:");
        dialog.setOnCloseRequest(event -> {
            // Get student number
            try {
                studentInfo = new StudentInfoJson(dialog.getEditor().getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        dialog.showAndWait();

        // Load main window
        FXMLLoader fxmlLoader = new FXMLLoader(Sisu.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        planViewController = fxmlLoader.<MainViewController>getController().tabPlanViewController;
        stage.setTitle("Sisu");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                studentInfo.saveJson();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("In shutdown hook");
        }, "Shutdown-thread"));
    }
}