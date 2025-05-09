package org.carfactory.model.dealercenter;

import org.carfactory.model.factory.Car;
import org.carfactory.model.factory.ControllerOfStorageCar;
import org.carfactory.model.suppliers.Storage;

import java.util.logging.Logger;


public class Dealer implements Runnable {
    private final Storage<Car> carStorage;
    private int delay = 3;
    private static final Logger logger = Logger.getLogger(Dealer.class.getName());
    private int number;
    private final ControllerOfStorageCar controller;


    public Dealer(Storage<Car> carStorage, int number, ControllerOfStorageCar controller){
        this.carStorage = carStorage;
        this.number = number;
        this.controller = controller;
    }

    public void setDelay(int delay){
        this.delay = delay;
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(delay * 1000L);
                synchronized (carStorage){
                    while (carStorage.getNowSize() == 0){
                        carStorage.wait();
                    }
                    Car car = carStorage.get();
                    logger.info("Dealer: " + number + "; " + "Car ID: " + car.getID() + " (" + car.getPartID() + ").");
                    controller.notifySold();
                }
            }
            catch (Exception e){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
