/**
 * Esta clase se encarga de realizar la comunicación serial por medio de JserialComms de arduino con java
 * Con el objetivo de los valores de un joystick en los ejes "y" y "x" se utilicen para dar comandos con teclas del teclado para ser utlizado despues.
 */

import com.fazecast.jSerialComm.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;


public class Controlador {
    /**
    * Este metodo le da inicio al program en su totailidad 
    * @param args son los argumentos necesarios para la ejecución del programa
    * no son utilizados explicitamente
    */
    public static void main(String[] args) {
        Thread serialThread = new Thread(Controlador::startSerialCommunication);
        serialThread.start();

    }
    
    /**
     * Este metodo se encarga de la lógica en bruto de la comunicación entre el Arduino y java
     * por medio ed envio y recibimiento de bits pre-establecidos.
     */
    private static void startSerialCommunication() {
        SerialPort serialPort = SerialPort.getCommPort("COM6"); //Comunicación con el puerto utilizado por el arduino
        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        serialPort.openPort();
        InputStream inputStream = serialPort.getInputStream();
        try {
            while (true) {
                if (inputStream.available() > 0) {
                    byte[] buffer = new byte[1024];
                    int len = inputStream.read(buffer);
                    String message = new String(buffer, 0, len);

                    String[] values = message.trim().split("[^\\d]+");
                    int xValue = Integer.parseInt(values[1]);
                    int yValue = Integer.parseInt(values[2]);
                    int Left_Mouse = Integer.parseInt(values[3]);
                    int Right_Mouse = Integer.parseInt(values[4]);
                    

                    Robot robot = new Robot();

                    if (xValue < 400) {
                        robot.keyPress(KeyEvent.VK_A);
                        robot.keyRelease(KeyEvent.VK_A);

                    } else if (xValue > 600) {
                        robot.keyPress(KeyEvent.VK_D);
                        robot.keyRelease(KeyEvent.VK_D);
                    }

                    if (yValue < 400) {
                        robot.keyPress(KeyEvent.VK_S);
                        robot.keyRelease(KeyEvent.VK_S);
                    } else if (yValue > 600) {
                        robot.keyPress(KeyEvent.VK_W);
                        robot.keyRelease(KeyEvent.VK_W);
                    }

                    if (Left_Mouse == 0) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    }
                    if (Right_Mouse == 1) {
                        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    }
                }
            }
        } catch (AWTException | IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            serialPort.closePort(); //Se termina la conexión entre arduino y java
        }
    }
}
