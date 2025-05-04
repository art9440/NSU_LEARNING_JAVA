package org.carfactory.model.suppliers;

import java.util.LinkedList;
import java.util.Queue;

public class Storage<T> {
    private final int size;
    private Queue<T> store = new LinkedList<T>();
    private int nowSize = 0;
    private volatile boolean full = false;
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    public Storage(int size){
        this.size = size;
    }

    public synchronized int getSize(){
        return size;
    }

    public synchronized void put(T detail){
        //System.out.println(detail.getClass());
        store.add(detail);
        nowSize++;
        notifySizeListener();
        if(nowSize == size){
            full = true;
        }
    }

    public synchronized T get(){
        nowSize--;
        notifySizeListener();
        if (nowSize != size){
            full = false;
        }
        return store.remove();
    }


    public synchronized int getNowSize(){
        return nowSize;
    }

    public void addSizeListener(Runnable listener){
        this.listener = listener;
    }

    private void notifySizeListener(){
        listener.run();
    }
}
