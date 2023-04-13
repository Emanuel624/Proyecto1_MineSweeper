import java.util.LinkedList;

public class Stack<T> {
    private LinkedList<T> stackList;

    // Constructor
    public Stack() {
        stackList = new LinkedList<>();
    }

    public void push(T newElement){
        this.stackList.addFirst(newElement);
    }
    
    public T pop(){
        return this.stackList.removeFirst();
    }
    
    public T peek(){
        return this.stackList.getFirst();
    }
    
    public boolean isEmpty() {
        return this.stackList.isEmpty();
    }
    
    public Object[] toArray() {
        return this.stackList.toArray();
    }
}
