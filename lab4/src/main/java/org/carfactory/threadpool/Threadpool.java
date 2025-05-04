package org.carfactory.threadpool;

import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Part;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;

public class Threadpool<T extends Part>  {
    private Thread[] suppliersThreads;
    private Supplier<T>[] suppliers;
    private int suppliersCount;
    private final Class<T> classPart;
    private Storage<T> storage;
    private int createdParts = 0;
    private final Object lock = new Object();
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    public Threadpool(Class<T> classPart, int suppliersCount, Storage<T> storage){
        this.classPart = classPart;
        this.suppliersCount = suppliersCount;
        this.storage = storage;
        suppliersThreads = new Thread[suppliersCount];
        suppliers = (Supplier<T>[]) new Supplier[suppliersCount];
        for (int i = 0; i < suppliersCount; i++){
            Supplier<T> supplier = new Supplier<>(storage, classPart);
            supplier.addCreatedListener(this::updateCreatedParts);
            suppliers[i] = supplier;
            suppliersThreads[i] = new Thread(supplier); //нужно давать каждому thread свой supplier
        }
    }

    public int getSuppliersCount(){
        return suppliersCount;
    }

    public void startThread(int i){
        suppliersThreads[i].start();
    }

    public void stopThread(int i){
        suppliersThreads[i].interrupt();
    }

    public void setDelayForAll(int delay){
        for (Supplier<T> supplier: suppliers){
            supplier.setDelay(delay);
        }
    }

    public void addCreatedListener(Runnable listener){
        this.listener = listener;
    }


    public int getCreatedParts(){
        synchronized (lock) {
            return createdParts;
        }
    }

    private void updateCreatedParts(){
        synchronized (lock) {
            createdParts++;
        }
        listener.run();
    }
}
