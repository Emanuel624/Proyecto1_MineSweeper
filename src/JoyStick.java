import com.fazecast.jSerialComm.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

public class JoyStick {
    public static void main(String[] args) {
        SerialPort serialPort = SerialPort.getCommPort("COM6"); // Reemplaza COM3 por el puerto de tu Arduino
        serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        serialPort.openPort();
        InputStream inputStream = serialPort.getInputStream();
        try {
            while (true) {
                if (inputStream.available() > 0) {
                    byte[] buffer = new byte[1024];
                    int len = inputStream.read(buffer);
                    String message = new String(buffer, 0, len);
                    System.out.print(message);

                    String[] values = message.trim().split("[^\\d]+");
                    int xValue = Integer.parseInt(values[1]);
                    int yValue = Integer.parseInt(values[2]);
                    int Left_Mouse= Integer.parseInt(values[3]);
                    
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
                    
                    if (Left_Mouse == 0){
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);     
                    }
                }
            }
        } catch (AWTException | IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            serialPort.closePort();
        }
    }
}