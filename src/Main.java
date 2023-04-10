/**
 * @author Emanuel Chavarría Hernández 2022205841
 * @version 1.0
 */
import java.awt.AWTException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Esta clase representa el main del programa, en ella se ejecuta 
 * el programa para sumar edades de personas creadas, por medio de JavaFx
 */
public class Main extends Application{
    
    
    /**
     * @param primaryStage es la interfaz principal del programa
     * @throws Exception si ocurre un error al generar la interfaz del programa
     * Ademas se añaden los botones y sus funcionalidades para entrar a los diversos niveles del juego "MineSweeper"
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear botones para las diferentes dificultades
        Button DummyButton = new Button("Dummy");
        Button SolitarioButton = new Button("Solitario");
        Button AdvanceButton = new Button("Advance");

        // Asignar acciones a los botones
        DummyButton.setOnAction(event -> {
            DummyLevel dummylevel = new DummyLevel();
            try {
                primaryStage.setScene(new Scene(dummylevel.createContent()));
                primaryStage.show();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
        
        
        SolitarioButton.setOnAction(event -> {
            Solitario solitario = new Solitario();
            try {
                primaryStage.setScene(new Scene(solitario.createContent()));
                primaryStage.show();
            } catch (AWTException e) {
                e.printStackTrace();
            }
        });
        
        
        AdvanceButton.setOnAction(event -> {
            AdvanceLevel advancelevel = new AdvanceLevel();
            try {
                primaryStage.setScene(new Scene(advancelevel.createContent()));
                primaryStage.show();
            } catch (AWTException e) {
                e.printStackTrace();
            }
            
        });

        
        // Crear layout para los botones
        VBox layout = new VBox(20, DummyButton, SolitarioButton, AdvanceButton);
        layout.setAlignment(Pos.CENTER);

        // Crear escena y mostrar ventana de inicio
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

        /**
        * Este metodo le da inicio al program en su totailidad 
        * @param args son los argumentos necesarios para la ejecución del programa
        * no son utilizados explicitamente
        */
        public static void main(String[] args){
            new Thread(() -> {
                Application.launch(Main.class);
            }).start();
        }
    }



