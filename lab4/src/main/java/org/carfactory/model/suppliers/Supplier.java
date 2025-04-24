package org.carfactory.model.suppliers;

import org.carfactory.model.details.Part;

import java.util.Objects;

public class Supplier<T extends Part> implements Runnable {
    private final Storage<T> storage;
    private final Class<T> classPart;
    private volatile int delay = 0;
    private int createdParts = 0;
    private int newID = 1;


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
                T part = classPart.getDeclaredConstructor(Integer.class).newInstance(newID);
                createdParts++;
                newID++;
                System.out.println(part.getClass());
                if (storage.getNowSize() == storage.getSize()){
                    wait();
                    storage.put(part);
                }

            } catch (Exception e){
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
