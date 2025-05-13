package org.carfactory.model.factory;

import org.carfactory.model.suppliers.Storage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ControllerOfStorageCar implements Runnable {
    private final BlockingQueue<Object> taskQueue = new LinkedBlockingQueue<>();
    private final Storage<Car> carStorage;


    private final Object saleLock = new Object();
    private boolean salePending = false;

    private volatile boolean running = true;

    private Runnable listener = () -> {};

    public ControllerOfStorageCar(Storage<Car> carStorage, int workersCount) {
        this.carStorage = carStorage;

        for (int i = 0; i < workersCount; i++) {
            taskQueue.offer(new Object());
        }
    }


    public void notifySold() {
        synchronized (saleLock) {
            salePending = true;
            saleLock.notify();
        }
    }


    public Object getBuildTask() throws InterruptedException {
        return taskQueue.take();
    }

    public int getTaskCount() {
        return taskQueue.size();
    }

    public void addTaskListener(Runnable listener) {
        this.listener = listener;
    }

    private void notifyTaskListener() {
        listener.run();
    }


    public void stop() {
        running = false;
        synchronized (saleLock) {
            saleLock.notify();
        }
    }

    @Override
    public void run() {
        while (running) {

            synchronized (saleLock) {
                while (!salePending && running) {
                    try {
                        saleLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                salePending = false;
            }


            int capacity    = carStorage.getSize();
            int threshold   = capacity / 2;
            int storageNow  = carStorage.getNowSize();
            int pending     = taskQueue.size();

            int toAdd = threshold - (storageNow + pending);
            for (int i = 0; i < toAdd; i++) {
                taskQueue.offer(new Object());
            }

            // update GUI
            notifyTaskListener();
        }
    }
}
