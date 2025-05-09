package org.carfactory.model.suppliers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Storage<T> {
    private final int size;
    private Queue<T> store = new LinkedList<T>();
    private int nowSize = 0;
    private volatile boolean full = false;
    private final List<Runnable> listeners = new ArrayList<>();


    public Storage(int size){
        this.size = size;
    }

    public synchronized int getSize(){
        return size;
    }

    public synchronized void put(T detail){

        store.add(detail);
        nowSize++;
        notifySizeListener();
        if(nowSize == size){
            full = true;
        }
        notifyAll();
    }

    public synchronized T get(){
        nowSize--;
        notifySizeListener();
        if (nowSize != size){
            full = false;
        }
        notifyAll();
        return store.remove();
    }


    public synchronized int getNowSize(){
        return nowSize;
    }

    public void addSizeListener(Runnable listener){
        listeners.add(listener);
    }

    private void notifySizeListener(){
        for(Runnable listener : listeners){
            listener.run();
        }
    }
}
