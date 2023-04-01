import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Prueba extends Application {

    private Robot robot;

    private double mouseX = 0;
    private double mouseY = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 400, 400);

        robot = new Robot();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
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

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
