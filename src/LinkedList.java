class LinkedList<T> {
    Node<T> head;
    int size;

    public LinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    public boolean isEmpty(){
        return this.head == null;
    }
    
    public void insertFirst(T data){
        Node newNode = new Node(data);
        newNode.next = this.head;
        this.head = newNode;
        this.size++;
    }
    
    public Node deleteFirst(){
        if (this.head != null){
            Node temp = this.head;
            this.head = this.head.next;
            this.size--;
            return temp;
        }else{
            return null;
        }
    }
    
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

    public int size() {
        return size;
    }

    public Node<T> getHead() {
        return head;
    }
    
        public int getSize() {
        return size;
    }

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

    
    public void forEach(MyConsumer<T> action) {
        Node<T> current = head;
        while (current != null) {
            action.accept(current.data);
            current = current.next;
        }
    }

    public MyStream<T> stream() {
        return new MyStream<>(head);
    }
}

class MyConsumer<T> {
    void accept(T t) {}
}

class MyStream<T> {
    Node<T> head;

    public MyStream(Node<T> head) {
        this.head = head;
    }

    public MyStream<T> iterate(Node<T> seed, MyPredicate<Node<T>> hasNext, MyUnaryOperator<Node<T>> next) {
        this.head = seed;
        return this;
    }

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

@FunctionalInterface
interface MyPredicate<T> {
    boolean test(T t);
}


class MyUnaryOperator<T> {
    T apply(T t) { return t; }
}

class MyFunction<T, R> {
    R apply(T t) { return null; }
}
