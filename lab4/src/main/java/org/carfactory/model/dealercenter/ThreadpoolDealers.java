package org.carfactory.model.dealercenter;

import org.carfactory.model.factory.Car;

import org.carfactory.model.factory.ControllerOfStorageCar;
import org.carfactory.model.suppliers.Storage;
import org.carfactory.model.suppliers.Supplier;

public class ThreadpoolDealers {
    private final int dealersCount;
    private final Storage<Car> carStorage;
    private final ControllerOfStorageCar controller;

    private final Object lock = new Object();
    private Runnable listener = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Thread[] dealersThreads;
    private Dealer[] dealers;


    public ThreadpoolDealers(int dealersCount, Storage<Car> carStorage, ControllerOfStorageCar controller){
        this.dealersCount = dealersCount;
        this.carStorage = carStorage;
        this.controller = controller;

        dealersThreads = new Thread[dealersCount];
        dealers = new Dealer[dealersCount];

        for (int i = 0; i < dealersCount; i++){
            Dealer dealer = new Dealer(carStorage, i, controller);
            dealers[i] = dealer;
            dealersThreads[i] = new Thread(dealer);
        }

    }

    public int getDealersCount(){
        return dealersCount;
    }

    public void startThread(int i){
        dealersThreads[i].start();
    }

    public void stopThread(int i){
        dealersThreads[i].interrupt();
    }

    public void setDelayForAll(int delay){
        for (Dealer dealer: dealers){
            dealer.setDelay(delay);
        }
    }
}
