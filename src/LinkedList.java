/**
 * Esta clase LinkedList es la implementación desde 0 de la estructura de datos "Lista enlazada" sin utilizar implementación ya creadas en Java.
 * @param <T> este parametro, permite recibir parametros estandar y agregarlos a esta clase de lista enlazada, sin esto, no podrían agregarse datos de clases externas a esta.
 */
class LinkedList<T> {
    Node<T> head;
    int size;

    /**
     * Se definen la cabeza y los valores de tamaño en caso de que la lista se encuentre vacia o Nula.
     */
    public LinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    /**
     * @return se encarga de verificar si la lista enlzada esta vacia, revisando la cabeza de la misma.
     */
    public boolean isEmpty(){
        return this.head == null;
    }
    
    /**
     * @param data, en este caso, se inserta en el primer espacio de la lista enlazada el parametro estandar recogido de otra clase, se tiene la logica para poder realizar esta acción. 
     */
    public void insertFirst(T data){
        Node<T> newNode = new Node<>(data);
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
    }
    
    /**
     * @return, como su contra parte anterior, se encarga de eliminar el primer elemento de la lista enlazada,tiene la lógica necesaria para dicha acción.
     */
    public Node<T> deleteFirst(){
        if (this.head != null){
            Node<T> temp = this.head;
            this.head = this.head.next;
            this.size--;
            return temp;        
        }else{
            return null;
        }
    }
    
    /**
     * @param data se utiliza el parametro obtenido de otra clase, para poder realizar la comprobación de datos dentro de la lista enlazada.
     * @return se retorna un elemento Booleano, para conocer si se encuentra o no el elemento de data deseado.
     */
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }

    /**
     * Este metodo publico, es utilizado, para imprimir los valores de las listas de manera correcta en la terminal.
     */
    public void displayList(){
        Node current = this.head;
        while (current != null){
            System.out.println(current.getData());
            current = current.getNext();
        }
    }
    
    /**
     * @param data este parametro, es utilizado para realizar el metodo publico de añadir elementos externos a esta clase a la lista enlazada, el data es la información que se va a añadir.
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    
    /**
     * @param otherList este metodo publiclo, permite unir 2 listas enlazadas de manera completa, mediante un unico metodo.
     */
    public void addAll(LinkedList<T> otherList) {
        Node<T> current = otherList.head;
        while (current != null) {
            add(current.getData());
            current = current.getNext();
        }
    }
    
    /**
     * Es metodo, "limpia" en su totalidad la lista, es decir la vuelve nula.
     */
    public void clear() {
        head = null;
        size = 0;
    }
    
    /**
     * @return se retorna el tamaño que posee la lista enlazada.
     */
    public int size() {
        return size;
    }
    
    /**
     * @return se retorna el parametro que se encuentra en la cabeza de la lista enlazada como tal.
     */
    public Node<T> getHead() {
        return head;
    }
    
    /**
    *Devuelve el elemento en la posición especificada de la lista.
    *@param index el índice del elemento a devolver
    *@return el elemento en la posición especificada
    *@throws IndexOutOfBoundsException si el índice está fuera de rango (menor que 0 o mayor o igual al tamaño de la lista)
    */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current.data;
    }

    /**
    *Itera a través de cada elemento en la lista y aplica la acción especificada en el parámetro de entrada.
    *@param action el objeto MyConsumer que contiene la acción a aplicar en cada elemento de la lista.
    */
    public void forEach(MyConsumer<T> action) {
        Node<T> current = head;
        while (current != null) {
            action.accept(current.data);
            current = current.next;
        }
    }
    
    /**
    *Retorna un MyStream<T> que permite realizar operaciones de transmisión de datos en la lista enlazada.
    *@return Un MyStream<T> que permite realizar operaciones de transmisión de datos en la lista enlazada.
    */
    public MyStream<T> stream() {
        return new MyStream<>(head);
    }
    
    /**
    *Convierte la lista enlazada a un arreglo de objetos.
    *@return un arreglo de objetos que contiene todos los elementos de la lista en el orden en que aparecen en ella.
    */
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Node<T> current = head;
        int i = 0;
        while (current != null) {
            arr[i] = current.data;
            current = current.next;
            i++;
        }
        return arr;
    }
    
    /**
    *Elimina la primera ocurrencia del elemento especificado de esta lista enlazada, si está presente.
    *Si la lista no contiene el elemento, no se modifica.
    *@param data el elemento a eliminar de la lista
    *@return true si se elimina el elemento de la lista, false en caso contrario
    */
    public boolean remove(T data) {
    if (head == null) {
        return false;
    }

    if (head.data.equals(data)) {
        head = head.next;
        size--;
        return true;
    }

    Node<T> prev = head;
    Node<T> current = head.next;

    while (current != null) {
        if (current.data.equals(data)) {
            prev.next = current.next;
            size--;
            return true;
        }
        prev = current;
        current = current.next;
    }

    return false;  
}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
*Interfaz funcional que representa un consumidor de un objeto de tipo genérico T.
*Proporciona un método accept que acepta un objeto de tipo T como parámetro y no devuelve nada.
*@param <T> El tipo de parametro que se usa.
*/
class MyConsumer<T> {
    void accept(T t) {}
}

/**
*Clase que implementa un Stream para recorrer una lista enlazada de manera funcional.
*@param <T> el tipo de elementos en la lista enlazada
*/
class MyStream<T> {
    Node<T> head;
    
    /**
    *Crea un nuevo flujo de elementos a partir de la cabeza especificada de una lista enlazada.
    *@param head la cabeza de la lista enlazada
    */
    public MyStream(Node<T> head) {
        this.head = head;
    }
    
    /**
     * Crea un nuevo stream iterando a través de los nodos de la lista enlazada comenzando desde el nodo proporcionado como semilla.
     * @param seed el nodo desde donde comenzar a iterar
     * @param hasNext el predicado que indica si el siguiente nodo está disponible
     * @param next el operador unario que proporciona el siguiente nodo
     * @return el nuevo stream creado
     */
    public MyStream<T> iterate(Node<T> seed, MyPredicate<Node<T>> hasNext, MyUnaryOperator<Node<T>> next) {
        this.head = seed;
        return this;
    }
        
    /**
    *Filtra los elementos de la corriente utilizando el predicado especificado.
    *@param predicate el predicado utilizado para filtrar los elementos
    *@return una nueva corriente que contiene solo los elementos que cumplen el predicado
    */
    public MyStream<T> filter(MyPredicate<T> predicate) {
        Node<T> current = head;
        Node<T> newHead = null;
        Node<T> tail = null;

        while (current != null) {
            if (predicate.test(current.data)) {
                Node<T> newNode = new Node<>(current.data);
                if (newHead == null) {
                    newHead = newNode;
                    tail = newNode;
                } else {
                    tail.next = newNode;
                    tail = newNode;
                }
            }
            current = current.next;
        }

        return new MyStream<>(newHead);
    }

    /**
    *Retorna la cantidad de elementos en la secuencia.
    *@return La cantidad de elementos en la secuencia.
    */
    public long count() {
        
        Node<T> current = head;
        long count = 0;
        while (current != null) {
            
            count++;
            current = current.next;
        }
        return count;
    }
}

/**
 * @FunctionalInterface indica que la interfaz es una interfaz funcional y puede ser usada como tal.
 * @param <T> el tipo de argumento de entrada. 
 */
@FunctionalInterface
interface MyPredicate<T> {
    boolean test(T t);
}

/**
*Clase genérica que representa un operador unario que toma un objeto del mismo tipo y devuelve otro objeto del mismo tipo.
*@param <T> el tipo de los objetos de entrada y salida del operador
*/
class MyUnaryOperator<T> {
    /**
    * 
    *Aplica el operador al objeto de entrada y devuelve el resultado.
    *@param t el objeto de entrada
    *@return el resultado de aplicar el operador al objeto de entrada
    */
    T apply(T t) { return t; }
}

/**
*Representa una función que acepta un argumento de tipo T y devuelve un resultado de tipo R.
*@param <T> el tipo de argumento de entrada.
*@param <R> el tipo de resultado de la función.
*/
class MyFunction<T, R> {
    R apply(T t) { return null; }
}
