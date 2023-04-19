
import java.awt.AWTException;

import java.awt.Robot;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


/**
 * La clase "Solitario" representa una dificultada extra de la solicitadas
 * en la cual se tiene el juego MineSweeper en su forma tradicional es decir para jugar solo.
 */
public class Solitario extends Application{
    
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
        
        private int jugadas = 0;
        
        LinkedList lista = new LinkedList();
        
        private Stack<Tile> sugerencias = new Stack<>();
        
        /**
         * @return devuelve al stackPane todos los diversos elementos añadidos a la GUI para luego ser utilizados en diversas funciones del programa. 
         * @throws AWTException si ocurre un error al generar la interfaz del programa.AWTException
         */
        
        //Lógica basica detrás del juego.
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

            //Botón para la funcionalidad del Joystick.
            JoyStick = new Button();
            JoyStick.setText("Juega con JoyStick");

            JoyStick.setTranslateX(0);
            JoyStick.setTranslateY(-150);
            
            
            //Botón para mostrar las sugerencias de donde hay bombas.
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
                    Object[] sugerenciasArray = sugerencias.toArray();
                    String content = "";
                    for (Object obj : sugerenciasArray) {
                        Tile tile = (Tile) obj;
                        content += "(" + tile.x + ", " + tile.y + ")\n";
                    }
                    alert.setContentText(content);
                    ButtonType seleccionarButton = new ButtonType("Seleccionar");
                    alert.getButtonTypes().add(seleccionarButton);
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == seleccionarButton) {
                        Tile ultimaSugerencia = sugerencias.peek();
                        ultimaSugerencia.border.setStroke(Color.AQUAMARINE);
                        ultimaSugerencia.border.setStrokeWidth(4);
                        ultimaSugerencia.open();
                    }
                }
            });



            //Colocar items en la GUI
            StackPane stackPane = new StackPane(root, timeElapsed, JoyStick, PilaSugenrencias);
            stackPane.setPrefSize(800, 600);

            return stackPane;
        } 


        //Conocer que si las celdas cercanas tienen bombas
        /**
         * @param tile utilizado para generar el tablero de juego juntos con sus bombas aleatoriamente creadas o sin bombas asociadas a la Tile.
         * @return se retorna la información necesarias para saber que una celda reconozca que tiene bombas alrededor suyo para despues ser utilizado.
         */
        private LinkedList<Tile> getNeighbors(Tile tile){
            LinkedList<Tile> neighbors = new LinkedList<>();

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




        /**
         * Esta clase privada se encarga de reconocer diferentes parametros del juego tales como:
         * si tiene bomba, si se abre la celda, si se marca como posible celda, colores de celda y bordes, etc.
         */
        private class Tile extends StackPane{
            private int x, y;
            private boolean hasBomb;
            private boolean isOpen = false;

            
            private boolean isMarked = false;


            private Rectangle border = new Rectangle(TILE_SIZE, TILE_SIZE);
            private Text text = new Text ();
            
            /**
             * @param x es la coordena en el eje "x" de la celda en cuestion por evaluar.
             * @param y es la coordena en el eje "y" de la celda en cuestion por evaluar.
             * @param hasBomb es el parametro booleano utilizado para saber si la celda tiene o no bomba.
             */
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

                //Se toman las acciones obtenidas de los inputs del mouse
                setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY){
                        if (isOpen) {
                            return; // Si la celda ya está abierta, no permitir interacción con ella
                        }                        
                        open();
                        if (hasBomb){
                            String[] args = null; //Se inician la comunicación de la clase con las acciones del arduino buzzer cuando tiene bomba
                            Buzzer_Bomba.main(args);
                        }else{
                            String[] args = null;//Se inician la comunicación de la clase con las acciones del arduino buzzer cuando no tiene bomba
                            Buzzer.main(args);
                        }
                        jugadas++;
                        
                        //Se agregan el contador necesario de las jugadas realizadas por el usario 
                        if (jugadas % 5 == 0) {
                            agregarSugerencia();
                        }
                        
                    }

                    //Lógica para contar la cantidad de bombas que se han marcado y las que faltan
                    if (e.getButton()== MouseButton.SECONDARY){
                        if (isOpen) {
                            return; // Si la celda ya está abierta, no permitir interacción con ella
                        }                        
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
            
            /**
             * Este metodo se encarga de recorrer el tablero de juego o grid para dar las sugerencias pertinenetes con las especificaciones pedidas
             * es decir que sean celdas sin bombas.
             */
            private void agregarSugerencia() {
                Stack<Tile> tiles = new Stack<>();
                for (int Y = 0; Y < Y_TILE; Y++) {
                    for (int X = 0; X < X_TILE; X++) {
                        Tile tile = grid[X][Y];
                        if (!tile.hasBomb && !tile.isOpen) {
                            tiles.push(tile);
                        }
                    }
                }
                if (!tiles.isEmpty()) {
                    Tile sugerencia = tiles.peek();
                    if (!sugerencias.isEmpty()) {
                        Tile ultimaSugerencia = sugerencias.peek();
                        while (sugerencia.x == ultimaSugerencia.x && sugerencia.y == ultimaSugerencia.y) {
                            tiles.pop();
                            if (tiles.isEmpty()) {
                                break;
                            }
                            sugerencia = tiles.peek();
                        }
                    }
                    sugerencias.push(sugerencia);
                }
            }




            
            /**
             * Es metodo se encarga de la lógica necesaria para "abrir" las celdas del tablero de juego
             */

            public void open() {
                if (isOpen) {
                    return;
                }

                if (hasBomb) {
                    System.out.println("Haz perdido");
                }

                isOpen = true;
                text.setVisible(true);
                border.setFill(null);

                //Codigo que cambia para usar funciones construidos, sin usar preconstruidas.
                if (text.getText().isEmpty()) {
                    getNeighbors(this).forEach(new MyConsumer<Tile>() {
                        @Override
                        public void accept(Tile t) {
                            t.open();
                        }
                    });
                }
            }

        }    
        
   /**
    * @param stage es la interfaz principal del programa de este nivel de dificultad. 
    * @throws Exception si ocurre un error al generar la interfaz del programa se utiliza este excepción.
    */     
    @Override
    public void start(Stage stage) throws Exception{

        Scene scene = new Scene(createContent());

        robot = new Robot();
        JoyStick.setOnAction(event -> {
            String[] args = null;
            Controlador.main(args);
            
            //Los valores de la clase Controlador son utilizados para controlar el movimiento del mouse
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
            /**
             * Este metodo se encarga del metodo que cuenta las bombas marcadas junto con el total de bombas que hay en tablero genereado automaticamente.
             * Además, de iniciacilizar la lógica para el cronometro y mostrarlo en la interfaz.
             */
            @Override
            public void handle(long now) {
                Duration duration = Duration.between(startTime, Instant.now());
                long seconds = duration.getSeconds();
                timeElapsed.setText("Tiempo de juego: " + seconds + " segundos");
                
                if (bombsMarkedCount == bombCount) {
                    // Revisar si el usuario ganó el juego
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
                        // Para el contador del tiempo cuando el usuario gane
                        this.stop();
                    }
                }
            }
         }.start();
        stage.setScene(scene);
        stage.show();
    }
    
    
    /**
    * Este metodo le da inicio al program en su totailidad 
    * @param args son los argumentos necesarios para la ejecución del programa
    * no son utilizados explicitamente
    */
    public static void main(String[] args){
        launch(args);
        
    }
}


