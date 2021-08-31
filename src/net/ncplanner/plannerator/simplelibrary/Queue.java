package net.ncplanner.plannerator.simplelibrary;
import java.util.ArrayList;
//Note: this file was modified; most notably the removal of the Iterators, Spliterators, etc. for compatibility with CodenameOne
public class Queue<T>{
    protected QueueEntry head;
    protected QueueEntry tail;
    protected int size;
    public synchronized void enqueue(T obj){
        QueueEntry t = new QueueEntry(obj);
        if(head==null||tail==null){
            head = t;
            tail = t;
            size = 1;
        }else{
            tail.next=t;
            tail = t;
            size++;
        }
    }
    public synchronized T dequeue(){
        if(head==null){
            size = 0;
            return null;
        }
        T obj = head.obj;
        head = head.next;
        size--;
        return obj;
    }
    public synchronized T peek(){
        if(head==null){
            return null;
        }
        return head.obj;
    }
    public synchronized void clear(){
        head = null;
        tail = null;
        size = 0;
    }
    public int size(){
        return recountSize();
    }
    public boolean isEmpty(){
        return head==null;
    }
    /**
     * Clones this Queue into a like-typed ArrayList, leaving the Queue untouched.
     */
    public ArrayList<T> toList(){
        ArrayList<T> t = new ArrayList<>(size);
        QueueEntry h = head;
        while(h!=null){
            t.add(h.obj);
            h = h.next;
        }
        return t;
    }
    public Queue<T> copy() {
        Queue<T> q = new Queue<T>();
        QueueEntry h = head;
        while(h!=null){
            q.enqueue(h.obj);
            h = h.next;
        }
        return q;
    }
    /**
     * Creates a shallow copy of this Queue.
     * WARNING ABOUT SHALLOW COPIES:  ENQUEUE operations WILL ALSO enqueue on ALL DUPLICATE/ORIGINAL QUEUES
     *      created since either queue in question was completely empty.
     *          ALSO, IF A DUPLICATE QUEUE IS THEN ENQUEUED TO, ALL PRIOR DATA ENQUEUED AFTER DUPLICATION WILL BE LOST!
     * DEQUEUE operations WILL NEVER effect other queues.  As such, shallow copies are ONLY recommended for backtracking DEQUEUE operations.
     * If you need to ENQUEUE to a duplicate, use the <code>copy()</code> function, as it generates a more memory-intensive deep copy.
     */
    public Queue<T> shallow(){
        Queue<T> q = new Queue<T>();
        q.head = head;
        q.tail = tail;
        q.size = size;
        return q;
    }
    public int recountSize() {
        QueueEntry h = head;
        size = 0;
        while(h!=null){
            size++;
            h = h.next;
        };
        return size;
    }
    protected class QueueEntry{
        protected T obj;
        protected QueueEntry next;
        protected QueueEntry(T obj){
            this.obj = obj;
        }
    }
}
