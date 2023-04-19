/**
*Clase genérica que representa una pila (stack) implementada mediante una lista enlazada.
*@param <T> el tipo de elemento que contendrá la pila.
*/
public class Stack<T> {
    private LinkedList<T> stackList;

    // Constructor
    
    public Stack() {
        stackList = new LinkedList<>();
    }

    /**
    *Añade un nuevo elemento a la cima de la pila.
    *@param newElement el elemento que se añadirá a la pila.
    */
    public void push(T newElement){
        this.stackList.insertFirst(newElement);
    }
    
    /**
    *Elimina y devuelve el elemento superior de la pila.
    *@return el elemento superior que fue eliminado de la pila
    */
    public T pop(){
        return this.stackList.deleteFirst().data;
    }
    
    /**
    * Devuelve el elemento en la cima de la pila sin eliminarlo.
    * @return el elemento en la cima de la pila
    */
    public T peek(){
        return this.stackList.getHead().data;
    }
    
    /**
     * Devuelve un valor booleano que indica si la pila está vacía. 
     * @return true si la pila está vacía, false si contiene al menos un elemento
     */
    public boolean isEmpty() {
        return this.stackList.isEmpty();
    }
    
    /**
     * Retorna un arreglo con los elementos de la pila, en orden LIFO (del último elemento agregado al primero)
     * @return un arreglo de tipo Object con los elementos de la pila
     */
    public Object[] toArray() {
        Object[] arr = new Object[this.stackList.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = this.stackList.get(this.stackList.size() - i - 1);
        }
        return arr;
    }
}
