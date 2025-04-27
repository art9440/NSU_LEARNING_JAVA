package org.carfactory.model.suppliers;

import org.carfactory.model.details.Part;

import java.util.ArrayList;
import java.util.List;

public class ThreadpoolAccessorySuppliers<T extends Part> implements Runnable{
    private Thread[] suppliers;
    private final Storage<T> storage;
    private final Class<T> classPart;
    private int suppliersCount;
    private int delay = 3;
    private int createdParts = 0;
    private int newID = 1;
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    public ThreadpoolAccessorySuppliers(Storage<T> storage, Class<T> classPart, int suppliersCount){
        this.storage = storage;
        this.classPart = classPart;
        this.suppliersCount = suppliersCount;
        suppliers = new Thread[suppliersCount];
        for (int i = 0; i < suppliersCount; i++){
            suppliers[i] = new Thread(this);
        }
    }

    public int getCreatedParts(){
        return createdParts;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }



    public int getSuppliersCount(){
        return suppliersCount;
    }

    public void startThread(int i){
        suppliers[i].start();
    }

    public void stopThread(int i){
        suppliers[i].interrupt();
    }


    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(delay * 1000L);
                T part = classPart.getDeclaredConstructor(Integer.class).newInstance(newID);
                createdParts++;
                notifyCreatedListener();
                newID++;
                System.out.println(part.getClass());
                synchronized (storage) {
                    while (storage.getNowSize() == storage.getSize()){
                        storage.wait();
                    }
                    storage.put(part);
                }
            } catch (Exception e){
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }

    public void addCreatedListener(Runnable listener){
        this.listener = listener;
    }

    private void notifyCreatedListener(){
        listener.run();
    }
}
