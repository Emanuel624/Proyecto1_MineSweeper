import java.awt.AWTException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Prueba extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear un botón
        Button startButton = new Button("Iniciar Solitario");
        
        // Acción al presionar el botón
        startButton.setOnAction((ActionEvent event) -> {
            Solitario solitario = new Solitario();
            try {
                primaryStage.setScene(new Scene(solitario.createContent()));
                primaryStage.show();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
        
        // Añadir el botón a una disposición
        StackPane root = new StackPane();
        root.getChildren().add(startButton);
        
        // Mostrar la escena
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Solitario");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
