import java.awt.AWTException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class Main extends Application{
    
               
    //Analizar bien este codigo, es clave.
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


        public static void main(String[] args){
            new Thread(() -> {
                Application.launch(Main.class);
            }).start();
        }
    }



