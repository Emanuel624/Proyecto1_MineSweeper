import java.util.LinkedList;

public class Prueba {
    public static void main(String[] args) {
        // Crear una lista enlazada de enteros
        LinkedList<Integer> linkedList = new LinkedList<>();

        // Agregar elementos a la lista
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        linkedList.add(4);
        linkedList.add(5);

        // Recorrer la lista e imprimir sus elementos
        for (int i = 0; i < linkedList.size(); i++) {
            System.out.println(linkedList.get(i));
        }
    }
}
