package org.carfactory.model.factory;

import org.carfactory.model.details.Accessory;
import org.carfactory.model.details.Body;
import org.carfactory.model.details.Engine;
import org.carfactory.model.suppliers.Storage;

public class Worker implements Runnable{
    private final Storage<Car> carStorage;
    private final Storage<Engine> engineStorage;
    private final Storage<Body> bodyStorage;
    private final Storage<Accessory> accessoryStorage;
    private int createdCars = 0;
    private final ControllerOfStorageCar controller;

    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    public Worker(Storage<Car> carStorage, Storage<Engine> engineStorage, Storage<Body> bodyStorage, Storage<Accessory> accessoryStorage,
                  ControllerOfStorageCar controller){
        this.carStorage = carStorage;
        this.engineStorage = engineStorage;
        this.bodyStorage = bodyStorage;
        this.accessoryStorage = accessoryStorage;
        this.controller = controller;
    }

    @Override
    public void run(){
        Engine engine;
        Body body;
        Accessory accessory;

        while(!Thread.currentThread().isInterrupted()){
            try{
                controller.getBuildTask();
                synchronized (engineStorage) {
                    while (engineStorage.getNowSize() == 0){
                        engineStorage.wait();
                    }
                    engine = engineStorage.get();
                    engineStorage.notifyAll();
                }

                synchronized (bodyStorage) {
                    while (bodyStorage.getNowSize() == 0){
                        bodyStorage.wait();
                    }
                    body = bodyStorage.get();
                    bodyStorage.notifyAll();
                }

                synchronized (accessoryStorage) {
                    while (accessoryStorage.getNowSize() == 0){
                        accessoryStorage.wait();
                    }
                    accessory = accessoryStorage.get();
                    accessoryStorage.notifyAll();
                }

                Car car = new Car(engine, body, accessory);
                createdCars++;
                notifyCreatedListener();
                synchronized (carStorage) {
                    while (carStorage.getNowSize() == carStorage.getSize()){
                        carStorage.wait();
                    }
                    carStorage.put(car);
                    carStorage.notifyAll();
                }
            }
            catch (Exception e){
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
