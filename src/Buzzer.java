
import com.fazecast.jSerialComm.*;

public class Buzzer {
    public static void main(String[] args) {
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPort port = ports[0]; // selecciona el primer puerto serie disponible

        if (port.openPort()) {

        } else {

            return;
        }

        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0); // Configura los tiempos de espera

        try {
            Thread.sleep(2000); // Espera 2 segundos para asegurarse de que la placa esté lista
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        port.writeBytes("1".getBytes(), 1); // Envia el carácter 'H' para encender la LED

        try {
            Thread.sleep(1000); // Espera 1 segundos antes de apagar la LED
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        port.writeBytes("0".getBytes(), 1); // Envia el carácter 'L' para apagar la LED

        port.closePort(); // Cierra el puerto serie
    }
}


    


    

