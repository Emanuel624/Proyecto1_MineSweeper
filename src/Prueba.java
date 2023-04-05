import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Prueba extends Application {
    
    // Declarar variables de la GUI
    private BorderPane root;
    private Text titulo;
    private Button boton;
    
    @Override
    public void start(Stage primaryStage) {
        // Inicializar las variables de la GUI
        root = new BorderPane();
        titulo = new Text("Formato Original");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        boton = new Button("Cambiar Formato");
        boton.setOnAction(e -> cambiarFormato());
        
        // Crear el layout principal y agregar los componentes
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.getChildren().addAll(titulo, boton);
        root.setCenter(vbox);
        
        // Crear la escena
        Scene scene = new Scene(root, 400, 400);
        
        // Mostrar la ventana
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Método que cambia el formato de la GUI
    private void cambiarFormato() {
        // Cambiar el color de fondo y de texto
        if (root.getBackground().equals(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)))) {
            root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
            titulo.setFill(Color.WHITE);
            boton.setStyle("-fx-background-color: #009688; -fx-text-fill: white;");
        } else {
            root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            titulo.setFill(Color.BLACK);
            boton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
