import com.fazecast.jSerialComm.*;

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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            serialPort.closePort();
        }
    }
}
