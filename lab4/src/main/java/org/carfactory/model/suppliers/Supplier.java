package org.carfactory.model.suppliers;

import org.carfactory.model.details.Part;

import java.util.Objects;

public class Supplier<T extends Part> implements Runnable {
    private final Storage<T> storage;
    private final Class<T> classPart;
    private volatile int delay = 3;
    private int createdParts = 0;
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };


    public Supplier(Storage<T> storage, Class<T> classPart){
        this.storage = storage;
        this.classPart = classPart;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    public int getCreatedParts(){
        return createdParts;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(delay * 1000L);
                T part = classPart.getDeclaredConstructor().newInstance();
                createdParts++;
                notifyCreatedListener();
                //System.out.println(part.getClass() + "," + part.getID());
                storage.put(part);


            } catch (Exception e){
                Thread.currentThread().interrupt();
                break;
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
