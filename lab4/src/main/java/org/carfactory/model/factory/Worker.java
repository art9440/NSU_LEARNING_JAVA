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
                engine = engineStorage.get();
                body = bodyStorage.get();
                accessory = accessoryStorage.get();

                Car car = new Car(engine, body, accessory);
                createdCars++;
                notifyCreatedListener();

                carStorage.put(car);

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
