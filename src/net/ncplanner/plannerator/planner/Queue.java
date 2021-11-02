package net.ncplanner.plannerator.planner;
import java.util.ArrayList;
public class Queue<T> extends ArrayList<T>{
    public void enqueue(T t){
        super.add(t);
    }
    public T dequeue(){
        return super.remove(0);
    }
    public T peek(){
        return super.get(0);
    }
}