package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class JavaFXText extends Application {
    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JavaFX está funcionando correctamente!");
        Scene scene = new Scene(label, 400, 200);

        primaryStage.setTitle("Prueba de JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
