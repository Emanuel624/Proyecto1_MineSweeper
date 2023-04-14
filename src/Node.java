/**
 * La clase Node, es la encargada de generar los Nodos, para poder realizar las diferentes acciones por medio de las estructuras de datos
 * creadas desde 0.
 * @param <T>  este parametro, permite que esta clase, reciba parametros dentro de los nodos, para poder agregar elementos desde otras clases externas a esta.
 */
class Node<T> {
    T data;     //Para permitir lectura de parametros estandar
    Node<T> next;   //Apuntar al siguiente nodo 
    
    /**
     * @param data en este caso son los datos predeterminados utilizados para poder añadirle información a los nodos desde clases externas a esta. 
     */
    public Node(T data) {
        this.data = data;
        this.next = null;
    }
    /**
     * @return este metodo, retorna la data que tiene cada nodo en cuestión
     */
    public T getData() {    //Se obtiene data de el nodo en cuestión por evaluar
        return data;
    }
    
    /**
     * @return el siguiente nodo en la lista enlazada
     */
    public Node <T> getNext(){  //Devuelve el siguiente nodo de la listas enlazada
        return this.next;
    }
        
}