import java.awt.AWTException;
import java.awt.Robot;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import static javafx.application.Application.launch;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import static javax.management.Query.value;


public class AdvanceLevel extends Application{
    
        //Escala del tablero por generar
        private static final int TILE_SIZE = 40;
        private static final int X_TILE = 8;
        private static final int Y_TILE = 8;

        //Variables para el tiempo de juego
        private Text timeElapsed = new Text();
        private Instant startTime;

        //Contar bombas
        private Text bombsMarked = new Text();
        private int bombsToMark;
        private int bombsMarkedCount = 0;
        int bombCount = 0;

        //Boton para jugar con el Joystick
        Button JoyStick;
        Button PilaSugenrencias;

        //Logica prueba
        private Robot robot;

        private double mouseX = 0;
        private double mouseY = 0;

        //Creación de la matriz en la que se basa el tablero
        private Tile [][] grid = new Tile [X_TILE][Y_TILE];
        
        
        //Stack de sugerencias
        private Stack<Tile> sugerencias = new Stack<>();
        private int jugadas = 0;

        //Creación de lista enlazada
        private LinkedList<Tile> availableCells;
        // Crear listas segura e incertidumbre
        LinkedList<Tile> listaSegura = new LinkedList<>();
        LinkedList<Tile> listaIncertidumbre = new LinkedList<>();
        LinkedList<Tile> celdasAbiertas = new LinkedList<>();
        
        
        //Lógica basica detras del juego
        Parent createContent() throws AWTException{
            Pane root = new Pane();

            root.setPrefSize(X_TILE * TILE_SIZE, Y_TILE * TILE_SIZE);

            //Despliege en pantalla de las bombas que se deben encontrar y las encontradas

            for (int y = 0; y < Y_TILE; y++ ){
                for(int x = 0; x < X_TILE; x++){
                    Tile tile = new Tile(x, y, Math.random() < 0.2);

                    grid[x][y] = tile;
                    root.getChildren().add(tile);

                    if (tile.hasBomb){
                        bombCount++;
                    }   
                }   
            }
            
            bombsMarked.setText("Bombas encontradas: 0/" + bombCount);
            bombsMarked.setTranslateX(320);
            bombsMarked.setTranslateY(20);
            root.getChildren().add(bombsMarked);

            for (int y = 0; y < Y_TILE; y++ ){
                for(int x = 0; x < X_TILE; x++){
                    Tile tile = grid[x][y];
                    if (tile.hasBomb)
                        continue;

                    //set bombs
                    long bombs = getNeighbors(tile).stream().filter(t -> t.hasBomb).count();

                    if (bombs > 0)
                        tile.text.setText(String.valueOf(bombs));
                }
            }

            //Botón para la funcionalidad del Joystick
            JoyStick = new Button();
            JoyStick.setText("Juega con JoyStick");

            JoyStick.setTranslateX(0);
            JoyStick.setTranslateY(-150);
            
            
            //Botón para mostrar las sugerencias de donde hay bombas
            PilaSugenrencias = new Button();
            PilaSugenrencias.setText("Pila sugerencias");
            PilaSugenrencias.setTranslateX(-240);
            PilaSugenrencias.setTranslateY(50);
            PilaSugenrencias.setOnAction(e -> {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Pila de sugerencias");
                alert.setHeaderText(null);
                if (sugerencias.isEmpty()) {
                    alert.setContentText("La pila de sugerencias está vacía.");
                } else {
                    String content = "";
                    for (Tile tile : sugerencias) {
                        content += "(" + tile.x + ", " + tile.y + ")\n";
                    }
                    alert.setContentText(content);
                }
                alert.showAndWait();
            });
            
            
            // Crear la lista enlazada
            availableCells = new LinkedList<>();
            // Agregar todas las celdas disponibles a la lista enlazada
            for (int y = 0; y < Y_TILE; y++) {
                for (int x = 0; x < X_TILE; x++) {
                    Tile tile = grid[x][y];
                    if (!tile.isOpen) {
                        availableCells.add(tile);
                    }
                }
            }
            // Imprimir el estado de la lista enlazada en la consola
            System.out.println("Lista general sin modificaciones:");
            for (Tile tile : availableCells) {
                System.out.println(tile);
            }
            
            //Colocar items en la GUI
            StackPane stackPane = new StackPane(root, timeElapsed, JoyStick, PilaSugenrencias);
            stackPane.setPrefSize(800, 600);

            return stackPane;
        } 


        //Conocer que si las celdas cercanas tienen bombas
        private List<Tile> getNeighbors(Tile tile){
            List<Tile> neighbors = new ArrayList<>();

            int[] points = new int[]{
                -1, -1,
                -1, 0,
                -1, 1,
                0, -1,
                0, 1,
                1, -1,
                1, 0,
                1, 1   
            };

            for (int i = 0; i < points.length; i++){
                int dx = points [i];
                int dy = points[++i];

                int newX = tile.x + dx;
                int newY = tile.y +dy;

                if (newX >= 0 && newX < X_TILE
                        && newY >= 0 && newY < Y_TILE){
                    neighbors.add(grid[newX][newY]);
                }
            }

            return neighbors;
        }

        private class Tile extends StackPane{
            private int x, y;
            private boolean hasBomb;
            private boolean isOpen = false;

            
            private boolean isMarked = false;


            private Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);
            private Text text = new Text ();

            
            
            public Tile(int x, int y, boolean hasBomb){

                this.x = x;
                this.y = y;
                this.hasBomb = hasBomb;

                
                border.setStroke(Color.LIGHTGRAY);


                text.setText(hasBomb ? "X" : "");
                text.setVisible(false);

                getChildren().addAll(border, text);

                setTranslateX(x * TILE_SIZE);
                setTranslateY(y * TILE_SIZE);


                setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY){
                        open();
                        if (hasBomb){
                            String[] args = null;
                            Buzzer_Bomba.main(args);
                        }else{
                            String[] args = null;
                            Buzzer.main(args);
                        }
                        jugadas++;
                        
                        if (jugadas % 5 == 0) {
                            agregarSugerencia();
                        }
                        
                        
                        // Lógica todas las celdas disponibles que no estan abiertas se meten dentro de la lista enlazada 
                        // eliminar las celdas abiertas de la lista de celdas disponibles
                        
                        for (int j = 0; j < Y_TILE; j++) {
                            for (int i = 0; i < X_TILE; i++) {
                                Tile tile = grid[i][j];
                                if (tile.isOpen|| tile.text.isVisible())  {
                                    celdasAbiertas.add(tile);
                                }
                            }
                        }
                        availableCells.removeAll(celdasAbiertas);

                                                
                        
                        // Generar un índice aleatorio para acceder a una celda de availableCells
                        Random rand = new Random();
                        int randomIndex = rand.nextInt(availableCells.size());
                        Tile randomTile = availableCells.get(randomIndex);

                        // Determinar si hay una mina en la celda aleatoria
                        if (randomTile.hasBomb) {
                            listaIncertidumbre.add(randomTile);
                            
                        } else {
                            listaSegura.add(randomTile);
                            
                        }
                        
                        availableCells.remove(randomTile);
                        
                        // Imprimir el estado de la lista enlazada en la consola
                        System.out.println("Lista general:");
                        for (Tile tile : availableCells) {
                            System.out.println(tile);
                        }
                        
                        // Imprimir el estado de las listas segura e incertidumbre en la consola
                        System.out.println("Lista segura:");
                        for (Tile tile : listaSegura) {
                            System.out.println(tile);
                        }

                        System.out.println("Lista incertidumbre:");
                        for (Tile tile : listaIncertidumbre) {
                            System.out.println(tile);
                        }
                        
                        

                           
                    }

                    //Lógica para contar la cantidad de bombas que se han marcado y las que faltan
                    if (e.getButton()== MouseButton.SECONDARY){
                        if (isMarked) {
                            // Desmarcar la celda
                            border.setFill(Color.BLACK);
                            isMarked = false;
                            bombsMarkedCount--;
                        } else {
                            // Markar la celda como posible bomba
                            border.setFill(Color.RED);
                            isMarked = true;
                            bombsMarkedCount++;
                            String[] args = null;
                            LED.main(args);
                        }
                        bombsMarked.setText("Bombas encontradas" + bombsMarkedCount + "/" + bombCount);
                    }
                });
            }
            
            
            //Codigo necesario para poder ver los valores de la lista enlazada en forma de string
            @Override
                public String toString() {
                    return String.format("(%d,%d)", x, y);
                }
            
            // Método para imprimir una lista de celdas
            private void printList(LinkedList<Tile> list) {
                for (Tile tile : list) {
                    System.out.println(tile);
                }
            }    
                
            private void agregarSugerencia() {
                List<Tile> tiles = new ArrayList<>();
                for (int Y = 0; Y < Y_TILE; Y++) {
                    for (int X = 0; X < X_TILE; X++) {
                        Tile tile = grid[X][Y];
                        if (!tile.hasBomb && !tile.isOpen) {
                            tiles.add(tile);
                        }
                    }
                }
                if (!tiles.isEmpty()) {
                    Tile sugerencia = tiles.get((int) (Math.random() * tiles.size()));
                    sugerencias.push(sugerencia);
                    
                }
            }
            
            public void open(){
                if (isOpen)
                    return;   


                if (hasBomb){
                    System.out.println("Haz perdido");
                }
                isOpen = true;
                text.setVisible(true);
                border.setFill(null);

                if (text.getText().isEmpty()){
                    getNeighbors(this).forEach(Tile::open);
                }   
            }
        }
   
        
    @Override
    public void start(Stage stage) throws Exception{

        Scene scene = new Scene(createContent());

        
        robot = new Robot();
        JoyStick.setOnAction(event -> {
            String[] args = null;
            Controlador.main(args);

            scene.setOnKeyPressed(keyEvent  -> {
                switch (keyEvent.getCode()) {
                    case W:
                        mouseY -= 10;
                        break;
                    case S:
                        mouseY += 10;
                        break;
                    case A:
                        mouseX -= 10;
                        break;
                    case D:
                        mouseX += 10;
                        break;
                }
            robot.mouseMove((int) mouseX, (int) mouseY);
            });
        });


        //Inicialización del cronometro
        startTime = Instant.now();






        //Presentar el contador de bombas y de tiempo
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Duration duration = Duration.between(startTime, Instant.now());
                long seconds = duration.getSeconds();
                timeElapsed.setText("Tiempo de juego: " + seconds + " segundos");
                
                if (bombsMarkedCount == bombCount) {
                    // Check if the user has won the game
                    boolean allOpened = true;
                    for (int y = 0; y < Y_TILE; y++) {
                        for (int x = 0; x < X_TILE; x++) {
                            Tile tile = grid[x][y];
                            if (tile.hasBomb && !tile.isMarked) {
                                allOpened = false;
                                break;
                            }
                        }
                    }
                    if (allOpened) {
                        System.out.println("You won!");
                        // Stop the timer when the user wins
                        this.stop();
                    }
                }
            }
         }.start();
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
        
    }
}



