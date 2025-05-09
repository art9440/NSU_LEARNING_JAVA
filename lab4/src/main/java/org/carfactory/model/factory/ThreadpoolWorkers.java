package org.carfactory.model.factory;

import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.suppliers.Storage;


public class ThreadpoolWorkers {
    private final Storage<Car> carStorage;
    private final Storage<Body> bodyStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Accessory> accessoryStorage;
    private final ControllerOfStorageCar controller;

    private int workersCount;

    private final Object lock = new Object();
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Thread[] workersThreads;
    private Worker[] workers;
    private int createdCars = 0;

    public ThreadpoolWorkers(int workersCount, Storage<Car> carStorage, Storage<Engine> engineStorage, Storage<Body> bodyStorage
                                , Storage<Accessory> accessoryStorage, ControllerOfStorageCar controller){
        this.carStorage = carStorage;
        this.bodyStorage = bodyStorage;
        this.engineStorage = engineStorage;
        this.accessoryStorage = accessoryStorage;
        this.workersCount = workersCount;
        this.controller = controller;

        workersThreads = new Thread[workersCount];
        workers = new Worker[workersCount];
        for (int i = 0; i < workersCount; i++){
            Worker worker = new Worker(carStorage, engineStorage, bodyStorage, accessoryStorage, controller);
            worker.addCreatedListener(this::updateCreatedCars);
            workers[i] = worker;
            workersThreads[i] = new Thread(worker);
        }
    }

    public int getWorkersCount(){
        return workersCount;
    }

    public void startThread(int i){
        workersThreads[i].start();
    }

    public void stopThread(int i){
        workersThreads[i].interrupt();
    }

    public void addCreatedListener(Runnable listener){
        this.listener = listener;
    }


    public int getCreatedParts(){
        synchronized (lock) {
            return createdCars;
        }
    }

    private void updateCreatedCars(){
        synchronized (lock) {
            createdCars++;
        }
        listener.run();
    }
}
