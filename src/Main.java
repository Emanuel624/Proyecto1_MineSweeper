import java.awt.AWTException;
import java.awt.Robot;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Main extends Application{
    
        //Escala del tablero po generar
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
        

        //Logica prueba
        private Robot robot;

        private double mouseX = 0;
        private double mouseY = 0;

        //Creación de la matriz en la que se basa el tablero
        private Tile [][] grid = new Tile [X_TILE][Y_TILE];

        
        //Lógica basica detras del juego
        private Parent createContent(int xTile, int yTile, int bombCount1) throws AWTException{
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

            System.out.println("Number of bombs: " + bombCount);
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


                    //Colocar items en la GUI
            StackPane stackPane = new StackPane(root, timeElapsed, JoyStick);
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
        
    //Analizar bien este codigo, es clave.
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Crear botones para las diferentes dificultades
        Button easyButton = new Button("Dummy");
        Button mediumButton = new Button("Media");
        Button hardButton = new Button("Advance");

        // Asignar acciones a los botones
        easyButton.setOnAction(event -> {
            try {
                startGame(primaryStage, 8, 8, 10);
            } catch (AWTException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        mediumButton.setOnAction(event -> {
            try {
                startGame(primaryStage, 16, 16, 40);
            } catch (AWTException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        hardButton.setOnAction(event -> {
            try {
                startGame(primaryStage, 24, 24, 99);
            } catch (AWTException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Crear layout para los botones
        VBox layout = new VBox(20, easyButton, mediumButton, hardButton);
        layout.setAlignment(Pos.CENTER);

        // Crear escena y mostrar ventana de inicio
        Scene scene = new Scene(layout, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void startGame(Stage primaryStage, int xTile, int yTile, int bombCount) throws AWTException {
        // Crear contenido para el juego
        Scene scene = new Scene(createContent(xTile, yTile, bombCount));
        robot = new Robot();

        // Asignar acciones al joystick y a las teclas
        JoyStick.setOnAction(event -> {
            String[] args = null;
            Controlador.main(args);

            scene.setOnKeyPressed(keyEvent -> {
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

        // Inicialización del cronómetro
        startTime = Instant.now();

        // Presentar el contador de bombas y de tiempo
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                Duration duration = Duration.between(startTime, Instant.now());
                long seconds = duration.getSeconds();
                timeElapsed.setText("Tiempo de juego: " + seconds + " segundos");

                if (bombsMarkedCount == bombCount) {
                    // Check if the user has won the game
                    boolean allOpened = true;
                    for (int y = 0; y < yTile; y++) {
                        for (int x = 0; x < xTile; x++) {
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

        // Mostrar escena del juego
        primaryStage.setScene(scene);
    }


        public static void main(String[] args){
            new Thread(() -> Application.launch(Main.class)).start();
        }
    }



