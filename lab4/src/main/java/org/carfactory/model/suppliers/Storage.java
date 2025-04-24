package org.carfactory.model.suppliers;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T> {
    private final int size;
    private Queue<T> store = new LinkedList<T>();
    private int nowSize = 0;
    private volatile boolean full = false;

    public Storage(int size){
        this.size = size;
    }

    public int getSize(){
        return size;
    }

    public synchronized void put(T detail){
        store.add(detail);
        nowSize++;
        if(nowSize == size){
            full = true;
        }
    }

    public synchronized T get(){
        nowSize--;
        if (nowSize != size){
            full = false;
        }
        return store.remove();
    }


    public int getNowSize(){
        return nowSize;
    }
}
