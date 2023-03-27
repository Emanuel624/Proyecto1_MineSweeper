import java.time.Duration;
import java.time.Instant;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Cronometro extends Application {

    private Instant startTime;
    private Text elapsedTimeText;

    @Override
    public void start(Stage primaryStage) {
        // Crear una etiqueta para mostrar el tiempo transcurrido
        elapsedTimeText = new Text("0 segundos");
        
        // Crear una disposición de pila para agregar la etiqueta
        StackPane root = new StackPane(elapsedTimeText);

        // Crear la escena
        Scene scene = new Scene(root, 100, 100);

        // Inicializar el tiempo de inicio
        startTime = Instant.now();

        // Crear un AnimationTimer para actualizar la etiqueta de tiempo transcurrido
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calcular el tiempo transcurrido desde el inicio
                Duration elapsedTime = Duration.between(startTime, Instant.now());
                // Actualizar la etiqueta con el tiempo transcurrido formateado
                elapsedTimeText.setText(String.format("%.2f segundos", elapsedTime.toMillis() / 1000.0));
            }
        };
        // Iniciar el AnimationTimer
        timer.start();

        // Mostrar la ventana
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

